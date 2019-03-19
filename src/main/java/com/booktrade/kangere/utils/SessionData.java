package com.booktrade.kangere.utils;

import com.booktrade.kangere.entities.User;
import com.vaadin.server.VaadinSession;

import java.util.Optional;

public class SessionData {


    private SessionData(){}



    public static Optional<User> getCurrentUser(){

        return Optional.of((User)VaadinSession.getCurrent().getAttribute("user"));
    }

    public static void setCurrentUser(User user){
        VaadinSession.getCurrent().setAttribute("user",user);
    }
}
