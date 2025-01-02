package com.medialab.minesweeper.dialogs;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GameWonDialog {
    private final Alert alert;

    public GameWonDialog() {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Won!");
        alert.setHeaderText(null);
        alert.setContentText("You Won! Computer lost.");
        alert.setOnHidden(evt -> Platform.requestNextPulse());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/targ.png")));
        alert.show();
    }

    public void showAndWait() {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/targ.png")));
        alert.showAndWait();
    }
}