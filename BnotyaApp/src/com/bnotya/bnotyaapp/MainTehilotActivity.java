package com.bnotya.bnotyaapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainTehilotActivity extends ActionBarActivity
{

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tehilot);
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
		// If the nav drawer is open, hide action items related to the content
		// view
		/*
		 * boolean drawerOpen = _drawerLayout.isDrawerOpen(_drawerList);
		 * menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		 * menu.findItem(R.id.action_about).setVisible(!drawerOpen);
		 */
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		/*
		 * if (_drawerToggle.onOptionsItemSelected(item)) { return true; }
		 */
		// Handle action buttons
		switch (item.getItemId())
		{		
			case android.R.id.home:
				NavUtils.navigateUpTo(this,
						new Intent(this, MainActivity.class));
				return true;
			case R.id.action_settings:
				// TODO
				return true;
			case R.id.action_about:
				// TODO
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}	
	}	
}
