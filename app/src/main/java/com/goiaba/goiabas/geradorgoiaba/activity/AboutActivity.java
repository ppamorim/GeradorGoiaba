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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goiaba.goiabas.geradorgoiaba.R;
import com.goiaba.goiabas.geradorgoiaba.utils.AnimationUtils;
import com.nineoldandroids.view.ViewHelper;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

public class AboutActivity extends ActionBarActivity {

    private LinearLayout mGithub;
    private ImageView mImage;
    private TextView mTextImage;
    private TextView mOpenSource;
    private TextView mTextDev;
    private TextView mCopyRight;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mGithub = (LinearLayout) findViewById(R.id.open_github);
        mImage = (ImageView) findViewById(R.id.image);
        mTextImage = (TextView) findViewById(R.id.text_image);
        mOpenSource = (TextView) findViewById(R.id.open_source);
        mTextDev = (TextView) findViewById(R.id.dev);
        mCopyRight = (TextView) findViewById(R.id.copiraiguete);

        mImage.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setX(mImage, mImage.getWidth() * -2);
                ViewHelper.setAlpha(mImage, 0);
                startAnimation(mImage);
            }
        });

        int screenSize = getScreenSize()*2;

        ViewHelper.setY(mCopyRight, getScreenHeight());
        ViewHelper.setX(mTextImage, screenSize);
        ViewHelper.setX(mOpenSource, screenSize);
        ViewHelper.setX(mTextDev, screenSize);
        ViewHelper.setX(mTextDev, screenSize);
        ViewHelper.setAlpha(mTextImage, 0);
        ViewHelper.setAlpha(mOpenSource, 0);
        ViewHelper.setAlpha(mTextDev, 0);

        mTextDev.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation(mTextImage);
            }
        }, 50);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation(mOpenSource);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startAnimation(mTextDev);
                    }
                }, 200);
            }
        }, 200);

        animate(mCopyRight)
                .setInterpolator(new DecelerateInterpolator(10))
                .setDuration(5000)
                .alpha(100)
                .translationY(0)
                .start();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_link))));
            }
        });
    }

    private void startAnimation(View view) {
        AnimationUtils.animateView(view);
    }

    public int getScreenSize() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        if(android.os.Build.VERSION.SDK_INT >= 13) {
            screenWidth = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        }
        return screenWidth;
    }

    public int getScreenHeight() {
        int screenWidth = getResources().getDisplayMetrics().heightPixels;
        if(android.os.Build.VERSION.SDK_INT >= 13) {
            screenWidth = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        }
        return screenWidth;
    }
}
