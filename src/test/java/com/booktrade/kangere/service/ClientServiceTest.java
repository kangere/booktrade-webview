package com.booktrade.kangere.service;

import com.booktrade.kangere.entities.Author;
import com.booktrade.kangere.entities.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientServiceTest {


    private static ClientService service;

    @BeforeAll
    public static void setUp(){
        service = ClientService.getInstance();
    }


    @Test
    public void getBookDetailsTest(){
        Book book = service.getBookDetails(9789332518667L);

        assertEquals(9789332518667L,book.getIsbn().longValue());
        assertEquals("en",book.getLanguage());
        assertEquals("https://www.googleapis.com/books/v1/volumes/KV4KrgEACAAJ",book.getExternalLink());
        assertEquals("Compilers",book.getTitle());
        assertEquals(2006,book.getPublishedDate().intValue());

        List<Author> authorList = book.getAuthors();

        assertEquals(2,authorList.size());

        Author author1 = authorList.get(0);
        assertEquals("Alfred",author1.getFname());
        assertEquals("V.",author1.getMname());
        assertEquals("Aho",author1.getLname());

        Author author2 = authorList.get(1);
        assertEquals("Monica",author2.getFname());
        assertEquals("S.",author2.getMname());
        assertEquals("Lam",author2.getLname());



    }

    @AfterAll
    public static void tearDown(){
        service.close();
    }
}
