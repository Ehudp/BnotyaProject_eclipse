package com.bnotya.bnotyaapp;

import com.bnotya.bnotyaapp.helpers.About;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainWomenActivity extends ActionBarActivity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_women);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
		}
		else
		{			
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.page_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// If the nav drawer is open, hide action items related to the content view
		/*boolean drawerOpen = _drawerLayout.isDrawerOpen(_drawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		menu.findItem(R.id.action_about).setVisible(!drawerOpen);*/
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		/*if (_drawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}*/
		// Handle action buttons
		switch (item.getItemId())
		{		
			case android.R.id.home:
				NavUtils.navigateUpTo(this,
						new Intent(this, MainActivity.class));
				return true;
			case R.id.action_home:
				NavUtils.navigateUpTo(this,
						new Intent(this, MainActivity.class));				
	            return true;		
			case R.id.action_settings:
				startActivity(new Intent(this, Preferences.class));
				return true;
			case R.id.action_about:
				About.showAboutDialog(this);
				return true;
			case R.id.action_exit:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}	
	}	
	
	public void openRandomCard(View view) 
    {  
		Intent intent = new Intent(this, CardFlipActivity.class);		
		intent.putExtra("EXTRA_SESSION_ISRANDOM", true);
		startActivity(intent);    	
    } 
    
    public void openWomenList(View view) 
    {    		
    	startActivity(new Intent(this, WomenListActivity.class));
    } 
    
    public void openTriviaPage(View view) 
    {    		
    	startActivity(new Intent(this, TriviaActivity.class));
    } 
}
