package com.example.chapterandcontentfragment;

public class ContentItem {
    private int number;
    private String instruction;

    ContentItem(){
        number = 0;
        instruction = "";
    }
    ContentItem(int number, String instruction){
        this.number = number;
        this.instruction= instruction;
    }
    public void setNumber(int number){
        this.number = number;
    }
    public int getNumber(){
        return number;
    }
    public void setInstruction(String instruction){
        this.instruction = instruction;
    }

    public String getInstruction(){
        return instruction;
    }
}