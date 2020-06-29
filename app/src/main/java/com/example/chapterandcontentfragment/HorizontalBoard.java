package com.example.chapterandcontentfragment;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Stack;

public class HorizontalBoard extends Board{
    private int DISTANCE = 50;
    private int MARGIN = 2;
    private int GAP;

    private Stack<Pair<Integer, Integer>> changedStateStack;
    private int currentStates[][];
    private View blockViews[][];
    private int scores[];

    //private TextView scoreView;
    private boolean isGuideOn;
    boolean actualBlocks[][];


    HorizontalBoard(int boardRow, int boardCol, Context context, boolean actualBlocks[][]){
        super(boardRow, boardCol, context);

        changedStateStack = new Stack<Pair<Integer, Integer>>();
        currentStates = new int[BOARD_ROW][BOARD_COL];
        blockViews = new View[BOARD_ROW][BOARD_COL];
        scores = new int[BOARD_COL];
        isGuideOn = false;
        this.actualBlocks = actualBlocks;

        for(int i=0;i<BOARD_ROW;i++){
            for(int j=1;j<BOARD_COL;j++)
                currentStates[i][j] = RIGHT;
        }

        for(int i=0;i<BOARD_ROW;i++)
            scores[i] = 1;
    }

    private long calScores(){
        long sum = 0;
        for(int i=0;i<BOARD_ROW;i++)
            sum+=scores[i];
        return sum;
    }

    private void checkChange(final int TOUTCHED_ROW, final int TOUTCHED_COL){
        switch (currentStates[TOUTCHED_ROW][TOUTCHED_COL]){
            case LEFT:
                for(int col = TOUTCHED_COL;col<BOARD_COL;col++){
                    if(currentStates[TOUTCHED_ROW][col] == RIGHT) break;
                    changedStateStack.push(new Pair<Integer, Integer>(TOUTCHED_ROW, col));
                }
                break;
            case RIGHT:
                for(int col = TOUTCHED_COL; col > 0; col--){
                    if(currentStates[TOUTCHED_ROW][col] == LEFT) break;
                    changedStateStack.push(new Pair<Integer, Integer>(TOUTCHED_ROW, col));
                }
                break;
        }
    }

    private void toggleState(final int i, final int j){
        switch (currentStates[i][j]){
            case LEFT:
                currentStates[i][j] = RIGHT;
                scores[i]--;
                break;
            case RIGHT:
                currentStates[i][j] = LEFT;
                scores[i]++;
                break;
        }
    }

    private void moveBlock(final int i, final int j){
        final int CUR_STATE = currentStates[i][j];
        switch (CUR_STATE){
            case LEFT:
                blockViews[i][j].animate().translationX(GAP).start();
                break;
            case RIGHT:
                blockViews[i][j].animate().translationX(0).start();

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

    private void resetBlocks(int i){
        scores[i] = 1;
        for(int j=1;j<BOARD_COL;j++){
            if(currentStates[i][j] == RIGHT) break;
            currentStates[i][j] = RIGHT;
            moveBlock(i,j);
        }
    }

    public void resetBlocks(){
        for(int i=0;i<BOARD_ROW;i++)
            scores[i] = 1;

        for(int i = 0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                if(actualBlocks[i][j]){
                    currentStates[i][j] = RIGHT;
                    moveBlock(i,j);
                }
            }
        }
    }

    private void setBlocks(int i){
        for(int j = scores[i];j<BOARD_COL;j++){
            if(currentStates[i][j] == LEFT) break;
            currentStates[i][j] = LEFT;
            moveBlock(i, j);
        }
        scores[i] = BOARD_COL;
    }

    private void guideMove(int TOUTCHED_ROW, int TOUTCHED_COL){
        if(!isGuideOn) return;
        for(int i=TOUTCHED_ROW-1;i>=0;i--)
            setBlocks(i);
        for(int i=TOUTCHED_ROW+1;i<BOARD_ROW;i++)
            resetBlocks(i);
    }

    public void setBlockViews(int blockViewsId[][]){
        for(int i=0;i<BOARD_ROW;i++){
            for(int j=1;j<BOARD_COL;j++){
                if(actualBlocks[i][j])
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
                        guideMove(rowId, colId);
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

    public void setGap(int distance, int margin){
        DISTANCE = distance;
        MARGIN = margin;
        GAP = -(DISTANCE+MARGIN);
    }

    public void setGuide(int flag){
        if(flag == 0)
            isGuideOn = false;
        else
            isGuideOn = true;
    }
}