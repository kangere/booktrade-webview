package com.booktrade.kangere.views;

import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;


public class MainView extends VerticalLayout implements View {

    public static final String NAME = "main";

    public MainView(){
        addComponent(new Label("Page Under Construction"));
    }
}
