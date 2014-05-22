package com.bnotya.bnotyaapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.*;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
import com.bnotya.bnotyaapp.adapters.ExpandableListAdapter;
import com.bnotya.bnotyaapp.fragments.MainDefaultFragment;
import com.bnotya.bnotyaapp.fragments.MainTehilotFragment;
import com.bnotya.bnotyaapp.fragments.WomenListFragment;
import com.bnotya.bnotyaapp.helpers.About;
import com.bnotya.bnotyaapp.models.NavDrawerItem;
import com.bnotya.bnotyaapp.services.DataBaseService;
import java.util.*;

public class MainActivity extends ActionBarActivity implements
		OnGestureListener, OnDoubleTapListener
{
	public enum MainActivityTab
	{
		DEFAULT, WOMENLIST, TEHILOT
	}

	private ExpandableListView _drawerList;
	private List<NavDrawerItem> _listDataHeaders;
	private HashMap<String, List<NavDrawerItem>> _listDataChildren;
	private DrawerLayout _drawerLayout;
	private ActionBarDrawerToggle _drawerToggle;
	private CharSequence _drawerTitle;
	private CharSequence _title;
	/* Tabs */
	public MainActivityTab visibleTab;
	private GestureDetectorCompat _detector;
	private ActionBar _actionBar;
	private ActionBar.TabListener _tabListener;
	private Tab _defaultTab;
	private Tab _womenListTab;
	private Tab _tehilotTab;
	/* For Menu Overflow in API < 11 */ 
	private Handler handler = new Handler(Looper.getMainLooper());

	public static MediaPlayer music;

	@SuppressLint("NewApi") 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// If EXIT is True exit application
		if (getIntent().getBooleanExtra("EXIT", false))
		{
			finish();
			return;
		}

		initDrawerList();
		_title = _drawerTitle = getTitle();
		initDrawerLayout(savedInstanceState);

		// To run multiple background threads
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			new InitMusicTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);			
			new InitDatabaseTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this);
		}
		else
		{
			new InitMusicTask().execute();			
			new InitDatabaseTask().execute(this);
		}		

		/* Tabs */
		visibleTab = MainActivityTab.DEFAULT;
		InitTabs();

		if (savedInstanceState == null)
		{
			// If there is no saved instance state, add a fragment to this
			// activity.
			// If there is saved instance state,
			// this fragment will have already been added to the activity.
			replaceFragment(new MainDefaultFragment(), 0);
		}

		// Detect touched area
		_detector = new GestureDetectorCompat(this, this);
		_detector.setOnDoubleTapListener(this);
	}

	private void InitTabs()
	{
		_actionBar = getSupportActionBar();
		_actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create a tab listener that is called when the user changes tabs.
		_tabListener = new ActionBar.TabListener()
		{
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction arg1)
			{

			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction arg1)
			{
				if (_actionBar.getSelectedTab() == _defaultTab
						&& visibleTab != MainActivityTab.DEFAULT)
				{
					visibleTab = MainActivityTab.DEFAULT;
					replaceFragment(new MainDefaultFragment(), 0);
				}
				else if (_actionBar.getSelectedTab() == _womenListTab
						&& visibleTab != MainActivityTab.WOMENLIST)
				{
					visibleTab = MainActivityTab.WOMENLIST;
					replaceFragment(new WomenListFragment(), 0);
				}
				else if (_actionBar.getSelectedTab() == _tehilotTab
						&& visibleTab != MainActivityTab.TEHILOT)
				{
					visibleTab = MainActivityTab.TEHILOT;
					replaceFragment(new MainTehilotFragment(), 1);
				}
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction arg1)
			{

			}
		};

		// Add tabs, specifying the tab's text and TabListener
		_defaultTab = _actionBar.newTab()
				.setText(getResources().getStringArray(R.array.views_array)[0])
				.setTabListener(_tabListener);
		_womenListTab = _actionBar.newTab()
				.setText(getResources().getStringArray(R.array.views_array)[2])
				.setTabListener(_tabListener);
		_tehilotTab = _actionBar.newTab()
				.setText(getResources().getStringArray(R.array.views_array)[1])
				.setTabListener(_tabListener);

		_actionBar.addTab(_defaultTab);
		_actionBar.addTab(_womenListTab);
		_actionBar.addTab(_tehilotTab);
	}

	private void SelectTab()
	{
		if (_actionBar.getSelectedTab() == _defaultTab)
		{
			_actionBar.selectTab(_womenListTab);
			visibleTab = MainActivityTab.WOMENLIST;
		}
		else if (_actionBar.getSelectedTab() == _womenListTab)
		{
			_actionBar.selectTab(_tehilotTab);
			visibleTab = MainActivityTab.TEHILOT;
		}
		else if (_actionBar.getSelectedTab() == _tehilotTab)
		{
			_actionBar.selectTab(_defaultTab);
			visibleTab = MainActivityTab.DEFAULT;
		}
	}

	private void SelectBackwardsTab()
	{
		if (_actionBar.getSelectedTab() == _defaultTab)
		{
			_actionBar.selectTab(_tehilotTab);
			visibleTab = MainActivityTab.TEHILOT;
		}
		else if (_actionBar.getSelectedTab() == _womenListTab)
		{
			_actionBar.selectTab(_defaultTab);
			visibleTab = MainActivityTab.DEFAULT;
		}
		else if (_actionBar.getSelectedTab() == _tehilotTab)
		{
			_actionBar.selectTab(_womenListTab);
			visibleTab = MainActivityTab.WOMENLIST;
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

		// init headers
		initHeaders();
		initChildHeaders();

		// set list adapter
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

	private void initChildHeaders()
	{
		// Setting child headers
		List<NavDrawerItem> womenChildrenList = fillNavigationData(
				R.array.women_views_array, R.array.nav_drawer_icons);

		_listDataChildren.put(_listDataHeaders.get(2).getTitle(),
				womenChildrenList);
	}

	private void initHeaders()
	{
		// Setting group headers
		_listDataHeaders = fillNavigationData(R.array.views_array,
				R.array.nav_drawer_icons);

		// Initiating _listDataChildren
		_listDataChildren = new HashMap<String, List<NavDrawerItem>>();

		// Adding child data
		for (int i = 0; i < _listDataHeaders.size(); i++)
		{
			_listDataChildren.put(_listDataHeaders.get(i).getTitle(),
					new ArrayList<NavDrawerItem>());
		}
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
				if (_actionBar != null)
					_actionBar.selectTab(_defaultTab);
				visibleTab = MainActivityTab.DEFAULT;
				break;
			}
			case 1:
			{
				replaceFragment(new MainTehilotFragment(), position);
				if (_actionBar != null)
					_actionBar.selectTab(_tehilotTab);
				visibleTab = MainActivityTab.TEHILOT;
				break;
			}
			case 2:
			{
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
						openTriviaPage(null);
						break;
					}
					case 1:
					{
						openInsightList(null);
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

	public void openTriviaPage(View view)
	{
		// replaceFragment(new TriviaFragment(), 0);
		Intent intent = new Intent(this, TriviaActivity.class);
		startActivity(intent);
	}

	public void openInsightList(View view)
	{
		Intent intent = new Intent(this, InsightListActivity.class);
		startActivity(intent);
	}

	public void openMailPage(View view)
	{
		String[] address =
		{
			"a@a.a"
		}; // TODO: Replace with Bnotya's address
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, address);

		startActivity(Intent.createChooser(emailIntent,
				getString(R.string.chooser_title)));
	}

	public void replaceFragment(Fragment fragment, int position)
	{
		Bundle args = new Bundle();

		args.putInt(MainDefaultFragment.ARG_VIEW_NUMBER, position);
		fragment.setArguments(args);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(null).commit();

		// call onPrepareOptionsMenu
		supportInvalidateOptionsMenu();
	}

	private void initMusic()
	{
		music = MediaPlayer.create(this, R.raw.backgroundmusic);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		boolean hasMusic = prefs.getBoolean(
				getString(R.string.music_on_preference), true);
		if (hasMusic)
			music.start();
		else
			music.release();
	}	

	/* Touch Implementation */

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		this._detector.onTouchEvent(event);
		// Be sure to call the superclass implementation
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent event)
	{
		return true;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2,
			float velocityX, float velocityY)
	{
		// Check if movement is left or right
		float x1 = event1.getX();
		float x2 = event2.getX();

		if (x1 - x2 > 0)
			SelectTab();
		else
			SelectBackwardsTab();

		return true;
	}

	@Override
	public void onLongPress(MotionEvent event)
	{

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		return true;
	}

	@Override
	public void onShowPress(MotionEvent event)
	{

	}

	@Override
	public boolean onSingleTapUp(MotionEvent event)
	{
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent event)
	{
		SelectTab();
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent event)
	{
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event)
	{
		return true;
	}

	/* End of Touch Implementation */
	
	/* Tasks */
	
	private class InitDatabaseTask extends AsyncTask<Context, Void, Void>
	{
		protected Void doInBackground(Context... contexts)
		{
			DataBaseService.initDatabaseHelper(contexts[0]);

			return null;
		}
	}
	
	private class InitMusicTask extends AsyncTask<Void, Void, Void>
	{
		protected Void doInBackground(Void...voids)
		{
			initMusic();

			return null;
		}
	}
}
