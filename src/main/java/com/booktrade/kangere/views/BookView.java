package com.booktrade.kangere.views;

import com.booktrade.kangere.components.OwnerRow;
import com.booktrade.kangere.entities.Book;
import com.booktrade.kangere.entities.ExtraBookDetails;
import com.booktrade.kangere.entities.OwnedBook;
import com.booktrade.kangere.entities.User;
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

        Optional<User> user = SessionData.getCurrentUser();

        List<OwnedBook> owners = service.getBookOwners(user.get().getEmail(),isbn);

        Grid<OwnedBook> ownedBookGrid = new Grid<>();

        //TODO: add renderer to show username

        ownedBookGrid.addColumn(OwnedBook::getBookCondition)
                .setCaption("Book Condition");

        ownedBookGrid.addColumn(OwnedBook::getTradeType)
                .setCaption("Trade Type");

        ownedBookGrid.addComponentColumn(ownedBook -> {

            Button view = new Button("View");
            view.addClickListener(listener -> Notification.show("View Users, " + ownedBook.getUser().toString() + " Book"));

            return view;
        });

        ListDataProvider<OwnedBook> dataProvider = new ListDataProvider<>(owners);

        ownedBookGrid.setDataProvider(dataProvider);

        return ownedBookGrid;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        isbn = Long.valueOf(event.getParameters());

        service = ClientService.getInstance();

        HorizontalLayout bookDetails = showBookDetails();

        Grid<OwnedBook> bookOwners = showBookOwners();

        addComponents(bookDetails, bookOwners);
    }
}
