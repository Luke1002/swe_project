package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;


public class LibraryAdminService extends UserService {


    public Boolean addElement(Element element) {
        try {
            if(element.getTitle()== null || element.getTitle().trim().isEmpty()){
                System.out.println("Titolo non inserito");
                return false;
            }
            if(element.getDescription()== null){
                element.setDescription("");
            }
            if(element.getQuantity() == null || element.getQuantity() < 1){
                System.out.println("Quantità non inserita, deve essere almeno 1");
            }

            element.setQuantityAvailable(element.getQuantity());
            if (element.getClass() != Element.class) {
                Integer elementId = null;
                if (element instanceof Book) {
                    if(((Book) element).getIsbn() == null  || ((Book) element).getIsbn().matches("\\d{13}")){
                        System.out.println("ISBN non valido");
                        return false;
                    }
                    if ((MainService.getInstance().getBookDAO().getBookByIsbn(((Book) element).getIsbn())) != null) {
                        System.out.println("ISBN già presente nel database");
                        return false;
                    }
                    if(((Book) element).getAuthor() == null){
                        ((Book) element).setAuthor("");
                    }
                    if(((Book) element).getPublisher() == null){
                        ((Book) element).setPublisher("");
                    }
                    elementId = MainService.getInstance().getBookDAO().addBook((Book) element);
                } else if (element instanceof DigitalMedia) {
                    if(((DigitalMedia) element).getProducer()== null){
                        ((DigitalMedia) element).setProducer("");
                    }
                    if(((DigitalMedia) element).getAgeRating() == null){
                        ((DigitalMedia) element).setAgeRating("");
                    }
                    if(((DigitalMedia) element).getDirector() == null){
                        ((DigitalMedia) element).setDirector("");
                    }
                    elementId = MainService.getInstance().getDigitalMediaDAO().addDigitalMedia((DigitalMedia) element);
                } else if (element instanceof PeriodicPublication) {
                    if(((PeriodicPublication) element).getPublisher() == null){
                        ((PeriodicPublication) element).setPublisher("");
                    }
                    if(((PeriodicPublication) element).getFrequency() == null){
                        ((PeriodicPublication) element).setFrequency("");
                    }
                    if(((PeriodicPublication) element).getReleaseMonth() == null || ((PeriodicPublication) element).getReleaseMonth() >12 || ((PeriodicPublication) element).getReleaseMonth() < 1){
                        System.out.println("Mese non valido");
                        return false;
                    }
                    if(((PeriodicPublication) element).getReleaseDay() == null || ((PeriodicPublication) element).getReleaseDay() >31 || ((PeriodicPublication) element).getReleaseMonth() < 1){
                        System.out.println("Giorno non valido");
                        return false;
                    }
                    if (((Arrays.asList(new Integer[]{4, 6, 9, 11}).contains(((PeriodicPublication) element).getReleaseMonth()) && (((PeriodicPublication) element).getReleaseMonth() == 2 && ((PeriodicPublication) element).getReleaseDay() > 28) || ((PeriodicPublication) element).getReleaseDay() > 31 ))){
                        System.out.println("Giorno non valido rispetto al mese");
                        return false;
                    }
                    elementId = MainService.getInstance().getPeriodicPublicationDAO().addPeriodicPublication((PeriodicPublication) element);
                }
                if (elementId == null) {
                    return false;
                }
                for (Genre genre : element.getGenres()) {
                    if (!MainService.getInstance().getGenreDAO().associateGenreWithElement(elementId, genre.getCode())) {
                        System.err.println("Impossibile aggiungere " + genre.getName() + " alla lista dei generi associati");
                    }
                }
                return true;
            } else {
                System.out.println("Elemento da aggiungere non valido");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Impossibile connettersi al database");
            return false;
        }
    }

    public Boolean removeElement(Element element) {
        try {
            MainService.getInstance().getElementDAO().getElement(element.getId());
            MainService.getInstance().getElementDAO().removeElement(element.getId());
            return true;
        } catch (SQLException e) {
            System.out.println("Impossibile connettersi al database");
        }
        catch(NoSuchElementException e){
            System.out.println("L'elemento da eliminare non esiste.");
        }
        return false;
    }

    public Boolean updateElement(Element element) {
        try {
            if(element.getTitle()== null || element.getTitle().trim().isEmpty()){
                System.out.println("Titolo non inserito");
                return false;
            }
            if(element.getDescription()== null){
                element.setDescription("");
            }
            if(element.getQuantity() == null || element.getQuantity() < 1){
                System.out.println("Quantità non inserita, deve essere almeno 1");
            }

            element.setQuantityAvailable(element.getQuantity());
            if (element instanceof Book) {
                if(((Book) element).getIsbn() == null  ||((Book) element).getIsbn().matches("\\d{13}")){
                    System.out.println("ISBN non valido");
                    return false;
                }
                if ((MainService.getInstance().getBookDAO().getBookByIsbn(((Book) element).getIsbn())) != null) {
                    System.out.println("ISBN già presente nel database");
                    return false;
                }
                if(((Book) element).getAuthor() == null){
                    ((Book) element).setAuthor("");
                }
                if(((Book) element).getPublisher() == null){
                    ((Book) element).setPublisher("");
                }
                MainService.getInstance().getBookDAO().updateBook((Book) element);
            } else if (element instanceof DigitalMedia) {
                if(((DigitalMedia) element).getProducer()== null){
                    ((DigitalMedia) element).setProducer("");
                }
                if(((DigitalMedia) element).getAgeRating() == null){
                    ((DigitalMedia) element).setAgeRating("");
                }
                if(((DigitalMedia) element).getDirector() == null){
                    ((DigitalMedia) element).setDirector("");
                }
                MainService.getInstance().getDigitalMediaDAO().updateDigitalMedia((DigitalMedia) element);
            } else if (element instanceof PeriodicPublication) {
                if(((PeriodicPublication) element).getPublisher() == null){
                    ((PeriodicPublication) element).setPublisher("");
                }
                if(((PeriodicPublication) element).getFrequency() == null){
                    ((PeriodicPublication) element).setFrequency("");
                }
                if(((PeriodicPublication) element).getReleaseMonth() == null || ((PeriodicPublication) element).getReleaseMonth() >12 || ((PeriodicPublication) element).getReleaseMonth() < 1){
                    System.out.println("Mese non valido");
                    return false;
                }
                if(((PeriodicPublication) element).getReleaseDay() == null || ((PeriodicPublication) element).getReleaseDay() >31 || ((PeriodicPublication) element).getReleaseMonth() < 1){
                    System.out.println("Giorno non valido");
                    return false;
                }
                if (((Arrays.asList(new Integer[]{4, 6, 9, 11}).contains(((PeriodicPublication) element).getReleaseMonth()) && (((PeriodicPublication) element).getReleaseMonth() == 2 && ((PeriodicPublication) element).getReleaseDay() > 28) || ((PeriodicPublication) element).getReleaseDay() > 31 ))){
                    System.out.println("Giorno non valido rispetto al mese");
                    return false;
                }
                MainService.getInstance().getPeriodicPublicationDAO().updatePeriodicPublication((PeriodicPublication) element);
            } else {
                return false;
            }

            return true;
        } catch (
                SQLException e) {
            return false;
        }
    }

    public Boolean addGenre(String genreName) {
        if (genreName == null || genreName.trim().toLowerCase().isEmpty()) {
            return false;
        }
        try {
            MainService.getInstance().getGenreDAO().getGenreByName(genreName.trim().toLowerCase());
            System.out.println("Genere già presente nel database");
            return false;
        }catch (SQLException _){
        }
        Genre newGenre = new Genre(genreName.trim().toLowerCase());
        try {
            return MainService.getInstance().getGenreDAO().addGenre(newGenre);
        } catch (SQLException e) {
            System.out.println("Impossibile connettersi al database");
            return false;
        }
    }

}
