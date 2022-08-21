package com.better.amino.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.better.amino.R;
import com.better.amino.api.Account;
import com.better.amino.api.utils.AccountUtils;
import com.better.amino.ui.SharedValue;
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
        SharedValue shared = new SharedValue(this);

        AccountUtils.logged = shared.getBoolean("logged");

        if (account.isLogged()){
            AccountUtils.sid = shared.getString("sid");
            AccountUtils.uid = shared.getString("uid");
            AccountUtils.nickname = shared.getString("nickname");
            AccountUtils.aminoId = shared.getString("aminoId");
            AccountUtils.sid = shared.getString("sid");
            AccountUtils.icon = shared.getString("icon");

            startActivity(new Intent(this, HomeActivity.class));
        }

        login_btn = findViewById(R.id.login_btn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        email.setText(shared.getString("email"));
        password.setText(shared.getString("password"));

        login_btn.setOnClickListener(view -> {
            if (email.getText().toString().equals(""))
            {email.setError("Email Can't Be Empty");}
            else if (password.getText().toString().equals(""))
            {password.setError("Password Can't Be Empty");}
            else {
                account.Login(email.getText().toString(), password.getText().toString());
                shared.saveString("email", email.getText().toString());
                shared.saveString("password", password.getText().toString());
                startActivity(new Intent(this, HomeActivity.class));
            }
        });
    }
}
