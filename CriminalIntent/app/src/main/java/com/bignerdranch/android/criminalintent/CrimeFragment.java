package com.bignerdranch.android.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.annotation.Target;
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
    private static final String DIALOG_CONFIRM_DELETE = "confirmdelete";
    private static final int DELETE_CODE = 1;
    private static final int REQUEST_PHOTO = 2;
    private static final int REQUEST_CONTACT = 3;
    private static final String DIALOG_IMAGE = "image";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallSuspectButton;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }

    private void updateDate(){
        mDateButton.setText(mCrime.getDateString(getActivity()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);

        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if(NavUtils.getParentActivityName(getActivity()) != null) {
                ((ActionBarActivity)getActivity()).getSupportActionBar()
                        .setDisplayHomeAsUpEnabled(true);
            }
        }

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mCrime.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                //This space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
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

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        //If camera is not available, disable camera functionality
        PackageManager pm = getActivity().getPackageManager();
        boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES. GINGERBREAD &&
                        Camera.getNumberOfCameras() > 0);
        if (!hasACamera){
            mPhotoButton.setEnabled(false);
        }

//        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                switch (v.getId()){
//                    case R.id.crime_imageView:
//
//                        return true;
//                    default: return false;
//                }
//            }
//        };

        mPhotoView = (ImageView)v.findViewById(R.id.crime_imageView);
        registerForContextMenu(mPhotoView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p = mCrime.getPhoto();
                if (p == null) {
                    return;
                }

                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path, p.getOrientation()).show(fm, DIALOG_IMAGE);
            }
        });

        mReportButton = (Button)v.findViewById(R.id.crime_reportButton);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        mCallSuspectButton = (Button)v.findViewById(R.id.crime_callButton);
        mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCrime.getSuspectNumber() == null){
                    //show Toast indicating no number
                    Toast.makeText(getActivity(),
                            R.string.crime_no_number, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Number: " + mCrime.getSuspectNumber());
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mCrime.getSuspectNumber()));
                    startActivity(i);
                }
            }
        });

        mSuspectButton = (Button)v.findViewById(R.id.crime_suspectButton);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null){
            mSuspectButton.setText(mCrime.getSuspect());
        }

        return v;
    }

    private void showPhoto(){
        //(Re)set the image button's image based on our photo
        Photo p = mCrime.getPhoto();
        BitmapDrawable b = null;
        if (p != null){
            String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path, p.getOrientation());

        mPhotoView.setImageDrawable(b);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.crime_fragment_delete_crime:
                FragmentManager fm = getActivity().getSupportFragmentManager();

                ConfirmDeleteFragment dialog = ConfirmDeleteFragment.newInstance(mCrime.getId());
                dialog.setTargetFragment(CrimeFragment.this, DELETE_CODE);
                dialog.show(fm, DIALOG_CONFIRM_DELETE);
                return true;
/*

CrimeLab crimeLab = CrimeLab.get(getActivity());
crimeLab.deleteCrime(mCrime);
crimeLab.saveCrimes();
*/
            case android.R.id.home:
                if(NavUtils.getParentActivityName(getActivity()) != null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            //Create a new Photo object and attach it to the crime
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            int orientation = data.getIntExtra(CrimeCameraFragment.EXTRA_PHOTO_ORIENTATION, 0);
            if (filename != null){
                Photo p = new Photo(filename, orientation);
                if (mCrime.getPhoto() != null){
                    mCrime.replacePhoto(getActivity(), p);
                    Log.d(TAG, "Replaced old Photo");
                }else{
                mCrime.setPhoto(p);
                }
                showPhoto();
            }
        } else if (requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();

            //Specify which fields you want you query to return values for.
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
            };
            //Perform your query = the contactUri is like the "where" clause here
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);

            //Double-check that you actually got results
            if (c.getCount() == 0){
                c.close();
                return;
            }

            //Pull out the first column of the first row of data - this is your suspect's name
            c.moveToFirst();
            String suspect = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //Pull out the second column of the first row of data - the suspect's ID
            String suspect_ID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

            //Find the suspects number
            if (Integer.parseInt(c.getString(
                    c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0){
                Cursor phoneCursor = getActivity().getContentResolver()
                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{suspect_ID}, null);

                //Double-check that you actually got results
                if (phoneCursor.getCount() == 0){
                    phoneCursor.close();
                } else {
                    phoneCursor.moveToFirst();
                    String suspectNumber = phoneCursor.getString(
                            phoneCursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                    //Log.d(TAG, "Set suspect number to " + suspectNumber);
                    mCrime.setSuspectNumber(suspectNumber);
                    phoneCursor.close();
                }
            }

            mCrime.setSuspect(suspect);
            mSuspectButton.setText(suspect);
            c.close();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_menu, menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        switch (v.getId()){
            case R.id.crime_imageView:
                inflater.inflate(R.menu.delete_photo_context, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_photo:
                mCrime.removePhoto(getActivity());
                PictureUtils.cleanImageView(mPhotoView);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private String getCrimeReport(){
        String solvedString = null;
        if(mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else{
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else{
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);

        return report;

    }
}
