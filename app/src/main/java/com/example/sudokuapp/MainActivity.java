package com.example.sudokuapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SudokuApp Debug";
    private Random random = new Random();

    //colors
    private final String activeBkgColor = "#FFFFFF";
    private final String emptyCellBkgColor = "#EDEFF6";
    private final String concreteCellBkgColor = "#D8DDF0";
    private final String errorCellTextColor = "#FB807C";


    private TextView activeTextView;
    private List<Integer> concreteCells = new ArrayList<>();

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

        View.OnFocusChangeListener cellFocusListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int bkgColor = getBackgroundColor((TextView) v); // Example integer value
                String hexString = String.format("#%06X", (0xFFFFFF & bkgColor));

                //if bkg is not concrete cell, change active bkg color
                if(!hexString.equals(concreteCellBkgColor)){
                    if (hasFocus) {

                        // Change the background color when the TextView gains focus
                        v.setBackgroundColor(Color.parseColor(activeBkgColor));
                    } else {
                        // Revert the background color when the TextView loses focus
                        v.setBackgroundColor(Color.parseColor(emptyCellBkgColor));
                    }
                }
            }
        };

        // Set click listeners on TextViews to update the active TextView
        View.OnClickListener cellClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTextView = (TextView) v;
                activeTextView.requestFocus();

            }
        };


        for (int[] matrix : appGameMatrix) {
            for (int cellID : matrix) {
                findViewById(cellID).setOnClickListener(cellClickListener);
                findViewById(cellID).setOnFocusChangeListener(cellFocusListener);
            }
        }

        //creates the starting board along with concrete numbers
        createRandomSudokuStartingBoard();

        // Set click listeners on buttons to update the text of the active TextView
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeTextView != null && !concreteCells.contains(activeTextView.getId())) {
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

    }
    
    private void createRandomSudokuStartingBoard(){
        int difficultyLevel = 20; // Number of cells to remove for the puzzle

        //make a puzzle board that we will use to run methods on
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
                        //add number to app board, add cellID to concreteCells, and set its styling
                        cellView.setText(value);
                        concreteCells.add(cellID); //add cellID to cells that should not change
                        cellView.setBackgroundColor(Color.parseColor("#D8DDF0"));
                    }
                }
            }

        }
        return;
    }


    private void setUserInputToBoard(int userNum){
        for(int i=0; i<appGameMatrix.length; i++){
            for(int j=0; j<appGameMatrix[i].length; j++){

                int cellID = appGameMatrix[i][j];
                TextView cellView = findViewById(cellID);
                if(cellView == activeTextView){

                    //get user input and add it to sudokuPuzzle board
                    int userValue = Integer.parseInt(cellView.getText().toString());
                    SudokuMaker.setValue(sudokuPuzzle, userValue, i, j);
                    SudokuMaker.printBoard(sudokuPuzzle);

                    //check if the input is valid, and highlight where errors are
                    List<int[]> errors = SudokuMaker.findErrorIntersects(sudokuPuzzle, userValue, i, j);
                    highlightInputIntersects(errors);

                }
            }

        }
        return;
    }

    private void highlightInputIntersects(List<int[]> errorCoordinates){
        String repeats = listToString(errorCoordinates);
        Log.d(TAG, repeats);

        for (int[] point: errorCoordinates) {
            int errorCellID = appGameMatrix[point[0]][point[1]];

            //attached cellView from the cellID
            TextView errorCellView = findViewById(errorCellID);
            int copyBkgColor = getBackgroundColor(errorCellView);
            Log.d(TAG, "active isfocused:: "+activeTextView.isFocused());
            if (activeTextView.isFocused()) {
                errorCellView.setTextColor(Color.parseColor(errorCellTextColor)); // Highlight color
            }
            Log.d(TAG, "bkgColor:: "+copyBkgColor);

        }


        return;
    }

    private void eraseUserInput() {
        return ;
    }

    private int getBackgroundColor(TextView textView) {
        Drawable background = textView.getBackground();
        if (background instanceof ColorDrawable) {
            return ((ColorDrawable) background).getColor();
        }

        return Color.TRANSPARENT; // Default color if background is not a ColorDrawable
    }

    public static String listToString(List<int[]> list) {
        StringBuilder sb = new StringBuilder();
        sb.append(":");

        for (int i = 0; i < list.size(); i++) {
            int[] array = list.get(i);
            sb.append(Arrays.toString(array));

            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append(":");
        return sb.toString();
    }

}