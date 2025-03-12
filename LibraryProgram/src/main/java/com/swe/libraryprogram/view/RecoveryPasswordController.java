package com.swe.libraryprogram.view;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class RecoveryPasswordController {

    @FXML
    private TextField email;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField confirmNewPassword;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private Connection connection;


}
