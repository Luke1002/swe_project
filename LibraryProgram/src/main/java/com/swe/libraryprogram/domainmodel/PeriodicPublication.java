package com.swe.libraryprogram.domainmodel;

import java.util.LinkedList;



public class PeriodicPublication extends Element{


    private String publisher;
    private Integer frequency;
    private Integer releaseMonth;
    private Integer releaseDay;
    private Integer issn;



    public PeriodicPublication(Integer id, String title, Integer releaseYear, String description, Integer quantity, Integer quantityAvailable, Integer length, LinkedList<Genre> genres, String publisher, Integer frequency, Integer releaseMonth, Integer releaseDay, Integer issn) {

        super(id, title, releaseYear, description, quantity, quantityAvailable, length, genres);

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

    public Integer getIssn() {
        return issn;
    }

    public void setIssn(Integer issn) {
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
