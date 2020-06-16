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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BoardActivity extends AppCompatActivity {
    final int HORIZONTAL = 0;
    final int VERTICAL = 1;
    final int TOP = 0;
    final int BOTTOM = 1;
    final int DOUBLE_VERTICAL = 2;
    final int FALSE = 0;
    final int TRUE = 1;

    private int BOARD_ROW = 6;
    private int BOARD_COL = 10;
    private int MARGIN = 2;
    private int isGuideModeOn;
    private int boardType;

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
        int detailId = intent.getIntExtra("DETAIL_ID", 0);
        //Toast.makeText(this, "DETAIL_ID "+detailId,Toast.LENGTH_LONG).show();
        AllinOneDBHelper dbHelper = new AllinOneDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+AllinOneContract.Board.TABLE_NAME,null);
        cursor.moveToPosition(detailId-1);

        BOARD_ROW = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.ROW));
        BOARD_COL = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.COL));
        isGuideModeOn = cursor.getInt(cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.GUIDE_MODE)));
        boardType = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.TYPE));
        cursor.close();

        Cursor detailCursor = db.rawQuery("SELECT * FROM "+AllinOneContract.Board.TABLE_NAME+detailId,null);

        if(boardType == HORIZONTAL)
            blockInfoReader = new BlockInfoReader(BOARD_ROW, BOARD_COL+1, this);
        else if(boardType == VERTICAL)
            blockInfoReader = new BlockInfoReader(BOARD_ROW+1, BOARD_COL, this);

        while (detailCursor.moveToNext()){
            int _id = detailCursor.getInt(detailCursor.getColumnIndex(AllinOneContract.Board._ID));
            String topText = detailCursor.getString(detailCursor.getColumnIndex(AllinOneContract.Board.TOP_TEXT));
            String bottomText = detailCursor.getString(detailCursor.getColumnIndex(AllinOneContract.Board.BOTTOM_TEXT));

            blockInfoReader.makeBlockInfo(_id, TOP, topText);
            blockInfoReader.makeBlockInfo(_id, BOTTOM, bottomText);
        }

        if(boardType == HORIZONTAL)
            createHorizontalBoard();
        else if(boardType == VERTICAL)
            createVerticalBoard();


        detailCursor.close();
        db.close();
        dbHelper.close();

        Toast.makeText(this, BOARD_ROW+","+BOARD_COL+","+isGuideModeOn+","+boardType,Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void createHorizontalBoard(){
        /*기기의 화면 크기를 가져옴*/
        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        ColorPalette palette = new ColorPalette();
        palette.setColorMode(false);

        /*N*M 크기의 백그라운도 보드 생성*/
        BlockCreator backgroundBlocks = new BlockCreator(BOARD_ROW, BOARD_COL+1, this);
        backgroundBlocks.setDisplayWidth(displaySize.x);
        backgroundBlocks.setDisplayHeight(displaySize.y);
        backgroundBlocks.calBlockLength();
        backgroundBlocks.setColorPalette(palette);
        backgroundBlocks.setBoardType(HORIZONTAL);
        backgroundBlocks.makeBlocks(R.layout.block_background, blockInfoReader.getInfo());

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
        foregroundBlocks.setBoardType(HORIZONTAL);
        foregroundBlocks.makeBlocks(R.layout.block_foreground, palette);

        BoardCreator foregroundBoard = new BoardCreator(BOARD_ROW, BOARD_COL+1, this);
        foregroundBoard.setBlockViews(foregroundBlocks.getBlockViews());
        foregroundBoard.setMargin(MARGIN);
        foregroundBoard.initConstraint(R.id.constraintLayout);
        foregroundBoard.constraintBlocks();

        HorizontalBoard horizontalBoard = new HorizontalBoard(BOARD_ROW, BOARD_COL+1, this);
        horizontalBoard.setDistance(foregroundBlocks.getBlockLength(), MARGIN);
        horizontalBoard.setBlockViews(foregroundBlocks.getBlockViewsId());
        //horizontalBoard.setGuide(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void createVerticalBoard(){
        //기기의 화면 크기를 가져옴
        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        ColorPalette palette = new ColorPalette();
        palette.setColorMode(false);

        //N*M 크기의 백그라운도 보드 생성 세로
        BlockCreator backgroundBlocks = new BlockCreator(BOARD_ROW+1, BOARD_COL, this);
        backgroundBlocks.setDisplayWidth(displaySize.x);
        backgroundBlocks.setDisplayHeight(displaySize.y);
        backgroundBlocks.calBlockLength();
        backgroundBlocks.setColorPalette(palette);
        backgroundBlocks.setBoardType(BlockCreator.VERTICAL);
        backgroundBlocks.makeBlocks(R.layout.block_background,blockInfoReader.getInfo());

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
        foregroundBlocks.setBoardType(BlockCreator.VERTICAL);
        foregroundBlocks.makeBlocks(R.layout.block_foreground, palette);

        BoardCreator foregroundBoard = new BoardCreator(BOARD_ROW+1, BOARD_COL, this);
        foregroundBoard.setBlockViews(foregroundBlocks.getBlockViews());
        foregroundBoard.setMargin(MARGIN);
        foregroundBoard.initConstraint(R.id.constraintLayout);
        foregroundBoard.constraintBlocks();

        //생성된 블럭 이미지에 터치 이벤트 및 이동 애니메이션 설정
        VerticalBoard verticalBoard = new VerticalBoard(BOARD_ROW+1, BOARD_COL, this);
        verticalBoard.setDistance(foregroundBlocks.getBlockLength(), MARGIN);
        verticalBoard.setBlockViews(foregroundBlocks.getBlockViewsId());
        //verticalBoard.setGuide(true);
    }
}
