package com.bnotya.bnotyaapp;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class TriviaActivity extends ActionBarActivity{	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trivia);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		else
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);			
		}		
	}
}
