package com.example.chapterandcontentfragment;

public class ContentItem {
    private int number;
    private String instruction;
    int detailId;

    ContentItem(){
        number = 0;
        instruction = "";
    }
    ContentItem(int number, String instruction, int detailId){
        this.number = number;
        this.instruction= instruction;
        this.detailId = detailId;
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

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId){
        this.detailId = detailId;
    }
}