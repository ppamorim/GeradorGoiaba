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

package com.goiaba.goiabas.geradorgoiaba.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer mPlayer;
    private final IBinder musicBind = new MusicBinder();
    private boolean mIsPlaying;

    public void onCreate() {
        super.onCreate();
    }

    public void initMusicPlayer() {
        //set mPlayer properties
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void playSong(boolean isToReset){

        if(mPlayer != null && isToReset) {
            mPlayer.reset();

        }

        if(mPlayer == null || isToReset) {
            Context context = getApplicationContext();
            mPlayer = MediaPlayer.create(context, context.getResources().getIdentifier("goiaba", "raw", context.getPackageName()));
            initMusicPlayer();
        }

        if(mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }

    }

    public void pauseSong(){
        if(mPlayer!= null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    public boolean isSongPlaying(){
        if(mPlayer!= null) {
            return mPlayer.isPlaying();
        } else {
            return false;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        mPlayer.stop();
        mPlayer.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if(mediaPlayer != null) {
            mediaPlayer.start();
        }
    }


}
