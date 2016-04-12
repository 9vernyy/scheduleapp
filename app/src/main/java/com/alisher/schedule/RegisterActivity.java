package com.alisher.schedule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFirstName;
    private EditText inputSecondName;
    private EditText inputEmail;
    private EditText inputLogin;
    private EditText inputPassword;
    private SessionManager session;
    private SQLiteHandler db;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFirstName = (EditText) findViewById(R.id.reg_fName);
        inputSecondName = (EditText) findViewById(R.id.reg_lName);
        inputEmail = (EditText) findViewById(R.id.reg_email);
        inputLogin = (EditText) findViewById(R.id.reg_login);
        inputPassword = (EditText) findViewById(R.id.reg_password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFirstName.getText().toString().trim();
                String surname = inputSecondName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String login = inputLogin.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!name.isEmpty() && !surname.isEmpty() && !email.isEmpty() &&
                        !login.isEmpty() && !password.isEmpty()) {
                    registerUser(name, surname, email, login, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Пожалуйста, введите данные о себе!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void registerUser(final String name, final String surname,
                              final String email, final String login, final String password) {

        pDialog.setMessage("Регистрация ...");
        showDialog();
        try {
            // Inserting row in users table
            db.addUser(name, surname, email, login, password);
            hideDialog();
            Toast.makeText(getApplicationContext(), "Пользователь успешно зарегистрирован. Попробуйте войти!!", Toast.LENGTH_LONG).show();

            // Launch login activity
            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
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
