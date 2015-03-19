package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.nfc.Tag;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Mike on 3/16/2015.
 */
public class CriminalIntentJSONSerializer {

    private Context mContext;
    private String mFilename;

    private static final String TAG = "JSONSerializer";

    public CriminalIntentJSONSerializer(Context c, String f){
        mContext = c;
        mFilename = f;
    }

    public void saveCrimes(ArrayList<Crime> crimes)
                throws JSONException, IOException{
        //Build an array in JSON
        JSONArray array = new JSONArray();
        for (Crime c : crimes){
            array.put(c.toJSON());
        }

        //Write the file to disk
        Writer writer = null;
        try{
            OutputStream out;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                File file = new File(mContext.getExternalFilesDir(null), mFilename);
                out = new FileOutputStream(file);
                Log.d(TAG, "Saved to external file");
            }else{
            out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            }
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally{
            if (writer != null)
                writer.close();
        }
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        try{
            InputStream in;
            //Open and read the file into a StringBuilder
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                File file = new File(mContext.getExternalFilesDir(null), mFilename);
                in = new BufferedInputStream(new FileInputStream(file));
                Log.d(TAG, "Loaded external file");
            }else{
                in = mContext.openFileInput(mFilename);
            }
            reader =  new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //Line breakers are omitted and irrelevant
                jsonString.append(line);
            }
            //Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //Build the array o f crimes from JSONObjects
            for (int i = 0; i < array.length();i++){
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e){
            //Ignore this one; it happens when starting fresh
        } finally{
            if (reader != null)
                reader.close();
        }
        return crimes;
    }

}


