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

    private int boardType;
    private BaseBar baseBars;

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
        this.blockViews = new View[blockViews.length][blockViews[0].length];
        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++)
                this.blockViews[i][j] = blockViews[i][j];

        }
    }

    void setBaseBar(int boardType, BaseBar baseBars){
        this.boardType = boardType;
        this.baseBars = baseBars;

        View tempBlockViews[][];

        switch (boardType){
            case BlockCreator.VERTICAL:
            case BlockCreator.VERTICAL_UPSIDE_DOWN:
                tempBlockViews = new View[BOARD_ROW+1][BOARD_COL];
                break;
            case BlockCreator.HORIZONTAL:
            case BlockCreator.HORIZONTAL_STAIR:
                tempBlockViews = new View[BOARD_ROW][BOARD_COL+1];
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + boardType);
        }

        switch (boardType){

            case BlockCreator.VERTICAL:
                for(int j=0;j<BOARD_COL;j++)
                    tempBlockViews[0][j] = baseBars.getBaseBar(j);
                for(int i=0;i<BOARD_ROW;i++){
                    for(int j=0;j<BOARD_COL;j++)
                        tempBlockViews[i+1][j] = blockViews[i][j];
                }
                break;

            case BlockCreator.VERTICAL_UPSIDE_DOWN:
                for(int j=0;j<BOARD_COL;j++)
                    tempBlockViews[BOARD_ROW][j] = baseBars.getBaseBar(j);
                for(int i=0;i<BOARD_ROW;i++){
                    for(int j=0;j<BOARD_COL;j++)
                        tempBlockViews[i][j] = blockViews[i][j];
                }
                break;

            case BlockCreator.HORIZONTAL:
            case BlockCreator.HORIZONTAL_STAIR:
                for(int i = 0;i<BOARD_ROW;i++)
                    tempBlockViews[i][0] = baseBars.getBaseBar(i);
                for(int i=0;i<BOARD_ROW;i++){
                    for(int j=0;j<BOARD_COL;j++)
                        tempBlockViews[i][j+1] = blockViews[i][j];
                }
                break;
        }


        blockViews = tempBlockViews;

        BOARD_ROW = blockViews.length;
        BOARD_COL = blockViews[0].length;
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

                controller.constrainWidth(curViewId, blockViews[i][j].getLayoutParams().width);
                controller.constrainHeight(curViewId, blockViews[i][j].getLayoutParams().height);
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
                switch (boardType){
                    case BlockCreator.VERTICAL:
                        if(i!=0){
                            controller.setMargin(curViewId, ConstraintSet.START, MARGIN);
                            controller.setMargin(curViewId, ConstraintSet.TOP, MARGIN);
                        }
                        else if(j == 0)
                            controller.setMargin(curViewId, ConstraintSet.START, MARGIN);
                        break;

                    case BlockCreator.VERTICAL_UPSIDE_DOWN:
                        if(i!=BOARD_ROW-1){
                            controller.setMargin(curViewId, ConstraintSet.START, MARGIN);
                            controller.setMargin(curViewId, ConstraintSet.TOP, MARGIN);
                        }
                        else if(j == 0)
                            controller.setMargin(curViewId, ConstraintSet.START, MARGIN);
                        break;

                    case BlockCreator.HORIZONTAL:
                    case BlockCreator.HORIZONTAL_STAIR:
                        if(j!=0){
                            controller.setMargin(curViewId, ConstraintSet.START, MARGIN);
                            controller.setMargin(curViewId, ConstraintSet.TOP, MARGIN);
                        }
                        else if(i == 0)
                            controller.setMargin(curViewId, ConstraintSet.TOP, MARGIN);

                        break;
                }

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
