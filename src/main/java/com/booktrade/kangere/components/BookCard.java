package com.booktrade.kangere.components;


import com.booktrade.kangere.entities.Book;
import com.booktrade.kangere.entities.ExtraBookDetails;
import com.booktrade.kangere.views.BookView;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;

import java.io.File;

public class BookCard extends VerticalLayout {

    private String title;

    private Book book;


    public BookCard(Book book){

        this.title = book.getTitle();

        this.book = book;




        Image thumbnail = new Image();

        if(book.getThumbnail() != null) {
            thumbnail.setSource(new ExternalResource(book.getThumbnail()));
        } else {

            String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
            FileResource resource = new FileResource(new File(basepath+ "/WEB-INF/classes/images/placeholder.jpg"));

            thumbnail.setSource(resource);
        }


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
