package com.booktrade.kangere.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;

public class LoginView extends VerticalLayout implements View {

    public static final String NAME = "login";

    private Navigator navigator;

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
        layout.addComponents(emailField);

        PasswordField passField = new PasswordField("Password");
        layout.addComponents(passField);

        Button login = new Button();
        login.setCaption("login");
        login.addClickListener(clickEvent -> {
//            navigator.navigateTo(MainView.NAME);
            Notification.show("Login Clicked");
        });

        layout.addComponent(login);
        return layout;
    }

    private Component buildRegisterForm(){

        FormLayout layout = new FormLayout();
        layout.setMargin(true);


        TextField fnameField = new TextField("First Name");
        layout.addComponent(fnameField);

        TextField lnameField = new TextField("Last Name");
        layout.addComponent(lnameField);

        TextField emailField = new TextField("Email");
        layout.addComponent(emailField);

        //TODO: Add username validation
//        Label usernameLabel = new Label("Email");
//        TextField usernameField = new TextField();
//        layout.addComponents(emailLabel,emailField);

        TextField phoneField = new TextField("Phonenumber");
        layout.addComponent(phoneField);

        //TODO: limit Country code field size
        TextField codeField = new TextField("Phone Country Code");
        layout.addComponent(codeField);

        PasswordField passField = new PasswordField("Password");
        layout.addComponent(passField);

        PasswordField confirmField = new PasswordField("Confirm Password");
        layout.addComponent(confirmField);

        Button register = new Button();
        register.setCaption("Register");
        register.addClickListener(event -> {
            Notification.show("Registration Clicked");
        });
        layout.addComponent(register);

        return layout;
    }


}
