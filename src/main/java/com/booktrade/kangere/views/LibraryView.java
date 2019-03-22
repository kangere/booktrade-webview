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
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LibraryView extends HorizontalLayout implements View {


    public static final String NAME = "library";

    private Optional<User> user;


    private ClientService service;

    public LibraryView(){
        setSizeFull();
        service = ClientService.getInstance();
        user = SessionData.getCurrentUser();


        FormLayout bookForm = buildBookForm();

        Grid<Book> myBooks = buildBookGrid();




        addComponents(bookForm,myBooks);



    }


    private FormLayout buildBookForm(){



        FormLayout layout = new FormLayout();
        layout.setCaption("Add Book");

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

        //TODO: Ensure item is selected from combobox or set a default value
        ComboBox<OwnedBook.BookCondition> conditionComboBox = new ComboBox<>("Select Book Condition");
        conditionComboBox.setItems(bookConditions);
        conditionComboBox.setTextInputAllowed(false);
        layout.addComponent(conditionComboBox);



        List<OwnedBook.TradeType> tradeTypesList = new ArrayList<>();
        tradeTypesList.add(OwnedBook.TradeType.TRADE);
        tradeTypesList.add(OwnedBook.TradeType.SELL);
        tradeTypesList.add(OwnedBook.TradeType.TRADE_OR_SELL);


        //TODO: Ensure item is selected from combobox or set a default value
        ComboBox<OwnedBook.TradeType> tradeTypeComboBox = new ComboBox<>("Choose Trade Type:");
        tradeTypeComboBox.setItems(tradeTypesList);
        tradeTypeComboBox.setTextInputAllowed(false);
        layout.addComponent(tradeTypeComboBox);

        FormLayout authorsForm = new FormLayout();
        authorsForm.setCaption("Authors");

        Binder<Author> authorBinder = new Binder<>();

        //TODO: add not empty validators
        TextField firstName = new TextField("First Name");
        authorBinder.bind(firstName,Author::getFname,Author::setFname);
        authorsForm.addComponent(firstName);

        TextField middleName = new TextField("Middle Name");
        authorBinder.bind(middleName ,Author::getMname,Author::setMname);
        authorsForm.addComponent(middleName);

        TextField lastName = new TextField("Last Name");
        authorBinder.bind(lastName,Author::getLname,Author::setLname);
        authorsForm.addComponent(lastName);

        layout.addComponent(authorsForm);

        //TODO: find out how to obtain dynamically added authors
        /*Button addAuthor = new Button("Add Author");
        addAuthor.addClickListener(listener ->{
           layout.addComponent(addAuthor());
        });
        layout.addComponent(addAuthor);*/



        Button addBook = new Button("Add Book");
        layout.addComponent(addBook);



        addBook.addClickListener(listener ->{




            Long isbnNumber = Long.valueOf(isbnField.getValue());

            Optional<Book> book = service.getBookDetails(isbnNumber);


            if(bookBinder.isValid()){
                if(!user.isPresent())
                    throw new IllegalStateException();

                if(!book.isPresent()){
                    Book book1 = new Book();
                    try {
                        bookBinder.writeBean(book1);
                    }catch (ValidationException e){
                        Notification.show(e.getMessage());

                    }

                    //TODO: get authors
                    Author author = new Author();
                    try {
                        authorBinder.writeBean(author);
                    } catch (ValidationException e){
                        Notification.show(e.getMessage());
                    }

                    book1.setAuthors(Arrays.asList(author));

                    book = Optional.of(book1);
                }


                OwnedBook ownedBook = new OwnedBook();
                ownedBook.setIsbn(book.get().getIsbn());
                ownedBook.setEmail(user.get().getEmail());
                ownedBook.setBookCondition(conditionComboBox.getValue());
                ownedBook.setTradeType(tradeTypeComboBox.getValue());
                ownedBook.setBook(book.get());

                if(service.addBook(ownedBook))
                    Notification.show("Book Added Successfully");
                else
                    Notification.show("Unable to add book try again!");
            }
        });

        return layout;
    }


    private Grid<Book> buildBookGrid(){

        Grid<Book> grid = new Grid<>();

        grid.addColumn(Book::getTitle).setCaption("Title");

        grid.addColumn(Book::getIsbn).setCaption("ISBN");

        grid.addColumn(Book::getLanguage).setCaption("Language");

        grid.addColumn(Book::getPublishedDate).setCaption("Date");

        grid.addComponentColumn(book -> {

            VerticalLayout layout = new VerticalLayout();

            List<Author> authors = book.getAuthors();

            for(Author author : authors)
                layout.addComponent(new Label(author.toString()));

            return layout;
        }).setCaption("Authors");

        List<Book> books = service.getUsersBooks();

        ListDataProvider<Book> dataProvider = DataProvider.ofCollection(books);

        grid.setDataProvider(dataProvider);

        grid.setSizeFull();

        return grid;
    }




    //TODO:
    private FormLayout addAuthor(){
        FormLayout authorsForm = new FormLayout();
        authorsForm.setCaption("Add Authors");

        Binder<Author> authorBinder = new Binder<>();

        TextField firstName = new TextField("First Name");
        authorBinder.bind(firstName,Author::getFname,Author::setFname);
        authorsForm.addComponent(firstName);

        TextField middleName = new TextField("Middle Name");
        authorBinder.bind(middleName ,Author::getMname,Author::setMname);
        authorsForm.addComponent(middleName);

        TextField lastName = new TextField("Last Name");
        authorBinder.bind(lastName,Author::getLname,Author::setLname);
        authorsForm.addComponent(lastName);

        return authorsForm;
    }


}
