package com.example.chapterandcontentfragment;

public class ChapterItem {
    private int imageResource;
    private String title;

    ChapterItem(){
        imageResource = 0;
        title = "";
    }

    ChapterItem(String title, int imageResource){
        this.title = title;
        this.imageResource = imageResource;
    }

    public void setImageResource(int imageResource){
        this.imageResource = imageResource;
    }
    public int getImageResource(){
        return imageResource;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
}