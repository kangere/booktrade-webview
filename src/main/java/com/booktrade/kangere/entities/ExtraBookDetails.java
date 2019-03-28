package com.booktrade.kangere.entities;

public class ExtraBookDetails {

    private String description;

    private String thumbnail;

    private Long isbn10;

    private Long isbn13;


    public ExtraBookDetails(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Long getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(Long isbn10) {
        this.isbn10 = isbn10;
    }

    public Long getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(Long isbn13) {
        this.isbn13 = isbn13;
    }
}
