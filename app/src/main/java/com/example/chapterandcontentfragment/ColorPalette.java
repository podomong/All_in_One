package com.example.chapterandcontentfragment;

import android.graphics.Color;

public class ColorPalette {
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

    int[] colors = new int[LENGTH];
    int backgroundColor;
    int foregroundColor;
    private boolean isColorModeOn = false;

    ColorPalette(){
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
    }
    void setColor(int i, int color){
        colors[i] = color;
    }

    int getColor(int i){
        return colors[i];
    }

    void setColorMode(boolean flag){isColorModeOn = flag;}
    boolean isColorModeOn(){return isColorModeOn;}

    int getBackgroundColor(){return backgroundColor;}
    int getForegroundColor(){return foregroundColor;}

    int[] getPaltette(){return colors;}
}
