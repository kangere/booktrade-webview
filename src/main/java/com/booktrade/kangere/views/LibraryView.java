package com.booktrade.kangere.views;

import com.booktrade.kangere.entities.Author;
import com.booktrade.kangere.entities.Book;
import com.booktrade.kangere.entities.OwnedBook;
import com.booktrade.kangere.entities.User;
import com.booktrade.kangere.service.ClientService;
import com.booktrade.kangere.utils.SessionData;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryView extends HorizontalLayout implements View {


    public static final String NAME = "library";

    private Optional<User> user;

    private PopupView popupView;

    private ClientService service;

    public LibraryView(){
        FormLayout bookForm = buildBookForm();

        user = SessionData.getCurrentUser();

        addComponent(bookForm);
    }


    private FormLayout buildBookForm(){



        FormLayout layout = new FormLayout();
        layout.setCaption("Add Book");

        TextField isbn = new TextField("Enter Isbn number");
        layout.addComponent(isbn);

        Button search = new Button("Search");
        layout.addComponent(search);


        Binder<Book> bookBinder = new Binder<>();

        TextField title = new TextField("Title");
        bookBinder.bind(title,Book::getTitle,Book::setTitle);
        layout.addComponent(title);


        TextField isbnField = new TextField("ISBN");
        bookBinder.forField(isbnField)
                .withConverter(new StringToLongConverter(""))
                .bind(Book::getIsbn,Book::setIsbn);
        layout.addComponent(isbnField);


        TextField publishedDate = new TextField("Date Published");
        bookBinder.forField(publishedDate)
                .withConverter(new StringToIntegerConverter("Number Required"))
                .bind(Book::getPublishedDate,Book::setPublishedDate);
        layout.addComponent(publishedDate);


        TextField publisher = new TextField("Publisher");
        bookBinder.bind(publisher,Book::getPublisher, Book::setPublisher);
        layout.addComponent(publisher);


        TextField language = new TextField("Language");
        bookBinder.bind(language,Book::getLanguage, Book::setLanguage);
        layout.addComponent(language);



        List<OwnedBook.BookCondition> bookConditions = new ArrayList<>();
        bookConditions.add(OwnedBook.BookCondition.DAMAGED);
        bookConditions.add(OwnedBook.BookCondition.LIKE_NEW);
        bookConditions.add(OwnedBook.BookCondition.NEW);
        bookConditions.add(OwnedBook.BookCondition.USED);


        ComboBox<OwnedBook.BookCondition> conditionComboBox = new ComboBox<>("Select Book Condition");
        conditionComboBox.setItems(bookConditions);
        conditionComboBox.setTextInputAllowed(false);
        layout.addComponent(conditionComboBox);


        List<OwnedBook.TradeType> tradeTypesList = new ArrayList<>();
        tradeTypesList.add(OwnedBook.TradeType.TRADE);
        tradeTypesList.add(OwnedBook.TradeType.SELL);
        tradeTypesList.add(OwnedBook.TradeType.TRADE_OR_SELL);


        ComboBox<OwnedBook.TradeType> tradeTypeComboBox = new ComboBox<>("Choose Trade Type:");
        tradeTypeComboBox.setItems(tradeTypesList);
        tradeTypeComboBox.setTextInputAllowed(false);
        layout.addComponent(tradeTypeComboBox);



        Button addBook = new Button("Add Book");
        layout.addComponent(addBook);

        search.addClickListener(listener -> {

            service = ClientService.getInstance();

            Long isbnNumber = Long.valueOf(isbn.getValue());

            Book book = service.getBookDetails(isbnNumber);

            bookBinder.readBean(book);


        });

        addBook.addClickListener(listener ->{

            Book book = new Book();

            try {
                bookBinder.writeBean(book);
            }catch (ValidationException e){
                Notification.show(e.getMessage());

            }


            if(bookBinder.isValid()){
                if(!user.isPresent())
                    throw new IllegalStateException();

                OwnedBook ownedBook = new OwnedBook();
                ownedBook.setIsbn(book.getIsbn());
                ownedBook.setEmail(user.get().getEmail());
                ownedBook.setBookCondition(conditionComboBox.getValue());
                ownedBook.setTradeType(tradeTypeComboBox.getValue());
                ownedBook.setBook(book);

                Notification.show(ownedBook.toString());
            }
        });

        return layout;
    }


}
