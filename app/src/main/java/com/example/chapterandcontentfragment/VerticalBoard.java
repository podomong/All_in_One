package com.example.chapterandcontentfragment;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import java.util.Stack;

public class VerticalBoard extends Board{
    private int DISTANCE = 50;
    private int MARGIN = 2;
    private int GAP;

    private Stack<Pair<Integer, Integer>>changedStateStack;
    private int currentStates[][];
    private View blockViews[][];
    private int scores[];

    //private TextView scoreView;
    private boolean isGuideOn;
    private int carryPos;
    private ColorPalette palette;

    VerticalBoard(int boardRow, int boardCol, Context context){
        super(boardRow, boardCol, context);

        changedStateStack = new Stack<Pair<Integer, Integer>>();
        currentStates = new int[BOARD_ROW][BOARD_COL];
        blockViews = new View[BOARD_ROW][BOARD_COL];
        scores = new int[BOARD_COL];
        isGuideOn = false;

        for(int i=1;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++)
                currentStates[i][j] = DOWN;
        }

        for(int j=0;j<BOARD_COL;j++)
            scores[j] = 0;
        //scoreView = ((Activity)mContext).findViewById(R.id.scoreView);

        carryPos = -1;
    }

    private long calScores(){
        long sum = 0;
        long d = 1;
        for(int j=BOARD_COL-1;j>=0;j--){
            sum+=scores[j]*d;
            d*=10;
        }
        return sum;
    }

    private void checkChange(final int TOUTCHED_ROW, final int TOUTCHED_COL){
        switch (currentStates[TOUTCHED_ROW][TOUTCHED_COL]){
            case DOWN:
                for(int row = TOUTCHED_ROW; row > 0; row--){
                    if(currentStates[row][TOUTCHED_COL] == UP) break;
                    changedStateStack.push(new Pair<Integer, Integer>(row, TOUTCHED_COL));
                }
                break;
            case UP:
                for(int row = TOUTCHED_ROW; row < BOARD_ROW; row++){
                    if(currentStates[row][TOUTCHED_COL] == DOWN) break;
                    changedStateStack.push(new Pair<Integer, Integer>(row, TOUTCHED_COL));
                }
                break;
        }
    }

    private void toggleState(final int i, final int j){
        switch (currentStates[i][j]){
            case DOWN:
                currentStates[i][j] = UP;
                scores[j]++;
                if(scores[j] == BOARD_ROW-1)
                    carryPos = j;
                break;
            case UP:
                currentStates[i][j] = DOWN;
                scores[j]--;
                break;
        }
    }

    private void moveBlock(final int i, final int j){
        final int CUR_STATE = currentStates[i][j];
        switch (CUR_STATE){
            case DOWN:
                blockViews[i][j].animate().translationY(0).start();
                break;
            case UP:
                blockViews[i][j].animate().translationY(-(DISTANCE+MARGIN)).start();
                break;
        }
    }

    private void applyChange(){
        int i, j;
        while(!changedStateStack.empty()){
            i = changedStateStack.peek().first;
            j = changedStateStack.peek().second;
            toggleState(i, j);
            moveBlock(i, j);
            changedStateStack.pop();
        }
    }

    private void resetBlocks(int j){
        scores[j] = 0;
        for(int i=1;i<BOARD_ROW;i++){
            if(currentStates[i][j] == DOWN) break;
            currentStates[i][j] = DOWN;
            moveBlock(i,j);
        }
    }

    private void guideMove(){
        if(!isGuideOn || carryPos == -1) return;
        for(int j=carryPos;j>=0;j--){
            if(scores[j] != BOARD_ROW-1) break;
            if(j>0){
                int i = scores[j-1]+1;
                toggleState(i, j-1);
                moveBlock(i, j-1);
                resetBlocks(j);
            }
            else
                resetBlocks(j);
        }

        carryPos = -1;
    }

    public void setBlockViews(int blockViewsId[][]){
        for(int i=1;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                if(palette.isActualBlock(BlockCreator.FOREGROUND, i,j))
                    setBlockView(i,j,blockViewsId[i][j]);
            }

        }
    }

    private void setBlockView(int i, int j, int blockId){
        final int constantRowId = i;
        final int constantColId = j;
        blockViews[i][j] = ((Activity)mContext).findViewById(blockId);

        blockViews[i][j].setOnTouchListener(new View.OnTouchListener(){
            int rowId = constantRowId;
            int colId = constantColId;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        checkChange(rowId, colId);
                        applyChange();
                        guideMove();
                        //scoreView.setText(Long.toString(calScores()));
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;

                    case MotionEvent.ACTION_UP:
                        break;

                    default:
                        break;
                }

                return false;
            }
        });
    }

    public void setDistance(int distance, int margin) {
        DISTANCE = distance;
        MARGIN = margin;
    };

    public void setGuide(int flag){
        if(flag == 0)
            isGuideOn = false;
        else
            isGuideOn = true;
    }

    void setColorPalette(ColorPalette palette){
        this.palette= palette;
    }
}
