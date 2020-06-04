package com.example.chapterandcontentfragment;

import android.content.Context;

public class Board {
    public final int DOWN = 0;
    public final int UP = 1;
    public final int LEFT = 0;
    public final int RIGHT = 1;

    protected Context mContext;
    protected int BOARD_ROW;
    protected int BOARD_COL;

    Board(){
        BOARD_ROW = 3;
        BOARD_COL = 3;
    }
    Board(int boardRow, int boardCol, Context context){
        mContext = context;
        BOARD_ROW = boardRow;
        BOARD_COL = boardCol;
    }
}
