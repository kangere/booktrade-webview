package com.booktrade.kangere.components;

import com.booktrade.kangere.entities.OwnedBook;
import com.booktrade.kangere.entities.User;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

public class OwnerRow extends HorizontalLayout {

    public OwnerRow(OwnedBook owner){
        addStyleName("layout-with-border");

        User user =  owner.getUser();
        Label username = new Label(user.getUsername());
        addComponent(username);

        Label condition = new Label(owner.getBookCondition().toString());
        addComponent(condition);

        Label tradeType = new Label(owner.getTradeType().toString());
        addComponent(tradeType);

        Button view = new Button("View");
        view.addClickListener(l ->{
            Notification.show("Viewing Book");
        });
        addComponent(view);

        setMargin(true);
    }
}
