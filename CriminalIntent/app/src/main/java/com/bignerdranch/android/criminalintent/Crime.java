package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mike on 2/27/2015.
 */
public class Crime {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime(){
        //Generate unique identifier
        mId = UUID.randomUUID();
        setDate(new Date());
    }

    public Crime(JSONObject json) throws JSONException{
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE)){
            mTitle = json.getString(JSON_TITLE);
        }
        mSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date(json.getLong(JSON_DATE));
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_SOLVED, mSolved);
        json.put(JSON_DATE, mDate.getTime());
        return json;
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
