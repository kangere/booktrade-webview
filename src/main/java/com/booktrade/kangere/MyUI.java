package com.booktrade.kangere;

import javax.servlet.annotation.WebServlet;

import com.booktrade.kangere.views.LoginView;
import com.booktrade.kangere.views.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {


    public static Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        navigator = new Navigator(this,this);

        LoginView loginView = new LoginView(navigator);

        navigator.addView(LoginView.NAME,loginView);
        navigator.addView(MainView.NAME,MainView.class);

        navigator.navigateTo(LoginView.NAME);
        navigator.setErrorView(loginView);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
