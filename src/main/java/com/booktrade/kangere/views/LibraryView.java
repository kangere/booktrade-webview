package com.booktrade.kangere.views;

import com.booktrade.kangere.service.ClientService;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;

public class LibraryView extends HorizontalLayout implements View {


    public static final String NAME = "library";

    private ClientService service;

    public LibraryView(){
        FormLayout bookForm = buildBookForm();



        addComponent(bookForm);
    }


    private FormLayout buildBookForm(){

        FormLayout layout = new FormLayout();
        layout.setCaption("Add Book");

        TextField isbn = new TextField("Enter Isbn number");

        Button verify = new Button("Verify");

        verify.addClickListener(listener -> {

            service = ClientService.getInstance();

            Long isbnNumber = Long.valueOf(isbn.getValue());

            String json = service.getBookDetails(isbnNumber);

            Notification.show(json);
        });

        layout.addComponents(isbn,verify);
        return layout;
    }
}
