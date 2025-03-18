package com.swe.libraryprogram.domainmodel;



public class Genre {

    private final String name;
    private final Integer code;


    public Genre(String name, Integer code) {

        this.name = name;
        this.code = code;

    }

    public Genre(String name) {

        this.name = name;
        this.code = null;

    }


    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }

}
