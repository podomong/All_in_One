package com.example.chapterandcontentfragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class BoardImage extends Board {
    private int DISPLAY_WIDTH;
    private int DISPLAY_HEIGHT;
    private double BLOCK_SIZE_RATE = 0.07; //default rate = 0.082
    private int LENGTH;

    private ConstraintSet controller;
    private ConstraintLayout layout;
    private ImageView[][] imageViews;
    private int[][] imageViewsId;

    BoardImage(){
        super();
    }

    BoardImage(int boardRow, int boardCol, Context context){
        super(boardRow, boardCol, context);
        imageViews = new ImageView[BOARD_ROW][BOARD_COL];
        imageViewsId = new int[BOARD_ROW][BOARD_COL];
    }

    void initConstraint(int constraintLayoutId){
        layout = ((Activity)mContext).findViewById(constraintLayoutId);
        controller = new ConstraintSet();
        controller.clone(layout);
    }

    /*이미지의 최대 크기를 지정해 블럭 이미지들을 생성함*/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void makeImages(){
        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                imageViews[i][j] = new ImageView(mContext);
                imageViews[i][j].setId(View.generateViewId());
                imageViews[i][j].setImageResource(R.drawable.block);
                imageViewsId[i][j] = imageViews[i][j].getId();

                imageViews[i][j].setMaxWidth(LENGTH);
                imageViews[i][j].setMaxHeight(LENGTH);
                imageViews[i][j].setAdjustViewBounds(true);
            }
        }
    }

    /*생성된 이미지 블럭 간에 constraint를 지정함*/
    void constraintImages(){
        int curViewId;
        int[] idInHorizontalChain = new int[BOARD_COL];
        int[] idInVerticalChain = new int[BOARD_ROW];

        /*이미지 블럭을 레이아웃에 등록하면서 한 행의 이미지 블럭들을 체인으로 연결함*/
        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                layout.addView(imageViews[i][j]);

                curViewId = imageViews[i][j].getId();
                idInHorizontalChain[j] = curViewId;

                controller.constrainWidth(curViewId, ConstraintSet.WRAP_CONTENT);
                controller.constrainHeight(curViewId, ConstraintSet.WRAP_CONTENT);
            }

            controller.createHorizontalChain(
                    ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                    idInHorizontalChain,
                    null,
                    ConstraintSet.CHAIN_PACKED
            );
        }

        /*한 열의 이미지 블럭들을 체인으로 연결함*/
        for(int j=0;j<BOARD_COL;j++){
            for(int i=0;i<BOARD_ROW;i++){
                curViewId = imageViews[i][j].getId();
                idInVerticalChain[i] = curViewId;
            }

            controller.createVerticalChain(
                    ConstraintSet.PARENT_ID, ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,
                    idInVerticalChain,
                    null,
                    ConstraintSet.CHAIN_PACKED
            );
        }

        controller.applyTo(layout);
    }

    int[][] getImageViewsId(){ return imageViewsId; }

    void setDisplayWidth(int displayWidth){ DISPLAY_WIDTH = displayWidth; }

    void setDisplayHeight(int displayHeight){
        DISPLAY_HEIGHT = displayHeight;
    }

    void calBlockLength(){ LENGTH = (int)(DISPLAY_WIDTH*BLOCK_SIZE_RATE); }

    int getBlockLength(){ return LENGTH; }
}
