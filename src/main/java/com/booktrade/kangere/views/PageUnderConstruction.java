package com.booktrade.kangere.views;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class PageUnderConstruction extends VerticalLayout {

    public PageUnderConstruction(String name){
        addComponent(new Label(name + " is currently under construction"));
    }
}
