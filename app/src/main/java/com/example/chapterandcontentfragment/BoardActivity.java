package com.example.chapterandcontentfragment;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class BoardActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        final int BOARD_ROW = 5;
        final int BOARD_COL = 5;

        /*기기의 화면 크기를 가져옴*/
        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);

        /*NxM 크기의 블럭 이미지 뷰를 생성하고 배치함*/
        BoardImage boardImage = new BoardImage(BOARD_ROW,BOARD_COL,this);

        boardImage.setDisplayWidth(displaySize.x);
        boardImage.setDisplayHeight(displaySize.y);
        boardImage.calBlockLength();

        boardImage.makeImages();
        boardImage.initConstraint(R.id.constraintLayout);
        boardImage.constraintImages();


        /*생성된 블럭 이미지에 터치 이벤트 및 이동 애니메이션 설정*/
        VerticalBoard verticalBoard = new VerticalBoard(BOARD_ROW, BOARD_COL, this);
        verticalBoard.setDistance(boardImage.getBlockLength());
        verticalBoard.setBlockImages(boardImage.getImageViewsId());
        verticalBoard.setGuide(true);

        /*HorizontalBoard horizontalBoard = new HorizontalBoard(BOARD_ROW, BOARD_COL, this);
        horizontalBoard.setDistance(boardImage.getBlockLength());
        horizontalBoard.setBlockImages(boardImage.getImageViewsId());
        horizontalBoard.setGuide(true);*/
    }
}
