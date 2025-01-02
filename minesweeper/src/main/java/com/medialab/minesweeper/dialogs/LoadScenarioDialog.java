package com.medialab.minesweeper.dialogs;

import com.medialab.minesweeper.Scenario;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

public class LoadScenarioDialog {
    private final ChoiceDialog<String> dialog;

    public LoadScenarioDialog() {
        String[] choices = Scenario.getScenarioIds();

        if (choices.length == 0) {
            dialog = new ChoiceDialog<>();
            dialog.setHeaderText("No scenarios found");
            dialog.getDialogPane().getContent().setDisable(true);
            dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        }

        else {
            dialog = new ChoiceDialog<>(choices[0], choices);
            dialog.setHeaderText("Load a scenario");
        }
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Load");
        dialog.setTitle("Load Scenario");
        dialog.setContentText("Select scenario ID:");
    }

    public Optional<String> showAndWait() {
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/targ.png")));
        return dialog.showAndWait();
    }


}

