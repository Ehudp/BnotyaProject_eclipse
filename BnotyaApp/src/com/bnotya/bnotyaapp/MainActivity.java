package com.bnotya.bnotyaapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.bnotya.bnotyaapp.adapters.ExpandableListAdapter;
import com.bnotya.bnotyaapp.fragments.MainDefaultFragment;
import com.bnotya.bnotyaapp.fragments.MainTehilotFragment;
import com.bnotya.bnotyaapp.fragments.MainWomenFragment;
import com.bnotya.bnotyaapp.helpers.About;
import com.bnotya.bnotyaapp.models.NavDrawerItem;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity
{
	private ExpandableListView _drawerList;
	private List<NavDrawerItem> _listDataHeaders;
	private HashMap<String, List<NavDrawerItem>> _listDataChildren;
	private DrawerLayout _drawerLayout;
	private ActionBarDrawerToggle _drawerToggle;
	private CharSequence _drawerTitle;
	private CharSequence _title;
	public static MediaPlayer music;
	private SharedPreferences _prefs;
	// For Menu Overflow in API < 11
	private Handler handler = new Handler(Looper.getMainLooper());

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initDrawerList();
		_title = _drawerTitle = getTitle();
		initDrawerLayout(savedInstanceState);

		initMusic();
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
		// For Menu Overflow in API < 11
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			menu.removeItem(R.id.action_overflow);
		}

		// If the nav drawer is open, hide action items
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

		switch (item.getItemId())
		{
			// For Menu Overflow in API < 11
			case R.id.action_overflow:
				openOptionsMenuDeferred();
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

	// For Menu Overflow in API < 11
	public void openOptionsMenuDeferred()
	{
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				openOptionsMenu();
			}
		});
	}

	private void initDrawerList()
	{
		// get the listview
		_drawerList = (ExpandableListView) findViewById(R.id.left_drawer);

		// preparing list data
		prepareListData();

		// setting list adapter
		_drawerList.setAdapter(new ExpandableListAdapter(this,
				_listDataHeaders, _listDataChildren));

		// Listview Group click listener
		_drawerList.setOnGroupClickListener(new DrawerGroupClickListener());

		// Listview on child click listener
		_drawerList.setOnChildClickListener(new DrawerChildClickListener());

		// Listview Group expanded listener
		_drawerList.setOnGroupExpandListener(new OnGroupExpandListener()
		{
			@Override
			public void onGroupExpand(int groupPosition)
			{

			}
		});

		// Listview Group collasped listener
		_drawerList.setOnGroupCollapseListener(new OnGroupCollapseListener()
		{
			@Override
			public void onGroupCollapse(int groupPosition)
			{

			}
		});
	}

	@SuppressLint("NewApi")
	private void initDrawerLayout(Bundle savedInstanceState)
	{
		_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		_drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

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
			selectGroup(0);
		}
	}

	/* The click listener for ExpandableListView in the navigation drawer */
	private class DrawerChildClickListener implements
			ExpandableListView.OnChildClickListener
	{
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id)
		{
			String key = _listDataHeaders.get(groupPosition).getTitle();

			Toast.makeText(
					getApplicationContext(),
					key
							+ " : "
							+ _listDataChildren.get(key).get(childPosition)
									.getTitle(), Toast.LENGTH_SHORT).show();
			selectChild(groupPosition, childPosition);
			return false;
		}
	}

	/* The click listener for ExpandableListView in the navigation drawer */
	private class DrawerGroupClickListener implements
			ExpandableListView.OnGroupClickListener
	{
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id)
		{
			String key = _listDataHeaders.get(groupPosition).getTitle();
			List<NavDrawerItem> children = _listDataChildren.get(key);
			if (children.size() == 0)
			{
				selectGroup(groupPosition);
			}
			return false;
		}
	}

	private ActionBarDrawerToggle initActionBarDrawerToggle()
	{
		return new ActionBarDrawerToggle(this, /* host Activity */
		_drawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		)
		{
			@SuppressLint("NewApi")
			public void onDrawerClosed(View view)
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				{
					getActionBar().setTitle(_title);
					// creates call to onPrepareOptionsMenu()
					invalidateOptionsMenu();
				}
				else
				{
					getSupportActionBar().setTitle(_title);
					supportInvalidateOptionsMenu();
				}
			}

			@SuppressLint("NewApi")
			public void onDrawerOpened(View drawerView)
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				{
					getActionBar().setTitle(_drawerTitle);
					// creates call to onPrepareOptionsMenu()
					invalidateOptionsMenu();
				}
				else
				{
					getSupportActionBar().setTitle(_drawerTitle);
					supportInvalidateOptionsMenu();
				}
			}
		};
	}

	private void prepareListData()
	{
		// Setting group headers
		_listDataHeaders = fillNavigationData(R.array.views_array,
				R.array.nav_drawer_icons);

		// Initiating _listDataChildren
		_listDataChildren = new HashMap<String, List<NavDrawerItem>>();

		// Adding child data

		// Setting child headers
		List<NavDrawerItem> womenChildrenList = fillNavigationData(
				R.array.women_views_array, R.array.nav_drawer_icons);

		_listDataChildren.put(_listDataHeaders.get(0).getTitle(),
				new ArrayList<NavDrawerItem>());
		_listDataChildren.put(_listDataHeaders.get(1).getTitle(),
				new ArrayList<NavDrawerItem>());
		_listDataChildren.put(_listDataHeaders.get(2).getTitle(),
				womenChildrenList);
	}

	private List<NavDrawerItem> fillNavigationData(int titlesID, int iconsID)
	{
		List<NavDrawerItem> result = new ArrayList<NavDrawerItem>();
		// Load nav item values
		String[] navMenuTitles = getResources().getStringArray(titlesID);
		TypedArray navMenuIcons = getResources().obtainTypedArray(iconsID);

		// Adding nav drawer items to array

		for (int i = 0; i < navMenuTitles.length; i++)
		{
			result.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
					.getResourceId(i, -1)));
		}

		// Recycle the typed array
		navMenuIcons.recycle();

		return result;
	}

	private void selectGroup(int position)
	{
		switch (position)
		{
			case 0:
			{
				replaceFragment(new MainDefaultFragment(), position);
				break;
			}
			case 1:
			{
				replaceFragment(new MainTehilotFragment(), position);
				break;
			}
			case 2:
			{
				replaceFragment(new MainWomenFragment(), position);
				break;
			}
		}

		// update selected item and title, then close the drawer
		_drawerList.setItemChecked(position, true);
		setTitle(_listDataHeaders.get(position).getTitle());
		_drawerLayout.closeDrawer(_drawerList);
	}

	private void selectChild(int groupPosition, int childPosition)
	{
		String key = _listDataHeaders.get(groupPosition).getTitle();

		switch (groupPosition)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				break;
			}
			case 2:
			{
				switch (childPosition)
				{
					case 0:
					{
						openRandomCard(null);
						break;
					}
					case 1:
					{
						openWomenList(null);
						break;
					}
					case 2:
					{
						openTriviaPage(null);
						break;
					}
				}

				break;
			}
		}

		// update selected item and title, then close the drawer
		_drawerList.setItemChecked(groupPosition, true);
		setTitle(_listDataChildren.get(key).get(childPosition).getTitle());
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

	public void openTehilotPage(View view)
	{
		replaceFragment(new MainTehilotFragment(), 1);
	}

	public void openWomenPage(View view)
	{
		replaceFragment(new MainWomenFragment(), 2);
	}

	public void replaceFragment(Fragment fragment, int position)
	{
		Bundle args = new Bundle();
		FragmentManager fragmentManager = getSupportFragmentManager();
		args.putInt(MainDefaultFragment.ARG_VIEW_NUMBER, position);
		fragment.setArguments(args);

		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
	}

	private void initMusic()
	{
		music = MediaPlayer.create(this, R.raw.backgroundmusic);
		_prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		boolean hasMusic = _prefs.getBoolean(
				getString(R.string.music_preference), true);
		if (hasMusic) music.start();
	}	
}
