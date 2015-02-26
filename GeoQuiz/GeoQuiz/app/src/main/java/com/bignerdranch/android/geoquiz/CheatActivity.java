package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Mike on 2/11/2015.
 */
public class CheatActivity extends Activity {

    private final static String TAG = "CheatActivity";
    private final static String CHEAT_INDICATOR = "cheated";

    private boolean mAnswerIsTrue;
    private boolean mHasCheated;

    private TextView mAnswerTextView;
    private Button mShowAnswer;

    public static final String EXTRA_ANSWER_IS_TRUE =
            "com.bignerdranch.android.geoquiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN =
            "com.bignerdranch.android.geoquiz.answer_shown";

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null){
            mHasCheated = savedInstanceState.getBoolean(CHEAT_INDICATOR);
            //Log.d(TAG, String.format("SavedState retrieved and mHasCheated set to %b",mHasCheated));
            setAnswerShownResult(mHasCheated);
        }else{
            mHasCheated = false;
            //Log.d(TAG, String.format("SavedState = null, set mHasCheated to False"));
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        mShowAnswer = (Button) findViewById(R.id.showAnswerButton);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                mHasCheated = true;
                setAnswerShownResult(mHasCheated);
                //Log.d(TAG, String.format("Set mHasCheated to %b", mHasCheated));
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CHEAT_INDICATOR, mHasCheated);
        //Log.d(TAG, String.format("Added mHasCheated=%b to SavedState", mHasCheated));
    }
}
