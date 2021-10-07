package algonquin.cst2335.tran0188;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        String emailAddress = prefs.getString("LoginName", null);

        EditText emailInput = findViewById(R.id.editText);
        emailInput.setText(emailAddress);

        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener( clk -> {

            String userTyped = emailInput.getText().toString();

            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);

            nextPage.putExtra("EmailAddress", userTyped);

            startActivity(nextPage);

            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("LoginName", userTyped);

            editor.apply();
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.w( "MainActivity", "onStart() - The application is now visible on screen" );
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.w( "MainActivity", "onStop() - The application is no longer visible" );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.w( "MainActivity", "onDestroy() - Any memory used by the application is now freed" );
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.w( "MainActivity", "onPause() - The application no longer responds to user input" );
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.w( "MainActivity", "onResume() - The application now responds to user input" );
    }



}