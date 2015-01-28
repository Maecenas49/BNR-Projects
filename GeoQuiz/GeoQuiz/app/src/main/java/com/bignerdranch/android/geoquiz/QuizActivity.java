package com.bignerdranch.android.geoquiz;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class QuizActivity extends Activity {


    private Button mTrueButton;
    private Button mFalseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Set view to True and False Buttons. Implementing OnClickListener for each
        mTrueButton = (Button)findViewById(R.id.true_button);
        //Uses anonymous inner class. There is no need for the overhead of a named class
        // because the class will be used in one place only.
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Since this is in an anonymous inner class, "this" cannot be used as context
                //"this" refers to OnClickListener, which is why "QuizActivity.this" is called
                Toast.makeText(QuizActivity.this,R.string.incorrect_toast,Toast.LENGTH_LONG).show();
            }
        });
        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(QuizActivity.this,R.string.correct_toast,Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
