package com.swe.libraryprogram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.IOException;
import java.sql.Statement;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setMinHeight(240);
        stage.setMinWidth(320);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "Oiomaasile200!";

        Connection c;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(jdbcUrl,username, password);
            Statement statement = c.createStatement();
            statement.execute("INSERT INTO cars(brand, model, year) VALUES ('Fiat', 'Panda', 2008);");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

        System.out.println("Opened database successfully");

        launch();
    }
}