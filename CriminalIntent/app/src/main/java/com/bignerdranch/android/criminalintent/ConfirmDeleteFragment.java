package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.UUID;

/**
 * Created by Mike on 3/19/2015.
 */
public class ConfirmDeleteFragment extends DialogFragment {

    private static UUID mCrimeId;
    public static final String CRIME_ID = "Crime ID";

    public static ConfirmDeleteFragment newInstance(UUID id){
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID, id);

        ConfirmDeleteFragment fragment = new ConfirmDeleteFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null){
            return;
        }

        Intent i = new Intent();

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCrimeId = (UUID) getArguments().getSerializable(CRIME_ID);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.yes_text,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CrimeLab crimeLab = CrimeLab.get(getActivity());
                        Crime crime = crimeLab.getCrime(mCrimeId);
                        crimeLab.deleteCrime(crime);
                        crimeLab.saveCrimes();
                        sendResult(Activity.RESULT_OK);

                        getActivity().finish();
                    }
                }).setNegativeButton(R.string.no_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                }).setTitle(R.string.confirm_delete).create();

        return dialog;
    }
}
