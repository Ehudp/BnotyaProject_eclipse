package com.bnotya.bnotyaapp.models;

public class WomenListItem
{
	private String title;
    private int icon;    
     
    public WomenListItem(){}
 
    public WomenListItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }
     
    public String getTitle(){
        return this.title;
    }
     
    public int getIcon(){
        return this.icon;
    }
     
    public void setTitle(String title){
        this.title = title;
    }
     
    public void setIcon(int icon){
        this.icon = icon;
    } 
}