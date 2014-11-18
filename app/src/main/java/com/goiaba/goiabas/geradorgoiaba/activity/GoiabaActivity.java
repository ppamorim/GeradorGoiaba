/*
* Copyright 2014 Pedro Paulo de Amorim
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.goiaba.goiabas.geradorgoiaba.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.goiaba.goiabas.geradorgoiaba.R;
import com.goiaba.goiabas.geradorgoiaba.service.MusicService;
import com.goiaba.goiabas.geradorgoiaba.utils.DebugUtil;
import com.goiaba.goiabas.geradorgoiaba.view.Slider;
import com.nineoldandroids.view.ViewHelper;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class GoiabaActivity extends ActionBarActivity {

    private static final String FINAL_TEXT = "final_text";
    private static final String SLIDER_POSITION = "slider_position";
    private static final String IMAGE_VISIBILITY = "image_visibility";
    private static final String SONG_PLAYING = "song_playing";
    private static final String CONTAINER_POSITION = "container_position";

    public static final String PREFS_CONFIG = "preferences_config";
    public static final String PREFS_SONG = "preferences_song";

    //Verify if service is running
    private boolean mMusicBound = false;
    private float mContainerPosition;
    private String mBaseText;

    private Slider mSlider;
    private TextView mCopyText;
    private RippleView mGenerate;
    private RelativeLayout mLogoImage;
    private ScrollView mContainerText;

    private MenuItem mMenuItem;

    private MusicService mMusicService;
    private Intent mPlayIntent;

    private Drawable mPauseDrawable;
    private Drawable mPlayDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goiaba);

        mSlider = (Slider) findViewById(R.id.slider);
        mCopyText = (TextView) findViewById(R.id.copy_text);
        mGenerate = (RippleView) findViewById(R.id.generate);
        mLogoImage = (RelativeLayout) findViewById(R.id.container_logo);
        mContainerText = (ScrollView) findViewById(R.id.container_text);

        Resources resources = getResources();

        mBaseText = resources.getString(R.string.goiaba);
        mPauseDrawable = resources.getDrawable(R.drawable.ic_pause_circle_outline_white_48dp);
        mPlayDrawable = resources.getDrawable(R.drawable.ic_play_circle_outline_white_48dp);

        mContainerPosition = ViewHelper.getY(mContainerText);
        ViewHelper.setY(mContainerText, getScreenSize());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mGenerate.setOnClickListener(onGenerateClick);

        if(mMenuItem != null) {
            if (savedInstanceState.getBoolean(SONG_PLAYING)) {
                mMenuItem.setIcon(mPauseDrawable);
            } else {
                mMenuItem.setIcon(mPlayDrawable);
            }
        } else {
            DebugUtil.log("mMenuItem null");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mPlayIntent ==null){
            mPlayIntent = new Intent(this, MusicService.class);
            bindService(mPlayIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(mPlayIntent);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(mPlayIntent);
        mMusicService =null;
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String savedText = savedInstanceState.getString(FINAL_TEXT);
        if(savedText != null && !savedText.equals("") && mCopyText != null) {
            mCopyText.setText(savedText);
        }
        if(mSlider != null) {
            mSlider.setValue(savedInstanceState.getInt(SLIDER_POSITION));
        }

        if(mLogoImage != null) {
            if(savedInstanceState.getBoolean(IMAGE_VISIBILITY)) {
                mLogoImage.setVisibility(View.VISIBLE);
                mContainerText.setVisibility(View.GONE);
            } else {
                mLogoImage.setVisibility(View.GONE);
                mContainerText.setVisibility(View.VISIBLE);
            }
        }

        ViewHelper.setY(mContainerText, 0);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mCopyText != null && !mCopyText.getText().equals("")) {
            outState.putString(FINAL_TEXT, mCopyText.getText().toString());
        }

        if(mSlider != null) {
            outState.putInt(SLIDER_POSITION, mSlider.getValue());
        }

        if(mLogoImage != null) {
            outState.putBoolean(IMAGE_VISIBILITY, mLogoImage.getVisibility() == View.VISIBLE);
        }

        if(mMusicService != null) {
            outState.putBoolean(SONG_PLAYING, mMusicService.isSongPlaying());
        }

        outState.putFloat(CONTAINER_POSITION, ViewHelper.getY(mContainerText));

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        mMenuItem = menu.findItem(R.id.controller);

        if(mMenuItem != null && mMusicService != null) {
            if (mMusicService.isSongPlaying()) {
                mMenuItem.setIcon(mPauseDrawable);
            } else {
                mMenuItem.setIcon(mPlayDrawable);
            }
        } else {
            DebugUtil.log("mMenuItem && mMusicService null");
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.about:
                startActivity(new Intent(getBaseContext(), AboutActivity.class));
                break;

            case R.id.controller:
                toggleIcon(mMenuItem, item.getIcon() == mPlayDrawable, false);
                break;

            case R.id.config:
                startActivity(new Intent(getBaseContext(), ConfigActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleIcon(MenuItem item, boolean isToPlay, boolean isToReset) {
        if (isToPlay) {
            item.setIcon(mPauseDrawable);
            mMusicService.playSong(isToReset);
        } else {
            item.setIcon(mPlayDrawable);
            mMusicService.pauseSong();
        }
    }

    private View.OnClickListener onGenerateClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(mMusicService != null) {
                toggleIcon(mMenuItem, true, true);
            } else {
                DebugUtil.log("mMusicService is null");
            }

            StringBuilder text = new StringBuilder("");
            int sliderCount = mSlider.getValue();

            if(sliderCount == 0) {
                sliderCount = 1;
            }

            for (int i = 0; i < sliderCount; i++) {
                text.append("\n").append(mBaseText);
            }

            mCopyText.setText(text.toString());

            if(mContainerText.getVisibility() != View.VISIBLE) {
                animate(mLogoImage)
                        .setInterpolator(new AnticipateOvershootInterpolator(1))
                        .alpha(0)
                        .setDuration(2000)
                        .translationY(getScreenSize())
                        .start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mContainerText.setVisibility(View.VISIBLE);

                        animate(mContainerText)
                                .setInterpolator(new DecelerateInterpolator(10))
                                .setDuration(5000)
//                                .alpha(100)
                                .translationY(0)
                                .start();
                        mLogoImage.setVisibility(View.GONE);
                    }
                }, 1000);


            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                final android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                final android.content.ClipData clipData = android.content.ClipData
                        .newPlainText(getString(R.string.app_name), text.toString());
                clipboardManager.setPrimaryClip(clipData);
            } else {
                final android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(text.toString());
            }

        }
    };

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicService = ((MusicService.MusicBinder)service).getService();
            mMusicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicBound = false;
        }
    };

    public int getScreenSize() {
        int screenWidth = getResources().getDisplayMetrics().heightPixels;
        if(android.os.Build.VERSION.SDK_INT >= 13) {
            screenWidth = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        }
        return screenWidth;
    }
}
