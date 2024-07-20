package com.example.sudokuapp;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SudokuMaker {

    private static final int SIZE = 6;
    private static final int SUBGRID_ROWS = 2;
    private static final int SUBGRID_COLS = 3;
    private int[][] board = new int[SIZE][SIZE];

    private Random random = new Random();

    private boolean isValid(int row, int col, int num) {
        for (int x = 0; x < SIZE; x++) {
            if (board[row][x] == num || board[x][col] == num) {
                return false;
            }
        }

        int startRow = row / SUBGRID_ROWS * SUBGRID_ROWS;
        int startCol = col / SUBGRID_COLS * SUBGRID_COLS;
        for (int i = 0; i < SUBGRID_ROWS; i++) {
            for (int j = 0; j < SUBGRID_COLS; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean fillBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    int[] nums = random.ints(1, SIZE + 1).distinct().limit(SIZE).toArray();
                    for (int num : nums) {
                        if (isValid(row, col, num)) {
                            board[row][col] = num;
                            if (fillBoard()) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private void removeNumbers(int difficulty) {
        int removed = 0;
        while (removed < difficulty) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (board[row][col] != 0) {
                board[row][col] = 0;
                removed++;
            }
        }
    }

    public int[][] createSudokuPuzzle(int difficulty) {
        board = new int[SIZE][SIZE];
        fillBoard();
        printBoard(board);
        removeNumbers(difficulty);
        return board;
    }

    public static void printBoard(int[][] board) {
        for (int[] row : board)  {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

    public static void setValue(int[][] puzzle, int value, int x, int y){
        puzzle[x][y] = value;
        return;
    }

    public static boolean isPuzzleSolved(int[][] puzzle){
        // Check each row
        for (int row = 0; row < SIZE; row++) {
            Set<Integer> rowSet = new HashSet<>();
            for (int col = 0; col < SIZE; col++) {
                if (puzzle[row][col] == 0 || !rowSet.add(puzzle[row][col])) {
                    return false;
                }
            }
        }

        // Check each column
        for (int col = 0; col < SIZE; col++) {
            Set<Integer> colSet = new HashSet<>();
            for (int row = 0; row < SIZE; row++) {
                if (puzzle[row][col] == 0 || !colSet.add(puzzle[row][col])) {
                    return false;
                }
            }
        }

        // Check each subgrid
        for (int row = 0; row < SIZE; row += SUBGRID_ROWS) {
            for (int col = 0; col < SIZE; col += SUBGRID_COLS) {
                Set<Integer> subgridSet = new HashSet<>();
                for (int i = 0; i < SUBGRID_ROWS; i++) {
                    for (int j = 0; j < SUBGRID_COLS; j++) {
                        int num = puzzle[row + i][col + j];
                        if (num == 0 || !subgridSet.add(num)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}
