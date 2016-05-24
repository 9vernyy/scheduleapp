package kz.alisher.scheduleapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private Button btnSignIn;
    private Button btnSignUp;
    @NotEmpty(messageId = R.string.login_valid)
    private EditText inputLogin;
    @NotEmpty(messageId = R.string.pass_valid)
    private EditText inputPassword;
    private SessionManager session;
    private SQLiteHandler db;
    private String login;
    private String password;
    private HashMap<String, String> user;

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

        user = db.getUserDetails();
        Log.d("USER", user.toString());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("fullname", user.get("name") + " " + user.get("surname"));
            intent.putExtra("email", user.get("email"));
            intent.putExtra("userId", user.get("id"));
            startActivity(intent);
            finish();
        }

        // SignIn button Click Event
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (validate()) {
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

    private boolean validate() {
        final boolean isValid = FormValidator.validate(this, new SimpleErrorPopupCallback(this, true));
        return isValid;
    }


    private void checkLogin(final String login, final String password) {
        SessionManager ses = new SessionManager(getApplicationContext());
        pDialog.setMessage("Вход в учетную запись ...");
        showDialog();

        try {
            if (user != null) {
                if (login.equals(user.get("login")) && password.equals(user.get("password"))) {
                    Toast.makeText(getApplicationContext(), "Успешно!", Toast.LENGTH_LONG).show();
                    ses.setLogin(true);
                    hideDialog();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("fullname", user.get("name") + " " + user.get("surname"));
                    intent.putExtra("email", user.get("email"));
                    intent.putExtra("userId", user.get("id"));
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Неверный логин или пароль!", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
            } else
                Toast.makeText(getApplicationContext(), "Такой пользователь не зарегистрирован!", Toast.LENGTH_SHORT).show();
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
