package com.bnotya.bnotyaapp;

import java.util.Calendar;
import com.bnotya.bnotyaapp.controls.SeekBarPreference;
import com.bnotya.bnotyaapp.controls.TimePreference;
import com.bnotya.bnotyaapp.helpers.About;
import com.bnotya.bnotyaapp.services.AlarmReceiver;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;

public class Preferences extends PreferenceActivity implements
		OnSharedPreferenceChangeListener
{
	private SeekBarPreference _seekBarPref;
	private AudioManager _audioManager;
	private TimePreference _timePref;
	private PendingIntent _pendingAlarmIntent;

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
		// Set listener:
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		// Get widgets:
		initNotificationTimePicker();
		initMusicVolume();		
	}
	
	@SuppressWarnings("deprecation")
	private void initNotificationTimePicker()
	{
		_timePref = (TimePreference)findPreference(getString(R.string.notification_time_preference));
		setTimePickerSummary();
	}
	
	private void setTimePickerSummary()
	{
		int currentHour = PreferenceManager.getDefaultSharedPreferences(this)
				.getInt(getString(R.string.notification_time_preference) + ".hour", 8);
	    int currentMinute = PreferenceManager.getDefaultSharedPreferences(this)
				.getInt(getString(R.string.notification_time_preference) + ".minute", 0);
	    
	    String summary = getString(R.string.notification_time_summary)
				.replace("hour", "" + currentHour);	    
	    
	    _timePref.setSummary(summary.replace("minute", "" + currentMinute));
	}

	@SuppressWarnings("deprecation")
	private void initMusicVolume()
	{
		_seekBarPref = (SeekBarPreference)findPreference(getString(R.string.music_volume_preference));

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
			if (key.equals(getString(R.string.music_on_preference)))
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
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.page_menu, menu);
		return true;
	}
	
	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.findItem(R.id.action_settings).setVisible(false);
		menu.findItem(R.id.action_exit).setVisible(false);
		return super.onPrepareOptionsMenu(menu);
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
			case R.id.action_about:
				About.showAboutDialog(this);
				return true;			
			default:
				return super.onOptionsItemSelected(item);
		}
	}	
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String key)
	{
		if (key.equals(getString(R.string.music_on_preference)))
		{
			if (MainActivity.music == null) return;
			setMusicOnOffSwitch(key);
		}
		else if(key.equals(getString(R.string.music_volume_preference)))
		{
			setMusicVolume();
		}
		else if(key.equals(getString(R.string.user_name_preference)))
		{
			// TODO: Set user name
		}
		else if(key.equals(getString(R.string.notification_on_preference)))
		{
			setNotificationOnOffSwitch(key);
		}
		else if(key.equals(getString(R.string.notification_time_preference) + ".hour")
				|| key.equals(getString(R.string.notification_time_preference) + ".minute"))
		{   
			setTimePickerSummary();		    
		}
	}

	@SuppressWarnings("deprecation")
	private void setNotificationOnOffSwitch(String key)
	{
		boolean hasNotification = getPreferenceScreen().getSharedPreferences()
				.getBoolean(key, true);
		if (hasNotification)
			handleNotification();
		else		
			cancelNotification();		
	}
	
	private void cancelNotification()
	{
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(_pendingAlarmIntent);
		
		disableAlarm();
	}

	private void handleNotification() 
	{		
		enableAlarm();
		
	    Intent alarmIntent = new Intent(this, AlarmReceiver.class);
	    _pendingAlarmIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);	    
	    
	    // Set the alarm
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(System.currentTimeMillis());
	    int hour = PreferenceManager.getDefaultSharedPreferences(this)
				.getInt(getString(R.string.notification_time_preference) + ".hour", 8);
	    int minute = PreferenceManager.getDefaultSharedPreferences(this)
				.getInt(getString(R.string.notification_time_preference) + ".minute", 0);
	    calendar.set(Calendar.HOUR_OF_DAY, hour);
	    calendar.set(Calendar.MINUTE, minute);
	    
	    alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
	            AlarmManager.INTERVAL_DAY, _pendingAlarmIntent);
	}
	
	private void enableAlarm()
	{
		ComponentName receiver = new ComponentName(this, AlarmReceiver.class);
		PackageManager pm = getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		        PackageManager.DONT_KILL_APP);
	}
	
	private void disableAlarm()
	{
		ComponentName receiver = new ComponentName(this, AlarmReceiver.class);
		PackageManager pm = getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		        PackageManager.DONT_KILL_APP);
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
	
	/*@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void initNotification()
	{
		Intent intent = new Intent(this, CardFlipActivity.class);
		intent.putExtra("EXTRA_SESSION_ISRANDOM", true);
		// Sets the Activity to start in a new, empty task
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			notification = new NotificationCompat.Builder(this)
					.setContentTitle("Localize").setContentText("Localize")
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentIntent(pendingIntent).build();
		}
		else
		{
			notification = new Notification(R.drawable.ic_launcher, "Localize",
					System.currentTimeMillis());
			notification.setLatestEventInfo(this, "Localize", "Localize",
					pendingIntent);
			notification.defaults = Notification.DEFAULT_ALL;
		}

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(1234567, notification);
	}*/

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
		MainActivity.music.release();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}
}
