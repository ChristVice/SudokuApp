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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
        int totalTimeSeconds;
        boolean didUserFinishGame, isMaxTimeReached;

        if (extras != null) {
            totalTimeSeconds = extras.getInt("Time Spent", 0); // Retrieve the integer value with a default of 0
            didUserFinishGame = extras.getBoolean("UserFinishGame?", true);
            isMaxTimeReached = extras.getBoolean("MaxTimeReached?", false);

            TextView textView = findViewById(R.id.time_took);
            TextView titleGreetView = findViewById(R.id.title_greet);


            if(isMaxTimeReached){
                titleGreetView.setText("Time limit was reached");
            }
            //add motivational message if user choose to end game
            else if(!didUserFinishGame){
                List<String> messages = Arrays.asList(
                        "Great effort! Every step counts.",
                        "Keep going! You'll get it next time.",
                        "Progress is progress. Well done!",
                        "You're on the right track!",
                        "Nice try! Practice makes perfect.",
                        "Don't give up! You're improving.",
                        "Good job! Each attempt makes you better.",
                        "Well played! Keep challenging yourself.",
                        "You're doing great! Persistence is key.",
                        "Way to go! Keep up the good work."
                );
                Random random = new Random();
                int randomIndex = random.nextInt(messages.size());

                titleGreetView.setText(messages.get(randomIndex));
            }

            //take seconds and convert into readable format
            String time = processTime(totalTimeSeconds);
            textView.setText(time); // Convert the integer to a string for display
        }


        //make button go back to game
        Button newGame = findViewById(R.id.new_game_bttn);
        newGame.setOnClickListener(view -> {
            Intent intent = new Intent(finishedgame.this, difficultyselection.class);
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