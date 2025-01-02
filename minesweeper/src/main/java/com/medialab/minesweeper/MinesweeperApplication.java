package com.medialab.minesweeper;

import com.medialab.minesweeper.dialogs.*;
import com.medialab.minesweeper.exceptions.InvalidDescriptionException;
import com.medialab.minesweeper.exceptions.FileNotFoundException;
import com.medialab.minesweeper.exceptions.InvalidValueException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.parser.ParseException;




import java.io.IOException;
import java.util.Objects;
import java.util.Optional;




public class MinesweeperApplication extends Application {

    public Scenario scenario;
    public Minesweeper minesweeper;
    public GridFX gridFX;
    private int counter;
    private Timeline timeline;

    private  Group root = new Group();
    private VBox vb;
    private Pane top_pane;

    String playing_filepath = "/sounds/t1.wav";

    private Label label1 = new Label("Total Number of mines:");
    private  Label label2 = new Label("Flag Number:");
    private Label label3 = new Label("Remaining Time:");


    /**
     * @return counter
     */
    public int getCounter() {
        return counter;
    }

    /**
     * @return the game timeline
     */
    public Timeline getTimeline() {
        return timeline;
    }

    /**
     * @return the flag number label
     */
    public Label getLabel2() {
        return label2;
    }


    /**
     * Menu Application -> Exit. Exits the game
     */
    private void exitApplication () {
        javafx.application.Platform.exit(); // System.exit(0);
    }

    /**
     * Menu Application -> Create. Creates the scenario text folder
     */
    private void createScenario() throws FileNotFoundException, IOException, InvalidDescriptionException, InvalidValueException {
        CreateScenarioDialog csd = new CreateScenarioDialog();
        Optional<String[]> result = csd.showAndWait();
        String[] scenarioToCreate;

        if (result.isPresent()) {
            scenarioToCreate = result.get();

            Scenario.createToFile(scenarioToCreate[0], Integer.parseInt(scenarioToCreate[1]),
                    Integer.parseInt(scenarioToCreate[2]),
                    Integer.parseInt(scenarioToCreate[4]), Integer.parseInt(scenarioToCreate[3]));

            ScenarioCreatedDialog scd = new ScenarioCreatedDialog(scenarioToCreate[0]);
            scd.showAndWait();
        }
        else {
            CreateScenarioFailedDialog csfd = new CreateScenarioFailedDialog("Invalid Value");
            csfd.showAndWait();
        }
    }

    /**
     * Menu Application -> Start. Start a new game.
     */
    private void startGame() throws InvalidValueException, IOException {
        this.gridFX = new GridFX(scenario, this);
        final ImageView selectedImage = new ImageView();
        Image image1 = new Image(getClass().getResourceAsStream("/img/targ2.png"), 770, 700, false, false);
        selectedImage.setImage(image1);


        root.getChildren().clear();
        root.getChildren().addAll(selectedImage, top_pane, vb, gridFX.getCanvas());
        timeline.play();
    }

    /**
     * Menu Application -> Load. Loads a new scenario.
     */
    private void loadGame() {
        LoadScenarioDialog lsd = new LoadScenarioDialog();

        Optional<String> result = lsd.showAndWait();
        if (result.isPresent()) {
            String scenarioId = result.get();
            System.out.printf("Scenario ID: \"%s\"\n", scenarioId);
            try {
                scenario = new Scenario(scenarioId);
                renderScenarioStats();
                ScenarioLoadedDialog dld = new ScenarioLoadedDialog(scenarioId);
                dld.showAndWait();

                this.minesweeper = new Minesweeper(scenario, this);
                this.gridFX = new GridFX(scenario, this);
                gridFX.deactivateCanvas();
                this.counter = scenario.getPlay_time();
                label3.setText("Remaining Time: "+Integer.toString(this.counter));
                timeline.stop();

                label2.setText("Flag Number:" + minesweeper.getFlags_number());

                root.getChildren().clear();
                root.getChildren().addAll(top_pane, vb, gridFX.getCanvas());
                //timeline.play();
            }
            catch (FileNotFoundException | InvalidDescriptionException | IOException | InvalidValueException e) {
                LoadScenarioFailedDialog lsfd = new LoadScenarioFailedDialog(scenarioId);
                lsfd.showAndWait();
            }
        }

    }

    private void renderScenarioStats() {
        label1.setText("Total Number of mines: " + scenario.getNum_of_mines());
    }

    /**
     * Menu Details -> Solution.
     * Player loses.
     * Mine positions are revealed
     */
    private void GameSolution() {
        minesweeper.loseGame();
    }

    private void GameStatistics() throws IOException, ParseException {
        try {
            Statistics.getStatistics();
        } catch (IOException e) {
            System.out.println("IOException from get statistics");
        } catch (ParseException e) {
           System.out.println("ParseException");
        }

        StatisticsDialog sdg = new StatisticsDialog(new Stage());
    }

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(MinesweeperApplication.class.getResource("minesweeper-view.fxml"));
        //Group root = new Group();
        //Scene scene = new Scene(root, 770, 600);
        Stage stage1 = new Stage();

        Image icon = new Image(getClass().getResourceAsStream("/img/targ.png"));
        stage.getIcons().add(icon);

        stage.setTitle("MediaLab Minesweeper");

        // create the menus
        Menu Application = new Menu("Application");
        Menu Details = new Menu("Details");

        // create menu Application items
        MenuItem Create = new MenuItem("Create");
        MenuItem Load = new MenuItem("Load");
        MenuItem Start = new MenuItem("Start");
        MenuItem Exit = new MenuItem("Exit");


        // create menu Details items
        MenuItem Rounds = new MenuItem("Rounds");
        MenuItem Solution = new MenuItem("Solution");



        // add menu Application items to menu
        Application.getItems().add(Create);
        Application.getItems().add(Load);
        Application.getItems().add(Start);
        Application.getItems().add(Exit);

        //add action events to menu Application Items
        Create.setOnAction(event -> {
            try {
                createScenario();
                //ScenarioCreatedDialog scd = new scd()
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found");
                CreateScenarioFailedDialog csfd = new CreateScenarioFailedDialog("File Not Found");
                csfd.showAndWait();
            } catch (IOException e) {
                CreateScenarioFailedDialog csfd = new CreateScenarioFailedDialog("IOException");
                csfd.showAndWait();
                e.printStackTrace();
            } catch (InvalidDescriptionException e) {
                System.out.println("Invalid Description");
                CreateScenarioFailedDialog csfd = new CreateScenarioFailedDialog("Invalid Description");
                csfd.showAndWait();
            } catch (InvalidValueException e) {
                System.out.println("Invalid value");
                CreateScenarioFailedDialog csfd = new CreateScenarioFailedDialog("Invalid Value");
                csfd.showAndWait();
            }
        });
        Load.setOnAction(event -> loadGame());

        Start.setOnAction(event -> {
            try {
                startGame();
            } catch (InvalidValueException e) {
                System.out.println("Invalid value");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IOException");
                e.printStackTrace();
            }
        });

        Exit.setOnAction(event -> exitApplication());

        // add menu Details items to menu
        Details.getItems().add(Rounds);
        Details.getItems().add(Solution);

        //add action events to menu Details Items
        Rounds.setOnAction(event -> {
            try {
                GameStatistics();
            } catch (IOException e) {
                System.out.println("IOException");
            } catch (ParseException e) {
                System.out.println("ParseException");
            }
        });
        Solution.setOnAction(event -> GameSolution());



        // create a menu bar
        MenuBar mb = new MenuBar();
        mb.prefWidthProperty().bind(stage.widthProperty());

        // add menus to menu bar
        mb.getMenus().add(Application);
        mb.getMenus().add(Details);

        // create a VBox
        vb = new VBox(mb);



        // create the three labels
        //Label label1 = new Label("Total Number of mines:");
        label1.setFont(new Font("Arial", 15));
        label1.setStyle("-fx-font-weight: bold");
        label1.relocate(100, 30);

        //Label label2 = new Label("Flag Number:");
        label2.setFont(new Font("Arial", 15));
        label2.setStyle("-fx-font-weight: bold");
        label2.relocate(100, 50);

        //Label label3 = new Label("Remaining Time:");
        label3.setFont(new Font("Arial", 15));
        label3.setStyle("-fx-font-weight: bold");
        label3.relocate(100, 70);


        //create the top pane
        top_pane = new Pane();
        top_pane.setPrefSize(700,100);


        top_pane.getChildren().add(label1);
        top_pane.getChildren().add(label2);
        top_pane.getChildren().add(label3);

        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            this.counter--;
            label3.setText("Remaining Time: "+ this.counter);
            if (counter == 0) {
                //minesweeper.loseGame();
                timeline.stop();
                minesweeper.loseGame();
            }
            //System.out.println(counter);
        }));
        this.timeline.setCycleCount(Animation.INDEFINITE);

        //Add Image
        final ImageView selectedImage = new ImageView();
        Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/targ2.png")), 770, 700, false, false);
        selectedImage.setImage(image1);


        //Adding the nodes to the root
        root.getChildren().clear();
        root.getChildren().add(selectedImage);
        root.getChildren().add(top_pane);
        root.getChildren().add(vb);

        //Creating the Scene
        Scene scene = new Scene(root, 770, 700);
        stage.setScene(scene);
        stage.show();

        Music song_start = new Music(playing_filepath);
        

        //PlayMusic(filepath);


    }



    public static void main(String[] args) {
        launch();
    }
}