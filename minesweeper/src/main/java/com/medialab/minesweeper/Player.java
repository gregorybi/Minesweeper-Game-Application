package com.medialab.minesweeper;

public class Player {
    private int correctChoices = 0; // number of correct choices the player has made
    private int totalTries = 0; // number of total choices the player has made

    //getters

    /**
     * @return Player correct choices
     */
    public int getCorrectChoices() { return correctChoices; }

    /**
     * @return player total choices
     */
    public int getTotalTries() { return totalTries; }

    /**
     * Increase the value of total player tries by 1
     */
    public void Successful_try() {
        totalTries++;
    }

}
