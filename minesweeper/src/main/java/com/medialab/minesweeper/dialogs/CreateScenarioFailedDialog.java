package com.medialab.minesweeper.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CreateScenarioFailedDialog {
    private final Alert alert;

    public CreateScenarioFailedDialog (String reason) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Create Scenario Failed");
        alert.setHeaderText(null);
        alert.setContentText(String.format("Failed to create Scenario because \"%s\" .", reason));
    }

    public void showAndWait() {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/targ.png")));
        alert.showAndWait();
    }
}
