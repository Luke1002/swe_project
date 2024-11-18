package com.swe.libraryprogram.domainmodel;

import java.util.LinkedList;

public class Book extends Element {

    private Integer isbn;
    private String author;
    private String publisher;
    private Integer nPages;
    private Integer edition;

    public Book(Integer id, String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, LinkedList<Genre> genres, Integer isbn, String author, String publisher, Integer nPages, Integer edition) {
        super(id, title, releaseYear, description, quantity, quantityAvailable, length, genres);
        this.isbn = isbn;
        this.author = author;
        this.publisher = publisher;
        this.nPages = nPages;
        this.edition = edition;
    }

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getnPages() {
        return nPages;
    }

    public void setnPages(Integer nPages) {
        this.nPages = nPages;
    }

    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }
}
