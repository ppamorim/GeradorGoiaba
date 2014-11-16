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

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.goiaba.goiabas.geradorgoiaba.R;
import com.goiaba.goiabas.geradorgoiaba.view.Slider;

public class GoiabaActivity extends ActionBarActivity {

    private static final String FINAL_TEXT = "final_text";
    private static final String SLIDER_POSITION = "slider_position";
    private static final String IMAGE_VISIBILITY = "image_visibility";

    private String mBaseText;

    private Slider mSlider;
    private TextView mCopyText;
    private RippleView mGenerate;
    private RelativeLayout mLogoImage;
    private ScrollView mContainerText;

    private ClipboardManager mClipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goiaba);

        mSlider = (Slider) findViewById(R.id.slider);
        mCopyText = (TextView) findViewById(R.id.copy_text);
        mGenerate = (RippleView) findViewById(R.id.generate);
        mLogoImage = (RelativeLayout) findViewById(R.id.container_logo);
        mContainerText = (ScrollView) findViewById(R.id.container_text);

        mBaseText = getString(R.string.goiaba);
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mGenerate.setOnClickListener(onGenerateClick);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String savedText = savedInstanceState.getString(FINAL_TEXT);
        int sliderValue = savedInstanceState.getInt(SLIDER_POSITION);
        if(savedText != null && !savedText.equals("") && mCopyText != null) {
            mCopyText.setText(savedText);
        }
        if(mSlider != null) {
            mSlider.setValue(sliderValue);
        }

        if(mLogoImage != null) {
            boolean isVisible = savedInstanceState.getBoolean(IMAGE_VISIBILITY);
            if(isVisible) {
                mLogoImage.setVisibility(View.VISIBLE);
                mContainerText.setVisibility(View.GONE);
            } else {
                mLogoImage.setVisibility(View.GONE);
                mContainerText.setVisibility(View.VISIBLE);
            }
        }
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

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.about:
                startActivity(new Intent(getBaseContext(), AboutActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onGenerateClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

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

                mLogoImage.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mContainerText.setVisibility(View.VISIBLE);
                        mContainerText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
                        mLogoImage.setVisibility(View.GONE);
                    }
                }, 300);


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
}
