package com.booktrade.kangere.utils;

import com.booktrade.kangere.entities.Author;
import com.booktrade.kangere.entities.Book;
import com.booktrade.kangere.entities.ExtraBookDetails;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Parser {


    public static ExtraBookDetails getExtraBookDetails(JSONObject jsonObject){

        ExtraBookDetails bookDetails = new ExtraBookDetails();

        JSONObject volumeInfo = jsonObject.getJSONObject("volumeInfo");

        String description = volumeInfo.getString("description");
        bookDetails.setDescription(description);

        JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");


        if(industryIdentifiers.length() > 1) {
            JSONObject second = industryIdentifiers.getJSONObject(1);
            Long isbn13 = Long.valueOf(second.getString("identifier"));
            bookDetails.setIsbn13(isbn13);
        } else if(industryIdentifiers.length() < 1 ){
            JSONObject first = industryIdentifiers.getJSONObject(0);
            Long isbn10 = Long.valueOf(first.getString("identifier"));
            bookDetails.setIsbn10(isbn10);
        }

        if(volumeInfo.has("imageLinks")){
            JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

            String thumbnail  = imageLinks.getString("thumbnail");
            bookDetails.setThumbnail(thumbnail);
        }


        return bookDetails;
    }

    public static Optional<Book> getBasicBookDetails(JSONObject json){

        if (json.getInt("totalItems") == 0)
            return Optional.empty();

        Book book = new Book();
        List<Author> authors = new ArrayList<>();

        JSONArray items = json.getJSONArray("items");
        JSONObject bookDetails = items.getJSONObject(0);

        String link = bookDetails.getString("selfLink");
        book.setExternalLink(link);

        JSONObject volumeInfo = bookDetails.getJSONObject("volumeInfo");

        String title = volumeInfo.getString("title");
        book.setTitle(title);


        JSONArray author_array = volumeInfo.getJSONArray("authors");

        for (int i = 0; i < author_array.length(); ++i) {

            String authorName = author_array.getString(i);
            String[] split = authorName.split("\\s+");

            Author author = new Author();
            author.setFname(split[0]);
            author.setLname(split[split.length - 1]);

            if (split.length > 2) {
                author.setMname(split[1]);
            }

            authors.add(author);

        }

        book.setAuthors(authors);


        JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");

        if(industryIdentifiers.length() > 1) {
            JSONObject second = industryIdentifiers.getJSONObject(1);
            Long isbn13 = Long.valueOf(second.getString("identifier"));
            book.setIsbn(isbn13);
        } else if(industryIdentifiers.length() < 1 ){
            JSONObject first = industryIdentifiers.getJSONObject(0);
            Long isbn10 = Long.valueOf(first.getString("identifier"));
            book.setIsbn(isbn10);
        }

        if (volumeInfo.has("publisher")) {
            String publisher = volumeInfo.getString("publisher");
            book.setPublisher(publisher);
        }

        String publishedDate = volumeInfo.getString("publishedDate");
        String language = volumeInfo.getString("language");

        if (publishedDate.length() > 4)
            publishedDate = publishedDate.substring(0, 4);

        book.setPublishedDate(Integer.valueOf(publishedDate));
        book.setLanguage(language);

        return Optional.of(book);
    }
}
