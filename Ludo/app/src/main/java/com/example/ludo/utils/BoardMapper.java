package com.example.ludo.utils;

import com.example.ludo.models.GridPoint;
import java.util.HashMap;
import java.util.Map;

public class BoardMapper {

    // The 52 common path cells mapping the 15x15 board.
    // Starts at the Red Start position (Col 1, Row 6) and goes clockwise.
    public static final GridPoint[] COMMON_PATH = {
            // Red outer path
            new GridPoint(1, 6), new GridPoint(2, 6), new GridPoint(3, 6), new GridPoint(4, 6), new GridPoint(5, 6),
            // Up towards Green
            new GridPoint(6, 5), new GridPoint(6, 4), new GridPoint(6, 3), new GridPoint(6, 2), new GridPoint(6, 1), new GridPoint(6, 0),
            // Cross top
            new GridPoint(7, 0), new GridPoint(8, 0),
            // Down Green outer path
            new GridPoint(8, 1), new GridPoint(8, 2), new GridPoint(8, 3), new GridPoint(8, 4), new GridPoint(8, 5),
            // Right towards Blue
            new GridPoint(9, 6), new GridPoint(10, 6), new GridPoint(11, 6), new GridPoint(12, 6), new GridPoint(13, 6), new GridPoint(14, 6),
            // Cross right
            new GridPoint(14, 7), new GridPoint(14, 8),
            // Left Blue outer path
            new GridPoint(13, 8), new GridPoint(12, 8), new GridPoint(11, 8), new GridPoint(10, 8), new GridPoint(9, 8),
            // Down towards Yellow
            new GridPoint(8, 9), new GridPoint(8, 10), new GridPoint(8, 11), new GridPoint(8, 12), new GridPoint(8, 13), new GridPoint(8, 14),
            // Cross bottom
            new GridPoint(7, 14), new GridPoint(6, 14),
            // Up Yellow outer path
            new GridPoint(6, 13), new GridPoint(6, 12), new GridPoint(6, 11), new GridPoint(6, 10), new GridPoint(6, 9),
            // Left towards Red
            new GridPoint(5, 8), new GridPoint(4, 8), new GridPoint(3, 8), new GridPoint(2, 8), new GridPoint(1, 8), new GridPoint(0, 8),
            // Cross left back to start
            new GridPoint(0, 7), new GridPoint(0, 6)
    };

    // The 5-step colored paths leading to the center trophy
    public static final GridPoint[] RED_HOME_PATH = {
            new GridPoint(1, 7), new GridPoint(2, 7), new GridPoint(3, 7), new GridPoint(4, 7), new GridPoint(5, 7)
    };
    public static final GridPoint[] GREEN_HOME_PATH = {
            new GridPoint(7, 1), new GridPoint(7, 2), new GridPoint(7, 3), new GridPoint(7, 4), new GridPoint(7, 5)
    };
    public static final GridPoint[] BLUE_HOME_PATH = {
            new GridPoint(13, 7), new GridPoint(12, 7), new GridPoint(11, 7), new GridPoint(10, 7), new GridPoint(9, 7)
    };
    public static final GridPoint[] YELLOW_HOME_PATH = {
            new GridPoint(7, 13), new GridPoint(7, 12), new GridPoint(7, 11), new GridPoint(7, 10), new GridPoint(7, 9)
    };

    // Player Index: 0=Red, 1=Green, 2=Blue, 3=Yellow
    // These define at which index of the COMMON_PATH array each player enters the board.
    public static final int[] START_INDEX = {0, 13, 26, 39};

    // Get the absolute grid position for a token based on its player ID and its relative progression (0-56).
    public static GridPoint getGridPosition(int playerIndex, int currentPosition) {
        if (currentPosition < 0) return null; // Token is at home base

        // If the token is still on the main outer path (first 51 steps)
        if (currentPosition <= 50) {
            int absoluteIndex = (START_INDEX[playerIndex] + currentPosition) % 52;
            return COMMON_PATH[absoluteIndex];
        }
        // Token has entered the colored home stretch
        else if (currentPosition <= 55) {
            int homeStretchIndex = currentPosition - 51;
            switch (playerIndex) {
                case 0: return RED_HOME_PATH[homeStretchIndex];
                case 1: return GREEN_HOME_PATH[homeStretchIndex];
                case 2: return BLUE_HOME_PATH[homeStretchIndex];
                case 3: return YELLOW_HOME_PATH[homeStretchIndex];
            }
        }

        // At position 56, the token has finished (in the center triangle)
        return new GridPoint(7, 7);
    }
}