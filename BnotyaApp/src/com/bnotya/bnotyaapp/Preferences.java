package com.bnotya.bnotyaapp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class Preferences extends PreferenceActivity
{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            onCreatePreferenceActivity();
        } else {
            onCreatePreferenceFragment();
        }
    }
    
    @SuppressWarnings("deprecation")
    private void onCreatePreferenceActivity() {
        addPreferencesFromResource(R.xml.prefs);
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void onCreatePreferenceFragment() {
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new AppPreferenceFragment ())
                .commit();
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)	
	public static class AppPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
        }
    }


}
