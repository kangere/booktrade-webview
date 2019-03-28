package com.booktrade.kangere.views;

import com.booktrade.kangere.entities.Book;
import com.booktrade.kangere.entities.ExtraBookDetails;
import com.booktrade.kangere.service.ClientService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;

public class BookView extends VerticalLayout implements View {

    public static final String NAME = "bookview";

    private Long isbn;

    private ClientService service;

    public BookView(){


    }

    private HorizontalLayout buildBookDetails(){

        Book book = service.getBookByIsbn(isbn).get(0);

        ExtraBookDetails details = service.getExtraBookDetails(book.getExternalLink());


        HorizontalLayout layout = new HorizontalLayout();


        Image thumbnail = new Image();
        if(details.getThumbnail() != null)
            thumbnail.setSource(new ExternalResource(details.getThumbnail()));
        else
            thumbnail.setCaption("No Thumbnail");

        thumbnail.setWidth(null);

        layout.addComponent(thumbnail);

        VerticalLayout text = new VerticalLayout();

        Label title = new Label();
        title.setValue(book.getTitle());
        text.addComponent(title);

        TextArea description = new TextArea();
        description.setValue(details.getDescription() != null ?details.getDescription():"");
        description.setReadOnly(true);
        description.setWidth("100%");
        text.addComponent(description);

        Label label = new Label();
        label.setValue(isbn.toString());
        text.addComponent(label);

        text.setWidth("100%");
        layout.addComponent(text);

        layout.setExpandRatio(text,1.0f);
        return layout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        isbn = Long.valueOf(event.getParameters());

        service = ClientService.getInstance();

        HorizontalLayout bookDetails = buildBookDetails();

        addComponent(bookDetails);
    }
}
