package com.example.chapterandcontentfragment;

import android.content.Context;

public class BlockInfoReader extends Board {
    private final int TOP = 0;
    private final int BOTTOM = 1;

    private final int HORIZONTAL = 0;
    private final int VERTICAL = 1;

    String info[][][];

    BlockInfoReader(int boardRow, int boardCol, Context context){
        super(boardRow, boardCol, context);
        info = new String[boardRow][boardCol][2];
    }

    void makeBlockInfo(int boardType){
        if(boardType == HORIZONTAL){
            int num = 1;
            for(int i=0;i<BOARD_ROW;i++) {
                for (int j = 0; j < BOARD_COL; j++) {

                    if(j==0){
                        info[i][j][TOP] = "";
                        info[i][j][BOTTOM] = "";
                    }

                    else{
                        info[i][j][TOP] = Integer.toString(num);
                        info[i][j][BOTTOM] = "";
                        num++;
                    }
                }
            }
            info[0][0][TOP] = "0";
        }
        else{
            for(int i=0;i<BOARD_ROW;i++){
                for(int j=0;j<BOARD_COL;j++){
                    info[i][j][TOP] = Integer.toString(i);
                    info[i][j][BOTTOM] = "";
                }
            }
        }
    }

    String[][][] getInfo(){return info;}
}
