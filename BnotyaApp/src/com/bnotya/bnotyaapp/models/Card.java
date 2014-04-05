package com.bnotya.bnotyaapp.models;

import android.content.Intent;
import android.content.res.Resources;
import com.bnotya.bnotyaapp.R;

public class Card
{
	private final int _backId;
	private final int _frontId;
    private final int _id;
    
    public Card(Intent intent, Resources resources, String packageName)
    {	
    	int cardId = 1;

		boolean isRandom = intent.getBooleanExtra("EXTRA_SESSION_ISRANDOM",
				false);
		if (isRandom)
		{
			int numberOfCards = resources.getInteger(
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

		_frontId = resources.getIdentifier(String.format("card%s", cardId),
				"drawable", packageName);
		_backId = resources.getIdentifier(
				String.format("card%s_%s", cardId, cardId), "drawable",
				packageName);
		
		_id = cardId;
    }
    
    public int getBackId()
    {
    	return _backId;
    }
    
    public int getFrontId()
    {
    	return _frontId;
    }
    
    public int getID()
    {
    	return _id;
    }
}
