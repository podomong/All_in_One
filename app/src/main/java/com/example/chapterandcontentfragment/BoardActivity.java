package com.example.chapterandcontentfragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
    private int backgroundColorIndex;
    private int boardNum;

    private BlockCreator[] backgroundBlocks = new BlockCreator[2];
    private BlockCreator[] foregroundBlocks = new BlockCreator[2];
    private BoardCreator[] backgroundBoard = new BoardCreator[2];
    private BoardCreator[] foregroundBoard = new BoardCreator[2];
    private VerticalBoard[] verticalBoard = new VerticalBoard[2];
    private HorizontalBoard[] horizontalBoard = new HorizontalBoard[2];
    private BaseBar[] backgroundBaseBar = new BaseBar[2];
    private BaseBar[] foregroundBaseBar = new BaseBar[2];

    private FrameLayout unitBar;

    private Button button, unitBarButton;
    private FrameLayout topFrame, bottomFrame;

    private BlockInfoReader blockInfoReader, topBlockInfoReader;

    @RequiresApi(api = Build.VERSION_CODES.P)
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
        backgroundColorIndex = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.COLOR_MODE));
        boardType = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.TYPE));
        boardNum = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Board.NUM));
        //System.out.println("guide mode : "+isGuideModeOn+", color mode : "+isColorModeOn);
        cursor.close();

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

        if(boardId == 11){
            Cursor topBoardDetailCursor = db.rawQuery("SELECT * FROM "+AllinOneContract.Board.TABLE_NAME+31,null);
            topBlockInfoReader = new BlockInfoReader(BOARD_ROW+1,BOARD_COL,this);

            while (topBoardDetailCursor.moveToNext()){
                int _id = topBoardDetailCursor.getInt(topBoardDetailCursor.getColumnIndex(AllinOneContract.Board._ID));

                String topText = topBoardDetailCursor.getString(topBoardDetailCursor.getColumnIndex(AllinOneContract.Board.TOP_TEXT));
                String bottomText = topBoardDetailCursor.getString(topBoardDetailCursor.getColumnIndex(AllinOneContract.Board.BOTTOM_TEXT));
                System.out.println("top text : "+topText + ", bottom text : "+bottomText);

                topBlockInfoReader.makeBlockInfo(_id, TOP, topText);
                topBlockInfoReader.makeBlockInfo(_id,BOTTOM, bottomText);
            }

            topBoardDetailCursor.close();
        }

        button = findViewById(R.id.button);
        topFrame = findViewById(R.id.topFrame);
        bottomFrame = findViewById(R.id.bottomFrame);
        unitBar = findViewById(R.id.unitBar);
        unitBarButton = findViewById(R.id.unitBarButton);

        if(boardNum>=2){
            switch (boardType){
                case BlockCreator.HORIZONTAL:
                case BlockCreator.HORIZONTAL_STAIR:
                    createHorizontalBoard(boardId,R.id.topFrame, boardType, BoardCreator.TOP_BOARD, backgroundColorIndex, isGuideModeOn);
                    createHorizontalBoard(boardId,R.id.bottomFrame, boardType, BoardCreator.BOTTOM_BOARD, backgroundColorIndex, isGuideModeOn);
                    smallerToLarger();
                    break;
                case BlockCreator.VERTICAL:
                case BlockCreator.VERTICAL_UPSIDE_DOWN:
                    if(boardId != 11)
                        createVerticalBoard(boardId,R.id.topFrame, boardType, BoardCreator.TOP_BOARD, backgroundColorIndex, isGuideModeOn);
                    else
                        createVerticalBoard(boardId,R.id.topFrame, BlockCreator.VERTICAL_UPSIDE_DOWN, BoardCreator.TOP_BOARD, backgroundColorIndex, isGuideModeOn);
                    createVerticalBoard(boardId,R.id.bottomFrame, boardType, BoardCreator.BOTTOM_BOARD, backgroundColorIndex, isGuideModeOn);
                    if(boardId != 11)
                        smallerToLarger();
                    break;
            }

            if(boardId != 11){
                unitBarButton.setVisibility(View.GONE);
                button.setOnClickListener(new View.OnClickListener() {
                    int curScale = BlockCreator.LARGER;
                    @Override
                    public void onClick(View v) {
                        switch (curScale){
                            case BlockCreator.SMALLER:
                                smallerToLarger();
                                curScale = BlockCreator.LARGER;
                                button.setText("2개판");
                                if(boardNum>=3)
                                    getSupportActionBar().show();
                                unitBarButton.setVisibility(View.GONE);
                                break;
                            case BlockCreator.LARGER:
                                largerToSmaller();
                                curScale = BlockCreator.SMALLER;
                                button.setText("1개판");
                                if(boardNum>=3)
                                    getSupportActionBar().hide();
                                unitBarButton.setVisibility(View.GONE);
                                break;
                        }
                    }
                });
            }
            else{
                getSupportActionBar().hide();
                foregroundBoard[BoardCreator.BOTTOM_BOARD].attachToBaseLine(BoardCreator.BOTTOM_BOARD);
                backgroundBoard[BoardCreator.BOTTOM_BOARD].attachToBaseLine(BoardCreator.BOTTOM_BOARD);
                foregroundBoard[BoardCreator.TOP_BOARD].attachToBaseLine(BoardCreator.TOP_BOARD);
                backgroundBoard[BoardCreator.TOP_BOARD].attachToBaseLine(BoardCreator.TOP_BOARD);

                final UnitBar unitBarControl = new UnitBar(this);
                unitBarControl.setLength( backgroundBlocks[BoardCreator.TOP_BOARD].getBlockLength(), MARGIN);
                unitBarControl.setUnitBar(unitBar);

                final String []unitBarText = {"소수\r\n자리막대","자연수\r\n자리막대"};
                button.setVisibility(View.GONE);
                unitBarButton.setText(unitBarText[0]);
                unitBarButton.setOnClickListener(new View.OnClickListener() {
                    int curUnitBarType = 0;
                    @Override
                    public void onClick(View v) {
                        curUnitBarType = (curUnitBarType+1)%UnitBar.UNIT_BAR_NUM;
                        unitBarControl.setOnUnitBarType(curUnitBarType);
                        unitBarButton.setText(unitBarText[curUnitBarType]);
                        button.setVisibility(View.GONE);
                    }
                });
            }
        }
        else{
            disableBottomViews();

            switch (boardType){
                case BlockCreator.HORIZONTAL:
                case BlockCreator.HORIZONTAL_STAIR:
                    createHorizontalBoard(boardId,R.id.topFrame, boardType, BoardCreator.TOP_BOARD, backgroundColorIndex, isGuideModeOn);
                    break;
                case BlockCreator.VERTICAL:
                case BlockCreator.VERTICAL_UPSIDE_DOWN:
                    createVerticalBoard(boardId,R.id.topFrame, boardType, BoardCreator.TOP_BOARD, backgroundColorIndex, isGuideModeOn);
                    break;
            }
        }

        detailCursor.close();
        db.close();
        dbHelper.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void createHorizontalBoard(int boardId, int guideId, int curBoardType, int curBoardPos, int backgroundColorIndex, int isGuideModeOn){
        /*기기의 화면 크기를 가져옴*/
        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        ColorPalette palette = new ColorPalette(BOARD_ROW, BOARD_COL+1, this);
        palette.setColorMode(0);
        palette.makeActualBlocks(curBoardType);
        palette.initBackgroundXML(boardId);

        /*N*M 크기의 백그라운도 보드 생성*/
        final BlockCreator backgroundBlocks = new BlockCreator(BOARD_ROW, BOARD_COL+1, this);
        backgroundBlocks.setBlockSizeRate(boardNum);
        backgroundBlocks.setDisplayWidth(displaySize.x);
        backgroundBlocks.setDisplayHeight(displaySize.y);
        backgroundBlocks.calBlockLength();
        backgroundBlocks.setColorPalette(palette);
        backgroundBlocks.setBoardType(curBoardType);
        backgroundBlocks.makeBackgroundBlocks(boardId, blockInfoReader.getInfo(), backgroundColorIndex);

        BaseBar backgroundBaseBar = new BaseBar(BOARD_ROW, BOARD_COL+1, this);
        backgroundBaseBar.setBoardType(boardType);
        backgroundBaseBar.initBaseBar();
        backgroundBaseBar.setLength(backgroundBlocks.getBlockLength(),MARGIN);
        backgroundBaseBar.calBarLength();

        LabelBar backgroundLabelBar = null;
        if(14<=boardId&&boardId<=17){
            backgroundLabelBar = new LabelBar(BOARD_ROW, BOARD_COL+1, this);
            backgroundLabelBar.initLabels(boardId);
            backgroundLabelBar.setLength(backgroundBlocks.getBlockLength());
            backgroundLabelBar.setLabelBars();
        }


        BoardCreator backgroundBoard = new BoardCreator(BOARD_ROW, BOARD_COL+1, this);
        backgroundBoard.setBlockViews(backgroundBlocks.getBlockViews());
        backgroundBoard.setBaseBar(boardType, backgroundBaseBar);
        if(backgroundLabelBar != null)
            backgroundBoard.setLabelBar(backgroundLabelBar);
        backgroundBoard.setMargin(MARGIN);
        backgroundBoard.initConstraint(R.id.constraintLayout);
        backgroundBoard.constraintBlocks(guideId);

        /*N*M 크기의 포그라운드 보드 생성 가로*/
        final BlockCreator foregroundBlocks = new BlockCreator(BOARD_ROW, BOARD_COL+1, this);
        foregroundBlocks.setBlockSizeRate(boardNum);
        foregroundBlocks.setDisplayWidth(displaySize.x);
        foregroundBlocks.setDisplayHeight(displaySize.y);
        foregroundBlocks.calBlockLength();
        foregroundBlocks.setColorPalette(palette);
        foregroundBlocks.setBoardType(curBoardType);
        foregroundBlocks.makeForegroundBlocks();

        BaseBar foregroundBaseBar = new BaseBar(BOARD_ROW, BOARD_COL+1, this);
        foregroundBaseBar.setBoardType(boardType);
        foregroundBaseBar.initBaseBar();
        foregroundBaseBar.setLength(backgroundBlocks.getBlockLength(),MARGIN);
        foregroundBaseBar.calBarLength();

        LabelBar foregroundLabelBar = null;
        if(14<=boardId&&boardId<=17){
            foregroundLabelBar = new LabelBar(BOARD_ROW, BOARD_COL+1, this);
            foregroundLabelBar.initLabels(boardId);
            foregroundLabelBar.setLength(backgroundBlocks.getBlockLength());
            foregroundLabelBar.setLabelBars();
        }

        BoardCreator foregroundBoard = new BoardCreator(BOARD_ROW, BOARD_COL+1, this);
        foregroundBoard.setBlockViews(foregroundBlocks.getBlockViews());
        foregroundBoard.setBaseBar(boardType, foregroundBaseBar);
        if(foregroundLabelBar != null)
            foregroundBoard.setLabelBar(foregroundLabelBar);
        foregroundBoard.setMargin(MARGIN);
        foregroundBoard.initConstraint(R.id.constraintLayout);
        foregroundBoard.constraintBlocks(guideId);

        HorizontalBoard horizontalBoard = new HorizontalBoard(BOARD_ROW, BOARD_COL+1, this,palette.getActualBlocksOnForeground());
        horizontalBoard.setGap(foregroundBlocks.getBlockLength(), MARGIN);
        horizontalBoard.setGroup(boardId);
        horizontalBoard.setBlockViews(foregroundBlocks.getBlockViewsId());
        //horizontalBoard.setGuide(isGuideModeOn);

        this.backgroundBlocks[curBoardPos] = backgroundBlocks;
        this.backgroundBoard[curBoardPos] = backgroundBoard;
        this.backgroundBaseBar[curBoardPos] = backgroundBaseBar;
        this.foregroundBlocks[curBoardPos] = foregroundBlocks;
        this.foregroundBoard[curBoardPos] = foregroundBoard;
        this.foregroundBaseBar[curBoardPos] = foregroundBaseBar;
        this.horizontalBoard[curBoardPos] = horizontalBoard;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void createVerticalBoard(int boardId,int guideId, int curBoardType, int curBoardPos,int backgroundColorIndex, int isGuideModeOn){
        //기기의 화면 크기를 가져옴
        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        ColorPalette palette = new ColorPalette(BOARD_ROW+1,BOARD_COL,this);
        palette.makeActualBlocks(curBoardType);
        if(boardId != 11)
            palette.setColorMode(0);
        else
            palette.setColorMode(1);
        palette.initBackgroundXML(boardId);

        //N*M 크기의 백그라운도 보드 생성 세로
        BlockCreator backgroundBlocks = new BlockCreator(BOARD_ROW+1, BOARD_COL, this);
        backgroundBlocks.setBlockSizeRate(boardNum);
        backgroundBlocks.setDisplayWidth(displaySize.x);
        backgroundBlocks.setDisplayHeight(displaySize.y);
        backgroundBlocks.calBlockLength();
        backgroundBlocks.setColorPalette(palette);
        backgroundBlocks.setBoardType(curBoardType);
        if(boardId != 11)
            backgroundBlocks.makeBackgroundBlocks(boardId, blockInfoReader.getInfo(), backgroundColorIndex);
        else{
            if(curBoardPos == BoardCreator.BOTTOM_BOARD)
                backgroundBlocks.makeBackgroundBlocks(11, blockInfoReader.getInfo(), backgroundColorIndex);
            else
                backgroundBlocks.makeBackgroundBlocks(31, topBlockInfoReader.getInfo(), backgroundColorIndex);
        }
        BaseBar backgroundBaseBar = new BaseBar(BOARD_ROW, BOARD_COL+1, this);
        backgroundBaseBar.setBoardType(curBoardType);
        backgroundBaseBar.initBaseBar();
        backgroundBaseBar.setLength(backgroundBlocks.getBlockLength(),MARGIN);
        backgroundBaseBar.calBarLength();

        BoardCreator backgroundBoard = new BoardCreator(BOARD_ROW+1, BOARD_COL, this);
        backgroundBoard.setBlockViews(backgroundBlocks.getBlockViews());
        backgroundBoard.setBaseBar(curBoardType, backgroundBaseBar);
        backgroundBoard.setMargin(MARGIN);
        backgroundBoard.initConstraint(R.id.constraintLayout);
        backgroundBoard.constraintBlocks(guideId);

        //N*M 크기의 포그라운드 보드 생성 세로
        BlockCreator foregroundBlocks = new BlockCreator(BOARD_ROW+1, BOARD_COL, this);
        foregroundBlocks.setBlockSizeRate(boardNum);
        foregroundBlocks.setDisplayWidth(displaySize.x);
        foregroundBlocks.setDisplayHeight(displaySize.y);
        foregroundBlocks.calBlockLength();
        foregroundBlocks.setColorPalette(palette);
        foregroundBlocks.setBoardType(curBoardType);
        foregroundBlocks.makeForegroundBlocks();

        BaseBar foregroundBaseBar = new BaseBar(BOARD_ROW, BOARD_COL+1, this);
        foregroundBaseBar.setBoardType(curBoardType);
        foregroundBaseBar.initBaseBar();
        foregroundBaseBar.setLength(backgroundBlocks.getBlockLength(),MARGIN);
        foregroundBaseBar.calBarLength();

        BoardCreator foregroundBoard = new BoardCreator(BOARD_ROW+1, BOARD_COL, this);
        foregroundBoard.setBlockViews(foregroundBlocks.getBlockViews());
        foregroundBoard.setBaseBar(curBoardType, foregroundBaseBar);
        foregroundBoard.setMargin(MARGIN);
        foregroundBoard.initConstraint(R.id.constraintLayout);
        foregroundBoard.constraintBlocks(guideId);

        //생성된 블럭 이미지에 터치 이벤트 및 이동 애니메이션 설정
        VerticalBoard verticalBoard = new VerticalBoard(BOARD_ROW+1, BOARD_COL, this,palette.getActualBlocksOnForeground());
        verticalBoard.setCurBoardType(curBoardType);
        verticalBoard.setGap(foregroundBlocks.getBlockLength(), MARGIN);
        verticalBoard.setBlockViews(foregroundBlocks.getBlockViewsId());

        this.backgroundBlocks[curBoardPos] = backgroundBlocks;
        this.backgroundBoard[curBoardPos] = backgroundBoard;
        this.backgroundBaseBar[curBoardPos] = backgroundBaseBar;
        this.foregroundBlocks[curBoardPos] = foregroundBlocks;
        this.foregroundBoard[curBoardPos] = foregroundBoard;
        this.foregroundBaseBar[curBoardPos] = foregroundBaseBar;
        this.verticalBoard[curBoardPos] = verticalBoard;
    }

    void smallerToLarger(){
        foregroundBoard[BoardCreator.TOP_BOARD].detachFromBaseLine();
        backgroundBoard[BoardCreator.TOP_BOARD].detachFromBaseLine();
        foregroundBoard[BoardCreator.BOTTOM_BOARD].detachFromBaseLine();
        backgroundBoard[BoardCreator.BOTTOM_BOARD].detachFromBaseLine();

        backgroundBlocks[BoardCreator.TOP_BOARD].changeBlocksScale(BlockCreator.LARGER);
        foregroundBlocks[BoardCreator.TOP_BOARD].changeBlocksScale(BlockCreator.LARGER);
        backgroundBaseBar[BoardCreator.TOP_BOARD].changeBarLength(backgroundBlocks[BoardCreator.TOP_BOARD].getBlockLength());
        foregroundBaseBar[BoardCreator.TOP_BOARD].changeBarLength(foregroundBlocks[BoardCreator.TOP_BOARD].getBlockLength());

        bottomFrame.setVisibility(View.GONE);
        unitBar.setVisibility(View.GONE);
        backgroundBlocks[BoardCreator.BOTTOM_BOARD].toggleBlocksVisibility();
        foregroundBlocks[BoardCreator.BOTTOM_BOARD].toggleBlocksVisibility();
        backgroundBaseBar[BoardCreator.BOTTOM_BOARD].toggleBarVisibility();
        foregroundBaseBar[BoardCreator.BOTTOM_BOARD].toggleBarVisibility();

        if(verticalBoard[BoardCreator.TOP_BOARD] != null){
            verticalBoard[BoardCreator.TOP_BOARD].setGap(backgroundBlocks[BoardCreator.TOP_BOARD].getBlockLength(), MARGIN);
            verticalBoard[BoardCreator.TOP_BOARD].resetBlocks();
        }

        if(verticalBoard[BoardCreator.BOTTOM_BOARD] != null){
            verticalBoard[BoardCreator.BOTTOM_BOARD].setGap(backgroundBlocks[BoardCreator.BOTTOM_BOARD].getBlockLength(), MARGIN);
            verticalBoard[BoardCreator.BOTTOM_BOARD].resetBlocks();
        }

        if(horizontalBoard[BoardCreator.TOP_BOARD]!=null){
            horizontalBoard[BoardCreator.TOP_BOARD].setGap(backgroundBlocks[BoardCreator.TOP_BOARD].getBlockLength(), MARGIN);
            horizontalBoard[BoardCreator.TOP_BOARD].resetBlocks();
        }

        if(horizontalBoard[BoardCreator.BOTTOM_BOARD] != null){
            horizontalBoard[BoardCreator.BOTTOM_BOARD].setGap(backgroundBlocks[BoardCreator.BOTTOM_BOARD].getBlockLength(), MARGIN);
            horizontalBoard[BoardCreator.BOTTOM_BOARD].resetBlocks();
        }
    }

    void largerToSmaller(){
        backgroundBlocks[BoardCreator.TOP_BOARD].changeBlocksScale(BlockCreator.SMALLER);
        foregroundBlocks[BoardCreator.TOP_BOARD].changeBlocksScale(BlockCreator.SMALLER);
        backgroundBaseBar[BoardCreator.TOP_BOARD].changeBarLength(backgroundBlocks[BoardCreator.TOP_BOARD].getBlockLength());
        foregroundBaseBar[BoardCreator.TOP_BOARD].changeBarLength(foregroundBlocks[BoardCreator.TOP_BOARD].getBlockLength());

        bottomFrame.setVisibility(View.VISIBLE);
        unitBar.setVisibility(View.VISIBLE);
        backgroundBlocks[BoardCreator.BOTTOM_BOARD].toggleBlocksVisibility();
        foregroundBlocks[BoardCreator.BOTTOM_BOARD].toggleBlocksVisibility();
        backgroundBaseBar[BoardCreator.BOTTOM_BOARD].toggleBarVisibility();
        foregroundBaseBar[BoardCreator.BOTTOM_BOARD].toggleBarVisibility();

        foregroundBoard[BoardCreator.BOTTOM_BOARD].attachToBaseLine(BoardCreator.BOTTOM_BOARD);
        backgroundBoard[BoardCreator.BOTTOM_BOARD].attachToBaseLine(BoardCreator.BOTTOM_BOARD);
        foregroundBoard[BoardCreator.TOP_BOARD].attachToBaseLine(BoardCreator.TOP_BOARD);
        backgroundBoard[BoardCreator.TOP_BOARD].attachToBaseLine(BoardCreator.TOP_BOARD);

        if(verticalBoard[BoardCreator.TOP_BOARD] != null){
            verticalBoard[BoardCreator.TOP_BOARD].setGap(backgroundBlocks[BoardCreator.TOP_BOARD].getBlockLength(), MARGIN);
            verticalBoard[BoardCreator.TOP_BOARD].resetBlocks();
        }

        if(verticalBoard[BoardCreator.BOTTOM_BOARD] != null){
            verticalBoard[BoardCreator.BOTTOM_BOARD].setGap(backgroundBlocks[BoardCreator.BOTTOM_BOARD].getBlockLength(), MARGIN);
            verticalBoard[BoardCreator.BOTTOM_BOARD].resetBlocks();
        }

        if(horizontalBoard[BoardCreator.TOP_BOARD] != null){
            horizontalBoard[BoardCreator.TOP_BOARD].setGap(backgroundBlocks[BoardCreator.TOP_BOARD].getBlockLength(), MARGIN);
            horizontalBoard[BoardCreator.TOP_BOARD].resetBlocks();
        }

        if(horizontalBoard[BoardCreator.BOTTOM_BOARD] != null){
            horizontalBoard[BoardCreator.BOTTOM_BOARD].setGap(backgroundBlocks[BoardCreator.BOTTOM_BOARD].getBlockLength(), MARGIN);
            horizontalBoard[BoardCreator.BOTTOM_BOARD].resetBlocks();
        }
    }

    void disableBottomViews(){
        bottomFrame.setVisibility(View.GONE);
        unitBar.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        unitBarButton.setVisibility(View.GONE);
    }
}
