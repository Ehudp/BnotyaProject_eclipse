package com.bnotya.bnotyaapp.models;

import android.content.Intent;
import android.content.res.Resources;

public class Insight
{
	private final int _insightId;
	private final int _id;

	public Insight(Intent intent, Resources resources, String packageName)
	{
		int id = 1;

		id += intent.getIntExtra("EXTRA_SESSION_ID", 0);

		_insightId = resources.getIdentifier(
				String.format("card%s_%s", id, id), "drawable", packageName);

		_id = id;
	}

	public int getInsightId()
	{
		return _insightId;
	}

	public int getID()
	{
		return _id;
	}
}
