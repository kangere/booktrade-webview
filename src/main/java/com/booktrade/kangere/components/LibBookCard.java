package com.booktrade.kangere.components;

import com.booktrade.kangere.entities.Book;
import com.booktrade.kangere.service.ClientService;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;

import java.io.File;

public class LibBookCard extends VerticalLayout {


    private ClientService service = ClientService.getInstance();

    public LibBookCard(Book book){

//        addStyleName("layout-with-border");

        Image thumbnail = new Image();


        String thumbnailLink = book.getThumbnail();

        if(thumbnailLink != null){
            thumbnail.setSource(new ExternalResource(thumbnailLink));
        } else {
            String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
            FileResource resource = new FileResource(new File(basepath+ "/WEB-INF/classes/images/placeholder.jpg"));

            thumbnail.setSource(resource);
        }

        addComponent(thumbnail);

        Label title = new Label(book.getTitle());
        addComponent(title);


        Button delete = new Button("Delete");
        delete.addClickListener(e ->{

            if(service.deleteBook(book.getIsbn())) {
                Notification.show("Deleted Book");
                //TODO: reload page
            }else {
                Notification.show("Unable to delete book,Try again!");
            }
        });
        addComponent(delete);
    }
}
