package com.example.chapterandcontentfragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ChapterAndContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_course);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);

        Intent intent = getIntent();
        int curCourseId = intent.getIntExtra(AllinOneContract.Course.COURSE_ID, 0);
        Bundle bundle = new Bundle();
        bundle.putInt(AllinOneContract.Course.COURSE_ID, curCourseId);

        Fragment chapterFragment = ChapterFragment.newInstance(curCourseId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.chapterFragment, chapterFragment);
        transaction.commit();
    }
}
