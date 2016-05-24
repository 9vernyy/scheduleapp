package kz.alisher.scheduleapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

/**
 * Created by Alisher Kozhabay on 4/18/2016.
 */
public class StartActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private SQLiteHandler db;
    private HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        sessionManager = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        user = db.getUserDetails();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    intent.putExtra("fullname", user.get("name") + " " + user.get("surname"));
                    intent.putExtra("email", user.get("email"));
                    intent.putExtra("userId", user.get("id"));
                    startActivity(intent);
                    finish();
                } else {
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 1000);
    }
}
