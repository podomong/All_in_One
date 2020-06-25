package com.example.chapterandcontentfragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

public class BlockCreator extends Board {
    static final int HORIZONTAL = 0;
    static final int VERTICAL = 1;
    static final int VERTICAL_UPSIDE_DOWN = 2;
    static final int HORIZONTAL_STAIR = 3;

    private final int TOP = 0;
    private final int BOTTOM = 1;

    static final int BACKGROUND = 0;
    static final int FOREGROUND = 1;

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
    void makeForegroundBlocks(){

        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                blockViews[i][j] = LayoutInflater.from(mContext).inflate(R.layout.block_foreground, null, true);
                blockViews[i][j].setId(View.generateViewId());
                blockViewsId[i][j] = blockViews[i][j].getId();

                if(palette.isActualBlock(FOREGROUND, i, j)){
                    if(palette.isColorModeOn()){
                        if(curBoardType == VERTICAL || curBoardType == VERTICAL_UPSIDE_DOWN)
                            ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getColor(j+1));
                        else
                            ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getColor(10-i));
                    }
                    else
                        ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getForegroundColor());
                    ((CardView)blockViews[i][j]).setCardElevation(3f);
                }
                else{
                    ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getColor(0));
                    ((CardView)blockViews[i][j]).setCardElevation(0f);
                }

                blockViews[i][j].setMinimumWidth(LENGTH);
                blockViews[i][j].setMinimumHeight(LENGTH);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void makeBackgroundBlocks(int boardId, String info[][][]){
        TextView topTextView, bottomTextView;
        LinearLayout linearLayout;

        //분수인 경우
        if((14 <=boardId && boardId <= 17) || (18 <= boardId && boardId <= 28)){
            Typeface typeface = Typeface.createFromAsset(
                    mContext.getAssets(), "nutso2.ttf"
            );

            for(int i=0;i<BOARD_ROW;i++){
                for(int j=0;j<BOARD_COL;j++){
                    if(!palette.isActualBlock(FOREGROUND, i,j))
                        blockViews[i][j] = LayoutInflater.from(mContext).inflate(palette.getDummyLineId(), null, true);
                    else
                        blockViews[i][j] = LayoutInflater.from(mContext).inflate(palette.getInnerLineId(), null, true);
                    blockViews[i][j].setId(View.generateViewId());

                    linearLayout = blockViews[i][j].findViewById(R.id.block_layout);
                    linearLayout.getLayoutParams().height = LENGTH;
                    linearLayout.getLayoutParams().width = LENGTH;
                    linearLayout.requestLayout();
                    blockViewsId[i][j] = blockViews[i][j].getId();

                    topTextView = blockViews[i][j].findViewById(R.id.topTextView);
                    bottomTextView = blockViews[i][j].findViewById(R.id.bottomTextView);

                    if(palette.isActualBlock(BACKGROUND, i, j))
                        ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getBackgroundColor());
                    else
                        ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getColor(0));
                    ((CardView)blockViews[i][j]).setCardElevation(0f);

                    if(topTextView != null && info[i][j][TOP] != null){
                        topTextView.setText(info[i][j][TOP]);
                        topTextView.setTypeface(typeface);
                        if(info[i][j][TOP].length()>1)
                            topTextView.setFontFeatureSettings("afrc");
                    }

                    if(bottomTextView != null && info[i][j][BOTTOM] != null){
                        bottomTextView.setText(info[i][j][BOTTOM]);
                        bottomTextView.setTypeface(typeface);
                    }

                }
            }
        }
        else{
            for(int i=0;i<BOARD_ROW;i++){
                for(int j=0;j<BOARD_COL;j++){
                    if(!palette.isActualBlock(FOREGROUND, i,j))
                        blockViews[i][j] = LayoutInflater.from(mContext).inflate(palette.getDummyLineId(), null, true);
                    else
                        blockViews[i][j] = LayoutInflater.from(mContext).inflate(palette.getInnerLineId(), null, true);
                    blockViews[i][j].setId(View.generateViewId());
                    blockViewsId[i][j] = blockViews[i][j].getId();

                    linearLayout = blockViews[i][j].findViewById(R.id.block_layout);
                    linearLayout.getLayoutParams().height = LENGTH;
                    linearLayout.getLayoutParams().width = LENGTH;
                    linearLayout.requestLayout();

                    topTextView = blockViews[i][j].findViewById(R.id.topTextView);
                    bottomTextView = blockViews[i][j].findViewById(R.id.bottomTextView);

                    if(palette.isActualBlock(BACKGROUND, i, j))
                        ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getBackgroundColor());
                    else
                        ((CardView)blockViews[i][j]).setCardBackgroundColor(palette.getColor(0));
                    ((CardView)blockViews[i][j]).setCardElevation(0f);

                    if(topTextView != null)
                        topTextView.setText(info[i][j][TOP]);
                    if(bottomTextView != null)
                        bottomTextView.setText(info[i][j][BOTTOM]);
                }
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
