package Project;

public class Random {

    /**
     * method that creates a random sudoku
     * @return random sudoku array 9x9
     */
    public static int[][] randomSudoku() {
        int[][] board = new int[9][9];
        for (int i = 0; i < 3; i++) {
            int[] arr = new int[3];
            arr[0] = (int) (Math.random() * 9) + 1;

            arr[1] = (int) (Math.random() * 9) + 1;
            while (arr[0] == arr[1]) {
                arr[1] = (int) (Math.random() * 9) + 1;
            }

            arr[2] = (int) (Math.random() * 9) + 1;
            while (arr[0] == arr[2] || arr[1] == arr[2]) {
                arr[2] = (int) (Math.random() * 9) + 1;
            }
            for ( int j = 0; j < 3; j++ ) {
                board[j+i*3][j+i*3] = arr[j];
            }
        }
        // add the rotations
        board = rotateProbability(board);
        // randomly assign starting point
        if ( board[0][0] == 0 ) {
            board[0][0] = (int) (Math.random() * 9) + 1;
            while ( board[0][0] == board[0][8] || board[0][0] == board[8][0] ) {
                board[0][0] = (int) (Math.random() * 9) + 1;
            }
        }
        return Calculator.calculateSudoku(board);
    }

    /**
     * method that will rotate the board based on probability - increasing randomness
     * @param givenBoard board to be rotated/not
     * @return new board after random rotations
     */
    private static int[][] rotateProbability(int[][] givenBoard) {
        int[][] board = givenBoard.clone();
        int rotations = (int)(Math.random()*4);
        for ( int i = 0; i < rotations; i++ ) {
            board = rotateOnce(board);
        }
        return board;
    }

    /**
     * helper method for rotating that rotates the board once to the right
     * @param givenBoard board to be rotated once to the right
     * @return given board after one rotation to the right
     */
    private static int[][] rotateOnce(int[][] givenBoard) {
        int[][] newBoard = new int[givenBoard.length][givenBoard[0].length];
        for ( int i = 0; i < givenBoard.length; i++ ) {
            int[] currArr = givenBoard[i].clone();
            for ( int j = 0; j < currArr.length; j++ ) {
                newBoard[j][givenBoard[0].length-i-1] = currArr[j];
            }
        }
        return newBoard;
    }

}

