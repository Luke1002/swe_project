package com.swe.libraryprogram.view;

import javafx.scene.control.TextFormatter;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public class CheckViewController extends BaseViewController {

    UnaryOperator<TextFormatter.Change> onlyNumbersFilter = change -> {
        String newText = change.getControlNewText();

        if (newText.matches("\\d*") && !newText.startsWith("0")) {
            if (!newText.isEmpty() && (Long.valueOf(Integer.MAX_VALUE) < Long.parseLong(newText))) {
                return null;
            }
            return change;
        } else {
            return null;
        }
    };

    UnaryOperator<TextFormatter.Change> isbnFilter = change -> {
        String newText = change.getControlNewText();

        if (newText.matches("\\d*") && newText.length() <= 13) {
            return change;
        } else {
            return null;
        }
    };


    UnaryOperator<TextFormatter.Change> yearFilter = change -> {
        String newText = change.getControlNewText();

        if (newText.matches("\\d*") && newText.length() <= 4) {
            //showDays();
            return change;
        } else {
            return null;
        }
    };

    UnaryOperator<TextFormatter.Change> ageFilter = change -> {
        String newText = change.getControlNewText();

        if (newText.matches("\\d*") && !newText.startsWith("0")) {
            if(!newText.isEmpty() && Integer.parseInt(newText) < 120) {
                return change;
            }
            return null;
        }
        return null;
    };

    UnaryOperator<TextFormatter.Change> totalLengthFilter = change -> {
        String newText = change.getControlNewText();

        if (newText.length() <= 64) {
            return change;
        } else {
            return null;
        }
    };

//    public void showFields() {
//        curr_element_type = choiceBox.getValue();
//        if (curr_element_type.equals("Libro")) {
//            editorText.setText("Casa editrice: ");
//            authorText.setText("Autore: ");
//            authorBox.setVisible(true);
//            bookBox.setVisible(true);
//            digitalMediaBox.setVisible(false);
//            periodicPublicationBox.setVisible(false);
//        } else if (curr_element_type.equals("Digital Media")) {
//            editorText.setText("Casa produttrice: ");
//            authorText.setText("Direttore: ");
//            authorBox.setVisible(true);
//            bookBox.setVisible(false);
//            digitalMediaBox.setVisible(true);
//            periodicPublicationBox.setVisible(false);
//
//        } else if (curr_element_type.equals("Periodico")) {
//            editorText.setText("Casa editrice: ");
//            authorBox.setVisible(false);
//            bookBox.setVisible(false);
//            digitalMediaBox.setVisible(false);
//            periodicPublicationBox.setVisible(true);
//
//        }
//    }
//
//    public void showDays() {
//        int curr_month = monthBox.getValue();
//        int curr_year;
//        if (yearField.getText().isEmpty()) {
//            curr_year = 0;
//        } else {
//            curr_year = Integer.parseInt(yearField.getText());
//        }
//        if (curr_month == 2) {
//            dayBox.getItems().setAll(Arrays.copyOfRange(days, 0, 28));
//            if (curr_year % 4 == 0 && curr_year % 100 != 0) {
//                dayBox.getItems().add(days[28]);
//            }
//        } else if (curr_month == 4 || curr_month == 6 || curr_month == 9 || curr_month == 11) {
//            dayBox.getItems().setAll(Arrays.copyOfRange(days, 0, 29));
//        } else {
//            dayBox.getItems().setAll(days);
//        }
//    }
}
