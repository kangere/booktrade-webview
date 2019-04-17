package com.booktrade.kangere.views;

import com.booktrade.kangere.MyUI;
import com.booktrade.kangere.entities.User;
import com.booktrade.kangere.service.ClientService;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;

import java.util.Optional;


public class MainView extends VerticalLayout implements View {

    public static final String NAME = "main";

    private Navigator internalNavigator;

    private Optional<User> user;

    public MainView(){

        user = Optional.of((User)VaadinSession.getCurrent().getAttribute("user"));

        Panel viewPanel = new Panel();
        viewPanel.setWidth("100%");

        internalNavigator = new Navigator(UI.getCurrent(),viewPanel);
        internalNavigator.addView(OrdersView.NAME,OrdersView.class);
        internalNavigator.addView(RequestsView.NAME,RequestsView.class);
        internalNavigator.addView(StoreView.NAME,StoreView.class);
        internalNavigator.addView(LibraryView.NAME,LibraryView.class);
        internalNavigator.addView(BookView.NAME,BookView.class);
        internalNavigator.addView(AccountInformationView.NAME, AccountInformationView.class);

        internalNavigator.setErrorView(StoreView.class);

        internalNavigator.navigateTo(StoreView.NAME);

        MenuBar menubar = buildMenuBar();


        addComponents(menubar,viewPanel);



    }


    private MenuBar buildMenuBar(){

        MenuBar menuBar = new MenuBar();

        menuBar.addItem("Store", menuItem -> {
            internalNavigator.navigateTo(StoreView.NAME);});
        menuBar.addItem("My Library",menuItem -> {
            internalNavigator.navigateTo(LibraryView.NAME);});
        menuBar.addItem("Requests",menuItem -> {
            internalNavigator.navigateTo(RequestsView.NAME);});
        menuBar.addItem("Orders",menuItem -> {
            internalNavigator.navigateTo(OrdersView.NAME);});

        String firstName = user.get().getFirstName();
        String lastName = user.get().getLastName();


        MenuBar.MenuItem options = menuBar.addItem(firstName + " " + lastName);
        options.addItem("Account",menuItem -> internalNavigator.navigateTo(AccountInformationView.NAME));
        options.addItem("Logout",menuItem -> logout());

        menuBar.setWidth(100f,Unit.PERCENTAGE);




        return menuBar;
    }

    private  void logout(){
        getUI().getSession().close();

        ClientService service = ClientService.getInstance();
        service.close();

        //TODO: Fix navigation to login view
//        getUI().getNavigator().navigateTo(LoginView.NAME);
        MyUI.navigator.navigateTo(LoginView.NAME);
    }
}
