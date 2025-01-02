package com.medialab.minesweeper;

import com.medialab.minesweeper.exceptions.FileNotFoundException;
import com.medialab.minesweeper.exceptions.InvalidDescriptionException;
import com.medialab.minesweeper.exceptions.InvalidValueException;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Scenario {
    private static final String scenariosPath = "./src/main/java/com/medialab/minesweeper/scenarios";
    private static final String pathNameFormat = "./src/main/java/com/medialab/minesweeper/scenarios/%s.txt";
    private final String id;
    private final int difficulty_level;
    private final int num_of_mines;
    private final int sup_mine;
    private final int play_time;

    /**
     * Creates a Scenario object from a scenario text file. The file must exist in the "scenarios" folder and
     * must have been created using the createToFile() static method.
     * It is invoked when the "load" menu option is used
     * @param scenarioId the Scenario ID. The file will be named "scenarioId.txt".
     * @throws FileNotFoundException if the scenario text file is not found in the "scenarios" folder
     */
    public Scenario (String scenarioId) throws FileNotFoundException,
            java.io.FileNotFoundException, InvalidDescriptionException {

        id = scenarioId;

        LinkedList<String> words = new LinkedList<>();

        String pathName = String.format(pathNameFormat, scenarioId);
        File file = new File(pathName);
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String word = sc.nextLine();
            words.add(word);
        }
        sc.close();

        if (words.size() != 4)
            throw new InvalidDescriptionException();

        difficulty_level = Integer.parseInt(words.get(0)) ;
        num_of_mines = Integer.parseInt(words.get(1)) ;
        sup_mine = Integer.parseInt(words.get(3)) ;
        play_time = Integer.parseInt(words.get(2)) ;


       // System.out.println(sup_mine);
    }

    /**
     * @return an array of every scenario ID in the scenarios folder
     */
    public static String[] getScenarioIds () {
        FileFilter filter = file -> file.isFile() && file.getName().matches("[a-zA-Z0-9_]+[.]txt");
        File f = new File(scenariosPath);
        File[] scenarioFiles = f.listFiles(filter); // might return null
        String[] scenarioIds = new String[scenarioFiles.length];
        for (int i = 0, length = scenarioFiles.length; i < length; i++) {
            String scenarioName = scenarioFiles[i].getName();
            scenarioIds[i] = scenarioName.substring(0, scenarioName.length()-4);
        }
        return scenarioIds;
    }


    //getters

    /**
     * @return the scenarioId
     */
    public String getId() { return id; }

    /**
     * @return the difficulty level
     */
    public int getDifficulty_level() {return difficulty_level;}

    /**
     * @return the number of mines
     */
    public int getNum_of_mines() {return num_of_mines;}

    /**
     * @return 1 if super mine exists or 0 otherwise
     */
    public int getSup_mine() {return sup_mine;}

    /**
     * @return play time
     */
    public int getPlay_time() {return play_time;}


    /**
 * Create a scenario text file. The created text file will be saved in the "scenarios" folder.
 *  It is invoked when the "create" menu option is used
 * @param scenarioId the Scenario ID. The file will be named "scenarioId.txt".
 * @param level the level of difficulty
 * @param mine_num the number of mines in the grid
 * @param super_mine true if there is super mine and false if there is not
 * @param time maximum available time in seconds
 * @throws IOException if the request to API fails or the file can't be created
 * @throws InvalidValueException if the requested parameters are not within the specified boundaries
*/
    public static void createToFile(String scenarioId, int level, int mine_num, int time, int super_mine)
            throws IOException, InvalidValueException {

        verifyScenario(level, mine_num, super_mine, time);
        writeToFile(scenarioId, level, mine_num, super_mine, time);

    }

    private static void verifyScenario (int level, int mine_num, int super_mine, int time)
        throws InvalidValueException {

        //if level 1
        if (level == 1) {
            if (mine_num<9 || mine_num>11) { //and incorrect number of mines
                throw new InvalidValueException();
            }
            if (time<120 || time>180) { //and incorrect time
                throw new InvalidValueException();
            }
            if (super_mine != 0)  //and incorrect existence of supermine
                throw new InvalidValueException();
        }

        //else if level 2
        else if (level == 2) {
            if (mine_num<35 || mine_num>45)  //and incorrect number of mines
                throw new InvalidValueException();

            if (time<240 || time>360)  //and incorrect time
                throw new InvalidValueException();

            if (super_mine != 0 && super_mine != 1) //incorrect super mine value
                throw new InvalidValueException();
        }
        //if level number is not 1 neither 2
        else
            throw new InvalidValueException();

    }

    private static void writeToFile(String scenarioId, int level, int mine_num, int super_mine, int time) throws IOException {
        String fileName = String.format(pathNameFormat, scenarioId);
        FileWriter fw = new FileWriter(fileName);

        fw.write(level + "\n");
        fw.write(mine_num + "\n");
        fw.write(time + "\n");
        fw.write(super_mine + "\n");
        fw.close();
    }

    public static void main(String[] args) throws InvalidValueException, IOException, FileNotFoundException, InvalidDescriptionException {
        //createToFile("4", 1, 10, 120, 0);
       // createToFile("5", 2, 35, 240, 1);

        Scenario scenario4 = new Scenario("4");
        Scenario scenario5 = new Scenario("5"); //super mine

        System.out.println(scenario4.getDifficulty_level());
        System.out.println(scenario4.getNum_of_mines());
        System.out.println(scenario4.getPlay_time());
        System.out.println(scenario4.getSup_mine());


    }

}
