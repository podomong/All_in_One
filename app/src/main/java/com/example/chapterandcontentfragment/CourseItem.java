package com.example.chapterandcontentfragment;

import android.graphics.Bitmap;

public class CourseItem {
    private Bitmap illustrationResource;
    private String title;
    private String instruction;

    CourseItem(){
        illustrationResource = null;
        instruction = null;
    }

    CourseItem(Bitmap illustrationResource, String title, String instruction){
        this.illustrationResource = illustrationResource;
        this.title = title;
        this.instruction = instruction;
    }

    public void setIllustrationResource(final Bitmap illustrationResource){
        this.illustrationResource = illustrationResource;
    }

    public Bitmap getIllustrationResource(){
        return illustrationResource;
    }

    public void setInstruction(String instruction){
        this.instruction = instruction;
    }

    public String getInstruction(){
        return instruction;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
