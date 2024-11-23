package com.swe.libraryprogram.domainmodel;

import java.util.LinkedList;

public class DigitalMedia extends Element{

    private String producer;
    private Integer length;
    private Integer ageRating;
    private String director;

    public DigitalMedia(Integer id, String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, LinkedList<Genre> genres, String producer, Integer length1, Integer ageRating, String director) {
        super(id, title, releaseYear, description, quantity, quantityAvailable, length, genres);
        this.producer = producer;
        this.length = length1;
        this.ageRating = ageRating;
        this.director = director;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    @Override
    public Integer getLength() {
        return length;
    }

    @Override
    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(Integer ageRating) {
        this.ageRating = ageRating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public String toString() {
        return "DigitalMedia{" +
                "producer='" + producer + '\'' +
                ", length=" + length +
                ", ageRating=" + ageRating +
                ", director='" + director + '\'' +
                '}';
    }
}
