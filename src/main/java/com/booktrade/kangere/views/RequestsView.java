package com.booktrade.kangere.views;

import com.booktrade.kangere.entities.Request;
import com.booktrade.kangere.entities.User;
import com.booktrade.kangere.service.ClientService;
import com.booktrade.kangere.utils.SessionData;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;


import java.util.ArrayList;
import java.util.List;


public class RequestsView extends VerticalLayout implements View {

    public static final String NAME = "requests";

    private ClientService service = ClientService.getInstance();


    public RequestsView(){
        User user = SessionData.getCurrentUser();


        ClientService service = ClientService.getInstance();

        List<Request> userRequests = service.getUserRequests(user.getEmail());

        List<Request> requestsSentList = new ArrayList<>();
        List<Request> requestsReceivedList = new ArrayList<>();

        String currentUserEmail = user.getEmail();

        for(Request request : userRequests){

            if(request.getOwnerEmail().equals(currentUserEmail))
                requestsReceivedList.add(request);
            else
                requestsSentList.add(request);
        }


        ListDataProvider<Request> requestSentDataProvider = new ListDataProvider<>(requestsSentList);
        ListDataProvider<Request> requestReceivedDataProvider = new ListDataProvider<>(requestsReceivedList);


        addComponent(new Label("Requests Sent"));


        Grid<Request> requestsSentGrid = createRequestSentGrid();

        requestsSentGrid.setDataProvider(requestSentDataProvider);
        requestsSentGrid.setWidth("100%");

        addComponent(requestsSentGrid);



        addComponent(new Label("Request Received"));


        Grid<Request> requestReceivedGrid = createRequestReceivedGrid();

        requestReceivedGrid.setDataProvider(requestReceivedDataProvider);
        requestReceivedGrid.setWidth("100%");

        addComponent(requestReceivedGrid);
    }


    private Grid<Request> createRequestSentGrid(){

        Grid<Request> grid = new Grid<>();

        grid.addColumn(Request::getOwnerBook).setCaption("Book Requested");

        grid.addColumn(Request::getOwnerEmail).setCaption("Book Owner");

        grid.addColumn(Request::getRequesterBook).setCaption("Book Offered");

        grid.addComponentColumn(request -> {

            Button retract = new Button("Retract");

            retract.addClickListener(clickEvent -> {

                request.setStatus(Request.RequestStatus.RETRACTED);
                if(service.updateUserRequest(request))
                    Notification.show("Trade Retracted");
                else
                    Notification.show("Unable to retract request, try again later");
            });

            return retract;
        });

        return grid;
    }

    private Grid<Request> createRequestReceivedGrid(){
        Grid<Request> grid = new Grid<>();

        grid.addColumn(Request::getOwnerBook).setCaption("My Book");

        grid.addColumn(Request::getRequesterEmail).setCaption("User Requesting");

        grid.addColumn(Request::getRequesterBook).setCaption("Book Offered");

        grid.addComponentColumn(request -> {
            Button decline = new Button("Decline");



            decline.addClickListener(clickEvent -> {
                request.setStatus(Request.RequestStatus.DECLINED);
                if(service.updateUserRequest(request))
                    Notification.show("Trade Declined");
                else
                    Notification.show("Unable to decline, try again later!");
            });
            return decline;
        });

        grid.addComponentColumn(request -> {
            Button accept = new Button("Accept");


            accept.addClickListener(clickEvent -> {

                request.setStatus(Request.RequestStatus.COMPLETED);

                if(service.updateUserRequest(request))
                    Notification.show("Trade Completed");
                else
                    Notification.show("Unable to complete trade, try again later!");

            });
            return accept;
        });


        return grid;

    }
}
