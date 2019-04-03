package com.booktrade.kangere.service;


import com.booktrade.kangere.entities.*;

import com.booktrade.kangere.utils.Parser;
import com.booktrade.kangere.utils.SessionData;
import org.json.JSONArray;
import org.json.JSONObject;


import javax.ws.rs.client.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.*;


public class ClientService {


    private static Optional<ClientService> serviceOptional = Optional.empty();

    private static Logger logger = Logger.getLogger(ClientService.class.getCanonicalName());

    private static final String BASE_URL = "http://localhost:8081/api";
    private static final String DETAILS_URI = "https://www.googleapis.com/books/v1/volumes";

    private Cookie sessionId;
    private Client client;
    private WebTarget base;

    private ClientService() {
        client = ClientBuilder.newClient();
        base = client.target(BASE_URL);
    }

    public static ClientService getInstance() {

        if (!serviceOptional.isPresent())
            serviceOptional = Optional.of(new ClientService());

        return serviceOptional.get();
    }


    public boolean login(String email, String password) {

        Response response = base.path("users")
                .register(new Authenticator(email, password))
                .path(email)
                .request(MediaType.APPLICATION_JSON)
                .get();



        int code = response.getStatus();

        if (code != 200)
            return false;

        sessionId = response.getCookies().get("JSESSIONID");

        User user = response.readEntity(User.class);

        SessionData.setCurrentUser(user);

        return true;
    }

    public boolean register(User user) {

        Invocation.Builder builder = base.path("register")
                .request(MediaType.APPLICATION_JSON);

        Response response = builder.post(Entity.entity(user, MediaType.APPLICATION_JSON));

        return response.getStatus() == 200;
    }

    public boolean addBook(OwnedBook ownedBook) {


        Invocation.Builder builder = base.path("users/{email}/books")
                .resolveTemplate("email", ownedBook.getEmail())
                .request(MediaType.APPLICATION_JSON);

        Response response = builder.cookie(sessionId).post(Entity.entity(ownedBook, MediaType.APPLICATION_JSON));

        return response.getStatus() == 200;
    }

    public List<Book> getUsersBooks(){

        Optional<User> user = SessionData.getCurrentUser();

        //TODO:  logout
        if(!user.isPresent())
            throw new IllegalStateException("No User Found");

        Response response = base.path("users/{email}/books")
                .resolveTemplate("email",user.get().getEmail())
                .request(MediaType.APPLICATION_JSON)
                .cookie(sessionId)
                .get();

        if(response.getStatus() != 200)
            logger.log(Level.SEVERE, response.getStatusInfo().toString());

       return response.readEntity(new GenericType<List<Book>>(){});

    }


    public List<Book> getBooksByTitle(String title){
        return getAvailableBooks(title,null);
    }

    public List<Book> getBookByIsbn(Long isbn){
        return getAvailableBooks(null,isbn);
    }

    public List<Book> getAllAvailableBooks(){
        return getAvailableBooks(null,null);
    }

    private List<Book> getAvailableBooks(String title, Long isbn){

        WebTarget target = base.path("books");

        if(title != null)
            target = target.queryParam("title",title);
         else if( isbn != null )
            target = target.queryParam("isbn",isbn);

        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .cookie(sessionId)
                .get();

        if(response.getStatus() != 200)
            logger.log(Level.SEVERE, response.getStatusInfo().toString());

        return response.readEntity(new GenericType<List<Book>>(){});
    }

    private boolean userExists(String email) {

        //TODO:implement
        return false;
    }



    public boolean deleteBook(Long isbn){

        Optional<User> user = SessionData.getCurrentUser();

        //TODO:  logout
        if(!user.isPresent())
            throw new IllegalStateException("No User Found");

        Invocation.Builder builder = base.path("users/{email}/books/{isbn}")
                .resolveTemplate("email", user.get().getEmail())
                .resolveTemplate("isbn",isbn)
                .request(MediaType.APPLICATION_JSON)
                .cookie(sessionId);

        Response response = builder.delete();

        if(response.getStatus() == 200)
            return true;

        logger.log(Level.SEVERE,response.getStatusInfo().toString());
        return false;
    }

    public List<OwnedBook> getBookOwners(String email, Long isbn){

        Optional<User> user = SessionData.getCurrentUser();

        //TODO:  logout
        if(!user.isPresent())
            throw new IllegalStateException("No User Found");

        Response response = base.path("users/{email}/books/{isbn}/owners")
                .resolveTemplate("email",user.get().getEmail())
                .resolveTemplate("isbn",isbn)
                .request(MediaType.APPLICATION_JSON)
                .cookie(sessionId)
                .get();

        if(response.getStatus() != 200)
            logger.log(Level.SEVERE, response.getStatusInfo().toString());

        return response.readEntity(new GenericType<List<OwnedBook>>(){});

    }



    public Optional<Book> getBookDetailsFromGoogleBooks(Long isbn) {


        String jsonString = client.target(DETAILS_URI)
                .queryParam("q", "isbn:" + isbn.toString())
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);


        JSONObject json = new JSONObject(jsonString);

        return Parser.getBasicBookDetails(json);

    }

    public ExtraBookDetails getExtraBookDetails(String url){

        String jsonString = client.target(url)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        return Parser.getExtraBookDetails(new JSONObject(jsonString));
    }



    public void close() {
        if (serviceOptional.isPresent())
            client.close();

    }




}
