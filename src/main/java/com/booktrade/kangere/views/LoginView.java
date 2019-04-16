package com.booktrade.kangere.views;

import com.booktrade.kangere.entities.User;
import com.booktrade.kangere.service.ClientService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;

public class LoginView extends VerticalLayout implements View {

    public static final String NAME = "login";

    private Navigator navigator;

    private ClientService service;

    public LoginView(Navigator navigator){

        this.navigator = navigator;

        setMargin(true);
        setSpacing(true);


        TabSheet layout = new TabSheet();
        layout.addTab(buildLoginForm(),"Login");
        layout.addTab(buildRegisterForm(),"Register");
        addComponent(layout);


        setComponentAlignment(layout,Alignment.MIDDLE_CENTER);

    }


    private Component buildLoginForm(){

        FormLayout layout = new FormLayout();
        layout.setMargin(true);


        TextField emailField = new TextField("Email");


        new Binder<User>().forField(emailField)
                .withValidator(new EmailValidator("Invalid Email"))
                .withValidator(str -> !str.isEmpty(),"Email cannot be empty")
                .bind(User::getEmail,User::setEmail);

        layout.addComponents(emailField);

        PasswordField passField = new PasswordField("Password");
        new Binder<User>().forField(passField)
                .withValidator(str -> !str.isEmpty(),"Password cannot be empty")
                .bind(User::getPassword, User::setPassword);
        layout.addComponents(passField);

        Button login = new Button();
        login.setCaption("login");
        login.addClickListener(clickEvent -> {

            String email = emailField.getValue();
            String password = passField.getValue();

            service = ClientService.getInstance();

            boolean loginSuccess = service.login(email,password);

            if(loginSuccess)
                navigator.navigateTo(MainView.NAME);
            else
                Notification.show("Invalid Credentials");

        });

        layout.addComponent(login);
        return layout;
    }

    private Component buildRegisterForm(){

        Binder<User> binder = new Binder<>();


        FormLayout layout = new FormLayout();
        layout.setMargin(true);


        TextField fnameField = new TextField("First Name");
        binder.bind(fnameField,User::getFirstName, User::setFirstName);
        layout.addComponent(fnameField);

        TextField lnameField = new TextField("Last Name");
        binder.bind(lnameField,User::getLastName, User::setLastName);
        layout.addComponent(lnameField);

        TextField emailField = new TextField("Email");
        binder.bind(emailField,User::getEmail,User::setEmail);
        layout.addComponent(emailField);

        //TODO: Add username validation
        TextField usernameField = new TextField("Username");
        binder.bind(usernameField,User::getUsername,User::setUsername);
        layout.addComponents(usernameField,emailField);

        TextField phoneField = new TextField("Phonenumber");
        binder.forField(phoneField)
                .withConverter(new StringToLongConverter("Must be a number"))
                .bind(User::getPhoneNumber,User::setPhoneNumber);
        layout.addComponent(phoneField);

        //TODO: Use DropDown
        TextField codeField = new TextField("Phone Country Code");
        binder.forField(codeField)
                .withValidator(str -> str.length() < 4, "Invalid Input")
                .withConverter(new StringToIntegerConverter("Number Required"))
                .bind(User::getCountryCode,User::setCountryCode);
        layout.addComponent(codeField);

        PasswordField passField = new PasswordField("Password");
        binder.bind(passField,User::getPassword,User::setPassword);
        layout.addComponent(passField);

        //TODO: Match password with previous password field
        PasswordField passConfirmField = new PasswordField("Confirm Password");
        binder.bind(passConfirmField,User::getPassword,User::setPassword);
        layout.addComponent(passConfirmField);

        PasswordField confirmField = new PasswordField("Confirm Password");
        binder.forField(confirmField)
                .withValidator(str -> passField.getValue().equals(str),"Password does not Match");
        layout.addComponent(confirmField);

        Button register = new Button();
        register.setCaption("Register");
        register.addClickListener(event -> {

            User user = new User();

            try {
                binder.writeBean(user);
            }catch(ValidationException e){
                Notification.show(e.getMessage());
            }

            if(binder.isValid()){

                user.setAccountType(User.AccountType.BASIC);
                service = ClientService.getInstance();

                boolean registrationSuccess = service.register(user);

                if(registrationSuccess){
                    Notification.show("Registration Successful, You can now login");
                } else {
                    Notification.show("Registration failed, Try Again");
                }
            }

        });
        layout.addComponent(register);

        return layout;
    }


}
