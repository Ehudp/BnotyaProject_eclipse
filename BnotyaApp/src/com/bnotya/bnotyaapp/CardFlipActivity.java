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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bnotya.bnotyaapp.helpers.SimpleGestureFilter;
import com.bnotya.bnotyaapp.helpers.SimpleGestureFilter.SimpleGestureListener;

public class CardFlipActivity extends FragmentActivity implements
		FragmentManager.OnBackStackChangedListener, SimpleGestureListener
{
	/* A handler object, used for deferring UI operations. */
	private Handler _handler = new Handler();
	/* Whether or not we're showing the back of the card. */	
	private boolean _showingBack = false;
	private SimpleGestureFilter detector;
	private int frontId;
	private int backId;

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
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new CardFrontFragment()).commit();
		}
		else
		{
			_showingBack = (getFragmentManager().getBackStackEntryCount() > 0);
		}

		// Monitor back stack changes to ensure the action bar shows the
		// appropriate button (either "photo" or "info").
		getSupportFragmentManager().addOnBackStackChangedListener(this);

		// Detect touched area
		detector = new SimpleGestureFilter(this, this);

		setCard();
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

	private void setCard()
	{
		Intent intent = getIntent();
		int cardId = 1;

		boolean isRandom = intent.getBooleanExtra("EXTRA_SESSION_ISRANDOM", false);
		if(isRandom)
		{
			int numberOfCards = getResources().getInteger(R.integer.number_of_cards) - 1;
			// Random from 1 to number of cards
			cardId += (Math.random() * numberOfCards); 
		}
		else
		{
			cardId += intent.getIntExtra("EXTRA_SESSION_ID", 0);
		}		
		
		frontId = getResources().getIdentifier(String.format("card%s", cardId) , "drawable", getPackageName());
		backId = getResources().getIdentifier(String.format("card%s_%s", cardId, cardId) , "drawable", getPackageName());	
	}

	public void flipCard(View view)
	{
		flipCard();
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
		// for the back of the card, uses custom animations, and is part of the
		// fragment
		// manager's back stack.
		getSupportFragmentManager().beginTransaction()

		// Replace the default fragment animations with animator resources
		// representing rotations when switching to the back of the card,
		// as well as animator resources representing rotations when flipping
		// back to the front
		// (e.g. when the system Back button is pressed).
				.setCustomAnimations(R.anim.card_flip_right_in,
						R.anim.card_flip_right_out, R.anim.card_flip_left_in,
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
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent me)
	{
		// Call onTouchEvent of SimpleGestureFilter class
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction)
	{
		flipCard();
		/*
		 * String str = "";
		 * 
		 * switch (direction) { case SimpleGestureFilter.SWIPE_RIGHT: str =
		 * "Swipe Right"; break; case SimpleGestureFilter.SWIPE_LEFT: str =
		 * "Swipe Left"; break; case SimpleGestureFilter.SWIPE_DOWN: str =
		 * "Swipe Down"; break; case SimpleGestureFilter.SWIPE_UP: str =
		 * "Swipe Up"; break; } Toast.makeText(this, str,
		 * Toast.LENGTH_SHORT).show();
		 */
	}

	@Override
	public void onDoubleTap()
	{
		flipCard();
	}

	public static class CardFrontFragment extends Fragment
	{
		public CardFrontFragment()
		{
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View view = inflater.inflate(R.layout.fragment_card_front,
					container, false);
			ImageView cardView = (ImageView) view.findViewById(R.id.card_front);
			cardView.setImageResource(((CardFlipActivity)getActivity()).frontId);
			return view;
		}
	}

	public static class CardBackFragment extends Fragment
	{
		public CardBackFragment()
		{
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View view = inflater.inflate(R.layout.fragment_card_back,
					container, false);
			ImageView cardView = (ImageView) view.findViewById(R.id.card_back);
			cardView.setImageResource(((CardFlipActivity)getActivity()).backId);
			return view;
		}
	}	

}
