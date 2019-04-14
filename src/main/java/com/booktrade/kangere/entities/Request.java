package com.booktrade.kangere.entities;

import java.util.Date;

public class Request {


    public enum RequestStatus{
        ACTIVE,DECLINED,RETRACTED,COMPLETED
    }


    private Long requestId;

    private String ownerEmail;


    private Long ownerBook;

    private String requesterEmail;


    private Long requesterBook;

    private Date createdAt;

    private Date completedAt;


    private RequestStatus status;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public Long getOwnerBook() {
        return ownerBook;
    }

    public void setOwnerBook(Long ownerBook) {
        this.ownerBook = ownerBook;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public Long getRequesterBook() {
        return requesterBook;
    }

    public void setRequesterBook(Long requesterBook) {
        this.requesterBook = requesterBook;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }
}
