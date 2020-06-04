package com.example.chapterandcontentfragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {
    ArrayList<CourseItem>courseItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        RecyclerView recyclerView = findViewById(R.id.courseRecyclerView);

        courseItems = new ArrayList<CourseItem>();
        String title = "Instruction";
        String instruction = "This is Cat. The domestic cat is a complex creature and unfortunately, " +
                "problems can arise for cats because sometimes we do not understand their natural drives and reactions. ";
        for(int i=0;i<5;i++)
            courseItems.add(new CourseItem(R.drawable.example_illustration,title, instruction));

        CourseAdapter adapter = new CourseAdapter(courseItems, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
