package com.bnotya.bnotyaapp.models;

public class Card
{
	private final String _backValue;
	private final String _frontValue;
    private final int _id;
    
    public Card(String backValue, String frontValue, int id)
    {
    	_backValue = backValue;
    	_frontValue = frontValue;
    	_id = id;
    }
    
    public String getBackValue()
    {
    	return _backValue;
    }
    
    public String getFrontValue()
    {
    	return _frontValue;
    }
    
    public int getID()
    {
    	return _id;
    }
}
