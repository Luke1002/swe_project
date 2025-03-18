package com.swe.libraryprogram.domainmodel;

import java.util.List;



public class DigitalMedia extends Element{


    private String producer;
    private String ageRating;
    private String director;



    public DigitalMedia(Integer id, String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, List<Genre> genres, String producer, String ageRating, String director) {

        super(id, title, releaseYear, description, quantity, quantityAvailable, length, genres);

        this.producer = producer;
        this.ageRating = ageRating;
        this.director = director;

    }

    public DigitalMedia(String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, List<Genre> genres, String producer, String ageRating, String director) {

        super(title, releaseYear, description, quantity, quantityAvailable, length, genres);

        this.producer = producer;
        this.ageRating = ageRating;
        this.director = director;

    }


    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
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
                ", ageRating=" + ageRating +
                ", director='" + director + '\'' +
                '}';
    }

}
