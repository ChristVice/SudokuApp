package com.example.sudokuapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class difficultyselection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_difficultyselection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = new Intent(difficultyselection.this, MainActivity.class);

        Button easyDiffBttn = findViewById(R.id.easy_diff);
        Button mediumtDiffBttn = findViewById(R.id.medium_diff);
        Button hardDiffBttn = findViewById(R.id.hard_diff);


        easyDiffBttn.setOnClickListener(view -> {
            int easyDiff = 10;
            intent.putExtra("Selected Difficulty", easyDiff);
            startActivity(intent);
        });
        mediumtDiffBttn.setOnClickListener(view -> {
            int mediumDiff = 15;
            intent.putExtra("Selected Difficulty", mediumDiff);
            startActivity(intent);
        });
        hardDiffBttn.setOnClickListener(view -> {
            int hardDiff = 20;
            intent.putExtra("Selected Difficulty", hardDiff);
            startActivity(intent);
        });


    }
}