package com.example.chapterandcontentfragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

public class BaseBar {
    private int LENGTH;
    private int MARGIN;
    private int BOARD_ROW;
    private int BOARD_COL;

    private int boardType;
    private View baseBar[];
    Context context;


    BaseBar(int boardRow, int boardCol, Context context){
        BOARD_ROW = boardRow;
        BOARD_COL = boardCol;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void initBaseBar(){
        switch (boardType){
            case BlockCreator.VERTICAL:
            case BlockCreator.VERTICAL_UPSIDE_DOWN:
                baseBar = new View[BOARD_COL];
                for(int j=0;j<BOARD_COL;j++){
                    baseBar[j] = new View(context);
                    baseBar[j].setId(View.generateViewId());
                    baseBar[j].setBackgroundColor(Color.parseColor(ColorPalette.BASE_LINE_COLOR));
                }
                break;

            case BlockCreator.HORIZONTAL:
            case BlockCreator.HORIZONTAL_STAIR:
                baseBar = new View[BOARD_ROW];
                for(int i=0;i<BOARD_ROW;i++){
                    baseBar[i] = new View(context);
                    baseBar[i].setId(View.generateViewId());
                    baseBar[i].setBackgroundColor(Color.parseColor(ColorPalette.BASE_LINE_COLOR));
                }
                break;
        }
    }


    void calBarLength(){
        int width = MARGIN, height = MARGIN;
        switch (boardType){
            case BlockCreator.VERTICAL:
            case BlockCreator.VERTICAL_UPSIDE_DOWN:
                width = LENGTH+MARGIN; height = 3*MARGIN;
                for(int j=0;j<BOARD_COL;j++){
                    if(j==BOARD_COL-1)
                        width -= MARGIN;
                    baseBar[j].setLayoutParams(new ViewGroup.LayoutParams(width, height));
                }
                break;

            case BlockCreator.HORIZONTAL:
            case BlockCreator.HORIZONTAL_STAIR:
                width = 3*MARGIN; height = LENGTH+MARGIN;
                for(int i=0;i<BOARD_ROW;i++){
                    if(i == BOARD_ROW-1)
                        height -= MARGIN;
                    baseBar[i].setLayoutParams(new ViewGroup.LayoutParams(width, height));
                }
                break;
        }

    }

    void changeBarLength(int length){
        LENGTH = length;
        int width = MARGIN, height = MARGIN;
        switch (boardType){
            case BlockCreator.VERTICAL:
            case BlockCreator.VERTICAL_UPSIDE_DOWN:
                width = LENGTH+MARGIN; height = 3*MARGIN;
                for(int j=0;j<BOARD_COL;j++){
                    if(j==BOARD_COL-1)
                        width -= MARGIN;
                    baseBar[j].getLayoutParams().width = width;
                    baseBar[j].getLayoutParams().height = height;
                    baseBar[j].requestLayout();
                }
                break;

            case BlockCreator.HORIZONTAL:
            case BlockCreator.HORIZONTAL_STAIR:
                width = 3*MARGIN; height = LENGTH+MARGIN;
                for(int i=0;i<BOARD_ROW;i++){
                    if(i == BOARD_ROW-1)
                        height -=MARGIN;
                    baseBar[i].getLayoutParams().width = width;
                    baseBar[i].getLayoutParams().height = height;
                    baseBar[i].requestLayout();
                }
                break;
        }
    }

    void toggleBarVisibility(){
        switch (boardType){
            case BlockCreator.VERTICAL:
            case BlockCreator.VERTICAL_UPSIDE_DOWN:

                for(int j=0;j<BOARD_COL;j++) {
                    if(baseBar[j].getVisibility() == View.VISIBLE)
                        baseBar[j].setVisibility(View.GONE);
                    else
                        baseBar[j].setVisibility(View.VISIBLE);
                }
                break;

            case BlockCreator.HORIZONTAL:
            case BlockCreator.HORIZONTAL_STAIR:
                for(int i=0;i<BOARD_ROW;i++) {
                    if(baseBar[i].getVisibility() == View.VISIBLE)
                        baseBar[i].setVisibility(View.GONE);
                    else
                        baseBar[i].setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    void setLength(int length, int margin){
        LENGTH = length;
        MARGIN = margin;
    }

    void setBoardType(int boardType){ this.boardType = boardType; }

    View getBaseBar(int idx){ return baseBar[idx];}
}
