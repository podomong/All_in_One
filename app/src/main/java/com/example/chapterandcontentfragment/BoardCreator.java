package com.example.chapterandcontentfragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class BoardCreator extends Board {
    static final int TOP_BOARD = 0;
    static final int BOTTOM_BOARD = 1;

    private int MARGIN = 2;
    private int DISPLAY_WIDTH;
    private int DISPLAY_HEIGHT;
    private double BLOCK_SIZE_RATE = 0.075; //default rate = 0.082
    private int LENGTH;

    private ConstraintSet controller;
    private ConstraintLayout layout;
    private View[][] blockViews;
    private int[][] blockViewsId;

    BoardCreator(int boardRow, int boardCol, Context context){
        super(boardRow, boardCol, context);
        blockViews = new View[BOARD_ROW][BOARD_COL];
        blockViewsId = new int[BOARD_ROW][BOARD_COL];

    }

    void initConstraint(int constraintLayoutId){
        layout = ((Activity)mContext).findViewById(constraintLayoutId);
        controller = new ConstraintSet();
        controller.clone(layout);
    }

    /*이미지의 최대 크기를 지정해 블럭 이미지들을 생성함*/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void makeBlocks(final int resourceId){
        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                blockViews[i][j] = LayoutInflater.from(mContext).inflate(resourceId, null, true);
                blockViews[i][j].setId(View.generateViewId());
                blockViewsId[i][j] = blockViews[i][j].getId();

                blockViews[i][j].setMinimumWidth(LENGTH);
                blockViews[i][j].setMinimumHeight(LENGTH);
            }
        }
    }

    void setBlockViews(View[][] blockViews){
        this.blockViews = blockViews;
    }

    void setBlockViewsId(int[][] blockViewsId){
        this.blockViewsId = blockViewsId;
    }

    /*생성된 이미지 블럭 간에 constraint를 지정함*/
    void constraintBlocks(int guideId){
        int curViewId;
        int[] idInHorizontalChain = new int[BOARD_COL];
        int[] idInVerticalChain = new int[BOARD_ROW];

        /*이미지 블럭을 레이아웃에 등록하면서 한 행의 이미지 블럭들을 체인으로 연결함*/
        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                layout.addView(blockViews[i][j]);

                curViewId = blockViews[i][j].getId();
                idInHorizontalChain[j] = curViewId;

                controller.constrainWidth(curViewId, ConstraintSet.WRAP_CONTENT);
                controller.constrainHeight(curViewId, ConstraintSet.WRAP_CONTENT);
            }

            controller.createHorizontalChain(
                    guideId, ConstraintSet.LEFT,
                    guideId, ConstraintSet.RIGHT,
                    idInHorizontalChain,
                    null,
                    ConstraintSet.CHAIN_PACKED
            );

        }

        /*한 열의 이미지 블럭들을 체인으로 연결함*/
        for(int j=0;j<BOARD_COL;j++){
            for(int i=0;i<BOARD_ROW;i++){
                curViewId = blockViews[i][j].getId();
                idInVerticalChain[i] = curViewId;
            }

            controller.createVerticalChain(
                    guideId, ConstraintSet.TOP,
                    guideId, ConstraintSet.BOTTOM,
                    idInVerticalChain,
                    null,
                    ConstraintSet.CHAIN_PACKED
            );
        }

        for(int i=0;i<BOARD_ROW;i++){

            for(int j=0;j<BOARD_COL;j++){
                curViewId = blockViews[i][j].getId();
                controller.setMargin(curViewId, ConstraintSet.START, MARGIN);
                controller.setMargin(curViewId, ConstraintSet.TOP, MARGIN);

            }
        }

        controller.applyTo(layout);
    }

    void attachToBaseLine(int boardPos){
        switch (boardPos){
            case TOP_BOARD:
                for(int j=0;j<BOARD_COL;j++)
                    controller.setVerticalBias(blockViews[0][j].getId(),1.0f);
                break;
            case BOTTOM_BOARD:
                for(int j=0;j<BOARD_COL;j++)
                    controller.setVerticalBias(blockViews[0][j].getId(), 0.0f);
                break;
        }
        controller.applyTo(layout);
    }

    void detachFromBaseLine(){
        for(int j=0;j<BOARD_COL;j++)
            controller.setVerticalBias(blockViews[0][j].getId(),0.5f);

        controller.applyTo(layout);
    }

    int[][] getBlockViewsId(){ return blockViewsId; }

    void setDisplayWidth(int displayWidth){ DISPLAY_WIDTH = displayWidth; }

    void setDisplayHeight(int displayHeight){
        DISPLAY_HEIGHT = displayHeight;
    }

    void setMargin(int margin){ MARGIN = margin; }

    void calBlockLength(){ LENGTH = (int)(DISPLAY_WIDTH*BLOCK_SIZE_RATE); }

    int getBlockLength(){ return LENGTH; }
}
