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

    private GridLayout grid = new GridLayout(3,1);

    public StoreView(){
       /* String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

        FileResource resource = new FileResource(new File(basepath+ "/WEB-INF/classes/images/lib.jpg"));

        Image poster = new Image("",resource);
        poster.setWidth("100%");
        poster.setHeight(400, Unit.PIXELS);*/
       grid.setStyleName("wrap");
        service = ClientService.getInstance();

        SearchBox searchBox = new SearchBox("Search",SearchBox.ButtonPosition.RIGHT);



        List<Book> books = service.getAllAvailableBooks();
        populateGrid(books);

        addComponents(searchBox,grid);



        searchBox.addSearchListener(e -> {

            String title = e.getSearchTerm();

            List<Book> filtered = service.getBooksByTitle(title);

            grid.removeAllComponents();
            populateGrid(filtered);
        });
    }

    private void populateGrid(List<Book> books){


        for(Book b : books){
            BookCard card = new BookCard(b);
            grid.addComponent(card);
        }



    }
}
