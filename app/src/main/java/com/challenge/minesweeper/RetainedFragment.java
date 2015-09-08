package com.challenge.minesweeper;


import android.app.Fragment;
import android.os.Bundle;


/**
 * A simple {@link Fragment} subclass.
 */
public class RetainedFragment extends Fragment {

    // data object we want to retain during orientation
    private  Cell[][] savedMasterGrid;
    private  Cell[][] savedGameGrid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setMasterGrid(Cell[][] masterGrid) {
        savedMasterGrid = masterGrid;
    }

    public Cell[][] getMasterGrid() {
        return savedMasterGrid;
    }

    public void setGameGrid(Cell[][] gameGrid) {
        savedGameGrid = gameGrid;
    }

    public Cell[][] getGameGrid() {
        return savedGameGrid;
    }

}
