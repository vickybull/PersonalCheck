package com.example.warmzhou.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.widget.CompoundButton.*;

public class AddCrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int RESULT_LOAD_IMAGE = 10;
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    private Crime mCrime;
    private EditText mTitleField;
    private EditText mMoneyField;
    private Button mDateButton;
    private RadioButton mCardRadioButton;
    private RadioButton mMoneyRadioButton;
    private EditText mRemark;
    private Button mAddButton;
    private ImageView mImageView;
    private Button mFinish;
    /*public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
//        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mCrime = new Crime();
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(getActivity(),
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            int permission1 = ActivityCompat.checkSelfPermission(getActivity(),
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有读的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mMoneyField = (EditText) v.findViewById(R.id.crime_money);
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(AddCrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });


        mCardRadioButton = (RadioButton) v.findViewById(R.id.crime_card_cb);
        mMoneyRadioButton = (RadioButton) v.findViewById(R.id.crime_money_cb);
        mRemark = (EditText) v.findViewById(R.id.remark);
        mImageView = (ImageView) v.findViewById(R.id.imageview);
        mAddButton = (Button) v.findViewById(R.id.add_photo_Button);
        mAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
        mFinish = (Button) v.findViewById(R.id.finish);
        mFinish.setVisibility(View.VISIBLE);
        mFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTitleField.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "请输入项目名称", Toast.LENGTH_SHORT).show();
                } else {
                    mCrime.setTitle(mTitleField.getText().toString());

                    if (mMoneyField.getText().toString().length() == 0) {
                        Toast.makeText(getActivity(), "请输入收入或支出", Toast.LENGTH_SHORT).show();
                    } else {

                        mCrime.setMoney(mMoneyField.getText().toString());
                        if (mCardRadioButton.isChecked() == false && mMoneyRadioButton.isChecked() == false) {
                            Toast.makeText(getActivity(), "请选择类别", Toast.LENGTH_SHORT).show();
                        } else {
                            if (mCardRadioButton.isChecked() == true && mMoneyRadioButton.isChecked() == false) {
                                mCrime.setSolved(true);
                            }
                            if (mCardRadioButton.isChecked() == false && mMoneyRadioButton.isChecked() == true) {
                                mCrime.setSolved(false);
                            }
                            CrimeLab.get(getActivity()).addCrime(mCrime);
                            mFinish.setVisibility(View.INVISIBLE);
                            getActivity().finish();
                        }
                    }
                }
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            mCrime.setPhoto(picturePath);
            cursor.close();

            mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        if (requestCode == REQUEST_DATE)

        {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }

    }

    private void updateDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mCrime.getDate());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        mDateButton.setText("" + calendar.get(Calendar.YEAR) + '年' + (calendar.get(Calendar.MONTH) + 1) + '月' + calendar.get(Calendar.DAY_OF_MONTH) + '日');
    }

    public void returnResult() {
        getActivity().setResult(Activity.RESULT_OK, null);
    }
}
