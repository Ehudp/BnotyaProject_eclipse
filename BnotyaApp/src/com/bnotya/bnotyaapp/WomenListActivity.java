package com.bnotya.bnotyaapp;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class WomenListActivity extends ActionBarActivity 
{
	ListView listView;   
	// Defined Array values to show in ListView
    String[] womenNames = new String[] 
    		{ 
    		"First", 
    		"Second", 
    		"Third" 
            };
	
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_women_list);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		else
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);			
		}
		
		// Get ListView object from xml
        listView = (ListView) findViewById(R.id.womenlist);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, womenNames);
        
     // Assign adapter to ListView
        listView.setAdapter(adapter);

     // ListView Item Click Listener
        listView.setOnItemClickListener(new OnItemClickListener() 
        {
              @Override
              public void onItemClick(AdapterView<?> parent, View view,
                 int position, long id) 
              {             
            	//String selectedWoman = womenNames[position];
            	//TODO: create activities for women
          		//try
          		//{
          			//Class selected = Class.forName("com.bnotya.bnotyaapp." + selectedWoman);
          			//startActivity(new Intent(this, selected));
          		//}
          		//catch(ClassNotFoundException e)
          		//{
          			//e.printStackTrace();
          		//}	                         

               // ListView Clicked item value
               String itemValue = (String) listView.getItemAtPosition(position);                  

                // Show Alert 
                Toast.makeText(getApplicationContext(),
                  "Position :"+position+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                  .show();
              }			
         });
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.women_list_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle presses on the action bar items
		switch (item.getItemId()) 
		{		
		case R.id.action_open_search:
			openSearch(null);
			return true;		
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openSearch(View view) 
	{		
		
	}
}
