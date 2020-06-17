package com.example.chapterandcontentfragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BoardActivity extends AppCompatActivity {
    final int TOP = 0;
    final int BOTTOM = 1;

    private int BOARD_ROW = 6;
    private int BOARD_COL = 10;
    private int MARGIN = 2;
    private int isGuideModeOn;
    private int boardType;
    private int isColorModeOn;

    private BlockInfoReader blockInfoReader;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);
        
        Intent intent = getIntent();
        int boardId = intent.getIntExtra("DETAIL_ID", 0);
        AllinOneDBHelper dbHelper = new AllinOneDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+AllinOneContract.Board.TABLE_NAME,null);
        cursor.moveToPosition(boardId-1);

        BOARD_ROW = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.ROW));
        BOARD_COL = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.COL));
        isGuideModeOn = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.GUIDE_MODE));
        isColorModeOn = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.COLOR_MODE));
        boardType = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.TYPE));
        System.out.println("guide mode : "+isGuideModeOn+", color mode : "+isColorModeOn);
        cursor.close();

        if(boardType == BlockCreator.HORIZONTAL || boardType == BlockCreator.HORIZONTAL_STAIR){
            Cursor detailCursor = db.rawQuery("SELECT * FROM "+AllinOneContract.Board.TABLE_NAME+boardId,null);

            if(boardType == BlockCreator.HORIZONTAL || boardType == BlockCreator.HORIZONTAL_STAIR)
                blockInfoReader = new BlockInfoReader(BOARD_ROW, BOARD_COL+1, this);
            else
                blockInfoReader = new BlockInfoReader(BOARD_ROW+1, BOARD_COL, this);

            while (detailCursor.moveToNext()){
                int _id = detailCursor.getInt(detailCursor.getColumnIndex(AllinOneContract.Board._ID));

                String topText = detailCursor.getString(detailCursor.getColumnIndex(AllinOneContract.Board.TOP_TEXT));
                String bottomText = detailCursor.getString(detailCursor.getColumnIndex(AllinOneContract.Board.BOTTOM_TEXT));

                blockInfoReader.makeBlockInfo(_id, TOP, topText);
                blockInfoReader.makeBlockInfo(_id, BOTTOM, bottomText);
            }

            if(boardType == BlockCreator.HORIZONTAL || boardType == BlockCreator.HORIZONTAL_STAIR)
                createHorizontalBoard(boardId, boardType, isColorModeOn, isGuideModeOn);
            //else
            //createVerticalBoard(boardId, boardType,isColorModeOn, isGuideModeOn);
            detailCursor.close();
        }

        db.close();
        dbHelper.close();

        //Toast.makeText(this, BOARD_ROW+","+BOARD_COL+","+isGuideModeOn+","+boardType,Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void createHorizontalBoard(int boardId, int curBoardType, int isColorModeOn, int isGuideModeOn){
        /*기기의 화면 크기를 가져옴*/
        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        ColorPalette palette = new ColorPalette(BOARD_ROW, BOARD_COL+1, this);
        palette.setColorMode(isColorModeOn);
        palette.makeActualBlocks(curBoardType);
        palette.initBackgroundXML(boardId);

        /*N*M 크기의 백그라운도 보드 생성*/
        BlockCreator backgroundBlocks = new BlockCreator(BOARD_ROW, BOARD_COL+1, this);
        backgroundBlocks.setDisplayWidth(displaySize.x);
        backgroundBlocks.setDisplayHeight(displaySize.y);
        backgroundBlocks.calBlockLength();
        backgroundBlocks.setColorPalette(palette);
        backgroundBlocks.setBoardType(curBoardType);
        backgroundBlocks.makeBackgroundBlocks(boardId, blockInfoReader.getInfo());

        BoardCreator backgroundBoard = new BoardCreator(BOARD_ROW, BOARD_COL+1, this);
        backgroundBoard.setBlockViews(backgroundBlocks.getBlockViews());
        backgroundBoard.setMargin(MARGIN);
        backgroundBoard.initConstraint(R.id.constraintLayout);
        backgroundBoard.constraintBlocks();

        /*N*M 크기의 포그라운드 보드 생성 가로*/
        BlockCreator foregroundBlocks = new BlockCreator(BOARD_ROW, BOARD_COL+1, this);
        foregroundBlocks.setDisplayWidth(displaySize.x);
        foregroundBlocks.setDisplayHeight(displaySize.y);
        foregroundBlocks.calBlockLength();
        foregroundBlocks.setColorPalette(palette);
        foregroundBlocks.setBoardType(curBoardType);
        foregroundBlocks.makeForegroundBlocks();

        BoardCreator foregroundBoard = new BoardCreator(BOARD_ROW, BOARD_COL+1, this);
        foregroundBoard.setBlockViews(foregroundBlocks.getBlockViews());
        foregroundBoard.setMargin(MARGIN);
        foregroundBoard.initConstraint(R.id.constraintLayout);
        foregroundBoard.constraintBlocks();

        HorizontalBoard horizontalBoard = new HorizontalBoard(BOARD_ROW, BOARD_COL+1, this);
        horizontalBoard.setColorPalette(palette);
        horizontalBoard.setDistance(foregroundBlocks.getBlockLength(), MARGIN);
        horizontalBoard.setBlockViews(foregroundBlocks.getBlockViewsId());
        horizontalBoard.setGuide(isGuideModeOn);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void createVerticalBoard(int boardId, int curBoardType, int isColorModeOn, int isGuideModeOn){
        //기기의 화면 크기를 가져옴
        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        ColorPalette palette = new ColorPalette(BOARD_ROW+1,BOARD_COL,this);
        palette.makeActualBlocks(curBoardType);
        palette.setColorMode(isColorModeOn);
        palette.initBackgroundXML(boardId);

        //N*M 크기의 백그라운도 보드 생성 세로
        BlockCreator backgroundBlocks = new BlockCreator(BOARD_ROW+1, BOARD_COL, this);
        backgroundBlocks.setDisplayWidth(displaySize.x);
        backgroundBlocks.setDisplayHeight(displaySize.y);
        backgroundBlocks.calBlockLength();
        backgroundBlocks.setColorPalette(palette);
        backgroundBlocks.setBoardType(curBoardType);
        backgroundBlocks.makeBackgroundBlocks(boardId, blockInfoReader.getInfo());

        BoardCreator backgroundBoard = new BoardCreator(BOARD_ROW+1, BOARD_COL, this);
        backgroundBoard.setBlockViews(backgroundBlocks.getBlockViews());
        backgroundBoard.setMargin(MARGIN);
        backgroundBoard.initConstraint(R.id.constraintLayout);
        backgroundBoard.constraintBlocks();

        //N*M 크기의 포그라운드 보드 생성 세로
        BlockCreator foregroundBlocks = new BlockCreator(BOARD_ROW+1, BOARD_COL, this);
        foregroundBlocks.setDisplayWidth(displaySize.x);
        foregroundBlocks.setDisplayHeight(displaySize.y);
        foregroundBlocks.calBlockLength();
        foregroundBlocks.setColorPalette(palette);
        foregroundBlocks.setBoardType(curBoardType);
        foregroundBlocks.makeForegroundBlocks();

        BoardCreator foregroundBoard = new BoardCreator(BOARD_ROW+1, BOARD_COL, this);
        foregroundBoard.setBlockViews(foregroundBlocks.getBlockViews());
        foregroundBoard.setMargin(MARGIN);
        foregroundBoard.initConstraint(R.id.constraintLayout);
        foregroundBoard.constraintBlocks();

        //생성된 블럭 이미지에 터치 이벤트 및 이동 애니메이션 설정
        VerticalBoard verticalBoard = new VerticalBoard(BOARD_ROW+1, BOARD_COL, this);
        verticalBoard.setColorPalette(palette);
        verticalBoard.setDistance(foregroundBlocks.getBlockLength(), MARGIN);
        verticalBoard.setBlockViews(foregroundBlocks.getBlockViewsId());
        verticalBoard.setGuide(isGuideModeOn);
    }
}
