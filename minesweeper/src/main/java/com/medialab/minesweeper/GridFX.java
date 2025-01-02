package com.medialab.minesweeper;

import com.medialab.minesweeper.exceptions.InvalidDescriptionException;
import com.medialab.minesweeper.exceptions.InvalidValueException;
import com.medialab.minesweeper.exceptions.PositionChoiceException;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


import java.io.IOException;

public class GridFX {
    private boolean clickable;

    private final int fontsize = 40;
    private final int squareEdge = 33;
    private final int textDispo = 8;
    private final Canvas canvas;
    private MinesweeperApplication app;


    /**
     * Constructs the game grid and adds a listener to it
     * @param scenario scenario of the game
     * @param app app of the game
     */

    public GridFX (Scenario scenario,MinesweeperApplication app) throws IOException, InvalidValueException {


        //minesweeper = new Minesweeper(scenario);
        /*
        rows = minesweeper.getgrid().length;
        cols = minesweeper.getgrid()[0].length;
        */
        this.app = app;
        int rows, cols;

        if (scenario.getDifficulty_level() == 1) {
            rows = 9;
            cols = 9;
        } else if (scenario.getDifficulty_level() == 2) {
            rows = 16;
            cols = 16;
        } else {
            throw new InvalidValueException();
        }


        canvas = new Canvas(rows*squareEdge, cols*squareEdge);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.relocate(100, 100);

        gc.setFill(Color.GRAY);
        gc.setLineWidth(2.0);

        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
             gc.strokeRect(this.squareEdge*i, this.squareEdge*j, this.squareEdge, this.squareEdge);
             gc.fillRect(this.squareEdge*i, this.squareEdge*j, this.squareEdge, this.squareEdge);

            }
        }

        //add mouse listener
        canvas.setOnMouseClicked(event -> {
            //System.out.println("Clicked on canvas");
            int row, col;
            row = (int) event.getX() / squareEdge;
            col = (int) event.getY() / squareEdge;
            try {
                if (event.getButton() == MouseButton.SECONDARY) {
                    app.minesweeper.MarkCell(row, col, app.scenario);
                } else if (event.getButton() == MouseButton.PRIMARY) {
                    app.minesweeper.PlayAtPosition(row, col);
                }
            } catch (PositionChoiceException e) {
                System.out.println("Wrong Position");
            } catch (InvalidValueException e) {
                System.out.println("Wrong number of flags");
            }
        });

    }

    //getters

    /**
     * @return the game canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }


    /**
     * Updates a grid cell if something happens to it.
     * @param x x-coordinate of the cell
     * @param y y-coordinate of the cell
     * @param cs char to write in the cell
     */
    public void updateCell (int x, int y, char cs) {
        switch (cs) {
            case '0':
                colorHelper(x, y, null, Color.WHITE, null);
                break;
            case '1':
                colorHelper(x, y, "1", Color.WHITE, Color.BLUE);
                break;
            case '2':
                colorHelper(x, y, "2", Color.WHITE, Color.GREEN);
                break;
            case '3':
                colorHelper(x, y, "3", Color.WHITE, Color.RED);
                break;
            case '4':
                colorHelper(x, y, "4", Color.WHITE, Color.PURPLE);
                break;
            case '5':
                colorHelper(x, y, "5", Color.WHITE, Color.MAROON);
                break;
            case '6':
                colorHelper(x, y, "6", Color.WHITE, Color.TURQUOISE);
                break;
            case '7':
                colorHelper(x, y, "7", Color.WHITE, Color.BLACK);
                break;
            case '8':
                colorHelper(x, y, "8", Color.WHITE, Color.GREY);
                break;
            case 'F':
                colorHelper(x, y, "F", Color.BLUEVIOLET, Color.CHOCOLATE);
                break;
            case 'M':
                colorHelper(x, y, "B", Color.BLACK, Color.RED);
                break;
            case '.':
                colorHelper(x, y, null, Color.GREY, null);
                break;
        }


    }

    private void colorHelper(int x, int y, String state, Color cellColor, Color charColor) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(cellColor);
        gc.fillRect(this.squareEdge*x + 1, this.squareEdge*y + 1, this.squareEdge - 2, this.squareEdge -2);

        if (state != null) {
            gc.setFill(charColor);
            gc.fillText(state,this.squareEdge*x + this.textDispo, this.squareEdge*(y + 1) - this.textDispo);
        }

    }

    /**
     * Deactivates the game canvas if the game is lost.
     */
    public void deactivateCanvas() {
        canvas.setOnMouseClicked(event -> {
        });
    }
}
