package com.swe.libraryprogram.controller;

import javafx.scene.control.TextFormatter;
import java.util.function.UnaryOperator;

public class ElementCheckViewController extends BaseViewController {

    UnaryOperator<TextFormatter.Change> onlyNumbersFilter = change -> {
        String newText = change.getControlNewText();

        if (newText.matches("\\d*") && !newText.startsWith("0") ) {
            if(newText.isEmpty()){
                return change;
            }
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
            return change;
        } else {
            return null;
        }
    };

    UnaryOperator<TextFormatter.Change> totalLength64Filter = change -> {
        String newText = change.getControlNewText();

        if (newText.length() <= 64) {
            return change;
        } else {
            return null;
        }
    };

    UnaryOperator<TextFormatter.Change> totalLength256Filter = change -> {
        String newText = change.getControlNewText();

        if (newText.length() <= 256) {
            return change;
        } else {
            return null;
        }
    };
}
