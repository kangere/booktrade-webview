package com.booktrade.kangere.views;

import com.booktrade.kangere.components.OwnerRow;
import com.booktrade.kangere.entities.*;
import com.booktrade.kangere.service.ClientService;
import com.booktrade.kangere.utils.SessionData;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

import java.util.List;
import java.util.Optional;

public class BookView extends VerticalLayout implements View {

    public static final String NAME = "bookview";

    private Long isbn;

    private User user;


    private ClientService service;

    public BookView() {


    }

    private HorizontalLayout showBookDetails() {

        Book book = service.getBookByIsbn(isbn).get(0);

        ExtraBookDetails details = service.getExtraBookDetails(book.getExternalLink());


        HorizontalLayout layout = new HorizontalLayout();


        Image thumbnail = new Image();
        if (details.getThumbnail() != null)
            thumbnail.setSource(new ExternalResource(details.getThumbnail()));
        else
            thumbnail.setCaption("No Thumbnail");

        thumbnail.setWidth(null);

        layout.addComponent(thumbnail);

        VerticalLayout text = new VerticalLayout();

        Label title = new Label();
        title.setValue(book.getTitle());
        text.addComponent(title);

        Label description = new Label();
        description.setWidth(500, Unit.PIXELS);
        description.setValue(details.getDescription() != null ? details.getDescription() : "");

        text.addComponent(description);

        Label label = new Label();
        label.setValue(isbn.toString());
        text.addComponent(label);

        text.setWidth("100%");
        layout.addComponent(text);

        layout.setExpandRatio(text, 1.0f);
        return layout;
    }


    private Grid<OwnedBook> showBookOwners() {

        List<OwnedBook> owners = service.getBookOwners(user.getEmail(), isbn);

        Grid<OwnedBook> ownedBookGrid = new Grid<>();

        //TODO: add renderer to show username
        ownedBookGrid.addComponentColumn(ownedBook -> {
            Label username = new Label();

            User owner = ownedBook.getUser();

            username.setValue(owner.getUsername());

            return username;
        }).setCaption("Username");

        ownedBookGrid.addColumn(OwnedBook::getBookCondition)
                .setCaption("Book Condition");


        ownedBookGrid.addColumn(OwnedBook::getTradeType)
                .setCaption("Trade Type");

        ownedBookGrid.addColumn(OwnedBook::getPrice)
                .setCaption("Price");

        ownedBookGrid.addComponentColumn(ownedBook -> {

            Button buy = new Button("Buy Book");

            buy.addClickListener(clickEvent -> Notification.show("Added to shopping cart"));

            List<Book> usersBooks = service.getUsersBooks();
            ListDataProvider<Book> dataProvider = new ListDataProvider<>(usersBooks);
            PopupView requestExchange = new PopupView("Request Exchange",
                    getPopupContent(ownedBook.getEmail(), dataProvider));


            if (ownedBook.getTradeType().equals(OwnedBook.TradeType.SELL)) {
                return buy;
            } else if (ownedBook.getTradeType().equals(OwnedBook.TradeType.TRADE))
                return requestExchange;

            HorizontalLayout layout = new HorizontalLayout();
            layout.addComponents(buy, requestExchange);
            return layout;
        });

        ListDataProvider<OwnedBook> dataProvider = new ListDataProvider<>(owners);

        ownedBookGrid.setDataProvider(dataProvider);

        return ownedBookGrid;
    }

    private Grid<Book> getPopupContent(String ownerEmail, ListDataProvider<Book> dataProvider) {
        Grid<Book> grid = new Grid<>();

        grid.addColumn(Book::getTitle).setCaption("Title");

        grid.addComponentColumn(book -> {
            Button trade = new Button("Trade");

            Request request = new Request();
            request.setOwnerBook(isbn);
            request.setOwnerEmail(ownerEmail);
            request.setRequesterEmail(user.getEmail());
            request.setRequesterBook(book.getIsbn());
            request.setStatus(Request.RequestStatus.ACTIVE);

            trade.addClickListener(event -> {

                if(service.createRequest(request))
                    Notification.show("Request sent");
                else
                    Notification.show("Unable to send Request");


            });
            return trade;
        });

        grid.setDataProvider(dataProvider);
        return grid;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        isbn = Long.valueOf(event.getParameters());

        service = ClientService.getInstance();

        user = SessionData.getCurrentUser();

        HorizontalLayout bookDetails = showBookDetails();

        Grid<OwnedBook> bookOwners = showBookOwners();

        addComponents(bookDetails, bookOwners);

        bookOwners.setWidth("100%");
    }
}
