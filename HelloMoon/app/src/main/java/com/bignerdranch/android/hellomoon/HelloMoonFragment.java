package com.bignerdranch.android.hellomoon;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Mike on 3/5/2015.
 */
public class HelloMoonFragment  extends Fragment{
    private final String TAG = "HelloMoonFragment";

    private Button mPlayButton;
    private Button mStopButton;
    private Button mPauseButton;

    private AudioPlayer mPlayer = new AudioPlayer();

    private VideoView mVideoView;
    private MediaController mMediaController;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hello_moon, container, false);

        mPlayButton = (Button) v.findViewById(R.id.hellomoon_playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.play(getActivity());
            }
        });

        mPauseButton = (Button) v.findViewById(R.id.hellomoon_pauseButton);
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.pause();
            }
        });

        mStopButton = (Button) v.findViewById(R.id.hellomoon_stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.stop();
            }
        });

        mVideoView = (VideoView) v.findViewById(R.id.hellomoon_VideoView);

        mMediaController = new MediaController(getActivity());
        mMediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mMediaController);
        Log.d(TAG, "android.resource://" +
               "com.bignerdranch.android.hellomoon/raw/apollo_17_stroll");
        mVideoView.setVideoURI( Uri.parse(
                        "android.resource://" + "com.bignerdranch.android.hellomoon/raw/apollo_17_stroll" ));

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }


}
