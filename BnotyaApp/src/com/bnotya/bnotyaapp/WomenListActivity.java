package com.bnotya.bnotyaapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.bnotya.bnotyaapp.adapters.WomenArrayAdapter;
import com.bnotya.bnotyaapp.helpers.About;
import com.bnotya.bnotyaapp.models.NavDrawerItem;
import com.bnotya.bnotyaapp.models.WomenListItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

@SuppressLint("NewApi")
public class WomenListActivity extends ActionBarActivity
{
	private ListView _listView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		initView();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_menu, menu);
		
		// TODO: fix this
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
		{
			// Associate searchable configuration with the SearchView
	        SearchManager searchManager =
	                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	        SearchView searchView =
	                (SearchView) menu.findItem(R.id.action_open_search).getActionView();
	        searchView.setSearchableInfo(
	                searchManager.getSearchableInfo(getComponentName()));
	        searchView.setIconifiedByDefault(false);
	    }*/

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle presses on the action bar items
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
			case R.id.action_open_search:
				onSearchRequested();
				return true;
			case R.id.action_exit:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}	

	private void initView()
	{
		setContentView(R.layout.women_list_view);
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

		// Get ListView object from xml
		_listView = (ListView) findViewById(R.id.womenlist);		
		
		WomenListItem[] listDataHeaders = fillData(R.array.women_names_array,
				R.array.women_list_icons);

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		WomenArrayAdapter adapter = new WomenArrayAdapter(this,
				R.layout.women_list_item, listDataHeaders);		

		// Assign adapter to ListView
		_listView.setAdapter(adapter);

		// ListView Item Click Listener
		_listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent(getBaseContext(),
						CardFlipActivity.class);
				intent.putExtra("EXTRA_SESSION_ID", position);				
				startActivity(intent);
			}
		});
	}
	
	private WomenListItem[] fillData(int titlesID, int iconsID)
	{		
		// Load item values
		String[] titles = getResources().getStringArray(titlesID);
		TypedArray icons = getResources().obtainTypedArray(iconsID);
		
		WomenListItem[] result = new WomenListItem[titles.length];

		// Adding items to array

		for (int i = 0; i < titles.length; i++)
		{
			result[i] = new WomenListItem(titles[i], icons
					.getResourceId(i, -1));
		}

		// Recycle the typed array
		icons.recycle();

		return result;
	}
}
