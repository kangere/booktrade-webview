package com.booktrade.kangere.entities;

import java.math.BigDecimal;

public class OwnedBook {

    public enum BookCondition{
        USED,LIKE_NEW,NEW,DAMAGED
    }

    public enum TradeType{
        SELL,TRADE,TRADE_OR_SELL
    }


    private Long isbn;


    private String email;


    private BookCondition bookCondition;

    private TradeType tradeType;

    private BigDecimal price;

    private Book book;

    private User user;

    public OwnedBook(){}

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BookCondition getBookCondition() {
        return bookCondition;
    }

    public void setBookCondition(BookCondition bookCondition) {
        this.bookCondition = bookCondition;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {

        return isbn + "\n"
                + email + "\n"
                + bookCondition + "\n"
                + tradeType + "\n"
                + book.toString();
    }
}
