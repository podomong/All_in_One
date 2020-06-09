package com.example.chapterandcontentfragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailFragment extends Fragment {
    private RecyclerView recyclerView;
    private ContentAdapter contentAdapter;
    private ArrayList<ContentItem> contentItems;
    public final String CURRENT_CHAPTER = "CURRENT_CHAPTER";

    private int courseId;
    private int chapterId;
    private String chapterTitle;

    public static DetailFragment newInstance(int courseId, int chapterId, String chapterTitle){
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(AllinOneContract.Content.COURSE_ID, courseId);
        args.putInt(AllinOneContract.Content.CHPATER_ID, chapterId);
        args.putString(AllinOneContract.Chapter.TITLE, chapterTitle);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        courseId = getArguments().getInt(AllinOneContract.Chapter.COURSE_ID);
        chapterId = getArguments().getInt(AllinOneContract.Chapter.CHPATER_ID);
        chapterTitle = getArguments().getString(AllinOneContract.Chapter.TITLE);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.detail_fragment, container, false);
        TextView titleView = rootView.findViewById(R.id.chapterTitleInContentList);
        titleView.setText(chapterTitle);
        //Toast.makeText(getActivity(), courseId +", "+chapterId,Toast.LENGTH_SHORT).show();
        contentItems = new ArrayList<>();

        AllinOneDBHelper dbHelper = new AllinOneDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        /*Cursor tempCursor = db.rawQuery("SELECT * FROM "+AllinOneContract.Chapter.TABLE_NAME +
                        " WHERE "+AllinOneContract.Chapter.COURSE_ID+" = "+courseId+
                        " AND "+AllinOneContract.Chapter.CHPATER_ID + " = "+chapterId
                , null);
        String chapterTitle = tempCursor.getString(tempCursor.getColumnIndex(AllinOneContract.Chapter.TITLE));
        titleView.setText(chapterTitle);
        tempCursor.close();*/

        Cursor cursor = db.rawQuery("SELECT * FROM "+AllinOneContract.Content.TABLE_NAME +
                        " WHERE "+AllinOneContract.Content.COURSE_ID+" = "+courseId+
                        " AND "+AllinOneContract.Content.CHPATER_ID + " = "+chapterId
                , null);



        while(cursor.moveToNext()){
            int curCourseId = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Content.COURSE_ID));
            int curChapterId = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Content.CHPATER_ID));
            if(curCourseId != this.courseId || curChapterId != this.chapterId) break;
            String instruction =
                    cursor.getString(cursor.getColumnIndex(AllinOneContract.Content.INSTRUCTION));
            int curContentId = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Content.CONTENT_ID));
            int detailId = cursor.getInt(cursor.getColumnIndex("_id"));
            contentItems.add(new ContentItem(curContentId, instruction, detailId));
        }
        cursor.close();
        db.close();
        dbHelper.close();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.contentList);
        contentAdapter = new ContentAdapter(getActivity(), contentItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(contentAdapter);

        return rootView;
    }
}

