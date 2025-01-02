package com.medialab.minesweeper.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoadScenarioFailedDialog {
    private final Alert alert;

    public LoadScenarioFailedDialog (String scenarioId) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Load Scenario Failed");
        alert.setHeaderText(null);
        alert.setContentText(String.format("Scenario \"%s\" doesn't exist.", scenarioId));
    }

    public void showAndWait() {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/targ.png")));
        alert.showAndWait();
    }
}

