package com.alisher.schedule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private Button btnSignIn;
    private Button btnSignUp;
    private EditText inputLogin;
    private EditText inputPassword;
    private SessionManager session;
    private SQLiteHandler db;
    private String login;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputLogin = (EditText) findViewById(R.id.log_login);
        inputPassword = (EditText) findViewById(R.id.log_password);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // SignIn button Click Event
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String login = inputLogin.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!login.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(login, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Одно из полей не заполнено!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(
                        LoginActivity.this,
                        RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String login, final String password) {
        SessionManager ses = new SessionManager(getApplicationContext());
        pDialog.setMessage("Вход в учетную запись ...");
        showDialog();
        HashMap<String, String> user = db.getUserDetails();
        Log.d("LoginActivity", user.get("login"));
        Log.d("LoginActivity", user.get("password"));

        try {
            //HashMap<String, String> user = db.getUserDetails();
            if (login.equals(user.get("login")) && password.equals(user.get("password"))) {
                Toast.makeText(getApplicationContext(), "Успешно!", Toast.LENGTH_LONG).show();
                ses.setLogin(true);
                hideDialog();
                Intent intent = new Intent(
                        LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Неверный логин или пароль!", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
