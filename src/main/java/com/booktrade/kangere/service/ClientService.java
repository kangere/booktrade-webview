package com.booktrade.kangere.service;


import com.booktrade.kangere.entities.User;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import java.util.Optional;

import static com.booktrade.kangere.service.URI.BASE_URL;

public class ClientService {


    private static Optional<ClientService> serviceOptional = Optional.empty();

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


    public User getUser(String email, String password){

         return base.path("/api/users")
                 .register(new Authenticator(email,password))
                .path(email)
                .request(MediaType.APPLICATION_JSON)
                .get()
                 .readEntity(User.class);

    }

    public void close(){
        if(serviceOptional.isPresent())
            client.close();
    }


}
