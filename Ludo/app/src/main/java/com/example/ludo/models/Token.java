package com.example.ludo.models;

public class Token {
    private int id; // 0 to 3 for a player
    private int colorResource; // e.g., R.color.ludo_red
    private int playerIndex; // 0=Red, 1=Green, 2=Yellow, 3=Blue
    private int position; // -1 means it is in the home base. 0-56 are board positions.
    private boolean isSafe;

    public Token(int id, int playerIndex, int colorResource) {
        this.id = id;
        this.playerIndex = playerIndex;
        this.colorResource = colorResource;
        this.position = -1; // Default to home
        this.isSafe = true;
    }

    public int getId() { return id; }
    public int getPlayerIndex() { return playerIndex; }
    public int getColorResource() { return colorResource; }
    public int getPosition() { return position; }

    public void setPosition(int position) { this.position = position; }

    public boolean isAtHome() { return position == -1; }
    public boolean isSafe() { return isSafe; }
    public void setSafe(boolean safe) { isSafe = safe; }
}