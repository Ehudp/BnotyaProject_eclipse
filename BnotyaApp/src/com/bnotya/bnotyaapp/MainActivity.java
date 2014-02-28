package com.bnotya.bnotyaapp;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{
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

	public void openTehilotPage(View view)
	{
		startActivity(new Intent(this, MainTehilotActivity.class));
	}

	public void openWomenPage(View view)
	{
		startActivity(new Intent(this, MainWomenActivity.class));
	}
}
