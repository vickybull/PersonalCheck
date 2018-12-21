package com.example.warmzhou.criminalintent.database.CrimeDbSchema;

public class CrimeDbSchema {
    public static final class UserTable {
        public static final String NAME = "users";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String ACCOUNT = "account";
            public static final String PASSWORD = "password";
        }
    }

    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String MONEY = "money";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String REMARK = "remark";
            public static final String PHOTO = "photo";
        }
    }
}
