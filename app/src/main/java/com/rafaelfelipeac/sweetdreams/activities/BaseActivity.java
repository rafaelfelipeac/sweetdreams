package com.rafaelfelipeac.sweetdreams.activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.rafaelfelipeac.sweetdreams.R;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        Configuration config = res.getConfiguration();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String locale = SP.getString("language", "en");

        switch (locale) {
            case "PT-BR":
                config.locale = new Locale("pt", "BR");
                break;
            default:
                config.locale = Locale.ENGLISH;
                break;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());

        setContentView(R.layout.activity_base);
    }

}
