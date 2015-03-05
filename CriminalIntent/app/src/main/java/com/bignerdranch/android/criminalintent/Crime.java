package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mike on 2/27/2015.
 */
public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime(){
        //Generate unique identifier
        mId = UUID.randomUUID();
        setDate(new Date());
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getDateString(Context context) {
        return DateFormat.getLongDateFormat(context).format(mDate)
                + " " + DateFormat.getTimeFormat(context).format(mDate);
    }
}
