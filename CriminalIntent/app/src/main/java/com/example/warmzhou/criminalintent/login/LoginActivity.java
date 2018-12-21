package com.example.warmzhou.criminalintent.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.warmzhou.criminalintent.CrimeListActivity;
import com.example.warmzhou.criminalintent.R;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private Button mButton;
    private EditText mAccountEditText;
    private EditText mPasswordEditText;
    private List<User> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsers = UserLab.get(this).getCrimes();
        mButton = (Button)findViewById(R.id.login_button);
        mAccountEditText=(EditText)findViewById(R.id.account);
        mPasswordEditText=(EditText)findViewById(R.id.password);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<mUsers.size();i++){
                    User user=mUsers.get(i);
                    if (user.getAccount().equals(mAccountEditText.getText().toString())){
                        if (user.getPassword().equals(mPasswordEditText.getText().toString())){
                            Intent intent = new Intent(LoginActivity.this,CrimeListActivity.class);
                            startActivity(intent);
                            break;
                        }else{
                            Toast.makeText(LoginActivity.this,"密码错误"+ mUsers.size(),Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this,"账号不存在"+mUsers.size(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
