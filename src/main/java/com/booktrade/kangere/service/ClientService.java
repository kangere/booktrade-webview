package com.booktrade.kangere.service;


import com.booktrade.kangere.entities.Author;
import com.booktrade.kangere.entities.Book;
import com.booktrade.kangere.entities.OwnedBook;
import com.booktrade.kangere.entities.User;
import com.vaadin.server.VaadinSession;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
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

         VaadinSession.getCurrent().setAttribute("user",user);
        
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
        return false;
    }

    private boolean userExists(String email){

        //TODO:implement
        return false;
    }

    public String getBookDetails(Long isbn){


        //TODO: finish implementation
        JsonObject json = client.target(DETAILS_URI)
                .queryParam("q","isbn:"+isbn.toString())
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        Book book = new Book();
        List<Author> authors = new ArrayList<>();

        JsonArray items = json.getArray("items");
        JsonObject bookDetails = items.getObject(0);

        String link = bookDetails.getString("selfLink");

        JsonObject volumeInfo = bookDetails.getObject("volumeInfo");
        String title = volumeInfo.getString("title");
        JsonArray author_array = volumeInfo.getArray("authors");

        //get authors
        for(int i = 0; i < author_array.length(); ++i){

            String authorName = author_array.get(i);


        }

        System.out.println(json.toJson());
        return json.toJson();
    }

    public void close(){
        if(serviceOptional.isPresent())
            client.close();
    }


}
