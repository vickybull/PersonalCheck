package com.example.warmzhou.criminalintent.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.warmzhou.criminalintent.database.CrimeDbSchema.CrimeBaseHelper;
import com.example.warmzhou.criminalintent.database.CrimeDbSchema.CrimeCursorWrapper;
import com.example.warmzhou.criminalintent.database.CrimeDbSchema.CrimeDbSchema.UserTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserLab {
    private static UserLab sUserLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static UserLab get(Context context) {
        if (sUserLab == null) {
            sUserLab = new UserLab(context);
        }
        return sUserLab;
    }

    private UserLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
        String sql = "Select COUNT(*) FROM " + UserTable.NAME;
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        if (count < 1) {
            User user = new User();
            user.setAccount("123");
            user.setPassword("123");
            addUser(user);
        }
    }

    public void addUser(User c) {
        ContentValues values = getContentValues(c);

        mDatabase.insert(UserTable.NAME, null, values);
    }

    public List<User> getCrimes() {
        List<User> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryUsers(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getUser());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public User getUser(UUID id) {
        CrimeCursorWrapper cursor = queryUsers(
                UserTable.Cols.UUID + " = ?", new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getUser();
        } finally {
            cursor.close();
        }
    }

    private CrimeCursorWrapper queryUsers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                UserTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }

    public void updateUser(User user) {
        String uuidString = user.getId().toString();
        ContentValues values = getContentValues(user);
        mDatabase.update(UserTable.NAME, values, UserTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    private static ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(UserTable.Cols.UUID, user.getId().toString());
        values.put(UserTable.Cols.ACCOUNT, user.getAccount());
        values.put(UserTable.Cols.PASSWORD, user.getPassword());
        return values;
    }
}
