package com.example.chapterandcontentfragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class UnitBar {
    static public int UNIT_BAR_NUM = 4;

    private int LENGTH;
    private int MARGIN;
    private Context context;
    private LinearLayout[] unitBarType = new LinearLayout[4];
    private int curUnitBarIndex = 0;
    UnitBar(Context context){
        this.context = context;
    }

    void setLength(int length, int margin){
        LENGTH = length;
        MARGIN = margin;
    }

    void setUnitBar(FrameLayout unitBar){
        //enroll unit_bar_0
        unitBarType[0] = (LinearLayout) ((Activity)context).getLayoutInflater().inflate(R.layout.unit_bar_0, null);
        for(int i=0;i<unitBarType[0].getChildCount();i++){
            View child = unitBarType[0].getChildAt(i);
            child.getLayoutParams().width = LENGTH;
            ((LinearLayout.LayoutParams)child.getLayoutParams()).setMargins(MARGIN, 0,0,0);
            child.requestLayout();
        }

        //enroll unit_bar_1
        unitBarType[1] = (LinearLayout) ((Activity)context).getLayoutInflater().inflate(R.layout.unit_bar_1, null);
        for(int i=0;i<unitBarType[1].getChildCount();i++){
            View child = unitBarType[1].getChildAt(i);
            child.getLayoutParams().width = LENGTH;
            ((LinearLayout.LayoutParams)child.getLayoutParams()).setMargins(MARGIN, 0,0,0);
            child.requestLayout();
        }

        LinearLayout topUnitBar, bottomUnitBar;
        int[] lengthSet = new int[3];

        //enroll unit_bar_2
        unitBarType[2] = (LinearLayout) ((Activity)context).getLayoutInflater().inflate(R.layout.unit_bar_2, null);
        topUnitBar = unitBarType[2].findViewById(R.id.topUnitBar);
        bottomUnitBar = unitBarType[2].findViewById(R.id.bottomUnitBar);
        for(int i=0;i<topUnitBar.getChildCount();i++){
            View child = topUnitBar.getChildAt(i);
            child.getLayoutParams().width = LENGTH;
            ((LinearLayout.LayoutParams)child.getLayoutParams()).setMargins(MARGIN, 0,0,0);
            child.requestLayout();
        }
        lengthSet[0] = 4*LENGTH + 3*MARGIN;
        lengthSet[1] = 4*LENGTH + 3*MARGIN;
        lengthSet[2] = 2*LENGTH + MARGIN;

        for(int i=0;i<bottomUnitBar.getChildCount();i++){
            View child = bottomUnitBar.getChildAt(i);
            child.getLayoutParams().width = lengthSet[i];
            ((LinearLayout.LayoutParams)child.getLayoutParams()).setMargins(MARGIN, 0,0,0);
            child.requestLayout();
        }

        //enroll unit_bar_3
        unitBarType[3] = (LinearLayout) ((Activity)context).getLayoutInflater().inflate(R.layout.unit_bar_3, null);
        topUnitBar = unitBarType[3].findViewById(R.id.topUnitBar);
        bottomUnitBar = unitBarType[3].findViewById(R.id.bottomUnitBar);
        for(int i=0;i<topUnitBar.getChildCount();i++){
            View child = topUnitBar.getChildAt(i);
            child.getLayoutParams().width = LENGTH;
            ((LinearLayout.LayoutParams)child.getLayoutParams()).setMargins(MARGIN, 0,0,0);
            child.requestLayout();
        }
        lengthSet[0] = 2*LENGTH + MARGIN;
        lengthSet[1] = 4*LENGTH + 3*MARGIN;
        lengthSet[2] = 4*LENGTH + 3*MARGIN;

        for(int i=0;i<bottomUnitBar.getChildCount();i++){
            View child = bottomUnitBar.getChildAt(i);
            child.getLayoutParams().width = lengthSet[i];
            ((LinearLayout.LayoutParams)child.getLayoutParams()).setMargins(MARGIN, 0,0,0);
            child.requestLayout();
        }


        unitBar.addView(unitBarType[0]);
        unitBar.addView(unitBarType[1]);
        unitBar.addView(unitBarType[2]);
        unitBar.addView(unitBarType[3]);

        setOffUnitBarType(1);
        setOffUnitBarType(2);
        setOffUnitBarType(3);
    }

    public void setOnUnitBarType(int index){
        setOffUnitBarType(curUnitBarIndex);
        unitBarType[index].setVisibility(View.VISIBLE);
        curUnitBarIndex = index;
    }

    private void setOffUnitBarType(int index){
        unitBarType[index].setVisibility(View.GONE);
    }
}
