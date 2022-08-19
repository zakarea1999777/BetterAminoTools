package com.better.amino.activites;

import android.app.Activity;
import android.os.Bundle;

import com.better.amino.R;
import com.better.amino.api.Account;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends Activity {
    MaterialButton login_btn;
    TextInputEditText email;
    TextInputEditText password;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Account account = new Account(this);
        
        login_btn = findViewById(R.id.login_btn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login_btn.setOnClickListener(view -> {
            if (email.getText().toString().equals(""))
            {email.setError("Email Can't Be Empty");}
            else if (password.getText().toString().equals(""))
            {password.setError("Password Can't Be Empty");}
            else {
                account.Login(email.getText().toString(), password.getText().toString());
            }
        });
    }
}
