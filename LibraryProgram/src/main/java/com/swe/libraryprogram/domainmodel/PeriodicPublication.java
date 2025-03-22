package com.swe.libraryprogram.domainmodel;

import java.util.List;


public class PeriodicPublication extends Element {


    private String publisher;
    private String frequency;
    private Integer releaseMonth;
    private Integer releaseDay;


    public PeriodicPublication(Integer id, String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, List<Genre> genres, String publisher, String frequency, Integer releaseMonth, Integer releaseDay) {

        super(id, title, releaseYear, description, quantity, quantityAvailable, length, genres);

        this.publisher = publisher;
        this.frequency = frequency;
        this.releaseMonth = releaseMonth;
        this.releaseDay = releaseDay;

    }

    public PeriodicPublication(String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, List<Genre> genres, String publisher, String frequency, Integer releaseMonth, Integer releaseDay) {

        super(title, releaseYear, description, quantity, quantityAvailable, length, genres);

        this.publisher = publisher;
        this.frequency = frequency;
        this.releaseMonth = releaseMonth;
        this.releaseDay = releaseDay;

    }


    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Integer getReleaseMonth() {
        return releaseMonth;
    }

    public void setReleaseMonth(Integer releaseMonth) {
        this.releaseMonth = releaseMonth;
    }

    public Integer getReleaseDay() {
        return releaseDay;
    }

    public void setReleaseDay(Integer releaseDay) {
        this.releaseDay = releaseDay;
    }


    @Override
    public String toString() {
        return "PeriodicPublication{" +
                "publisher='" + publisher + '\'' +
                ", frequency=" + frequency +
                ", releaseMonth=" + releaseMonth +
                ", releaseDay=" + releaseDay +
                '}';
    }

}
