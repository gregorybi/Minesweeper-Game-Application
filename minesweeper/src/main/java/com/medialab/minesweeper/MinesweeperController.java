package com.medialab.minesweeper;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MinesweeperController {
    // top pane
    @FXML
    private Label MineNumberLabel;

    @FXML
    private Label FlagNumberLabel;

    @FXML
    private Label RemainingTimeLabel;

    @FXML
    public Canvas Grid;

    @FXML
    protected void initialize() {

    }

    private Minesweeper  minesweeper;
}