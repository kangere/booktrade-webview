package com.booktrade.kangere.views;

import com.booktrade.kangere.entities.User;
import com.booktrade.kangere.service.ClientService;
import com.booktrade.kangere.utils.SessionData;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;

public class AccountInformationView extends VerticalLayout implements View {

    public static final String NAME = "account";

    private ClientService service = ClientService.getInstance();

    public AccountInformationView(){

        FormLayout formLayout = createForm();

        addComponent(formLayout);
    }


    private  FormLayout createForm(){

        User user = SessionData.getCurrentUser();

        FormLayout form = new FormLayout();
        form.setMargin(true);


        TextField firstNameField = new TextField("First Name");
        firstNameField.setValue(user.getFirstName());
        form.addComponent(firstNameField);

        TextField lastNameField = new TextField("Last Name");
        lastNameField.setValue(user.getLastName());
        form.addComponent(lastNameField);

        TextField phoneNumberField = new TextField("Phonenumber");
        phoneNumberField.setValue(user.getPhoneNumber().toString());
        form.addComponent(phoneNumberField);

        TextField usernameField = new TextField("Username");
        usernameField.setValue(user.getUsername());
        form.addComponent(usernameField);


        Button save = new Button("Save");
        save.addClickListener(clickEvent -> {

            String fname = firstNameField.getValue();
            String lname = lastNameField.getValue();
            Long number = Long.valueOf(phoneNumberField.getValue());
            String username = usernameField.getValue();

            if(!user.getFirstName().equals(fname) || !user.getLastName().equals(lname)
                || !user.getPhoneNumber().equals(number) || !user.getUsername().equals(username)){

                user.setFirstName(fname);
                user.setLastName(lname);
                user.setPhoneNumber(number);
                user.setUsername(username);

                if(service.updateUserInformation(user))
                    Notification.show("Info Saved");
                else
                    Notification.show("Unable to update, Try again");
            } else {
                Notification.show("User Information not edited");
            }


        });
        form.addComponent(save);


        return form;
    }
}
