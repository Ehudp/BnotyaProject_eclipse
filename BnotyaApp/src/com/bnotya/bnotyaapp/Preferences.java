package com.bnotya.bnotyaapp;

import com.bnotya.bnotyaapp.controls.SeekBarPreference;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity implements
		OnSharedPreferenceChangeListener
{
	private static SeekBarPreference _seekBarPref;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		/*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
		{*/
			onCreatePreferenceActivity();
		/*}
		else
		{
			onCreatePreferenceFragment();
		}*/
	}

	@SuppressWarnings("deprecation")
	private void onCreatePreferenceActivity()
	{
		addPreferencesFromResource(R.xml.prefs);
		// Get widgets :
		_seekBarPref = (SeekBarPreference) this.findPreference("SEEKBAR_VALUE");

		// Set listener :
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

		// Set seekbar summary :
		int radius = PreferenceManager.getDefaultSharedPreferences(this)
				.getInt("SEEKBAR_VALUE", 50);
		_seekBarPref.setSummary(this.getString(R.string.settings_summary)
				.replace("1", "" + radius));
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void onCreatePreferenceFragment()
	{
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new AppPreferenceFragment())
				.commit();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class AppPreferenceFragment extends PreferenceFragment
			implements OnSharedPreferenceChangeListener
	{
		@Override
		public void onCreate(final Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);

			addPreferencesFromResource(R.xml.prefs);
			// Get widgets :
			_seekBarPref = (SeekBarPreference) this
					.findPreference("SEEKBAR_VALUE");

			// Set listener :
			getPreferenceScreen().getSharedPreferences()
					.registerOnSharedPreferenceChangeListener(this);

			// Set seekbar summary :
			int radius = PreferenceManager.getDefaultSharedPreferences(
					this.getActivity()).getInt("SEEKBAR_VALUE", 50);
			_seekBarPref.setSummary(this.getString(R.string.settings_summary)
					.replace("1", "" + radius));
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key)
		{
			if (key.equals(getString(R.string.music_preference)))
			{
				if (MainActivity.music == null) return;
				boolean hasMusic = getPreferenceScreen().getSharedPreferences()
						.getBoolean(key, true);
				if (hasMusic)
					MainActivity.music.start();
				else
					MainActivity.music.stop();
			}
			else
			{
				// Set seekbar summary :
				int volume = PreferenceManager.getDefaultSharedPreferences(
						this.getActivity()).getInt("SEEKBAR_VALUE", 50);
				_seekBarPref.setSummary(this.getString(
						R.string.settings_summary).replace("1", "" + volume));
				if (MainActivity.music != null)
				{
					MainActivity.music.setVolume(volume, volume);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String key)
	{
		if (key.equals(getString(R.string.music_preference)))
		{
			if (MainActivity.music == null) return;
			boolean hasMusic = getPreferenceScreen().getSharedPreferences()
					.getBoolean(key, true);
			if (hasMusic)
				MainActivity.music.start();
			else
				MainActivity.music.stop();
		}
		else
		{
			// Set seekbar summary :
			int volume = PreferenceManager.getDefaultSharedPreferences(this)
					.getInt("SEEKBAR_VALUE", 50);
			_seekBarPref.setSummary(this.getString(R.string.settings_summary)
					.replace("1", "" + volume));
			if (MainActivity.music != null)
			{
				MainActivity.music.setVolume(volume, volume);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume()
	{
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause()
	{
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}
}
