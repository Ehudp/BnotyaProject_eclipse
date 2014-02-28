package com.bnotya.bnotyaapp;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class CardFlipActivity extends FragmentActivity implements
		FragmentManager.OnBackStackChangedListener
{
	/**
	 * A handler object, used for deferring UI operations.
	 */
	private Handler _handler = new Handler();

	/**
	 * Whether or not we're showing the back of the card (otherwise showing the
	 * front).
	 */
	private boolean _showingBack = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_flip);

		if (savedInstanceState == null)
		{
			// If there is no saved instance state, add a fragment representing
			// the front of the card to this activity. 
			// If there is saved instance state,
			// this fragment will have already been added to the activity.
			getSupportFragmentManager().beginTransaction().add(R.id.container, new CardFrontFragment()).commit();
		}
		else
		{
			_showingBack = (getFragmentManager().getBackStackEntryCount() > 0);
		}

		// Monitor back stack changes to ensure the action bar shows the
		// appropriate button (either "photo" or "info").
		getSupportFragmentManager().addOnBackStackChangedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		// Add either a "photo" or "finish" button to the action bar, depending
		// on which page is currently selected.
		MenuItem item = menu.add(Menu.NONE, R.id.action_flip, Menu.NONE,
				_showingBack ? R.string.action_card_front
						: R.string.action_card_back);
		item.setIcon(_showingBack ? R.drawable.ic_action_search
				: R.drawable.ic_launcher);	
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			// TODO
			case android.R.id.home:				
				NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
				return true;

			case R.id.action_flip:
				flipCard();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void flipCard()
	{
		if (_showingBack)
		{			
			getSupportFragmentManager().popBackStack();
			return;
		}

		// Flip to the back.

		_showingBack = true;

		// Create and commit a new fragment transaction that adds the fragment
		// for the back of the card, uses custom animations, and is part of the fragment
		// manager's back stack.		
		getSupportFragmentManager().beginTransaction()

		// Replace the default fragment animations with animator resources
		// representing rotations when switching to the back of the card, 
		// as well as animator resources representing rotations when flipping back to the front
		// (e.g. when the system Back button is pressed).
				.setCustomAnimations(R.anim.card_flip_right_in,
						R.anim.card_flip_right_out,
						R.anim.card_flip_left_in,
						R.anim.card_flip_left_out)

				// Replace any fragments currently in the container view with a
				// fragment
				// representing the next page (indicated by the just-incremented
				// currentPage
				// variable).
				.replace(R.id.container, new CardBackFragment())

				// Add this transaction to the back stack, allowing users to
				// press Back
				// to get to the front of the card.
				.addToBackStack(null)

				// Commit the transaction.
				.commit();

		// Defer an invalidation of the options menu (on modern devices, the
		// action bar). This
		// can't be done immediately because the transaction may not yet be
		// committed. Commits
		// are asynchronous in that they are posted to the main thread's message
		// loop.
		_handler.post(new Runnable()
		{
			@SuppressLint("NewApi")
			@Override
			public void run()
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
				{
					invalidateOptionsMenu();
				}
				else
				{
					supportInvalidateOptionsMenu();
				}
			}
		});
	}

	@SuppressLint("NewApi")
	@Override
	public void onBackStackChanged()
	{
		_showingBack = (getSupportFragmentManager().getBackStackEntryCount() > 0);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
		{
			invalidateOptionsMenu();
		}
		else
		{
			supportInvalidateOptionsMenu();
		}
	}

	/**
	 * A fragment representing the front of the card.
	 */
	public static class CardFrontFragment extends Fragment
	{
		public CardFrontFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			return inflater.inflate(R.layout.fragment_card_front, container,
					false);
		}
	}

	/**
	 * A fragment representing the back of the card.
	 */
	public static class CardBackFragment extends Fragment
	{
		public CardBackFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			return inflater.inflate(R.layout.fragment_card_back, container,
					false);
		}
	}
}
