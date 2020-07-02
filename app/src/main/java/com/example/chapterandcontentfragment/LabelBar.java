package com.example.chapterandcontentfragment;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class LabelBar {
    private int WIDTH;
    private int HEIGHT;
    private int BOARD_ROW;
    private int BOARD_COL;

    private TextView labelBars[];
    private String labels[];
    Context context;

    LabelBar(int boardRow, int boardCol, Context context){
        this.context = context;
        BOARD_ROW = boardRow;
        BOARD_COL = boardCol;
    }

    void setLength(int length){
        HEIGHT = length;
        WIDTH = (5*HEIGHT)/4;
    }

    void initLabels(int boardId){
        labels = new String[BOARD_ROW];

        switch (boardId){
            case 14:
                labels[0] = "10등분";
                labels[1] = "5등분";
                labels[2] = "2등분";
                labels[3] = "1등분";
                break;
            case 15:
                labels[0] = "9등분";
                labels[1] = "3등분";
                labels[2] = "1등분";
                break;
            case 16:
                labels[0] = "8등분";
                labels[1] = "4등분";
                labels[2] = "2등분";
                labels[3] = "1등분";
                break;
            case 17:
                labels[0] = "6등분";
                labels[1] = "3등분";
                labels[2] = "2등분";
                labels[3] = "1등분";
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void setLabelBars(){
        labelBars = new TextView[BOARD_ROW];
        for(int row=0;row<BOARD_ROW;row++){
            labelBars[row] = (TextView) TextView.inflate(context, R.layout.label_bar, null);
            labelBars[row].setId(View.generateViewId());
            labelBars[row].setText(labels[row]);
            labelBars[row].setLayoutParams(new ViewGroup.LayoutParams(WIDTH, HEIGHT));

        }
    }

    TextView getLabelView(int index){
        return labelBars[index];
    }
}
