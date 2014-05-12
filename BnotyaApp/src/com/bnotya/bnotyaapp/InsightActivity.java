package com.bnotya.bnotyaapp;

import com.bnotya.bnotyaapp.helpers.About;
import com.bnotya.bnotyaapp.models.Insight;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class InsightActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insight_view);

		ImageView insightView = (ImageView) findViewById(R.id.insight);
		
		// Setup insight
		Insight insight = new Insight(getIntent(), getResources(), getPackageName());	
		
		insightView.setImageResource(insight.getInsightId());
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
}
