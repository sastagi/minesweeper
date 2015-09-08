package com.challenge.minesweeper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Santosh on 3/20/15.
 */
public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private Cell[][] gameGrid;
    private int size=8;
    private int columnWidth;

    public GridAdapter(Context c, Cell[][] grid, int gridSize, int columnWidth) {
        mContext = c; gameGrid = grid; size=gridSize; this.columnWidth=columnWidth;
    }

    public int getCount() {
        return size*size;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        //Get position of cell(x,y) in grid
        int xPos = position % size;
        int yPos = (position - xPos) / size;

        int cellWidth = columnWidth;//Generate cell width based on screen size

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(cellWidth, cellWidth));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(R.drawable.empty);
        switch (gameGrid[xPos][yPos].mineCount){
            case 1:imageView.setImageResource(R.drawable.one);break;
            case 2:imageView.setImageResource(R.drawable.two);break;
            case 3:imageView.setImageResource(R.drawable.three);break;
            case 4:imageView.setImageResource(R.drawable.four);break;
            case 5:imageView.setImageResource(R.drawable.five);break;
            case 6:imageView.setImageResource(R.drawable.six);break;
            case 7:imageView.setImageResource(R.drawable.seven);break;
            case 8:imageView.setImageResource(R.drawable.eight);break;
        }
        if(gameGrid[xPos][yPos].isMine)
            imageView.setImageResource(R.drawable.mine);
        if(!gameGrid[xPos][yPos].isOpen)
            imageView.setImageResource(R.drawable.cover);
        return imageView;
    }
}