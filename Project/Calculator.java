package Project;

import java.util.ArrayList;
import java.util.Arrays;

public class Calculator {

    /**
     * checks if the board is solved - no zeros and no errors
     * @param board board to be checked
     * @return true if completed, else false
     */
    public static boolean isDoneAndComplete(int[][] board) {
        return isCompleted(board) && isFullyDone(board);
    }

    /**
     * checks if the board is correct - no errors like same number in a row
     * @param board board to be checked
     * @return true if correctly completed, else false
     */
    public static boolean isCompleted(int[][] board) {
//        System.out.println("Testing allowance "+Arrays.deepToString(board));
        return checkColumns(board) && checkRows(board) && checkSquares(board);
    }

    /**
     * checks if the board is fully completed
     * @param board board to be checked
     * @return true if it is completed, else false
     */
    public static boolean isFullyDone(int[][] board) {
        for (int[] row : board) {
            for (int num : row) {
                if (num == 0) return false;
            }
        }
        return true;
    }

    /**
     * method where you can pass in an uncompleted board, and it returns completed board
     * @param board board to be calculated
     */
    public static int[][] calculateSudoku(int[][] board) {
        int[][] givenBoard = board.clone();
        if ( solver(0,0,givenBoard) ) return givenBoard;
        else throw new UnsolvableBoardException("This board isn't solvable!");
    }

    /**
     * method helper for solving sudoku
     * @param posX starting position of solving for x-axis (start at 0)
     * @param posY starting position of solving for y-axis (start at 0)
     * @param board uncompleted sudoku board
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
     * @param board board we are checking
     * @param x x coordinate of the position we are checking
     * @param y y coordinate of the position we are checking
     * @param num value we are checking
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
     * @param board the sudoku board that we are checking
     */
    private static boolean checkRows(int[][] board) {
        int[] nums = new int[9];
        for ( int row = 0; row < 9; row++ ) {
            for ( int i = 0; i < 9; i++ ) {
                nums[i] = board[row][i];
            }
//            System.out.println("Row "+ Arrays.toString(nums));
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
     * @param board the sudoku board that we are checking
     */
    private static boolean checkColumns(int[][] board) {
        int[] nums = new int[9];
        for ( int column = 0; column < 9; column++ ) {
            for ( int i = 0; i < 9; i++ ) {
                nums[i] = board[i][column];
            }
//            System.out.println("Column "+ Arrays.toString(nums));
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
     * @param board the sudoku board that we are checking
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
                Arrays.sort(nums);
                for ( int m = 0; m < 8; m++ ) {
                    if ( nums[m] != 0 && nums[m] == nums[m+1] ) return false;
                }
            }
        }
        return true;
    }

    /**
     * function that will return why the number cannot be
     * inputted on a certain grid cell by returning an array
     * of positions that collide with the given action
     * @param board sudoku board that the user works on
     * @param num number that the user wants to input on the position
     * @param row row that the user wants to input to
     * @param column column that the user wants to input to
     * @return array of positions that are colliding
     */
    public static int[][] findCollidingPoints(int[][] board, int num, int row, int column) {
        ArrayList<String> collidingList = new ArrayList<>();
        for ( int i = 0; i < 9; i++ ) {
            if ( board[row][i] == num ) {
                String data = String.valueOf(row) + i;
                collidingList.add(data);
            }
            if ( board[i][column] == num ) {
                String data = i + String.valueOf(column);
                collidingList.add(data);
            }
        }

        int rowStart = 3*((int)(row/3));
        int colStart = 3*((int)(column/3));
        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                if ( board[rowStart+i][colStart+j] == num ) {
                    String data = String.valueOf(rowStart+i) + (colStart + j);
                    collidingList.add(data);
                }
            }
        }

        for ( int i = 0; i < collidingList.size(); i++ ) {
            for ( int j = i+1; j < collidingList.size(); j++ ) {
                if ( collidingList.get(i).equals(collidingList.get(j)) ) {
                    collidingList.remove(i);
                }
            }
        }

        int[][] result = new int[collidingList.size()][2];
        for ( int i = 0; i < result.length; i++ ) {
            result[i][0] = Integer.parseInt(collidingList.get(i).substring(1, 2));
            result[i][1] = Integer.parseInt(collidingList.get(i).substring(0, 1));
        }
        return result;
    }

    /**
     * method to check if the given row in the board is complete
     * @param board the sudoku board
     * @param row row to be checked
     */
    public static boolean checkRow(int[][] board, int row) {
        for ( int num : board[row] ) {
            if ( num == 0 ) return false;
        }
        return true;
    }

    /**
     * method to check if the given column in the board is complete
     * @param board the sudoku board
     * @param column column to be checked
     */
    public static boolean checkColumn(int[][] board, int column) {
        for ( int i = 0; i < 9; i++ ) {
            if ( board[i][column] == 0 ) return false;
        }
        return true;
    }

    /**
     * method to check if the square that contains given
     * cell in the board is complete
     * @param board the sudoku board
     * @param row row parameter of the cell, which square will be checked
     * @param column column  parameter of the cell, which square will be checked
     */
    public static boolean checkSquare(int[][] board, int row, int column) {
        int startRow = 3*((int)(row/3));
        int startColumn = 3*((int)(column/3));
        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                if ( board[startRow+i][startColumn+j] == 0 ) return false;
            }
        }
        return true;
    }

    /**
     * Exception used to throw when the board is impossible to solve
     */
    private static class UnsolvableBoardException extends RuntimeException {
        UnsolvableBoardException(String reason) {
            super(reason);
        }
    }
}
