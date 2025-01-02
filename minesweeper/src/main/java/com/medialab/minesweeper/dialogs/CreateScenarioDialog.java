package com.medialab.minesweeper.dialogs;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;

public class CreateScenarioDialog {
    Dialog<String[]> dialog;

    public CreateScenarioDialog() {
        dialog = new Dialog<>();
        dialog.setTitle("Create Scenario");
        dialog.setHeaderText(null);

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField ScenarioIdTextField = new TextField();
        //ScenarioIdTextField.setPromptText("Scenario ID");
        TextField DiffLevelTextField = new TextField();
        //dictionaryIdTextField.setPromptText("Difficulty Level");
        TextField MineNumberTextField = new TextField();

        ToggleGroup group = new ToggleGroup();
        RadioButton button1 = new RadioButton("0");
        button1.setToggleGroup(group);
        button1.setSelected(true);
        RadioButton button2 = new RadioButton("1");
        button2.setToggleGroup(group);

        //TextField SuperMineTextField = new TextField();
        TextField TimeCounterTextField = new TextField();



        grid.add(new Label("Scenario ID:"), 0, 0);
        grid.add(ScenarioIdTextField, 1, 0);

        grid.add(new Label("Difficulty level:"), 0, 1);
        grid.add(DiffLevelTextField, 1, 1);

        grid.add(new Label("Number Of Mines:"), 0, 2);
        grid.add(MineNumberTextField, 1, 2);

        grid.add(new Label("Super Mine"), 0, 3);
        grid.add(button1, 1, 3);
        grid.add(button2, 2, 3);

        grid.add(new Label("Time Counter"), 0, 4);
        grid.add(TimeCounterTextField, 1, 4);


        RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
        //String toogleGroupValue = selectedRadioButton.getText();

        Node createButton = dialog.getDialogPane().lookupButton(createButtonType);


        ScenarioIdTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(!ScenarioIdTextField.getText().matches("[0-9_]+"));
        });

        DiffLevelTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(!DiffLevelTextField.getText().matches("[0-9_]+"));
        });

        MineNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(!MineNumberTextField.getText().matches("[0-9_]+"));
        });


        dialog.getDialogPane().setContent(grid);

        //Platform.runLater(DiffLevelTextField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                if (!ScenarioIdTextField.getText().isEmpty() && !DiffLevelTextField.getText().isEmpty()
                    &&  !MineNumberTextField.getText().isEmpty() && !selectedRadioButton.getText().isEmpty()
                    && !TimeCounterTextField.getText().isEmpty())

                    System.out.println(((RadioButton)group.getSelectedToggle()).getText());

                ((RadioButton)group.getSelectedToggle()).getText();

                    return new String[]{ScenarioIdTextField.getText(), DiffLevelTextField.getText(),
                                    MineNumberTextField.getText(), ((RadioButton)group.getSelectedToggle()).getText(),
                                    TimeCounterTextField.getText()};
            }
            return null;
        });
    }

    public Optional<String[]> showAndWait() {
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/targ.png")));
        return dialog.showAndWait();
    }
}

