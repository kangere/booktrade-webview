package com.booktrade.kangere.utils;

import com.booktrade.kangere.entities.ExtraBookDetails;
import org.json.JSONArray;
import org.json.JSONObject;


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
}
