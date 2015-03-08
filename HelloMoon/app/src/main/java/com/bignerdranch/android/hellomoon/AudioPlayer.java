package com.bignerdranch.android.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Mike on 3/5/2015.
 */
public class AudioPlayer {
    private MediaPlayer mPlayer;


    public void stop(){
        if (mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void play(Context c){

        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(c, R.raw.one_small_step);
        }

        if (!mPlayer.isPlaying()) {
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stop();
                }
            });
                mPlayer.start();
        }
    }

    public void pause(){
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }
}
