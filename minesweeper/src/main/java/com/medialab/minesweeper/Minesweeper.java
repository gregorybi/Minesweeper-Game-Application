package com.medialab.minesweeper;

import com.medialab.minesweeper.dialogs.GameLostDialog;
import com.medialab.minesweeper.dialogs.GameWonDialog;
import com.medialab.minesweeper.exceptions.InvalidValueException;
import com.medialab.minesweeper.exceptions.PositionChoiceException;
import javafx.application.Application;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;



import static java.lang.Character.isDigit;

public class Minesweeper {
    private final Player player;
    //private final Scenario scenario;
    private GameStatus gameStatus = GameStatus.IN_PROGRESS;
    private static final String minesPath = "./src/main/java/com/medialab/minesweeper/mines";
    private static final String pathNameFormat = "./src/main/java/com/medialab/minesweeper/mines/mines_%s.txt";
    private int [][]grid;
    private char [][]grid_to_show;
    private int flags_number = 0;

    private MinesweeperApplication app;



    public enum GameStatus {
        PLAYER_WIN,
        PLAYER_LOSE,
        IN_PROGRESS
    }

    static class pair
    {
        int first, second;

        public pair(int first, int second)
        {
            this.first = first;
            this.second = second;
        }
    }

   // static final int ROW = grid.length;
    //static final int COL = grid[0].length;

    // Direction vectors
    static int dRow[] = { -1, 0, 1, 0, -1, -1, 1, 1 };
    static int dCol[] = { 0, 1, 0, -1, -1, 1, -1, 1 };

    // Function to check if a cell
    // is be visited or not
    private boolean isValid(boolean vis[][],
                           int row, int col)
    {

        // If cell lies out of bounds
        if (row < 0 || col < 0 ||
                row >= grid.length || col >= grid[0].length)
            return false;

        // If cell is already visited
        if (vis[row][col])
            return false;

        // Otherwise
        return true;
    }


    /**
     * Create a new minesweeper game
     * @param scenario the scenario of the game
     */
    public Minesweeper(Scenario scenario, MinesweeperApplication app) throws IOException {
        player = new Player();
        this.app = app;

        if (scenario.getDifficulty_level() == 1) {
            grid = new int[9][9];
            grid_to_show = new char[9][9];
            for (int i=0; i< grid.length; i++)
                for (int j=0; j< grid[0].length; j++)
                    grid_to_show[i][j] = '.';
        }
        else if (scenario.getDifficulty_level() == 2) {
            grid = new int[16][16];
            grid_to_show = new char[16][16];
            for (int i=0; i< grid.length; i++)
                for (int j=0; j< grid[0].length; j++)
                    grid_to_show[i][j] = '.';
        }

        //Assign mines to the grid
        randomPos(grid, scenario.getNum_of_mines());

        //super mine existence and assignment
        if (scenario.getSup_mine() == 1)
            super_mine_assign(grid);

        //Create the mines file
        write_mines_file(scenario.getId(), grid);
    }

    //getters

    /**
     * @return the game grid
     */
    public int [][]getgrid() { return grid; }

    /**
     * @return the grid representing the game situation
     */
    public char [][]getGrid_to_show() {return grid_to_show;}

    /**
     * @return status of the game
     * @see GameStatus
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * @return total flags number
     */
    public int getFlags_number() {return flags_number;}

    /**
     * Sets the game as lost.
     */
    public void loseGame() {
        for (int i=0; i<grid.length; i++)
            for (int j=0; j<grid[0].length; j++)
                if (grid[i][j] != 0) {
                    grid_to_show[i][j] = 'M';
                    app.gridFX.updateCell(i, j, 'M');
                }

        app.gridFX.deactivateCanvas();
        app.getTimeline().stop();
        GameLostDialog gld = new GameLostDialog();

        gameStatus = GameStatus.PLAYER_LOSE;

        try {
            Statistics.addStatistics(
                    app.scenario.getNum_of_mines(),
                    player.getTotalTries(),
                    app.scenario.getPlay_time() - app.getCounter(),
                    false);
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ParseException e) {
            System.out.println("ParseException");
        }
    }

    /**
     * Sets the game as won.
     */
    public void wonGame() {
        for (int i=0; i<grid.length; i++)
            for (int j=0; j<grid[0].length; j++)
                if (grid[i][j] != 0) {
                    grid_to_show[i][j] = 'M';
                    app.gridFX.updateCell(i, j, 'M');
                }

        app.gridFX.deactivateCanvas();
        app.getTimeline().stop();

        gameStatus = GameStatus.PLAYER_WIN;

        try {
            Statistics.addStatistics(
                    app.scenario.getNum_of_mines(),
                    player.getTotalTries(),
                    app.scenario.getPlay_time() - app.getCounter(),
                    true);
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ParseException e) {
            System.out.println("ParseException");
        }
    }

    /**
     * Check if the player wins
     * @return true if player wins or false if not
     */
    public boolean check_win_Game() {
        //check the entirety of the grid
        for (int i=0; i< grid.length; i++)
            for (int j=0; j<grid[0].length; j++) { //check all the grid_to_show cells

                //if there is unopened clear cell, the game is not over
                if (grid_to_show[i][j] == '.' && grid[i][j] == 0)
                    return false;

                //if there is flag in clear cell, the game is not over
                else if (grid_to_show[i][j] == 'F' && grid[i][j] == 0)
                    return false;
            }
        //else the game is over
        //all the clear cells contain mines
        gameStatus = GameStatus.PLAYER_WIN;
        return true;
    }

    /**
     * Click at the (row, column) box in the game grid
     * @param row the row of the box
     * @param column the column of the box
     * @return char grid with M if mine is clicked or grid with the number of adjacent mines if clear box clicked
     * @throws PositionChoiceException if the position is out of bounds or already clicked or already revealed by super mine
     */
    public char[][] PlayAtPosition (int row, int column) throws PositionChoiceException {
        assert gameStatus == GameStatus.IN_PROGRESS;
        //int adjacent_mines = -1;

        //if position is out of bounds or already played,
        //or it is a mine cell revealed by super mine
        if (row < 0 || column < 0 ||
                row >= grid.length || column >= grid[0].length
            || isDigit(grid_to_show[row][column]) || grid_to_show[row][column] == 'M')
            throw new PositionChoiceException();

        if (grid[row][column] != 0) { //clicked in mine
            loseGame();
            grid_to_show[row][column] = 'M';
            app.gridFX.updateCell(row, column, grid_to_show[row][column]);
            printGrid();
        }

        else { //not clicked in mine
            boolean [][]vis = new boolean[grid.length][grid[0].length];

             if ((check_neighbors(row, column)) == 0) { //run BFS for the adjacent boxes

                 //the box does not have adjacent mines so run BFS for the adjacent boxes
                 //and mark it with 0
                 grid_to_show[row][column] = (char) (check_neighbors(row, column)+'0');
                 app.gridFX.updateCell(row, column, grid_to_show[row][column]);

                 // Stores indices of the matrix cells
                 Queue<pair> q = new LinkedList<>();

                 // Mark the starting cell as visited
                 // and push it into the queue
                 q.add(new pair(row, column));
                 vis[row][column] = true;

                 // Iterate while the queue
                 // is not empty
                 while (!q.isEmpty()) {
                     pair cell = q.peek();
                     int x = cell.first;
                     int y = cell.second;

                     //System.out.print(grid[x][y] + " ");

                     q.remove();

                     // Go to the adjacent cells
                     for (int i = 0; i < 8; i++) {
                         int adjx = x + dRow[i];
                         int adjy = y + dCol[i];

                         //BFS must not look in cells with numbers, or cell with uncovered mine from super mine
                         if (isValid(vis, adjx, adjy) && !(isDigit(grid_to_show[adjx][adjy]))
                            && grid_to_show[adjx][adjy] != 'M') {

                             if (check_neighbors(adjx, adjy) == 0)
                             { //adjacent box is clear, so add it to the queue
                                 q.add(new pair(adjx, adjy));
                                 vis[adjx][adjy] = true;
                             }

                             //if cell with flag is uncovered the flag is deleted and flags_number--
                             if (grid_to_show[adjx][adjy] == 'F')
                                 flags_number--;

                             grid_to_show[adjx][adjy] = (char)(check_neighbors(adjx, adjy)+'0');

                             app.gridFX.updateCell(adjx, adjy, grid_to_show[adjx][adjy]);
                             printGrid();
                         }
                     }
                 }
             }

             else { // the box has adjacent mines so return the grid with the number of
                    //adjacent mines for that box
                    //if the box has a flag, delete it and flags_number--

                 if (grid_to_show[row][column] == 'F')
                     flags_number--;

               grid_to_show[row][column] = (char) (check_neighbors(row, column)+'0');

               app.gridFX.updateCell(row, column, grid_to_show[row][column]);
               printGrid();
             }
             player.Successful_try(); //add 1 to the player successful tries counter

            if (check_win_Game()) { // check if player has won the game
                GameWonDialog gwd = new GameWonDialog();
                wonGame();
            }
        }
        this.app.getLabel2().setText("Flag Number:" + flags_number);
        System.out.println();


        return grid_to_show;
    }

    /**
     * Marks a grid cell with a flag or removes the flag if exists.
     * The cell is considered still playable.
     * @param row row of marked cell
     * @param column column of marked cell
     * @return char grid with 'F' in the flag position or '.' if the flag is removed
     * @throws PositionChoiceException if the position is out of bounds or already revealed
     * @throws InvalidValueException if the flags are more than the mine number
     */
    public char[][] MarkCell (int row, int column, Scenario scenario) throws  PositionChoiceException,
            InvalidValueException{
        assert gameStatus == GameStatus.IN_PROGRESS;

        //if position is out of bounds or already revealed
        if (row < 0 || column < 0 ||
                row >= grid.length || column >= grid[0].length
                || isDigit(grid_to_show[row][column]))
            throw new PositionChoiceException();

        if (player.getTotalTries()<5 && grid[row][column] == 2) {
            //if player marked super mine and has tried at most 4 times
            //reveal the row and column of super mine
            show_super_mine(row, column);
        }

        else if (grid_to_show[row][column] == '.') { //mark the cell
            if (flags_number + 1 > scenario.getNum_of_mines())
                throw new InvalidValueException();

            grid_to_show[row][column] = 'F';
            flags_number++;
        }
        else if (grid_to_show[row][column] == 'F') { //unmark the cell
            grid_to_show[row][column] = '.';
            flags_number--;
        }

        /*System.out.println(flags_number);
        System.out.println(scenario.getNum_of_mines());*/

        this.app.getLabel2().setText("Flag Number:" + flags_number);
        this.app.gridFX.updateCell(row, column, grid_to_show[row][column]);

        return grid_to_show;
    }

    /**
     * Place mines (ones) randomly in the grid
     * Assign super mine (3rd mine to place)
     * @param grid the 2D array  representing the grid
     * @param mine_num the number of mines to assign
     * @return the new grid with the mines placed
     */
    public int[][] randomPos(int[][] grid, int mine_num) {

        for (int k = 0; k < mine_num; k++) {
            Random randomPosition = new Random();
            int randomX = randomPosition.nextInt(grid.length);
            int randomY = randomPosition.nextInt(grid[0].length);

            while (grid[randomX][randomY] == 1) { //if there is already a mine recalculate position
                randomX = randomPosition.nextInt(grid.length);
                randomY = randomPosition.nextInt(grid[0].length);
            }
            grid[randomX][randomY] = 1;
        }
        return grid;
    }

    /**
     * Assign super mine if required.
     * The super mine is the 1st that I encounter if I search the grid
     * The super mine value is 2 (the others are being represented with 1)
     * @param grid the game grid
     * @return the new game grid with the super mine
     */

    public int[][] super_mine_assign(int[][] grid) {

        int counter = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    counter = 1;
                    grid[i][j] = 2;
                    break;
                }
            }
            if (counter == 1)
                break;
        }

        return grid;
    }

    /**
     * Reveal the row and column of the super mine cell
     * @param x row of super mine cell
     * @param y column of super mine cell
     * @return the new char grid
     */
    public char[][] show_super_mine(int x, int y) {

        //reveal the column of the super mine
        for(int i=0; i<grid.length; i++)
            if (!isDigit(grid_to_show[i][y])) {//if the cell is not opened yet

                if (grid_to_show[i][y] == 'F') //if the cell has flag remove it and flags_number--
                    flags_number--;

                if (grid[i][y] == 1) {
                    grid_to_show[i][y] = 'M'; //mark the mine
                    app.gridFX.updateCell(i, y, grid_to_show[i][y]);
                }
                else {//show number of adjacent mines
                    grid_to_show[i][y] = (char) (check_neighbors(i, y) + '0');
                    app.gridFX.updateCell(i, y, grid_to_show[i][y]);
                }
            }

        //reveal the row of the super mine
        for(int i=0; i<grid[0].length; i++)
            if (!isDigit(grid_to_show[x][i])) {//if the cell is not opened yet

                if (grid_to_show[x][i] == 'F') //if the cell has flag remove it and flags_number--
                    flags_number--;

                if (grid[x][i] == 1) {
                    grid_to_show[x][i] = 'M'; //mark the mine
                    app.gridFX.updateCell(x, i, grid_to_show[x][i]);
                }
                else { //show number of adjacent mines
                    grid_to_show[x][i] = (char) (check_neighbors(x, i) + '0');
                    app.gridFX.updateCell(x, i, grid_to_show[x][i]);
                }
            }

        grid_to_show[x][y] = 'M'; //mark the cell of super mine uniquely
        app.gridFX.updateCell(x, y, grid_to_show[x][y]);

        return grid_to_show;
    }

    private static void write_mines_file(String scenarioId, int[][] grid) throws IOException {
        String fileName = String.format(pathNameFormat, scenarioId);
        FileWriter fw = new FileWriter(fileName);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1)
                    fw.write((j+1) + ", " + (i+1) + ", 0" + "\n");

                else if (grid[i][j] == 2)
                    fw.write((j+1) + ", " + (i+1) + ", 1" + "\n");
            }
        }
        fw.close();
    }

    /**
     * Calculate the number of mines in adjacent boxes
     * @param row box row
     * @param column box column
     * @return number of mines in adjacent boxes
     */

    private  int check_neighbors(int row, int column) {
        int adjacent_mines = 0;

        if (row == 0 && column != 0 && column != grid[0].length-1)
        { //upper row except corners
            for (int i = row; i <= row+1; i++)
                for (int j = column - 1; j <= column + 1; j++)
                    if (grid[i][j] != 0)
                        adjacent_mines++;
        }

        else if (row == grid.length-1 && column != 0 && column != grid[0].length-1)
        { //lower row except corners
            for (int i = row-1; i <= row; i++)
                for (int j = column - 1; j <= column + 1; j++)
                    if (grid[i][j] != 0)
                        adjacent_mines++;
        }

        else if (column == 0 && row != 0 && row != grid.length-1)
        { //leftmost column except corners
            for (int i = row-1; i <= row+1; i++)
                for (int j = column; j <= column+1; j++)
                    if (grid[i][j] != 0)
                        adjacent_mines++;
        }

        else if (column == grid[0].length-1 && row != 0 && row != grid.length-1)
        { //rightmost column except corners
            for (int i = row-1; i <= row+1; i++)
                for (int j = column-1; j <= column; j++)
                    if (grid[i][j] != 0)
                        adjacent_mines++;
        }

        //corners
        else if (row == 0 && column == 0)
        { //upper left corner
            for (int i = row; i <= row+1; i++)
                for (int j = column; j <= column + 1; j++)
                    if (grid[i][j] != 0)
                        adjacent_mines++;
        }

        else if (row == 0 && column == grid[0].length-1)
        { //upper right corner
            for (int i = row; i <= row+1; i++)
                for (int j = column-1; j <= column; j++)
                    if (grid[i][j] != 0)
                        adjacent_mines++;
        }

        else if (row == grid.length-1 && column == 0)
        { //lower left corner
            for (int i = row-1; i <= row; i++)
                for (int j = column; j <= column+1; j++)
                    if (grid[i][j] != 0)
                        adjacent_mines++;
        }

        else if (row == grid.length-1 && column == grid[0].length-1)
        { //lower right corner
            for (int i = row-1; i <= row; i++)
                for (int j = column-1; j <= column; j++)
                    if (grid[i][j] != 0)
                        adjacent_mines++;
        }

        else { //every other box
            for (int i = row - 1; i <= row + 1; i++)
                for (int j = column - 1; j <= column + 1; j++)
                    if (grid[i][j] != 0)
                        adjacent_mines++;
        }
        return adjacent_mines;
    }

/*
//driver code
    public static void main(String[] args) throws FileNotFoundException, IOException, InvalidDescriptionException, PositionChoiceException, InvalidValueException, InterruptedException {
        Scenario scenario4 = new Scenario("4");
        Minesweeper minesweeper4 = new Minesweeper(scenario4);


        //Scenario scenario5 = new Scenario("5"); //super mine
        //Minesweeper minesweeper5 = new Minesweeper(scenario5);

        System.out.println(Arrays.deepToString(minesweeper4.getgrid()).replace("], ", "]\n"));
        System.out.println();
        System.out.println();
        //System.out.println(Arrays.deepToString(minesweeper4.getGrid_to_show()).replace("], ", "]\n"));
        //System.out.println(Arrays.deepToString(minesweeper4.show_grid(minesweeper4.getgrid())).replace("], ", "]\n"));


        System.out.println();
        System.out.println();

        System.out.println(Arrays.deepToString(minesweeper4.MarkCell(0, 8, scenario4)).replace("], ", "]\n"));


        TimeUnit.SECONDS.sleep(3);

        System.out.println();
        //System.out.println(Arrays.deepToString(minesweeper4.show_super_mine(1, 6)).replace("], ", "]\n"));

        //System.out.println();
        //System.out.println(Arrays.deepToString(minesweeper4.getgrid()).replace("], ", "]\n"));
       // TimeUnit.SECONDS.sleep(3);

       // System.out.println(Arrays.deepToString(minesweeper4.PlayAtPosition(0, 8)).replace("], ", "]\n"));

    }*/

    private void printGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length;  j++) {
                System.out.print(grid_to_show[j][i] + " ");
            } System.out.println();
        }System.out.println();
    }
}


