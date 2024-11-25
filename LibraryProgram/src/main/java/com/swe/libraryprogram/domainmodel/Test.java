package com.swe.libraryprogram.domainmodel;

public class Test {

    public static void main(String[] args) {
        Book libro = new Book(1, "gino", 2010, "blablablablabla", 10, 2, 300, null, 12345, "me", "tu", 500, 1);
        System.out.println(libro.toString());

        DigitalMedia dm = new DigitalMedia(1, "gino", 2010, "blablablablabla", 10, 2, 300, null, "zio", 309, 2, "mio");
        System.out.println(dm.toString());

        PeriodicPublication pp = new PeriodicPublication(1, "gino", 2010, "blablablablabla", 10, 2, 300, null, "zio", 309, 2, 2, 3333);
        System.out.println(pp.toString());
    }


}
