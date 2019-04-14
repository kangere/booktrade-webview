package com.booktrade.kangere.utils;

import com.booktrade.kangere.entities.User;
import com.vaadin.server.VaadinSession;

import java.util.Optional;

public class SessionData {


    private SessionData(){}



    public static User getCurrentUser(){

        User user = (User)VaadinSession.getCurrent().getAttribute("user");

        if(user == null)
            throw new IllegalStateException("User not found");

        return user;
    }

    public static void setCurrentUser(User user){
        VaadinSession.getCurrent().setAttribute("user",user);
    }
}
