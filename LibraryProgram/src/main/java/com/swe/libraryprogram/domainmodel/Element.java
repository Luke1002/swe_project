package com.swe.libraryprogram.domainmodel;

import java.util.List;
import java.util.stream.Collectors;


public class Element {


    private final Integer id;
    private String title;
    private Integer releaseYear;
    private String description;
    private Integer quantity = 1;
    private Integer quantityAvailable = 1;
    private Integer length;

    private List<Genre> genres;



    public Element(Integer id, String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, List<Genre> genres) {

        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.description = description;
        this.quantity = quantity;
        this.quantityAvailable = quantityAvailable;
        this.length = length;
        this.genres = genres;

    }

    public Element(String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, List<Genre> genres) {

        this.id = -1;
        this.title = title;
        this.releaseYear = releaseYear;
        this.description = description;
        this.quantity = quantity;
        this.quantityAvailable = quantityAvailable;
        this.length = length;
        this.genres = genres;

    }


    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getGenresAsString() {
        if (genres.isEmpty()){
            return "";
        }
        return genres.stream().map(Genre::getName).collect(Collectors.joining(", "));
    }


}