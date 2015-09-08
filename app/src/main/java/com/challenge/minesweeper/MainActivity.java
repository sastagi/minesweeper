package com.challenge.minesweeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;


public class MainActivity extends Activity{

    private int size = 8;
    private int numberOfMines = 10;
    private boolean gameOver;
    private Cell[][] masterGrid;
    private Cell[][] gameGrid;
    private Queue<Cell> noMineCells;
    private GridAdapter gridAdapter;
    private int cheatCount=3;
    private boolean gameNotCreated=true;
    private boolean gameLost=false;
    private int columnWidth;

    private MenuItem validateButton;
    private RetainedFragment dataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridView gridview = (GridView) findViewById(R.id.gridview);

        final int iDisplayWidth = getResources().getDisplayMetrics().widthPixels ;

        columnWidth = iDisplayWidth/size ;

        createGame();

        gridview.setColumnWidth(columnWidth);
        gridview.setNumColumns(size);
        gridview.setStretchMode( GridView.NO_STRETCH ) ;

        gridview.setAdapter(gridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(!gameOver) {
                    int xPos = position % size;
                    int yPos = (position - xPos) / size;
                    Cell cell = gameGrid[xPos][yPos];
                    if (gameGrid[xPos][yPos].isMine) {
                        gameGrid[xPos][yPos].isOpen = true;
                        gameOver();
                        Toast.makeText(MainActivity.this, "Game over", Toast.LENGTH_SHORT).show();
                        //Don't allow game to continue
                    } else {
                        markCell(cell);
                    }
                    gridAdapter.notifyDataSetChanged();
                    gridview.setAdapter(gridAdapter);
                }
            }
        });
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                int xPos = position % size;
                int yPos = (position - xPos) / size;
                Cell cell = gameGrid[xPos][yPos];
                cheat(cell);
                return true;
            }
        });

        FragmentManager fm = getFragmentManager();
        dataFragment = (RetainedFragment) fm.findFragmentByTag("data");
        if (dataFragment == null) {
            Log.d("DATA FRAGMENT STATE IS","NULL");
            dataFragment = new RetainedFragment();
            fm.beginTransaction().add(dataFragment, "data").commit();
            dataFragment.setMasterGrid(masterGrid);
            dataFragment.setGameGrid(gameGrid);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // store the data in the fragment
        dataFragment.setMasterGrid(masterGrid);
        dataFragment.setGameGrid(gameGrid);
    }

    private void createGame(){
        if(gameNotCreated){

            masterGrid = new Cell[size+2][size+2];

            FragmentManager fm = getFragmentManager();
            dataFragment = (RetainedFragment) fm.findFragmentByTag("data");

            if(dataFragment!=null){
                masterGrid = dataFragment.getMasterGrid();
                gameGrid = dataFragment.getGameGrid();
            }else{
                initializeGame();
            }
            noMineCells = new LinkedList<Cell>();
            gridAdapter = new GridAdapter(this, gameGrid, size, columnWidth);
            gameNotCreated = false;
        }
    }

    private void markCell(Cell cell){
        int xPos = cell.position % (size+2);
        int yPos = (cell.position - xPos) / (size+2);
        cell.isOpen=true;

        if(cell.mineCount==0){
            assessCell(masterGrid[xPos-1][yPos-1]);
            assessCell(masterGrid[xPos-1][yPos]);
            assessCell(masterGrid[xPos-1][yPos+1]);

            assessCell(masterGrid[xPos][yPos-1]);
            assessCell(masterGrid[xPos][yPos+1]);

            assessCell(masterGrid[xPos+1][yPos-1]);
            assessCell(masterGrid[xPos+1][yPos]);
            assessCell(masterGrid[xPos+1][yPos+1]);
        }
        if(!noMineCells.isEmpty()){
            markCell(noMineCells.poll());
        }
    }

    private void assessCell(Cell cell){
        if(!cell.isOpen){
            cell.isOpen=true;
            if(cell.mineCount==0){
                Log.d("Adding cell",""+cell.position);
                noMineCells.add(cell);
            }
        }
    }

    private void gameOver(){
        gameLost = true;
        for (int i = 0; i < size * size; i++) {
            int xPost = i % size;
            int yPost = (i - xPost) / size;
            if (gameGrid[xPost][yPost].isMine)
                gameGrid[xPost][yPost].isOpen = true;
        }
        gameOver=true;
        gridAdapter.notifyDataSetChanged();
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(gridAdapter);
        validateButton.setIcon(R.drawable.validate_lost);
    }


    public void validateGame(MenuItem item){
        for (int i = 0; i < size * size; i++) {
            int xPost = i % size;
            int yPost = (i - xPost) / size;
            if (!gameGrid[xPost][yPost].isMine)
                if(!gameGrid[xPost][yPost].isOpen) {
                    gameLost=true;
                    validateButton.setIcon(R.drawable.validate_lost);
                }
        }
        if(gameLost)
            gameOver();
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Congratulations");
            alertDialog.setMessage("Mines identified successfully.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            gameOver();
        }
    }

    private void cheat(Cell cell){
        if(cheatCount>0){
            if(cell.isMine){
                Toast.makeText(MainActivity.this, "This is mine", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "Not a mine", Toast.LENGTH_SHORT).show();
            }
            cheatCount--;
        }
        else{
            Toast.makeText(MainActivity.this, "No more chances left", Toast.LENGTH_SHORT).show();
        }
    }

    public void newGame(MenuItem item){
        gameLost = false;
        validateButton.setIcon(R.drawable.validate_game);
        gameOver=false;
        masterGrid = new Cell[size+2][size+2];
        initializeGame();
        noMineCells = new LinkedList<Cell>();
        gridAdapter = new GridAdapter(this, gameGrid, size, columnWidth);
        gridAdapter.notifyDataSetChanged();
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(gridAdapter);

    }

    private void initializeGame(){
        Game game = new Game(masterGrid,size,numberOfMines);
        gameGrid = new Cell[size][size];
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                gameGrid[i][j] = masterGrid[i+1][j+1];
            }
        }
        game=null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        validateButton = menu.findItem(R.id.validate);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
