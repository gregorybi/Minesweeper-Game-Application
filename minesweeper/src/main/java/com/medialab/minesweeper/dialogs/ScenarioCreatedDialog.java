package com.medialab.minesweeper.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ScenarioCreatedDialog {
    private final Alert alert;

    public ScenarioCreatedDialog(String scenarioId) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Scenario Created");
        alert.setHeaderText(null);
        alert.setContentText(String.format("Scenario \"%s\" created successfully.", scenarioId));
    }

    public void showAndWait() {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/targ.png")));
        alert.showAndWait();
    }
}
