package com.medialab.minesweeper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Statistics {
    private int mines;
    private int tries;
    private int time;
    private boolean winner;
    private static final String roundfile = "./src/main/java/com/medialab/minesweeper/Round.json";

    public Statistics(int mines, int tries, int time, boolean winner) {
        this.mines = mines;
        this.tries = tries;
        this.time = time;
        this.winner = winner;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("mines", this.mines);
        obj.put("tries", this.tries);
        obj.put("time", this.time);
        obj.put("winner", this.winner);
        return obj;
    }

    public Statistics(JSONObject obj) {
        this.mines = ((Long) obj.get("mines")).intValue();
        this.tries = ((Long) obj.get("tries")).intValue();
        this.time = ((Long) obj.get("time")).intValue();
        this.winner = ((boolean) obj.get("winner"));
    }

    /**
     * Converts a Statistics object to a JSON Object
     * and adds it to the "Round.json" file
     * @param mines number of mines
     * @param tries number of tries
     * @param time play time
     * @param winner Player or Computer
     * @throws IOException
     * @throws ParseException
     */
    public static void addStatistics(int mines, int tries, int time, boolean winner) throws IOException, ParseException {
        JSONArray jsarr;
        try {
            FileReader fr = new FileReader(roundfile);
            // read array
            JSONParser jsonParser = new JSONParser();
            jsarr = (JSONArray) jsonParser.parse(fr);
            fr.close();
        } catch (FileNotFoundException e) {
            jsarr = new JSONArray();
        }
        while(jsarr.size() > 4)
            jsarr.remove(0);

        Statistics statistics = new Statistics(mines, tries, time, winner);
        jsarr.add(statistics.toJSON());
        FileWriter fw = new FileWriter(roundfile);
        fw.write(jsarr.toJSONString());
        fw.flush();
        fw.close();
    }

    /**
     * @return an ArrayList of the JSON Objects in the  "Round.json" file
     * @throws IOException
     * @throws ParseException
     */
    public static ArrayList<Statistics> getStatistics() throws IOException, ParseException {
        FileReader fr = new FileReader(roundfile);
        JSONParser jsonParser = new JSONParser();
        JSONArray jsarr = (JSONArray) jsonParser.parse(fr);
        fr.close();

        ArrayList<Statistics> ls = new ArrayList<>();

        for(Object obj : jsarr) {
            ls.add(new Statistics((JSONObject) obj));
        }
        return ls;
    }
}
