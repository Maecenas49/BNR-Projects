package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;


/**
 * Created by Mike on 2/27/2015.
 */
public class CrimeFragment extends Fragment {
    public static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";
    private static final String TAG = "CrimeFragment";

    private static final String DIALOG_DATE_OR_TIME = "dateortime";
    private static final int REQUEST_DATE = 0;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void updateDate(){
        mDateButton.setText(mCrime.getDateString(getActivity()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);

        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence c, int start, int before, int count){
                mCrime.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after){
                //This space intentionally left blank
            }

            public void afterTextChanged(Editable c){
                //This one too
            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
                updateDate();
                mDateButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        FragmentManager fm = getActivity().getSupportFragmentManager();

                        DateOrTimePickerFragment dialog = DateOrTimePickerFragment
                                .newInstance(mCrime.getDate());
                        dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                        dialog.show(fm, DIALOG_DATE_OR_TIME);
/*                        DatePickerFragment dialog = DatePickerFragment
                                .newInstance(mCrime.getDate());
                        dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                        dialog.show(fm, DIALOG_DATE_OR_TIME);*/
                    }
                });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Set crime's solved property
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();

        }
    }
}
