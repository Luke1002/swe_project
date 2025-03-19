module com.swe.libraryprogram {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires jdk.compiler;

    opens com.swe.libraryprogram to javafx.fxml;
    exports com.swe.libraryprogram;
    exports com.swe.libraryprogram.view;
    opens com.swe.libraryprogram.domainmodel to javafx.base;
    opens com.swe.libraryprogram.view to javafx.fxml;
}