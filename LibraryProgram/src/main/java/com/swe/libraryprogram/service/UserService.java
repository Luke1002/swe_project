package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {


    public User login(String email, String password) {
        if (!(email == null || password == null)) {
            try {
                return MainService.getInstance().getUserDAO().authenticate(email, password);
            } catch (SQLException e) {
                System.out.println("Impossible connettersi con il database");
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public Boolean signup(String email, String password, String name, String surname, String phone) {
        if (email == null || email.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            System.out.println("E-mail non valida.");
            return false;
        }
        if (password == null || password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!#$%&=?@])[A-Za-z\\d!#$%&=?@]{8,20}$")) {
            System.out.println("Password non valida.");
            return false;
        }
        if (name == null || name.isEmpty()) {
            System.out.println("Nome non inserito.");
            return false;
        }
        if (surname == null || surname.isEmpty()) {
            System.out.println("Cognome non inserito.");
            return false;
        }
        if (phone != null && phone.matches("^[+]?[0-9]{0,1}[0-9]{1,4}[ ]?[0-9]{3}[ ]?[0-9]{6,7}$")) {
            System.out.println("Numeero di telefono non valido.");
            return false;
        }
        try {
            try {
                MainService.getInstance().getUserDAO().getUser(email);
                throw new RuntimeException("E-mail già in uso");
            } catch (RuntimeException _) {
            }
            return MainService.getInstance().getUserDAO().addUser(new User(email, password, name, surname, phone));
        } catch (SQLException e) {
            System.out.println("Impossible connettersi con il database");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void logout() {
        MainService.getInstance().resetUserState();
    }

    public List<Element> getAllElements() {
        List<Element> elements;
        try {
            elements = MainService.getInstance().getElementDAO().getAllElements();
            return elements;
        } catch (SQLException _) {
            return null;
        }
    }

    public List<Element> searchElements(List<Element> elements, String titleFilter, List<String> genresFilter, Integer yearFilter, Integer lengthFilter, Boolean isAvailable) {
        titleFilter = titleFilter == null ? "" : titleFilter;
        genresFilter = genresFilter == null ? new ArrayList<>() : genresFilter;
        List<Element> filteredElements = new ArrayList<>();
        isAvailable = isAvailable != null && isAvailable;
        for (Element element : elements) {
            Boolean titleFilterCompliant = titleFilter.isEmpty() || element.getTitle().toLowerCase().contains(titleFilter);
            Boolean genreFilterCompliant = genresFilter.isEmpty() || genresFilter.stream().allMatch(genre -> element.getGenresAsString().toLowerCase().contains(genre));
            Boolean yearFilterCompliant = (yearFilter == null || (element.getReleaseYear() != null && yearFilter.equals(element.getReleaseYear())));
            Boolean lengthFilterCompliant = (lengthFilter == null || (element.getLength() != null && lengthFilter <= element.getLength()));
            Boolean isAvailableCompliant = (!isAvailable || (element.getQuantityAvailable() != null && element.getQuantityAvailable() > 0));
            if (titleFilterCompliant && genreFilterCompliant && yearFilterCompliant && lengthFilterCompliant && isAvailableCompliant) {
                filteredElements.add(element);
            }
        }
        return filteredElements;
    }
}
