package com.booktrade.kangere.service;


import com.booktrade.kangere.entities.Author;
import com.booktrade.kangere.entities.Book;
import com.booktrade.kangere.entities.OwnedBook;
import com.booktrade.kangere.entities.User;

import com.booktrade.kangere.utils.SessionData;
import com.vaadin.server.VaadinSession;
import org.json.JSONArray;
import org.json.JSONObject;


import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



public class ClientService {


    private static Optional<ClientService> serviceOptional = Optional.empty();

    private static final String BASE_URL = "http://localhost:8081";
    private static final String DETAILS_URI = "https://www.googleapis.com/books/v1/volumes";


    private Client client;
    private WebTarget base;

    private ClientService(){
        client = ClientBuilder.newClient();
        base = client.target(BASE_URL);
    }

    public static ClientService getInstance(){

        if(!serviceOptional.isPresent())
            serviceOptional = Optional.of(new ClientService());

       return serviceOptional.get();
    }


    public boolean login(String email, String password){

         Response response =  base.path("/api/users")
                 .register(new Authenticator(email,password))
                .path(email)
                .request(MediaType.APPLICATION_JSON)
                .get();
         
         int code = response.getStatus();
         
         if(code != 200)
             return false;
         
         User user = response.readEntity(User.class);

         SessionData.setCurrentUser(user);
        
         return true;
    }

    public boolean register(User user){

        Invocation.Builder builder = base.path("/api/register")
                .request(MediaType.APPLICATION_JSON);

        Response response = builder.post(Entity.entity(user,MediaType.APPLICATION_JSON));

        return response.getStatus() == 200;
    }

    public boolean addBook(OwnedBook ownedBook){

        //TODO: implement
        Invocation.Builder builder = base.path("/api/users/{email}/books")
                .request(MediaType.APPLICATION_JSON);
        return false;
    }

    private boolean userExists(String email){

        //TODO:implement
        return false;
    }

    //TODO: Refactor
    public Book getBookDetails(Long isbn){


        String jsonString = client.target(DETAILS_URI)
                .queryParam("q","isbn:"+isbn.toString())
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);


        JSONObject json = new JSONObject(jsonString);

        Book book = new Book();
        book.setIsbn(isbn);
        List<Author> authors = new ArrayList<>();

        JSONArray items = json.getJSONArray("items");
        JSONObject bookDetails = items.getJSONObject(0);

        String link = bookDetails.getString("selfLink");
        book.setExternalLink(link);

        JSONObject volumeInfo = bookDetails.getJSONObject("volumeInfo");

        String title = volumeInfo.getString("title");
        book.setTitle(title);

        JSONArray author_array = volumeInfo.getJSONArray("authors");
        //get authors
        for(int i = 0; i < author_array.length(); ++i){

            String authorName = author_array.getString(i);
            String[] split = authorName.split("\\s+");

            Author author = new Author();
            author.setFname(split[0]);
            author.setLname(split[split.length-1]);

            if(split.length > 2){
                author.setMname(split[1]);
            }

            authors.add(author);

        }

        book.setAuthors(authors);



        if(volumeInfo.has("publisher")) {
            String publisher = volumeInfo.getString("publisher");
            book.setPublisher(publisher);
        }

        String publishedDate = volumeInfo.getString("publishedDate");
        String language = volumeInfo.getString("language");

        book.setPublishedDate(Integer.valueOf(publishedDate));
        book.setLanguage(language);



        System.out.println(json.toString());
        return book;
    }

    private Optional<Authenticator> getAuthenticator(){
        Optional<User> user = SessionData.getCurrentUser();


        if(!user.isPresent())
            return Optional.empty();

        return Optional.of(new Authenticator(user.get().getEmail(),user.get().getPassword()));
    }

    public void close(){
        if(serviceOptional.isPresent())
            client.close();
    }


}
