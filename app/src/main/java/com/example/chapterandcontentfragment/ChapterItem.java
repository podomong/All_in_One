package com.example.chapterandcontentfragment;

import android.graphics.Bitmap;

public class ChapterItem {
    private Bitmap imageResource;
    private String title;

    ChapterItem(){
        imageResource = null;
        title = "";
    }

    ChapterItem(String title, Bitmap imageResource){
        this.title = title;
        this.imageResource = imageResource;
    }

    public void setImageResource(Bitmap imageResource){
        this.imageResource = imageResource;
    }
    public Bitmap getImageResource(){
        return imageResource;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
}