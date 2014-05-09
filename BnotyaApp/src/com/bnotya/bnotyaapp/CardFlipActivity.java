package com.bnotya.bnotyaapp;

import com.bnotya.bnotyaapp.fragments.CardFragment;
import com.bnotya.bnotyaapp.helpers.About;
import com.bnotya.bnotyaapp.models.Card;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class CardFlipActivity extends ActionBarActivity implements
		OnGestureListener, OnDoubleTapListener
{
	/* Whether or not we're showing the back of the card. */
	public boolean showingBack = false;
	private GestureDetectorCompat _detector;
	public Card card;
	private ActionBar _actionBar;
	private ActionBar.TabListener _tabListener;
	private Tab _cardFrontTab;
	private Tab _cardBackTab;	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_flip);

		InitTabs();

		if (savedInstanceState == null)
		{
			// If there is no saved instance state, add a fragment representing
			// the front of the card to this activity.
			// If there is saved instance state,
			// this fragment will have already been added to the activity.
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new CardFragment()).commit();
		}

		// Detect touched area
		_detector = new GestureDetectorCompat(this, this);
		_detector.setOnDoubleTapListener(this);		
		
		// Setup card
		card = new Card(getIntent(), getResources(), getPackageName());	
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
				if (_actionBar.getSelectedTab() == _cardFrontTab && showingBack
						|| _actionBar.getSelectedTab() == _cardBackTab
						&& !showingBack)
				{
					flipCard();
				}
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction arg1)
			{

			}
		};

		// Add 2 tabs, specifying the tab's text and TabListener
		_cardFrontTab = _actionBar.newTab().setText(R.string.card_front)
				.setTabListener(_tabListener);
		_cardBackTab = _actionBar.newTab().setText(R.string.card_back)
				.setTabListener(_tabListener);

		_actionBar.addTab(_cardFrontTab);
		_actionBar.addTab(_cardBackTab);
	}

	private void SelectTab()
	{
		if (_actionBar.getSelectedTab() == _cardFrontTab)
			_actionBar.selectTab(_cardBackTab);
		else
			_actionBar.selectTab(_cardFrontTab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.page_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
				return true;
			case R.id.action_home:
				NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
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

	private void flipCard()
	{
		showingBack = !showingBack;

		// Create and commit a new fragment transaction that adds the fragment
		// for the card and uses custom animations.
		getSupportFragmentManager().beginTransaction()

		// Replace the default fragment animations with animator resources
		// representing rotations when switching to the back of the card,
		// as well as animator resources representing rotations when flipping
		// back to the front.
				.setCustomAnimations(R.anim.card_flip_right_in,
						R.anim.card_flip_right_out, R.anim.card_flip_left_in,
						R.anim.card_flip_left_out)

				// Replace any fragments currently in the container view with a
				// fragment representing the next page
				// (indicated by the just-incremented
				// currentPage variable).
				.replace(R.id.container, new CardFragment())

				// Commit the transaction.
				.commit();
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
		/*
		 * Toast.makeText(getApplicationContext(), "onDown: " +
		 * event.toString(), Toast.LENGTH_SHORT).show();
		 */
		return true;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2,
			float velocityX, float velocityY)
	{
		/*
		 * Toast.makeText(getApplicationContext(), "onFling: " +
		 * event1.toString()+event2.toString(), Toast.LENGTH_SHORT).show();
		 */
		SelectTab();
		return true;
	}

	@Override
	public void onLongPress(MotionEvent event)
	{
		/*
		 * Toast.makeText(getApplicationContext(), "onLongPress: " +
		 * event.toString(), Toast.LENGTH_SHORT).show();
		 */
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		/*
		 * Toast.makeText(getApplicationContext(), "onScroll: " +
		 * e1.toString()+e2.toString(), Toast.LENGTH_SHORT).show();
		 */
		// SelectTab();
		return true;
	}

	@Override
	public void onShowPress(MotionEvent event)
	{
		/*
		 * Toast.makeText(getApplicationContext(), "onShowPress: " +
		 * event.toString(), Toast.LENGTH_SHORT).show();
		 */
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event)
	{
		/*
		 * Toast.makeText(getApplicationContext(), "onSingleTapUp: " +
		 * event.toString(), Toast.LENGTH_SHORT).show();
		 */
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent event)
	{
		/*
		 * Toast.makeText(getApplicationContext(), "onDoubleTap: " +
		 * event.toString(), Toast.LENGTH_SHORT).show();
		 */
		SelectTab();
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent event)
	{
		/*
		 * Toast.makeText(getApplicationContext(), "onDoubleTapEvent: " +
		 * event.toString(), Toast.LENGTH_SHORT).show();
		 */
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event)
	{
		/*
		 * Toast.makeText(getApplicationContext(), "onSingleTapConfirmed: " +
		 * event.toString(), Toast.LENGTH_SHORT).show();
		 */
		return true;
	}

	/* End of Touch Implementation */

	public void flipCard(View view)
	{
		SelectTab();
	}
}
