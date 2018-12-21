package com.example.warmzhou.criminalintent.database.CrimeDbSchema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.warmzhou.criminalintent.database.CrimeDbSchema.CrimeDbSchema.CrimeTable;
import com.example.warmzhou.criminalintent.database.CrimeDbSchema.CrimeDbSchema.UserTable;

public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATEBASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATEBASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeTable.NAME + "(" +
                "_id integer primary key autoincrement," +
                CrimeTable.Cols.UUID + "," +
                CrimeTable.Cols.MONEY + "," +
                CrimeTable.Cols.TITLE + "," +
                CrimeTable.Cols.DATE + "," +
                CrimeTable.Cols.SOLVED + "," +
                CrimeTable.Cols.REMARK + "," +
                CrimeTable.Cols.PHOTO +
                ")"
        );

        db.execSQL("create table " + UserTable.NAME + "(" +
                "_id integer primary key autoincrement," +
                CrimeTable.Cols.UUID + "," +
                UserTable.Cols.ACCOUNT + "," +
                UserTable.Cols.PASSWORD +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
