package com.example.sudokuapp;

import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SudokuApp Debug";
    private Random random = new Random();

    private TextView activeTextView;

    private int[][] appGameMatrix;
    int[][] sudokuPuzzle;
    SudokuMaker sudokuGame = new SudokuMaker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


         appGameMatrix = new int[][]{
                {R.id.cell_1, R.id.cell_2, R.id.cell_3, R.id.cell_4, R.id.cell_5, R.id.cell_6},
                 {R.id.cell_7, R.id.cell_8, R.id.cell_9, R.id.cell_10, R.id.cell_11, R.id.cell_12},
                 {R.id.cell_13, R.id.cell_14, R.id.cell_15, R.id.cell_16, R.id.cell_17, R.id.cell_18},
                 {R.id.cell_19, R.id.cell_20, R.id.cell_21, R.id.cell_22, R.id.cell_23, R.id.cell_24},
                 {R.id.cell_25, R.id.cell_26, R.id.cell_27, R.id.cell_28, R.id.cell_29, R.id.cell_30},
                 {R.id.cell_31, R.id.cell_32, R.id.cell_33, R.id.cell_34, R.id.cell_35, R.id.cell_36},
        };

        int[] buttonIds = {
                R.id.number1, R.id.number2, R.id.number3,
                R.id.number4, R.id.number5, R.id.number6,
                R.id.number7, R.id.number8, R.id.number9
        };

        // Set click listeners on TextViews to update the active TextView
        View.OnClickListener cellClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTextView = (TextView) v;
            }
        };


        for (int[] matrix : appGameMatrix) {
            for (int cellID : matrix) {
                findViewById(cellID).setOnClickListener(cellClickListener);
            }
        }


        // Set click listeners on buttons to update the text of the active TextView
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeTextView != null) {
                    Button clickedButton = (Button) v;
                    String buttonText = clickedButton.getText().toString();

                    try {
                        int number = Integer.parseInt(buttonText); // Ensure the button text is a valid integer
                        activeTextView.setText(buttonText);
                        setUserInputToBoard(number);
                    } catch (NumberFormatException e) {
                        Log.d("Invalid input :: ", buttonText);
                    }
                }
            }
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(buttonClickListener);
        }

        createRandomSudokuStartingBoard();
    }
    
    private void createRandomSudokuStartingBoard(){
        int difficultyLevel = 20; // Number of cells to remove for the puzzle
        sudokuPuzzle = sudokuGame.createSudokuPuzzle(difficultyLevel);

        Log.d(TAG, "");
        SudokuMaker.printBoard(sudokuPuzzle);
        setStartingCells(sudokuPuzzle);
        return;
    }

    private void setStartingCells(int[][] startingValues){
        for(int i=0; i<appGameMatrix.length; i++){
            for(int j=0; j<appGameMatrix[i].length; j++){

                int cellID = appGameMatrix[i][j];
                TextView cellView = findViewById(cellID);
                if(cellView != null){
                    int startValue = startingValues[i][j];
                    String value = String.valueOf(startValue);
                    if(startValue != 0){
                        cellView.setText(value);
                        //cellView.setTextAppearance(R.style.FilledCellStyle);
                        cellView.setBackgroundColor(Color.parseColor("#D8DDF0"));
                    }
                }
            }

        }
        return;
    }


    private void setUserInputToBoard(int userNum){
        Log.d(TAG, "User input number: " + userNum);
        for(int i=0; i<appGameMatrix.length; i++){
            for(int j=0; j<appGameMatrix[i].length; j++){

                int cellID = appGameMatrix[i][j];
                TextView cellView = findViewById(cellID);
                if(cellView == activeTextView){
                    int userValue = Integer.parseInt(cellView.getText().toString());
                    SudokuMaker.setValue(sudokuPuzzle, userValue, i, j);
                    SudokuMaker.printBoard(sudokuPuzzle);
                    Log.d(TAG,"Solved :: "+SudokuMaker.isPuzzleSolved(sudokuPuzzle));
                }
            }

        }


        return;
    }

}