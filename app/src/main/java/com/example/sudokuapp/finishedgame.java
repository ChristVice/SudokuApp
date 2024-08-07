package com.example.sudokuapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class finishedgame extends AppCompatActivity {
    private static final String TAG = "SudokuApp Debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finishedgame);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        //get the seconds
        Bundle extras = getIntent().getExtras();
        int totalTimeSeconds = 0;

        if (extras != null) {
            totalTimeSeconds = extras.getInt("Time Spent", 0); // Retrieve the integer value with a default of 0
            TextView textView = findViewById(R.id.time_took);

            //take seconds and convert into readable format
            String time = processTime(totalTimeSeconds);
            textView.setText(time); // Convert the integer to a string for display
        }


        //make button go back to game
        Button newGame = findViewById(R.id.new_game_bttn);
        newGame.setOnClickListener(view -> {
            Intent intent = new Intent(finishedgame.this, MainActivity.class);
            startActivity(intent);
        });

    }


    private String processTime(int timeInSeconds) {
        if (timeInSeconds >= 60) {
            int minutes = timeInSeconds / 60;
            int seconds = timeInSeconds % 60;
            if (minutes == 1) {
                return seconds > 0 ? minutes + " minute and " + seconds + " seconds" : minutes + " minute";
            } else {
                return seconds > 0 ? minutes + " minutes and " + seconds + " seconds" : minutes + " minutes";
            }
        }

        return timeInSeconds + (timeInSeconds == 1 ? " second" : " seconds");
    }


}