package com.bignerdranch.android.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import java.util.Date;

/**
 * Created by Mike on 3/4/2015.
 */
public class DateOrTimePickerFragment extends DialogFragment {
    private Date mDate;

    private static final String DATE_DIALOG = "Date";
    private static final String TIME_DIALOG = "Time";

    public static DateOrTimePickerFragment newInstance(Date date){
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

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.select_date_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Call Date Picker Fragment
                        FragmentManager fm = getActivity().getSupportFragmentManager();

                        DatePickerFragment dateDialog = DatePickerFragment.newInstance(mDate);
                        dateDialog.setTargetFragment(getTargetFragment(), getTargetRequestCode());
                        dateDialog.show(fm,DATE_DIALOG);
                    }
                })
                .setNegativeButton(R.string.select_time_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Call Time Picker Fragment
                        FragmentManager fm = getActivity().getSupportFragmentManager();

                        TimePickerFragment timeDialog = TimePickerFragment.newInstance(mDate);
                        timeDialog.setTargetFragment(getTargetFragment(), getTargetRequestCode());
                        timeDialog.show(fm, TIME_DIALOG);
                    }
                })
                .setTitle(R.string.select_date_time).create();

        return dialog;
        }
}
