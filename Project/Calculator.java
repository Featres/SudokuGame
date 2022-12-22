package Project;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Calculator {
    static int[][] example_board1 =           {
            {0, 0, 6, 1, 0, 0, 0, 0, 8},
            {0, 8, 0, 0, 9, 0, 0, 3, 0},
            {2, 0, 0, 0, 0, 5, 4, 0, 0},
            {4, 0, 0, 0, 0, 1, 8, 0, 0},
            {0, 3, 0, 0, 7, 0, 0, 4, 0},
            {0, 0, 7, 9, 0, 0, 0, 0, 3},
            {0, 0, 8, 4, 0, 0, 0, 0, 6},
            {0, 2, 0, 0, 5, 0, 0, 8, 0},
            {1, 0, 0, 0, 0, 2, 5, 0, 0} };
    static int[][] example_board2 = {
            {0, 0, 0, 0, 6, 0, 0, 2, 7},
            {0, 0, 0, 0, 0, 0, 0, 0, 5},
            {0, 0, 4, 0, 9, 1, 0, 8, 0},
            {0, 0, 8, 0, 0, 0, 0, 0, 4},
            {0, 0, 0, 4, 3, 0, 0, 0, 0},
            {0, 7, 0, 0, 8, 0, 0, 3, 0},
            {3, 0, 0, 0, 0, 9, 0, 0, 1},
            {7, 2, 0, 1, 0, 0, 0, 0, 0},
            {0, 9, 0, 0, 0, 0, 2, 0, 0} };

    /**
     * checks if the board is solved - no zeros and no errors
     * @param board - board to be checked
     * @return - true if completed, else false
     */
    public static boolean isDoneAndComplete(int[][] board) {
        return isCompleted(board) && isFullyDone(board);
    }

    /**
     * checks if the board is correct - no errors like same number in a row
     * @param board - board to be checked
     * @return - true if correctly completed, else false
     */
    public static boolean isCompleted(int[][] board) {
        System.out.println("Testing allowance "+Arrays.deepToString(board));
        return checkColumns(board) && checkRows(board) && checkSquares(board);
    }

    /**
     * checks if the board is fully completed
     * @param board - board to be checked
     * @return - true if it is completed, else false
     */
    public static boolean isFullyDone(int[][] board) {
        for ( int i = 0; i < board.length; i++ ) {
            for ( int j = 0; j < board[j].length; j++ ) {
                if ( board[i][j] == 0 ) return false;
            }
        }
        return true;
    }

    /**
     * method where you can pass in an uncompleted board, and it returns completed board
     * @param
     * board: uncompleted sudoku board (int[9][9])
     */
    public static int[][] calculateSudoku(int[][] board) {
        int[][] givenBoard = board.clone();
        if ( solver(0,0,givenBoard) ) return givenBoard;
        else throw new UnsolvableBoardException("This board isn't solvable!");
    }

    /**
     * method helper for solving sudoku
     * @params:
     * posX: starting position of solving for x-axis (start at 0)
     * posY: starting position of solving for y-axis (start at 0)
     * board: uncompleted sudoku board
     */
    private static boolean solver(int posX, int posY, int[][] board) {
        if (posX == 9) {
            posX = 0;
            posY++;
            if (posY == 9) return true;
        }
        if ( board[posX][posY] != 0 ) return solver(posX+1,posY,board);
        for (int num = 1; num <= 9; num++) {
            if (assumption(board, posX, posY, num)) {
                board[posX][posY] = num;
                if (solver(posX+1,posY,board)) return true;
            }
        }
        board[posX][posY] = 0;
        return false;
    }

    /**
     * checks if num can be put in the board on position (x,y)
     * @params:
     * board: board we are checking
     * x, y: coordinated of the position we are checking
     * num: value we are checking
     */
    private static boolean assumption(int[][] board, int x, int y, int num) {
        int tmp = board[x][y];
        board[x][y] = num;
        boolean result = checkRows(board) && checkColumns(board) && checkSquares(board);
        board[x][y] = tmp;
        return result;
    }

    /**
     * checks if a certain number occurs in a certain row
     * @params:
     * board: the sudoku board
     * x: index of row we are checking
     * num: number we are checking
     */
    private static boolean checkRows(int[][] board) {
        int[] nums = new int[9];
        for ( int row = 0; row < 9; row++ ) {
            for ( int i = 0; i < 9; i++ ) {
                nums[i] = board[row][i];
            }
            System.out.println("Row "+ Arrays.toString(nums));
            Arrays.sort(nums);
            for ( int i = 0; i < 8; i++ ) {
                if ( nums[i] != 0 && nums[i] == nums[i+1] ) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * checks if a certain number occurs in a certain column
     * @params:
     * board: the sudoku board
     * x: index of column we are checking
     * num: number we are checking
     */
    private static boolean checkColumns(int[][] board) {
        int[] nums = new int[9];
        for ( int column = 0; column < 9; column++ ) {
            for ( int i = 0; i < 9; i++ ) {
                nums[i] = board[i][column];
            }
            System.out.println("Column "+ Arrays.toString(nums));
            Arrays.sort(nums);
            for ( int i = 0; i < 8; i++ ) {
                if ( nums[i] != 0 && nums[i] == nums[i+1] ) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * checks if a certain number occurs in a square of a certain position
     * @params:
     * board: the sudoku board
     * x, y: coordinates of the position
     * num: value we are checking
     */
    private static boolean checkSquares(int[][] board) {
        int[] nums = new int[9];
        int h = 0;
        for ( int i = 0; i < 9; i+=3 ) {
            for ( int j = 0; j < 9; j+=3 ) {
                for ( int k = i; k < i+3; k++ ) {
                    for ( int l = j; l < j+3; l++ ) {
                        nums[h++] = board[k][l];
                    }
                }
                h = 0;
                System.out.println("Square "+Arrays.toString(nums));
                Arrays.sort(nums);
                for ( int m = 0; m < 8; m++ ) {
                    if ( nums[m] != 0 && nums[m] == nums[m+1] ) return false;
                }
            }
        }
        return true;
    }

    /**
     * Exception used to throw when the board is impossible to solve
     * @params:
     * reason: reason of throwing an error
     */
    private static class UnsolvableBoardException extends RuntimeException {
        UnsolvableBoardException(String reason) {
            super(reason);
        }
    }
}
