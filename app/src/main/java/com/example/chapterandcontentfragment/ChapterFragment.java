package com.example.chapterandcontentfragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChapterFragment extends Fragment {
    private RecyclerView recyclerView;
    private ChapterAdapter chapterAdapter;
    private ArrayList<ChapterItem> chapterItems;
    private int courseId;

    public static ChapterFragment newInstance(int courseId){
        ChapterFragment chapterFragment = new ChapterFragment();
        Bundle args = new Bundle();
        //args.putString(AllinOneContract.Chapter.TITLE, );
        args.putInt(AllinOneContract.Course.COURSE_ID, courseId);
        chapterFragment.setArguments(args);
        return chapterFragment;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        courseId = getArguments().getInt(AllinOneContract.Course.COURSE_ID);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.chapter_fragment, container, false);

        chapterItems = new ArrayList<>();

        AllinOneDBHelper dbHelper = new AllinOneDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+AllinOneContract.Chapter.TABLE_NAME, null);
        while(cursor.moveToNext()){
            int curCourseId = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Chapter.COURSE_ID));
            if(curCourseId != this.courseId) break;
            int chapterId = cursor.getInt(cursor.getColumnIndex(AllinOneContract.Chapter.CHPATER_ID));
            String title =
                    cursor.getString(cursor.getColumnIndex(AllinOneContract.Chapter.TITLE));
            byte[] rawImage =
                    cursor.getBlob(cursor.getColumnIndex(AllinOneContract.Chapter.IMAGE));
            Bitmap bmpImage = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
            chapterItems.add(new ChapterItem(title, bmpImage));
        }
        cursor.close();
        db.close();
        dbHelper.close();

        //final int courseId;
        //final int chapterId;

        recyclerView = (RecyclerView) rootView.findViewById(R.id.chapterList);
        chapterAdapter = new ChapterAdapter(getActivity(), chapterItems);
        chapterAdapter.setOnItemClickListener(new ChapterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(getActivity(),"Hello. You pushed Chapter "+(position+1)+".",Toast.LENGTH_SHORT).show();

                Fragment detailFragment = DetailFragment.newInstance(courseId, position+1, chapterItems.get(position).getTitle());
                //Bundle args = new Bundle();
                //args.putString(AllinOneContract.Chapter.TITLE, chapterItems.get(position).getTitle());
                //args.putInt(AllinOneContract.Course.COURSE_ID, courseId);
                //detailFragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.detailFragment, detailFragment);
                transaction.commit();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(chapterAdapter);

        return rootView;
    }
}
