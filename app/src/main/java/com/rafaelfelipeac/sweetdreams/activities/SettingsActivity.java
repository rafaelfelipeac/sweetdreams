package com.rafaelfelipeac.sweetdreams.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import com.rafaelfelipeac.sweetdreams.R;
import java.util.Locale;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getDelegate().getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Resources res = getResources();
        Configuration config = res.getConfiguration();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        setLanguage(actionBar, config, SP);

        res.updateConfiguration(config, res.getDisplayMetrics());

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    private void setLanguage(ActionBar actionBar, Configuration config, SharedPreferences SP) {
        String locale = SP.getString("language", "en");

        switch (locale) {
            case "PT-BR":
                config.locale = new Locale("pt", "BR");
                actionBar.setTitle("Configurações");
                break;
            default:
                config.locale = Locale.ENGLISH;
                actionBar.setTitle("Settings");
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intentMain = new Intent(SettingsActivity.this, MainNavDrawerActivity.class);
                startActivity(intentMain);
                overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
                return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentMain = new Intent(SettingsActivity.this, MainNavDrawerActivity.class);
        startActivity(intentMain);
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals("language")) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
        }
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.app_preferences);
        }
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
}
