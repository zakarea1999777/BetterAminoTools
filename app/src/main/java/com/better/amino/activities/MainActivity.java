package com.better.amino.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import com.better.amino.R;
import com.better.amino.api.Account;
import com.better.amino.api.utils.AccountUtils;
import com.better.amino.ui.SharedValue;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends Activity {

    MaterialButton login_btn;
    TextInputEditText email;
    TextInputEditText password;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        AlertDialog alertDialog = materialAlertDialogBuilder.create();
        materialAlertDialogBuilder.setTitle(R.string.disclaimer);
        materialAlertDialogBuilder.setMessage(R.string.disclaimer_message);
        materialAlertDialogBuilder.setNegativeButton(R.string.decline, (dialog, which) -> {
            alertDialog.dismiss();
            finish();
        });
        materialAlertDialogBuilder.setPositiveButton(R.string.accept, (dialog, which) -> {
        });
        materialAlertDialogBuilder.show();
        
        Account account = new Account(this);
        SharedValue shared = new SharedValue(this);

        AccountUtils.logged = shared.getBoolean("logged");

        if (account.isLogged()) {
            AccountUtils.sid = shared.getString("sid");
            AccountUtils.uid = shared.getString("uid");
            AccountUtils.nickname = shared.getString("nickname");
            AccountUtils.aminoId = shared.getString("aminoId");
            AccountUtils.sid = shared.getString("sid");
            AccountUtils.icon = shared.getString("icon");
            AccountUtils.bio = shared.getString("bio");

            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        login_btn = findViewById(R.id.login_btn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        email.setText(shared.getString("email"));
        password.setText(shared.getString("password"));

        login_btn.setOnClickListener(view -> {
            if (email.getText().toString().equals("")) {
                email.setError("Email Can't Be Empty");
            } else if (password.getText().toString().equals("")) {
                password.setError("Password Can't Be Empty");
            } else {
                account.Login(email.getText().toString(), password.getText().toString());
                shared.saveString("email", email.getText().toString());
                shared.saveString("password", password.getText().toString());
            }
        });
    }
}
