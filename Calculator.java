public class Calculator {

    public static void main(String[] args) {
        int[][] board =           { {0, 0, 6, 1, 0, 0, 0, 0, 8},
                {0, 8, 0, 0, 9, 0, 0, 3, 0},
                {2, 0, 0, 0, 0, 5, 4, 0, 0},
                {4, 0, 0, 0, 0, 1, 8, 0, 0},
                {0, 3, 0, 0, 7, 0, 0, 4, 0},
                {0, 0, 7, 9, 0, 0, 0, 0, 3},
                {0, 0, 8, 4, 0, 0, 0, 0, 6},
                {0, 2, 0, 0, 5, 0, 0, 8, 0},
                {1, 0, 0, 0, 0, 2, 5, 0, 0} };
        if ( solver(0,0, board) ) System.out.println("Yayy");
        else System.out.println("nooo");
    }

    /**
     * method where you can pass in a uncompleted board and it returns completed board
     * @params:
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
        return checkRows(board, y, num) && checkColumns(board, x, num) && checkSquares(board, x, y, num);
    }

    /**
     * checks if a certain number occurs in a certain column
     * @params:
     * board: the sudoku board
     * x: index of column we are checking
     * num: number we are checking
     */
    private static boolean checkRows(int[][] board, int y, int num) {
        for ( int otherNums : board[y] ) {
            if ( otherNums == num ) return false;
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
    private static boolean checkColumns(int[][] board, int x, int num) {
        for ( int row = 0; row < 9; row ++ ) {
            if ( board[row][x] == num ) return false;
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
    private static boolean checkSquares(int[][] board, int x, int y, int num) {
        int startingX = ((int)(x/3))*3;
        int startingY = ((int)(y/3))*3;
        for ( int a = 0; a < 3; a++ ) {
            for ( int b = 0; b < 3; b++ ) {
                if ( board[startingY+a][startingX+b] == num ) return false;
            }
        }
        return true;
    }

    /**
     * Exception used to throw when the board is impossible to solve
     * @params:
     * reason: reason of throwing a error
     */
    private static class UnsolvableBoardException extends RuntimeException {
        UnsolvableBoardException(String reason) {
            super(reason);
        }
    }
}
