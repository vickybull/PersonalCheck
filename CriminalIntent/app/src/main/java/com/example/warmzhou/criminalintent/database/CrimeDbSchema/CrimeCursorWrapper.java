package com.example.warmzhou.criminalintent.database.CrimeDbSchema;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.warmzhou.criminalintent.Crime;
import com.example.warmzhou.criminalintent.database.CrimeDbSchema.CrimeDbSchema.CrimeTable;
import com.example.warmzhou.criminalintent.database.CrimeDbSchema.CrimeDbSchema.UserTable;
import com.example.warmzhou.criminalintent.login.User;

import java.util.Date;
import java.util.UUID;


public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        String money = getString(getColumnIndex(CrimeTable.Cols.MONEY));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String remark = getString(getColumnIndex(CrimeTable.Cols.REMARK));
        String photo=getString(getColumnIndex(CrimeTable.Cols.PHOTO));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setMoney(money);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setRemark(remark);
        crime.setPhoto(photo);
        return crime;
    }

    public User getUser() {
        String uuidString = getString(getColumnIndex(UserTable.Cols.UUID));
        String account = getString(getColumnIndex(UserTable.Cols.ACCOUNT));
        String password = getString(getColumnIndex(UserTable.Cols.PASSWORD));

        User user = new User(UUID.fromString(uuidString));
        user.setAccount(account);
        user.setPassword(password);
        return user;
    }

}
