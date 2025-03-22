package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService {


    public User login(String email, String password) {
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            return null;
        }
        try {
            return MainService.getInstance().getUserDAO().authenticate(email, password);
        }
        catch (SQLException e){
            return null;
        }
    }

    public Boolean signup(String email, String password, String name, String surname, String phone) {
        if (email == null || password == null || name == null || surname == null || email.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty() || password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!#$%&=?@])[A-Za-z\\d!#$%&=?@]{8,20}$")) {
            return false;
        }
        try{
                if (MainService.getInstance().getUserDAO().getUser(email) != null) {
                    return false;
                }
            else{

                return MainService.getInstance().getUserDAO().addUser(new User(email, password, name, surname, phone));
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public void logout() {
        MainService.getInstance().resetUserState();
    }

    public List<Element> getAllElements() {
        List<Element> elements = new ArrayList<>();
        try {
            elements = MainService.getInstance().getElementDAO().getAllElements();
            return elements;
        } catch (SQLException _) {
            return null;
        }
    }

    public List<Element> searchElements(List<Element> elements, String titleFilter, List<String> genresFilter, Integer yearFilter, Integer lengthFilter, Boolean isAvailable) {
        List<Element> filteredElements = new ArrayList<>();
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
