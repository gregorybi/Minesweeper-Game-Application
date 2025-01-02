package com.medialab.minesweeper.dialogs;

import com.google.gson.Gson;
import com.medialab.minesweeper.Data;
import com.medialab.minesweeper.Scenario;
import com.medialab.minesweeper.Statistics;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class StatisticsDialog {
    Dialog<String[]> dialog;
    private Label label = new Label("Statistics");
    private TableView table = new TableView();

    public StatisticsDialog(Stage stage) throws IOException, ParseException {
//        dialog = new Dialog<>();
//        dialog.setTitle("Statistics");
//        dialog.setHeaderText(null);
//        ObservableList<Statistics> list = FXCollections.observableArrayList(Statistics.getStatistics());

        Scene scene = new Scene(new Group());
        stage.setTitle("Statistics Table");
        stage.setWidth(400);
        stage.setHeight(500);

        //ButtonType createButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        //dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        label.setFont(new Font("Arial", 20));

        TableColumn <Data, String> minesNameCol = new TableColumn("Mines");
        minesNameCol.setCellValueFactory(new PropertyValueFactory<>("mines"));

        TableColumn <Data, String> triesCol = new TableColumn("Tries");
        triesCol.setCellValueFactory(new PropertyValueFactory<>("tries"));

        TableColumn <Data, String> timeCol = new TableColumn("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn <Data, String> winnerCol = new TableColumn("Winner");
        winnerCol.setCellValueFactory(new PropertyValueFactory<>("winner"));

        table.getColumns().addAll(minesNameCol, triesCol, timeCol, winnerCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


//        String jsonString = "[{\"mines\":10,\"tries\":0,\"winner\":false,\"time\":5}," +
//                "{\"mines\":10,\"tries\":4,\"winner\":false,\"time\":4}," +
//                "{\"mines\":10,\"tries\":2,\"winner\":false,\"time\":2}," +
//                "{\"mines\":10,\"tries\":3,\"winner\":false,\"time\":33}," +
//                "{\"mines\":10,\"tries\":2,\"winner\":false,\"time\":6}]";

        String jsonString = "";
        String pathName = "./src/main/java/com/medialab/minesweeper/Round.json";
        File file = new File(pathName);
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            jsonString = sc.nextLine();
        }

        //String jsonString = Files.readString(fileName);
        jsonString = jsonString.replace("false", "Computer");
        jsonString = jsonString.replace("true", "Player");

        Gson gson = new Gson();
        Data[] dataList = gson.fromJson(jsonString, Data[].class);


        ObservableList<Data> list = FXCollections.observableArrayList(dataList);

        table.setItems(list);

        //table.getColumns().addAll(minesNameCol, triesCol, timeCol, winnerCol);


        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);


        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/targ.png")));
        stage.setScene(scene);
        stage.show();


    }

    public Optional<String[]> showAndWait() {
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/targ.png")));
        return dialog.showAndWait();
    }
}
