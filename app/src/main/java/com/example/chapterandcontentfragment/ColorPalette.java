package com.example.chapterandcontentfragment;

import android.content.Context;
import android.graphics.Color;

public class ColorPalette extends Board{
    private int LENGTH = 11;
    static final String TRANSPARENT = "#00000000";
    static final String RED = "#D62E21";
    static final String ORANGE = "#E38A24";
    static final String YELLOW = "#FFEA4F";
    static final String LIGHT_GREEN = "#A7D210";
    static final String GREEN = "#018E40";
    static final String TURQUOISE = "#028178";
    static final String SKY_BLUE = "#038CC2";
    static final String BLUE = "#03569A";
    static final String INDIGO = "#331269";
    static final String PURPLE = "#7D1E72";
    static final String LIGHT_YELLOW = "#F2ECE1";
    static final String WHITE ="#FFFFFF";

    public final int HORIZONTAL = 0;
    public final int VERTICAL = 1;
    public final int VERTICAL_UPSIDE_DOWN = 2;
    public final int HORIZONTAL_STAIR = 3;

    int[] colors = new int[LENGTH];
    int backgroundColor;
    int foregroundColor;
    private boolean isColorModeOn = false;
    boolean actualBlocksOnBackground[][];
    boolean actualBlocksOnForeground[][];

    private int dummyLineId;
    private int innerLineId;

    ColorPalette(int boardRow, int boardCol, Context context){
        super(boardRow, boardCol, context);

        colors[0] = Color.parseColor(TRANSPARENT);
        colors[1] = Color.parseColor(PURPLE);
        colors[2] = Color.parseColor(INDIGO);
        colors[3] = Color.parseColor(BLUE);
        colors[4] = Color.parseColor(SKY_BLUE);
        colors[5] = Color.parseColor(TURQUOISE);
        colors[6] = Color.parseColor(GREEN);
        colors[7] = Color.parseColor(LIGHT_GREEN);
        colors[8] = Color.parseColor(YELLOW);
        colors[9] = Color.parseColor(ORANGE);
        colors[10] = Color.parseColor(RED);

        backgroundColor = Color.parseColor(LIGHT_YELLOW);
        foregroundColor = Color.parseColor(WHITE);

        actualBlocksOnBackground = new boolean[boardRow][boardCol];
        actualBlocksOnForeground = new boolean[boardRow][boardCol];
    }
    void setColor(int i, int color){
        colors[i] = color;
    }

    int getColor(int i){
        return colors[i];
    }

    void setColorMode(int flag){
        if(flag == 0)
            isColorModeOn = false;
        else
            isColorModeOn = true;
    }
    boolean isColorModeOn(){return isColorModeOn;}

    int getBackgroundColor(){return backgroundColor;}
    int getForegroundColor(){return foregroundColor;}

    int[] getPaltette(){return colors;}

    boolean isActualBlock(int groundType, int i, int j){
        if(groundType == 0)
            return actualBlocksOnBackground[i][j];
        else
            return actualBlocksOnForeground[i][j];
    }

    void makeActualBlocks(int boardType){

        switch (boardType){
            case HORIZONTAL:
                for(int i=0;i<BOARD_ROW;i++){
                    for(int j=0;j<BOARD_COL;j++) {
                        actualBlocksOnBackground[i][j] = true;
                        if(j>0)
                            actualBlocksOnForeground[i][j] = true;
                        else
                            actualBlocksOnForeground[i][j] = false;
                    }
                }

                break;

            case VERTICAL:
                for(int i=0;i<BOARD_ROW;i++) {
                    for (int j = 0; j < BOARD_COL; j++) {
                        actualBlocksOnBackground[i][j] = true;
                        if (i > 0)
                            actualBlocksOnForeground[i][j] = true;
                        else
                            actualBlocksOnForeground[i][j] = false;
                    }
                }

                break;
            case VERTICAL_UPSIDE_DOWN:
                for(int i=0;i<BOARD_ROW;i++){
                    for(int j=0;j<BOARD_COL;j++){
                        actualBlocksOnBackground[i][j] = true;
                        if(i<BOARD_ROW-1)
                            actualBlocksOnForeground[i][j] = true;
                        else
                            actualBlocksOnForeground[i][j] = false;
                    }
                }

                break;
            case HORIZONTAL_STAIR:
                for(int i=0;i<BOARD_ROW;i++){
                    for(int j=0;j<BOARD_COL;j++){
                        actualBlocksOnBackground[i][j] = false;
                        actualBlocksOnForeground[i][j] = false;
                    }
                }

                final int BASE = 3;
                for(int i = 0; i<BOARD_COL-BASE+1;i++){
                    for(int j=0;j<BASE+i;j++){
                        actualBlocksOnBackground[i][j] = true;
                        if(j>0)
                            actualBlocksOnForeground[i][j] = true;
                    }
                }

                break;
        }
    }

    void initBackgroundXML(int boardId){
        switch (boardId){
            case 1:
            case 4:
            case 5:
            case 6:
                dummyLineId = R.layout.block_background_single;
                innerLineId = R.layout.block_background_single;
                break;

            case 2:
            case 3:
                dummyLineId = R.layout.block_background_double_55_45;
                innerLineId = R.layout.block_background_double_55_45;
                break;

            case 7:
                dummyLineId = R.layout.block_background_double_30_70;
                innerLineId = R.layout.block_background_single;
                break;

            case 8:
            case 9:
                dummyLineId = R.layout.block_background_single;
                innerLineId = R.layout.block_background_double_30_70;
                break;

            case 14:
            case 15:
            case 16:
            case 17:
                dummyLineId = R.layout.block_background_single;
                innerLineId = R.layout.block_background_single_fraction;
                break;

            case 27:
            case 28:
                dummyLineId = R.layout.block_background_single;
                innerLineId = R.layout.block_background_double_60_40_fraction;
                break;
        }
    }

    boolean[][] getActualBlocksOnBackground(){return actualBlocksOnBackground;}

    boolean[][] getActualBlocksOnForeground(){return actualBlocksOnForeground;}

    int getDummyLineId(){ return dummyLineId; }

    int getInnerLineId(){ return innerLineId; }
}
