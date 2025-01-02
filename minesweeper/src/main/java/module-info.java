module com.medialab.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires javafx.media;



    opens com.medialab.minesweeper to javafx.fxml;
    exports com.medialab.minesweeper;
    requires com.google.gson;
    requires java.desktop;
}