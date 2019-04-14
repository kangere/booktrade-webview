package com.booktrade.kangere.views;

import com.booktrade.kangere.components.LibBookCard;
import com.booktrade.kangere.entities.*;
import com.booktrade.kangere.service.ClientService;
import com.booktrade.kangere.utils.SessionData;
import com.booktrade.kangere.validators.NotEmptyValidator;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.*;
import java.util.logging.Logger;

public class LibraryView extends HorizontalLayout implements View {


    public static final String NAME = "library";

    private User user;


    private ClientService service;

    private List<Locale> languages = Arrays.asList(
            Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.CHINESE, Locale.ITALIAN
            , Locale.KOREAN, Locale.JAPANESE);

    public LibraryView() {


        setWidth("100%");
        service = ClientService.getInstance();
        user = SessionData.getCurrentUser();


        FormLayout bookForm = buildBookForm();
        bookForm.setWidth(null);
        addComponent(bookForm);

//        Grid<Book> myBooks = buildBookGrid();

        GridLayout myBooks = showUsersBooks();

        myBooks.setWidth("100%");
        addComponent(myBooks);

        setExpandRatio(myBooks, 1.0f);

        setMargin(true);

    }


    private FormLayout buildBookForm() {


        FormLayout layout = new FormLayout();
        layout.setCaption("Add Book");

        Binder<Book> bookBinder = new Binder<>();


        TextField title = new TextField("Title");
        bookBinder.forField(title)
                .withValidator(new NotEmptyValidator(100))
                .bind(Book::getTitle, Book::setTitle);
        layout.addComponent(title);

        TextField isbnField = new TextField("ISBN");
        bookBinder.forField(isbnField)
                .withValidator(new NotEmptyValidator())
                .withConverter(new StringToLongConverter(""))
                .bind(Book::getIsbn, Book::setIsbn);
        layout.addComponent(isbnField);


        TextField publishedDate = new TextField("Date Published");
        bookBinder.forField(publishedDate)
                .withValidator(new NotEmptyValidator())
                .withConverter(new StringToIntegerConverter("Number Required"))
                .bind(Book::getPublishedDate, Book::setPublishedDate);
        layout.addComponent(publishedDate);


        TextField publisher = new TextField("Publisher");
        bookBinder.forField(publisher)
                .withValidator(new NotEmptyValidator(60))
                .bind(Book::getPublisher, Book::setPublisher);
        layout.addComponent(publisher);


//        TextField language = new TextField("Language");
//        bookBinder.bind(language, Book::getLanguage, Book::setLanguage);
//        layout.addComponent(language);

        ComboBox<Locale> language = new ComboBox<>("Language");
        language.setItems(languages);
        layout.addComponent(language);


        Binder<OwnedBook> ownedBookBinder = new Binder<>();

        List<OwnedBook.BookCondition> bookConditions = new ArrayList<>();
        bookConditions.add(OwnedBook.BookCondition.DAMAGED);
        bookConditions.add(OwnedBook.BookCondition.LIKE_NEW);
        bookConditions.add(OwnedBook.BookCondition.NEW);
        bookConditions.add(OwnedBook.BookCondition.USED);


        ComboBox<OwnedBook.BookCondition> conditionComboBox = new ComboBox<>("Select Book Condition");
        conditionComboBox.setItems(bookConditions);
        conditionComboBox.setValue(OwnedBook.BookCondition.USED);
        conditionComboBox.setTextInputAllowed(false);
        conditionComboBox.setEmptySelectionAllowed(false);
        ownedBookBinder.bind(conditionComboBox, OwnedBook::getBookCondition, OwnedBook::setBookCondition);
        layout.addComponent(conditionComboBox);


        TextField price = new TextField("Book Price");
        ownedBookBinder.forField(price)
                .withConverter(new StringToBigDecimalConverter("Invalid Input"))
                .bind(OwnedBook::getPrice, OwnedBook::setPrice);
        layout.addComponent(price);


        List<OwnedBook.TradeType> tradeTypesList = new ArrayList<>();
        tradeTypesList.add(OwnedBook.TradeType.TRADE);
        tradeTypesList.add(OwnedBook.TradeType.SELL);
        tradeTypesList.add(OwnedBook.TradeType.TRADE_OR_SELL);


        ComboBox<OwnedBook.TradeType> tradeTypeComboBox = new ComboBox<>("Choose Trade Type:");
        tradeTypeComboBox.setItems(tradeTypesList);
        tradeTypeComboBox.setValue(OwnedBook.TradeType.TRADE_OR_SELL);
        tradeTypeComboBox.setTextInputAllowed(false);
        tradeTypeComboBox.setEmptySelectionAllowed(false);
        ownedBookBinder.bind(tradeTypeComboBox, OwnedBook::getTradeType, OwnedBook::setTradeType);
        tradeTypeComboBox.addValueChangeListener(listerner -> {
            if (listerner.getValue().equals(OwnedBook.TradeType.TRADE))
                price.setReadOnly(true);
            else
                price.setReadOnly(false);
        });

        layout.addComponent(tradeTypeComboBox);

        FormLayout authorsForm = new FormLayout();
        authorsForm.setCaption("Authors");

        Binder<Author> authorBinder = new Binder<>();


        TextField firstName = new TextField("First Name");
        authorBinder.forField(firstName)
                .withValidator(new NotEmptyValidator())
                .bind(Author::getFname, Author::setFname);
        authorsForm.addComponent(firstName);

        TextField middleName = new TextField("Middle Name");
        authorBinder.forField(middleName)
                .bind(Author::getMname, Author::setMname);
        authorsForm.addComponent(middleName);

        TextField lastName = new TextField("Last Name");
        authorBinder.forField(lastName)
                .withValidator(new NotEmptyValidator())
                .bind(Author::getLname, Author::setLname);
        authorsForm.addComponent(lastName);
        //TODO: Dynamically add author forms
        layout.addComponent(authorsForm);


        Button addBook = new Button("Add Book");
        layout.addComponent(addBook);


        addBook.addClickListener(listener -> {


            Long isbnNumber = Long.valueOf(isbnField.getValue());

            Optional<Book> book = service.getBookDetailsFromGoogleBooks(isbnNumber);


            if (bookBinder.isValid() && authorBinder.isValid()
                    && ownedBookBinder.isValid()) {


                if (!book.isPresent()) {
                    Book book1 = new Book();
                    try {
                        bookBinder.writeBean(book1);
                    } catch (ValidationException e) {
                        Notification.show(e.getMessage());

                    }

                    //TODO: get authors
                    Author author = new Author();
                    try {
                        authorBinder.writeBean(author);
                    } catch (ValidationException e) {
                        Notification.show(e.getMessage());
                    }

                    book1.setAuthors(Arrays.asList(author));
                    book1.setLanguage(language.getValue().getLanguage());
                    book = Optional.of(book1);

                } else {
                    ExtraBookDetails details = service.getExtraBookDetails(book.get().getExternalLink());

                    String thumbnail = details.getThumbnail();
                    String description = details.getDescription();

                    if (thumbnail != null)
                        book.get().setThumbnail(details.getThumbnail());

                    if (description != null)
                        book.get().setDescription(description);
                }


                OwnedBook ownedBook = new OwnedBook();
                try {
                    ownedBookBinder.writeBean(ownedBook);
                } catch (ValidationException e) {
                    Notification.show(e.getMessage());
                }

                ownedBook.setIsbn(book.get().getIsbn());
                ownedBook.setEmail(user.getEmail());
                ownedBook.setBook(book.get());

                if (service.addBook(ownedBook))
                    Notification.show("Book Added Successfully");
                else
                    Notification.show("Unable to add book try again!");
            }
        });

        return layout;
    }


    private GridLayout showUsersBooks() {

        GridLayout layout = new GridLayout(3, 1);
        layout.setSpacing(true);
        layout.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);

        List<Book> books = service.getUsersBooks();

        for (Book b : books) {
            LibBookCard card = new LibBookCard(b);
            layout.addComponent(card);
        }

        return layout;
    }


}
