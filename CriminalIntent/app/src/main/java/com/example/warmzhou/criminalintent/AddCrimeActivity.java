package com.example.warmzhou.criminalintent;

import android.support.v4.app.Fragment;

public class AddCrimeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new AddCrimeFragment();
    }
}
