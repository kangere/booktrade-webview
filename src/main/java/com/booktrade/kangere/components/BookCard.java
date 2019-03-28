package com.booktrade.kangere.components;


import com.booktrade.kangere.entities.Book;
import com.booktrade.kangere.entities.ExtraBookDetails;
import com.booktrade.kangere.views.BookView;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

public class BookCard extends VerticalLayout {

    private String title;



    private Book book;

    private ExtraBookDetails details;

    public BookCard(Book book, ExtraBookDetails details){

        this.title = book.getTitle();

        this.book = book;
        this.details = details;



        Image thumbnail = new Image();

        if(details.getThumbnail() != null)
            thumbnail.setSource(new ExternalResource(details.getThumbnail()));
        else
            thumbnail.setCaption("Thumbnail not available");

//        thumbnail.setWidth(100,Unit.PIXELS);
//        thumbnail.setHeight(100,Unit.PIXELS);

        addComponents(thumbnail);

        Label titleLabel = new Label();
        titleLabel.setValue(title);
        addComponent(titleLabel);



        Button view = new Button("View");

        view.addClickListener(e->{

            getUI().getNavigator().navigateTo(BookView.NAME + "/" + book.getIsbn());

        });

        addComponent(view);
    }

    private HorizontalLayout titleLayout(){

        HorizontalLayout top = new HorizontalLayout();



        return top;
    }

}
