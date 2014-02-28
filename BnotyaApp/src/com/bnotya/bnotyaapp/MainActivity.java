package com.bnotya.bnotyaapp;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity
{
	private DrawerLayout _drawerLayout;
	private ListView _drawerList;
	private ActionBarDrawerToggle _drawerToggle;
	private CharSequence _drawerTitle;
	private CharSequence _title;
	private String[] _navigationTitles;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_title = _drawerTitle = getTitle();
		_navigationTitles = getResources().getStringArray(R.array.views_array);
		_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		_drawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		_drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		_drawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, _navigationTitles));
		_drawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
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

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		_drawerToggle = initActionBarDrawerToggle();
		
		_drawerLayout.setDrawerListener(_drawerToggle);

		if (savedInstanceState == null)
		{
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = _drawerLayout.isDrawerOpen(_drawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		menu.findItem(R.id.action_about).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (_drawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		// Handle action buttons
		switch (item.getItemId())
		{
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

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			selectItem(position);
		}
	}
	
	@SuppressLint("NewApi")
	private ActionBarDrawerToggle initActionBarDrawerToggle()
	{
		return new ActionBarDrawerToggle(this, /* host Activity */
				_drawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open, /* "open drawer" description for accessibility */
				R.string.drawer_close /* "close drawer" description for accessibility */
				)
				{					
					public void onDrawerClosed(View view)
					{
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
						{
							getActionBar().setTitle(_title);
							invalidateOptionsMenu(); // creates call to
														// onPrepareOptionsMenu()
						}
						else
						{
							getSupportActionBar().setTitle(_title);
							supportInvalidateOptionsMenu();
						}
					}

					public void onDrawerOpened(View drawerView)
					{
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
						{
							getActionBar().setTitle(_drawerTitle);
							invalidateOptionsMenu(); // creates call to
														// onPrepareOptionsMenu()
						}
						else
						{
							getSupportActionBar().setTitle(_drawerTitle);
							supportInvalidateOptionsMenu();
						}
					}
				};
	}

	private void selectItem(int position)
	{	
		Bundle args = new Bundle();		
		FragmentManager fragmentManager = getSupportFragmentManager();
		
		switch(position)
		{
			case 0:
			{
				MainTehilotFragment fragment = new MainTehilotFragment();
				args.putInt(MainTehilotFragment.ARG_VIEW_NUMBER, position);
				fragment.setArguments(args);
				
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
				break;
			}				
			case 1:
			{
				MainWomenFragment fragment = new MainWomenFragment();
				args.putInt(MainWomenFragment.ARG_VIEW_NUMBER, position);
				fragment.setArguments(args);
				
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
				
				break;
			}			
			case 2:
			{
				startActivity(new Intent(this, MainTehilotActivity.class));
			}
			case 3:
			{
				startActivity(new Intent(this, MainWomenActivity.class));
			}
		}

		// update selected item and title, then close the drawer
		_drawerList.setItemChecked(position, true);
		setTitle(_navigationTitles[position]);
		_drawerLayout.closeDrawer(_drawerList);
	}

	@SuppressLint("NewApi")
	@Override
	public void setTitle(CharSequence title)
	{
		_title = title;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			getActionBar().setTitle(_title);
		}
		else
		{
			getSupportActionBar().setTitle(_title);
		}
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		_drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		_drawerToggle.onConfigurationChanged(newConfig);
	}	

	/**
	 * Fragment that appears in the "content_frame"
	 */
	public static class MainTehilotFragment extends Fragment
	{
		public static final String ARG_VIEW_NUMBER = "view_number";

		public MainTehilotFragment()
		{
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.activity_main_tehilot,
					container, false);	
			
			int i = getArguments().getInt(ARG_VIEW_NUMBER);
			String view = getResources().getStringArray(R.array.views_array)[i];
			
			/*int imageId = getResources().getIdentifier(
					view.toLowerCase(Locale.getDefault()), "drawable",
					getActivity().getPackageName());
			((ImageView) rootView.findViewById(R.id.image))
					.setImageResource(imageId);*/
			
			getActivity().setTitle(view);
			return rootView;
		}
	}
	
	/**
	 * Fragment that appears in the "content_frame"
	 */
	public static class MainWomenFragment extends Fragment
	{
		public static final String ARG_VIEW_NUMBER = "view_number";

		public MainWomenFragment()
		{
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.activity_main_women,
					container, false);			
			
			int i = getArguments().getInt(ARG_VIEW_NUMBER);
			String view = getResources().getStringArray(R.array.views_array)[i];
			
			getActivity().setTitle(view);
			return rootView;
		}	
		
	}
	
	public void openRandomCard(View view) 
    {    		
    	startActivity(new Intent(this,CardFlipActivity.class));
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
