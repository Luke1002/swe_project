package com.swe.libraryprogram.domainmodel;

import java.util.List;



public class Book extends Element {


    private String isbn;
    private String author;
    private String publisher;
    private Integer edition;



    public Book(Integer id, String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, List<Genre> genres, String isbn, String author, String publisher, Integer edition) {

        super(id, title, releaseYear, description, quantity, quantityAvailable, length, genres);

        this.isbn = isbn;
        this.author = author;
        this.publisher = publisher;
        this.edition = edition;

    }

    public Book(String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, List<Genre> genres, String isbn, String author, String publisher, Integer edition) {

        super(title, releaseYear, description, quantity, quantityAvailable, length, genres);

        this.isbn = isbn;
        this.author = author;
        this.publisher = publisher;
        this.edition = edition;

    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
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

    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }


    @Override
    public String toString() {
        return "Book{" +
                "isbn=" + isbn +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", edition=" + edition +
                '}';
    }

}
