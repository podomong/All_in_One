package com.example.chapterandcontentfragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

public class BlockCreator extends Board {
    static final int HORIZONTAL = 0;
    static final int VERTICAL = 1;

    private final int TOP = 0;
    private final int BOTTOM = 1;

    private int DISPLAY_WIDTH;
    private int DISPLAY_HEIGHT;
    private double BLOCK_SIZE_RATE = 0.075; //default rate = 0.082
    private int LENGTH;

    private View[][] blockViews;
    private int[][] blockViewsId;

    private ColorPalette palette;
    private int curBoardType;

    BlockCreator(int boardRow, int boardCol, Context context){
        super(boardRow, boardCol, context);
        blockViews = new View[BOARD_ROW][BOARD_COL];
        blockViewsId = new int[BOARD_ROW][BOARD_COL];
    }

    /*이미지의 최대 크기를 지정해 블럭 이미지들을 생성함*/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void makeBlocks(final int resourceId, ColorPalette palette){

        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                blockViews[i][j] = LayoutInflater.from(mContext).inflate(resourceId, null, true);
                blockViews[i][j].setId(View.generateViewId());
                blockViewsId[i][j] = blockViews[i][j].getId();

                blockViews[i][j].setMinimumWidth(LENGTH);
                blockViews[i][j].setMinimumHeight(LENGTH);
            }
        }

        if(curBoardType == HORIZONTAL){
            for(int i=0;i<BOARD_ROW;i++){
                for(int j=0;j<BOARD_COL;j++){
                    if(j == 0){
                        ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getColor(0));
                        ((CardView)blockViews[i][j]).setCardElevation(0f);
                    }

                    else{
                        if(palette.isColorModeOn())
                            ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getColor(10-i));
                        else
                            ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getForegroundColor());
                    }
                }
            }
        }
        else{
            for(int j=0;j<BOARD_COL;j++){
                for(int i=0;i<BOARD_ROW;i++){
                    if(i==0) {
                        ((CardView) blockViews[i][j]).setCardBackgroundColor(palette.getColor(0));
                        ((CardView) blockViews[i][j]).setCardElevation(0f);
                    }
                    else{
                        if(palette.isColorModeOn())
                            ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getColor(j+1));
                        else
                            ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getForegroundColor());
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void makeBlocks(final int resourceId, String info[][][]){
        TextView topTextView, bottomTextView;


        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                blockViews[i][j] = LayoutInflater.from(mContext).inflate(resourceId, null, true);
                blockViews[i][j].setId(View.generateViewId());
                blockViewsId[i][j] = blockViews[i][j].getId();
            }
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
        params.gravity= Gravity.CENTER;

        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                topTextView = blockViews[i][j].findViewById(R.id.topTextView);
                bottomTextView = blockViews[i][j].findViewById(R.id.bottomTextView);
                ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getBackgroundColor());

                topTextView.setText(info[i][j][TOP]);
                bottomTextView.setText(info[i][j][BOTTOM]);
                /*if(info[i][j][TOP] != ""){
                    topTextView.setText(info[i][j][TOP]);
                    params.weight = 0.6f;
                    topTextView.setLayoutParams(params);
                }

                if(info[i][j][BOTTOM] != ""){
                    bottomTextView.setText(info[i][j][BOTTOM]);
                    params.weight = 0.4f;
                    bottomTextView.setLayoutParams(params);

                }*/
            }
        }
    }

    void calBlockLength(){ LENGTH = (int)(DISPLAY_WIDTH*BLOCK_SIZE_RATE); }

    void setBoardType(int boardType){curBoardType = boardType;}

    int[][] getBlockViewsId(){ return blockViewsId; }

    void setDisplayWidth(int displayWidth){ DISPLAY_WIDTH = displayWidth; }

    void setDisplayHeight(int displayHeight){
        DISPLAY_HEIGHT = displayHeight;
    }

    void setColorPalette(ColorPalette palette){this.palette = palette;}

    View[][] getBlockViews(){ return blockViews;}

    int getBlockLength(){ return LENGTH; }
}
