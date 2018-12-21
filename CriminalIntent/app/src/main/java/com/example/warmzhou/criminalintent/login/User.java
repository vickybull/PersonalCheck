package com.example.warmzhou.criminalintent.login;

import java.util.UUID;

public class User {
    private UUID mId;
    private String mAccount;
    private String mPassword;

    public User() {
        this(UUID.randomUUID());
    }

    public User(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        mAccount = account;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
