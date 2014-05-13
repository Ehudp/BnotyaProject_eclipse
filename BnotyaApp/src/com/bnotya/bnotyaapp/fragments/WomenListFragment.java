package com.bnotya.bnotyaapp.fragments;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.bnotya.bnotyaapp.CardFlipActivity;
import com.bnotya.bnotyaapp.R;
import com.bnotya.bnotyaapp.adapters.CustomArrayAdapter;
import com.bnotya.bnotyaapp.models.ListItem;

public class WomenListFragment extends Fragment
{
	public static final String ARG_VIEW_NUMBER = "view_number";
	private ListView _listView;

	public WomenListFragment()
	{
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.women_list_view,
				container, false);		

		getActivity().setTitle(R.string.women_list_title);
		initWomenList(rootView);
		return rootView;
	}	

	private void initWomenList(View rootView)
	{
		// Get ListView object from xml		
		_listView = (ListView) rootView.findViewById(R.id.womenlist);

		ListItem[] listDataHeaders = fillData(R.array.women_names_array,
				R.array.women_list_icons);

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data
		CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(),
				R.layout.women_list_item, listDataHeaders);

		// Assign adapter to ListView
		_listView.setAdapter(adapter);

		// ListView Item Click Listener
		_listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent(getActivity(),
						CardFlipActivity.class);
				intent.putExtra("EXTRA_SESSION_ID", position);				
				startActivity(intent);
			}
		});
	}

	private ListItem[] fillData(int titlesID, int iconsID)
	{
		// Load item values
		String[] titles = getResources().getStringArray(titlesID);
		TypedArray icons = getResources().obtainTypedArray(iconsID);

		ListItem[] result = new ListItem[titles.length];

		// Adding items to array

		for (int i = 0; i < titles.length; i++)
		{
			result[i] = new ListItem(titles[i], icons.getResourceId(i, -1));
		}

		// Recycle the typed array
		icons.recycle();

		return result;
	}
}
