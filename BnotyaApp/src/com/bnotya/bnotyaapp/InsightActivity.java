package com.bnotya.bnotyaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.bnotya.bnotyaapp.helpers.About;
import com.bnotya.bnotyaapp.models.Insight;
import com.bnotya.bnotyaapp.services.DataBaseService;

public class InsightActivity extends ActionBarActivity implements OnClickListener
{
	private Insight _insight;
	private ImageButton _btnAddToFavorites;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insight_view);

		ImageView insightView = (ImageView)findViewById(R.id.insight);
		
		Intent intent = getIntent();
		
		// Setup insight
		_insight = new Insight(intent, getResources(), getPackageName());	
		
		insightView.setImageResource(_insight.getInsightId());
		
		_btnAddToFavorites = (ImageButton)findViewById(R.id.btnAddToFavorites);
		if(intent.getBooleanExtra("EXTRA_IS_FROM_LIST", false))
		{
			_btnAddToFavorites.setVisibility(View.INVISIBLE);
		}
		else
		{		
			_btnAddToFavorites.setOnClickListener(this);
		}
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
				NavUtils.navigateUpTo(this, new Intent(this, InsightListActivity.class));
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
				exitApplication();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void exitApplication()
	{
		Intent intent = new Intent(this, MainActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent.putExtra("EXIT", true);
	    startActivity(intent);
	    finish();
	}

	@Override
	public void onClick(View v)
	{
		Insight insight = DataBaseService.dbHelper.getInsightById(_insight.getId(), this);
		_insight.setIsFavorite(true);
		
		if(insight == null)
		{			
			DataBaseService.dbHelper.addInsight(_insight);
		}
		else
		{
			if(!insight.getIsFavorite())
				DataBaseService.dbHelper.updateInsight(_insight);
		}
	}
}
