package com.booktrade.kangere.entities;

import java.util.List;

public class Book {


    private Long isbn;

    private String title;

    private Integer publishedDate;

    private String publisher;

    private String language;


    private String externalLink;

    private String description;

    private String thumbnail;

    private List<Author> authors;

    public Book(){}

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Integer publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(isbn).append("\n")
                .append(title).append("\n")
                .append(publishedDate).append("\n")
                .append(publisher).append("\n")
                .append(language).append("\n")
                .append(externalLink).append("\n");

        for (Author author: authors)
            builder.append(author.toString());


        return builder.toString();
    }
}
