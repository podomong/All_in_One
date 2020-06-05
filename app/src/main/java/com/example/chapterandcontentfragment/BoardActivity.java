package com.example.chapterandcontentfragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class BoardActivity extends AppCompatActivity {
    final int HORIZONTAL = 0;
    final int VERTICAL = 1;
    final int DOUBLE_VERTICAL = 2;
    final int FALSE = 0;
    final int TRUE = 1;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Intent intent = getIntent();
        int detailId = intent.getIntExtra("DETAIL_ID", 0);
        Toast.makeText(this, "DETAIL_ID "+detailId,Toast.LENGTH_LONG).show();
        AllinOneDBHelper dbHelper = new AllinOneDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+AllinOneContract.Board.TABLE_NAME,null);
        cursor.moveToPosition(detailId-1);

        final int BOARD_ROW = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.ROW));
        final int BOARD_COL = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.COL));
        final int isGuideModeOn = cursor.getInt(cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.GUIDE_MODE)));
        final int boardType = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.TYPE));

        cursor.close();
        db.close();
        dbHelper.close();

        Toast.makeText(this, BOARD_ROW+","+BOARD_COL+","+isGuideModeOn+","+boardType,Toast.LENGTH_LONG).show();


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
        if(boardType == VERTICAL){

            VerticalBoard verticalBoard = new VerticalBoard(BOARD_ROW, BOARD_COL, this);
            verticalBoard.setDistance(boardImage.getBlockLength());
            verticalBoard.setBlockImages(boardImage.getImageViewsId());
            if(isGuideModeOn == TRUE)
                verticalBoard.setGuide(true);
        }
        else{
            HorizontalBoard horizontalBoard = new HorizontalBoard(BOARD_ROW, BOARD_COL, this);
            horizontalBoard.setDistance(boardImage.getBlockLength());
            horizontalBoard.setBlockImages(boardImage.getImageViewsId());
            if(isGuideModeOn == TRUE)
                horizontalBoard.setGuide(true);
        }


    }
}
