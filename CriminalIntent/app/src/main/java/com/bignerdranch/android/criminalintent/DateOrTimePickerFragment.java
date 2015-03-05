package com.bignerdranch.android.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mike on 3/4/2015.
 */
public class DateOrTimePickerFragment extends DialogFragment {
    private Date mDate;
    private Button mDateButton;
    private Button mTimeButton;

    private static final String DATE_DIALOG = "Date";

    public Fragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(DatePickerFragment.EXTRA_DATE, date);

        DateOrTimePickerFragment fragment = new DateOrTimePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDate = (Date)getArguments().getSerializable(DatePickerFragment.EXTRA_DATE);

        View v = (View) getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_date_time, null);

        mDateButton = (Button)getActivity().findViewById(R.id.select_date_picker);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();

                DatePickerFragment dialog = DatePickerFragment.newInstance(mDate);
                dialog.setTargetFragment(getTargetFragment(), getTargetRequestCode());
                dialog.show(fm, DATE_DIALOG);

            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.select_date_time).create();
    }
}
