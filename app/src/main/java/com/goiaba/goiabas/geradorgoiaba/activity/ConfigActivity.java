package com.goiaba.goiabas.geradorgoiaba.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.goiaba.goiabas.geradorgoiaba.R;
import com.goiaba.goiabas.geradorgoiaba.utils.AnimationUtils;
import com.goiaba.goiabas.geradorgoiaba.utils.DebugUtil;
import com.goiaba.goiabas.geradorgoiaba.view.Switch;
import com.nineoldandroids.view.ViewHelper;

public class ConfigActivity extends ActionBarActivity {

    private TextView mConfigSong;
    private TextView mMore;
    private Switch mSwitch;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mConfigSong = (TextView) findViewById(R.id.text_active_song);
        mMore= (TextView) findViewById(R.id.text_more);
        mSwitch = (Switch) findViewById(R.id.active_song);

        mSharedPreferences = getSharedPreferences(GoiabaActivity.PREFS_CONFIG, MODE_PRIVATE);

        ViewHelper.setX(mSwitch, getScreenSize());
        ViewHelper.setAlpha(mSwitch, 0);
        ViewHelper.setAlpha(mMore, 0);
        ViewHelper.setAlpha(mConfigSong, 0);

        mMore.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setX(mMore, mMore.getWidth()*-2);
                startAnimation(mMore);
            }
        });

        mConfigSong.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setX(mConfigSong, mConfigSong.getWidth()*-2);
                startAnimation(mConfigSong);
            }
        });

        mSwitch.post(new Runnable() {
            @Override
            public void run() {
                startAnimation(mSwitch);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwitch.setOncheckListener(onCheckListener);
        mSwitch.setChecked(mSharedPreferences.getBoolean(GoiabaActivity.PREFS_SONG, true));

    }

    @Override
    protected void onPause() {
        savePreference(mSwitch.isCheck());
        super.onPause();

    }

    private void startAnimation(View view) {
        AnimationUtils.animateView(view);
    }

    private void savePreference(boolean check) {
        mSharedPreferences.edit().putBoolean(GoiabaActivity.PREFS_SONG, check).commit();
    }

    private Switch.OnCheckListener onCheckListener = new Switch.OnCheckListener() {
        @Override
        public void onCheck(boolean check) {
            DebugUtil.log("is checked: " + check);
            savePreference(check);
        }
    };

    public int getScreenSize() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        if(android.os.Build.VERSION.SDK_INT >= 13) {
            screenWidth = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        }
        return screenWidth;
    }

}
