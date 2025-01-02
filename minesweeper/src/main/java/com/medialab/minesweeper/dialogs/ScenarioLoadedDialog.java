package com.medialab.minesweeper.dialogs;

import com.medialab.minesweeper.Scenario;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ScenarioLoadedDialog {
    private final Alert alert;

    public ScenarioLoadedDialog(String scenarioId) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Scenario Loaded");
        alert.setHeaderText(null);
        alert.setContentText(String.format("Scenario \"%s\" loaded successfully.", scenarioId));
    }

    public void showAndWait() {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/targ.png")));
        alert.showAndWait();
    }
}

