package com.bignerdranch.android.geoquiz;

/**
 * Created by Mike on 2/1/2015.
 */
public class TrueFalse {
    private int mQuestion;

    private boolean mHasCheated;
    private boolean mTrueQuestion;

    public TrueFalse(int question, boolean trueQuestion){
        setQuestion(question);
        setTrueQuestion(trueQuestion);
        setHasCheated(false);
    }

    public boolean isTrueQuestion() {
        return mTrueQuestion;
    }

    public void setTrueQuestion(boolean trueQuestion) {
        mTrueQuestion = trueQuestion;
    }

    public int getQuestion() {
        return mQuestion;
    }

    public void setQuestion(int question) {
        mQuestion = question;
    }

    public boolean hasCheated() {
        return mHasCheated;
    }

    public void setHasCheated(boolean hasCheated) {
        mHasCheated = hasCheated;
    }
}
