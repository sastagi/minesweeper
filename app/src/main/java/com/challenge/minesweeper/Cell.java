package com.challenge.minesweeper;

/**
 * Created by Santosh on 3/21/15.
 */
public class Cell {
    boolean isMine = false;
    boolean isOpen = false;
    int mineCount = 0;
    int position;

    @Override
    public String toString() {
        return "My status is: "+isOpen;
    }

}
