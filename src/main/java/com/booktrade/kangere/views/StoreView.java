package com.booktrade.kangere.views;

import com.booktrade.kangere.components.BookCard;
import com.booktrade.kangere.entities.Book;
import com.booktrade.kangere.entities.ExtraBookDetails;
import com.booktrade.kangere.service.ClientService;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import org.vaadin.addons.searchbox.SearchBox;

import java.io.File;
import java.util.List;


public class StoreView extends VerticalLayout implements View {

    public static final String NAME = "store";

    private ClientService service;

    public StoreView(){
       /* String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

        FileResource resource = new FileResource(new File(basepath+ "/WEB-INF/classes/images/lib.jpg"));

        Image poster = new Image("",resource);
        poster.setWidth("100%");
        poster.setHeight(400, Unit.PIXELS);*/
        service = ClientService.getInstance();

        SearchBox searchBox = new SearchBox("Search",SearchBox.ButtonPosition.RIGHT);

        searchBox.addSearchListener(e -> Notification.show("Searching"));

        GridLayout grid = booksGrid();

        addComponents(searchBox,grid);

    }

    private GridLayout booksGrid(){

        List<Book> books = service.getAllAvailableBooks();

        GridLayout layout  = new GridLayout(3,5);

        for(Book b : books){

            ExtraBookDetails details = new ExtraBookDetails();

            if(b.getExternalLink() != null) {
                details = service.getExtraBookDetails(b.getExternalLink());
            }
            BookCard card = new BookCard(b,details);
            layout.addComponent(card);
        }

        return layout;
    }
}
