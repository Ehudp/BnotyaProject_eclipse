package com.bnotya.bnotyaapp;

import com.bnotya.bnotyaapp.controls.SeekBarPreference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity implements
		OnSharedPreferenceChangeListener
{
	private SeekBarPreference _seekBarPref;
	private AudioManager _audioManager;

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
		// Get widgets:
		_seekBarPref = (SeekBarPreference)findPreference(getString(R.string.music_volume_preference));

		// Set listener:
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

		_audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = _audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int currentVolume = _audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		_seekBarPref.setMax(maxVolume);
		_seekBarPref.setProgress(currentVolume);
		
		// Set volume summary:		
		_seekBarPref.setSummary(getString(R.string.settings_summary)
				.replace("1", "" + currentVolume));
	}

	/*@TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
			_seekBarPref = (SeekBarPreference)findPreference(getString(R.string.music_volume_preference));

			// Set listener :
			getPreferenceScreen().getSharedPreferences()
					.registerOnSharedPreferenceChangeListener(this);

			_audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
			int maxVolume = _audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			int currentVolume = _audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			_seekBarPref.setMax(maxVolume);
			_seekBarPref.setProgress(currentVolume);
			
			// Set volume summary:		
			_seekBarPref.setSummary(getString(R.string.settings_summary)
					.replace("1", "" + currentVolume));
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key)
		{
			if (key.equals(getString(R.string.music_preference)))
			{
				if (MainActivity.music == null) return;
				setMusicOnOffSwitch(key);
			}
			else
			{
				setMusicVolume();
			}
		}
	}*/
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String key)
	{
		if (key.equals(getString(R.string.music_preference)))
		{
			if (MainActivity.music == null) return;
			setMusicOnOffSwitch(key);
		}
		else if(key.equals(getString(R.string.music_volume_preference)))
		{
			setMusicVolume();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void setMusicOnOffSwitch(String key)
	{
		boolean hasMusic = getPreferenceScreen().getSharedPreferences()
				.getBoolean(key, true);
		if (hasMusic)
			MainActivity.music.start();
		else
			MainActivity.music.release();
	}
	
	private void setMusicVolume()
	{
		// Set volume summary:	
		int volume = PreferenceManager.getDefaultSharedPreferences(this)
				.getInt(getString(R.string.music_volume_preference), 50);
		_seekBarPref.setSummary(getString(R.string.settings_summary)
				.replace("1", "" + volume));
		
		if (MainActivity.music != null)
		{				
			_audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);				
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume()
	{
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		boolean hasMusic = getPreferenceScreen().getSharedPreferences()
				.getBoolean(getString(R.string.music_preference), true);
		if (hasMusic)
			MainActivity.music.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause()
	{
		super.onPause();
		MainActivity.music.release();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}
}
