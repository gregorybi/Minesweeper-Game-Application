package com.medialab.minesweeper;


public class Data {
    public String mines;
    public String tries;
    public String time;
    public String winner;

    public Data(String mines, String tries, String time, String winner) {
        this.mines = mines;
        this.tries = tries;
        this.time = time;
        this.winner = winner;
    }

    public String getMines() {
        return mines;
    }

    public void setMines(String mines) {
        this.mines = mines;
    }

    public String getTries() {
        return tries;
    }

    public void setTries(String tries) {
        this.tries = tries;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

//    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{mines=").append(mines);
        sb.append(", tries=").append(tries);
        sb.append(", time=").append(time);
        sb.append(", winner=").append(winner);
        sb.append('}');
        return sb.toString();
    }
}

