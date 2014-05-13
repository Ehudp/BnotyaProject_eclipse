package com.bnotya.bnotyaapp;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.bnotya.bnotyaapp.adapters.CustomArrayAdapter;
import com.bnotya.bnotyaapp.helpers.About;
import com.bnotya.bnotyaapp.models.Insight;
import com.bnotya.bnotyaapp.models.ListItem;
import com.bnotya.bnotyaapp.services.DataBaseService;

public class InsightListActivity extends ActionBarActivity
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
				exitApplication();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void exitApplication()
	{
		Intent intent = new Intent(this, MainActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent.putExtra("EXIT", true);
	    startActivity(intent);
	    finish();
	}

	@SuppressLint("NewApi") 
	private void initView()
	{
		setContentView(R.layout.insight_list_view);
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
		_listView = (ListView) findViewById(R.id.insightlist);
		
		final List<Insight> insights = DataBaseService.dbHelper.getFavoriteInsights(this);
		ListItem[] listDataHeaders = fillData(R.array.women_list_icons, insights);

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		CustomArrayAdapter adapter = new CustomArrayAdapter(this,
				R.layout.insight_list_item, listDataHeaders);		

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
						InsightActivity.class);
				intent.putExtra("EXTRA_SESSION_ID", insights.get(position).getId() - 1);
				intent.putExtra("EXTRA_IS_FROM_LIST", true);
				startActivity(intent);
			}
		});
	}
	
	private ListItem[] fillData(int iconsID, List<Insight> insights)
	{	
		// set insight names as titles						
		int size = insights.size();
		// Load item values		
		TypedArray icons = getResources().obtainTypedArray(iconsID);		
		ListItem[] result = new ListItem[size];

		// Adding items to array
		for (int i = 0; i < size; i++)
		{
			result[i] = new ListItem(insights.get(i).getName(), 
					icons.getResourceId(i, -1));
		}

		// Recycle the typed array
		icons.recycle();

		return result;
	}
}

