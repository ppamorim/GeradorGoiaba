package com.goiaba.goiabas.geradorgoiaba.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.goiaba.goiabas.geradorgoiaba.R;
import com.goiaba.goiabas.geradorgoiaba.view.Switch;

public class ConfigActivity extends ActionBarActivity {

    private Switch mSwitch;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        mSwitch = (Switch) findViewById(R.id.active_song);

        mSharedPreferences = getSharedPreferences(GoiabaActivity.PREFS_CONFIG, MODE_PRIVATE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwitch.setOncheckListener(onCheckListener);
        mSwitch.setChecked(mSharedPreferences.getBoolean(GoiabaActivity.PREFS_SONG, true));


    }

    private Switch.OnCheckListener onCheckListener = new Switch.OnCheckListener() {
        @Override
        public void onCheck(boolean check) {
            mSharedPreferences.edit().putBoolean(GoiabaActivity.PREFS_SONG, check).commit();
        }
    };

}
