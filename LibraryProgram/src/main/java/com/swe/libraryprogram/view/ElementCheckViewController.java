package com.swe.libraryprogram.view;

import javafx.scene.control.TextFormatter;
import java.util.function.UnaryOperator;

public class ElementCheckViewController extends BaseViewController {

    UnaryOperator<TextFormatter.Change> onlyNumbersFilter = change -> {
        String newText = change.getControlNewText();

        if (newText.matches("\\d*") && !newText.startsWith("0")) {
            try{
                Integer.parseInt(newText);
                return change;
            }
            catch(NumberFormatException e){
                return null;
            }
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
            showDays();
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

    protected void showFields() {
    }

    protected void showDays() {
    }
}
