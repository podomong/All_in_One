package com.example.chapterandcontentfragment;

public class CourseItem {
    private int illustrationResource;
    private String title;
    private String instruction;

    CourseItem(){
        illustrationResource = 0;
        instruction = null;
    }

    CourseItem(int illustrationResource, String title, String instruction){
        this.illustrationResource = illustrationResource;
        this.title = title;
        this.instruction = instruction;
    }

    public void setIllustrationResource(final int illustrationResource){
        this.illustrationResource = illustrationResource;
    }

    public int getIllustrationResource(){
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
