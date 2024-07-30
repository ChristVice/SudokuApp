package com.example.sudokuapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    //colors
    private final String activeBkgColor = "#FFFFFF";
    private final String emptyCellBkgColor = "#EDEFF6";
    private final String cellHighlightColor = "#D8DDF0";
    private final String errorTextColor = "#FB807C";
    private final String userInputTextColor = "#3CB9FF";


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
                if (hasFocus) {
                    // Change the background color when the TextView gains focus
                    v.setBackgroundColor(Color.parseColor(activeBkgColor));
                    highlightRowAndColumn((TextView) v);
                } else {
                    // Revert the background color when the TextView loses focus
                    v.setBackgroundColor(Color.parseColor(emptyCellBkgColor));
                    unhighlightRowAndColumn((TextView) v);
                }
            }
        };

        // Set click listeners on TextViews to update the active TextView
        View.OnClickListener cellClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeTextView != null) {
                    // Remove focus from the previously active TextView
                    activeTextView.clearFocus();
                }
                activeTextView = (TextView) v;
                activeTextView.requestFocus();
            }
        };

        for (int[] matrix : appGameMatrix) {
            for (int cellID : matrix) {
                findViewById(cellID).setOnFocusChangeListener(cellFocusListener);
                findViewById(cellID).setOnClickListener(cellClickListener);
            }
        }

        // Create the starting board along with concrete numbers
        createRandomSudokuStartingBoard();

        // Set click listeners on buttons to update the text on the active TextView
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeTextView != null && !concreteCells.contains(activeTextView.getId())) {
                    Button clickedButton = (Button) v;
                    String buttonText = clickedButton.getText().toString();

                    try {
                        int number = Integer.parseInt(buttonText); // Ensure the button text is a valid integer

                        // Set the cell to button number, change its color, and update the puzzle board
                        activeTextView.setText(buttonText);
                        activeTextView.setTextColor(Color.parseColor(userInputTextColor));
                        setUserInputToBoard(number);

                        if (SudokuMaker.isPuzzleSolved(sudokuPuzzle)) {
                            Log.d(TAG, "Puzzle is solved!");
                        }

                    } catch (NumberFormatException e) {
                        Log.d("Invalid input :: ", buttonText);
                    }
                }
            }
        };
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(buttonClickListener);
        }

        // Get the delete button and set the click listener
        View.OnClickListener deleteButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "delete button clicked");

                // If active not a concrete, enable eraser
                if (!concreteCells.contains(activeTextView.getId()))
                    eraseUserInput();
            }
        };
        findViewById(R.id.delete_num_button).setOnClickListener(deleteButtonClickListener);
    }

    private void createRandomSudokuStartingBoard() {
        int difficultyLevel = 20; // Number of cells to remove for the puzzle

        // Make a puzzle board that we will use to run methods on
        sudokuPuzzle = sudokuGame.createSudokuPuzzle(difficultyLevel);

        Log.d(TAG, "");
        SudokuMaker.printBoard(sudokuPuzzle);
        setStartingCells(sudokuPuzzle);
    }

    private void setStartingCells(int[][] startingValues) {
        for (int i = 0; i < appGameMatrix.length; i++) {
            for (int j = 0; j < appGameMatrix[i].length; j++) {
                int cellID = appGameMatrix[i][j];
                TextView cellView = findViewById(cellID);
                if (cellView != null) {
                    int startValue = startingValues[i][j];
                    String value = String.valueOf(startValue);
                    if (startValue != 0) {
                        // Add number to app board, add cellID to concreteCells, and set its styling
                        cellView.setText(value);
                        concreteCells.add(cellID); // Add cellID to cells that should not change
                        String concreteTxtColor = "#9198B5";
                        cellView.setTextColor(Color.parseColor(concreteTxtColor));
                        cellView.setBackgroundColor(Color.parseColor(emptyCellBkgColor));
                    }
                }
            }
        }
    }

    private void setUserInputToBoard(int userNum) {
        for (int i = 0; i < appGameMatrix.length; i++) {
            for (int j = 0; j < appGameMatrix[i].length; j++) {
                int cellID = appGameMatrix[i][j];
                TextView cellView = findViewById(cellID);
                if (cellView == activeTextView) {
                    // Get user input and add it to sudokuPuzzle board
                    int userValue = Integer.parseInt(cellView.getText().toString());
                    SudokuMaker.setValue(sudokuPuzzle, userValue, i, j);
                    SudokuMaker.printBoard(sudokuPuzzle);
                }
            }
        }
    }


    /*
    private void highlightInputIntersects(List<int[]> errorCoordinates) {
        String repeats = listToString(errorCoordinates);
        Log.d(TAG, repeats);

        for (int[] point : errorCoordinates) {
            int errorCellID = appGameMatrix[point[0]][point[1]];

            // Attached cellView from the cellID
            TextView errorCellView = findViewById(errorCellID);
            if (errorCellView != null) {
                if (activeTextView.isFocused()) {
                    errorCellView.setTextColor(Color.parseColor(errorCellTextColor)); // Highlight color
                }
            }
        }
    }
     */

    private void highlightRowAndColumn(TextView currentTextView) {
        int[] activePoint = getActiveCoordinate(currentTextView);

        for (int x = 0; x < appGameMatrix.length; x++) {
            TextView row = findViewById(appGameMatrix[activePoint[0]][x]);
            TextView col = findViewById(appGameMatrix[x][activePoint[1]]);

            if(row != currentTextView || col != currentTextView){
                row.setBackgroundColor(Color.parseColor(cellHighlightColor));
                col.setBackgroundColor(Color.parseColor(cellHighlightColor));
            }
        }

        //highlight the currentView
        currentTextView.setBackgroundColor(Color.parseColor(activeBkgColor));
    }

    private void unhighlightRowAndColumn(TextView currentTextView) {
        int[] activePoint = getActiveCoordinate(currentTextView);

        for (int x = 0; x < appGameMatrix.length; x++) {
            TextView row = findViewById(appGameMatrix[activePoint[0]][x]);
            TextView col = findViewById(appGameMatrix[x][activePoint[1]]);

            if(row != currentTextView || col != currentTextView){
                row.setBackgroundColor(Color.parseColor(emptyCellBkgColor));
                col.setBackgroundColor(Color.parseColor(emptyCellBkgColor));
            }
        }

        //unhighlight the currentView
        currentTextView.setBackgroundColor(Color.parseColor(emptyCellBkgColor));
    }

    private int[] getActiveCoordinate(TextView currentTextView) {
        for (int i = 0; i < appGameMatrix.length; i++) {
            for (int j = 0; j < appGameMatrix[i].length; j++) {
                int cellID = appGameMatrix[i][j];
                TextView cellView = findViewById(cellID);
                if (cellView == currentTextView) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0, 0}; // Default value
    }

    private void eraseUserInput() {
        if (activeTextView != null) {
            activeTextView.setText("");
        }
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