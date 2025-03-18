package com.swe.libraryprogram.domainmodel;

import java.util.List;



public class PeriodicPublication extends Element{


    private String publisher;
    private Integer frequency;
    private Integer releaseMonth;
    private Integer releaseDay;
    private String issn;



    public PeriodicPublication(Integer id, String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, List<Genre> genres, String publisher, Integer frequency, Integer releaseMonth, Integer releaseDay, String issn) {

        super(id, title, releaseYear, description, quantity, quantityAvailable, length, genres);

        this.publisher = publisher;
        this.frequency = frequency;
        this.releaseMonth = releaseMonth;
        this.releaseDay = releaseDay;
        this.issn = issn;

    }

    public PeriodicPublication(String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, List<Genre> genres, String publisher, Integer frequency, Integer releaseMonth, Integer releaseDay, String issn) {

        super(title, releaseYear, description, quantity, quantityAvailable, length, genres);

        this.publisher = publisher;
        this.frequency = frequency;
        this.releaseMonth = releaseMonth;
        this.releaseDay = releaseDay;
        this.issn = issn;

    }


    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
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

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }


    @Override
    public String toString() {
        return "PeriodicPublication{" +
                "publisher='" + publisher + '\'' +
                ", frequency=" + frequency +
                ", releaseMonth=" + releaseMonth +
                ", releaseDay=" + releaseDay +
                ", issn=" + issn +
                '}';
    }

}
