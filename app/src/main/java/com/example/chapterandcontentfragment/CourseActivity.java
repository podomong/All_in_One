package com.example.chapterandcontentfragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {
    ArrayList<CourseItem>courseItems= new ArrayList<CourseItem>();

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        createDB(this);

        AllinOneDBHelper dbHelper = new AllinOneDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+AllinOneContract.Course.TABLE_NAME, null);
        while(cursor.moveToNext()){
            String title =
                    cursor.getString(cursor.getColumnIndex(AllinOneContract.Course.TITLE));
            String instruction =
                    cursor.getString(cursor.getColumnIndex(AllinOneContract.Course.INSTRUCTION));
            byte[] rawIllustration =
                    cursor.getBlob(cursor.getColumnIndex(AllinOneContract.Course.ILLUSTRATION));
            Bitmap bmpIllustration = BitmapFactory.decodeByteArray(rawIllustration, 0, rawIllustration.length);
            courseItems.add(new CourseItem(bmpIllustration, title, instruction));
        }
        cursor.close();
        db.close();
        dbHelper.close();

        //RecyclerView recyclerView = findViewById(R.id.courseRecyclerView);
        //CourseAdapter adapter = new CourseAdapter(courseItems, this);
        //recyclerView.setAdapter(adapter);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //recyclerView.setLayoutManager(linearLayoutManager);

        //Handler handler = new Handler();
        //handler.postDelayed(new SplashHandler(), 1500);
        ImageView titleImage = findViewById(R.id.titleImage);
        titleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this, ChapterAndContentActivity.class);
                intent.putExtra(AllinOneContract.Course.COURSE_ID, 1);
                startActivity(intent);
                CourseActivity.this.finish();
            }
        });
    }

    private class SplashHandler implements Runnable{
        public void run(){
            Intent intent = new Intent(CourseActivity.this, ChapterAndContentActivity.class);
            intent.putExtra(AllinOneContract.Course.COURSE_ID, 1);
            startActivity(intent);
            CourseActivity.this.finish();
        }
    }

    private void createDB(Context context){
        SharedPreferences prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun",true);

        if(isFirstRun){
            System.out.println("First Run and Database created");
            String appDataPath = context.getApplicationInfo().dataDir;
            File dbFolder = new File(appDataPath + "/databases");
            dbFolder.mkdir();
            File dbFilePath = new File(appDataPath + "/databases/"+AllinOneDBHelper.DATABASE_NAME);

            try {
                InputStream inputStream = context.getAssets().open(AllinOneDBHelper.DATABASE_NAME);
                OutputStream outputStream = new FileOutputStream(dbFilePath);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer))>0)
                    outputStream.write(buffer, 0, length);

                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException e){ }
            prefs.edit().putBoolean("isFirstRun",false).apply();
        }

    }
}
