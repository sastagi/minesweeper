package com.challenge.minesweeper;

import java.util.Random;
import java.util.TreeSet;

/**
 * Created by Santosh on 3/21/15.
 */
public class Game {

    Game(Cell[][] masterGrid, int size, int numberOfMines) {
        initializeGrid(masterGrid, size, numberOfMines);
    }

    private void initializeGrid(Cell[][] grid, int size, int numberOfMines) {

        markBorderCells(grid,size+2);

        //Generate cells for inner grid
        for (int i = 1; i < size+1; i++) {
            for (int j = 1; j < size+1; j++) {
                grid[i][j] = new Cell();
                grid[i][j].position = (j*(size+2))+i;//Position index useful in onclick listener for grid
            }
        }

        placeMines(grid, size, numberOfMines);

        //Get mine count for cells within inner grid
        for (int i = 1; i < size+1; i++) {
            for (int j = 1; j < size+1; j++) {
                int count = 0;
                count += markXAdjacent(grid, i, j);
                count += markYAdjacent(grid, i, j);
                count += markXAdjacent(grid, i - 1, j);
                count += markXAdjacent(grid, i + 1, j);
                grid[i][j].mineCount = count;
            }
        }
    }

    private void markBorderCells(Cell[][] grid, int size){
        Cell cell = new Cell();
        cell.mineCount=1;
        cell.isMine=false;
        for (int i = 0; i < size; i++) {
            grid[i][0] = cell;
            grid[i][size-1] = cell;
            grid[0][i] = cell;
            grid[size-1][i] = cell;
        }
    }

    private void placeMines(Cell[][] grid, int size, int numberOfMines){
        final TreeSet<Integer> mines = new TreeSet<Integer>();
        final Random r = new Random();
        int max = (size)*(size-1)-2;//upper bound for position index for inner grid
        for (int i = 0; i < numberOfMines; i++) {
            int n = r.nextInt(max - size + 1) + size;//random number within the inner grid for mine position
            while (!mines.add(n)) {
                n = r.nextInt(max - size + 1) + size;
            }
            int xPos = n % size+1;
            int yPos = (n - xPos) / size+1;
            grid[xPos][yPos].isMine = true;
        }
    }

    private int markXAdjacent(final Cell[][] grid, final int i, final int j) {
        int count = 0;
        if (grid[i][j - 1].isMine) {
            count++;
        }
        if (grid[i][j + 1].isMine) {
            count++;
        }
        return count;
    }

    private int markYAdjacent(final Cell[][] grid, final int i, final int j) {
        int count = 0;
        if (grid[i - 1][j].isMine) {
            count++;
        }
        if (grid[i + 1][j].isMine) {
            count++;
        }
        return count;
    }
}