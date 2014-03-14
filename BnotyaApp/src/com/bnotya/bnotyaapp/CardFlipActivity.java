package com.bnotya.bnotyaapp;

import com.bnotya.bnotyaapp.fragments.CardFragment;
import com.bnotya.bnotyaapp.helpers.About;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class CardFlipActivity extends FragmentActivity implements
		OnGestureListener, OnDoubleTapListener
{
	/* Whether or not we're showing the back of the card. */
	public boolean showingBack = false;
	private GestureDetectorCompat detector;
	public int frontId;
	public int backId;

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
					.add(R.id.container, new CardFragment()).commit();
		}
		else
		{
			showingBack = (getSupportFragmentManager().getBackStackEntryCount() > 0);
		}

		// Detect touched area
		detector = new GestureDetectorCompat(this, this);
		detector.setOnDoubleTapListener(this);

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

	private void setCard()
	{
		Intent intent = getIntent();
		int cardId = 1;

		boolean isRandom = intent.getBooleanExtra("EXTRA_SESSION_ISRANDOM",
				false);
		if (isRandom)
		{
			int numberOfCards = getResources().getInteger(
					R.integer.number_of_cards) - 1;
			// Random from 1 to number of cards
			cardId += (Math.random() * numberOfCards);

			// Set extras to prevent card changes on configuration change
			intent.putExtra("EXTRA_SESSION_ID", (cardId - 1));
			intent.putExtra("EXTRA_SESSION_ISRANDOM", false);
		}
		else
		{
			cardId += intent.getIntExtra("EXTRA_SESSION_ID", 0);
		}

		frontId = getResources().getIdentifier(String.format("card%s", cardId),
				"drawable", getPackageName());
		backId = getResources().getIdentifier(
				String.format("card%s_%s", cardId, cardId), "drawable",
				getPackageName());
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
		this.detector.onTouchEvent(event);
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
		flipCard();
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
		flipCard();
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
		flipCard();
	}	
}
