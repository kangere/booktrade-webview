package com.booktrade.kangere.service;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Authenticator implements ClientRequestFilter {

    private String email;

    private String password;

    public Authenticator(String email, String password){
        this.email = email;
        this.password = password;
    }


    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String,Object> headers = requestContext.getHeaders();
        final String basicAuthentication = getBasicAuthentication();
        headers.add("Authorization",basicAuthentication);
    }

    private String getBasicAuthentication(){
        String token = this.email + ":" + this.password;

        try{
            return "BASIC " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e){
            throw new IllegalStateException("Cannot encode with UTF-8",e);
        }
    }
}
