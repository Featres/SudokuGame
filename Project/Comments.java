package Project;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Comments extends JLabel {
    private ArrayList<ArrayList<ArrayList<Integer>>> data;
    // data - 9 arraylists of 9 arraylists:
    // equal to 9 rows with 9 cells
    private final Game game;
    private final int[][] gridPositions;
    private boolean commentsMode;

    public Comments(Game game) {
        int boardX = Game.boardX;
        int boardY = Game.boardY;
        int boardSize = Game.boardSize;

        this.data = new ArrayList<>();
        for ( int i = 0; i < 9; i++ ) {
            ArrayList<ArrayList<Integer>> tmp = new ArrayList<>();
            for ( int j = 0; j < 9; j++ ) {
                tmp.add(new ArrayList<>());
            }
            this.data.add(tmp);
        }

        this.game = game;
        this.gridPositions = Comments.gridPositionsGenerator();

        this.setBounds(boardX, boardY, boardSize, boardSize);
        this.setOpaque(false);

        // TODO clear with brush
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int boardSize = Game.boardSize;
        int gridSize = (int)(boardSize/9);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                if (this.data.get(row).get(col).size() == 0) continue;

                ArrayList<Integer> tmp = new ArrayList<>( this.data.get(row).get(col) );
                Collections.sort(tmp);

                for ( int i = 0; i < tmp.size(); i++ ) {
                    int[][] pos = this.gridPositions;
                    int size = (int)(gridSize/3.2);

                    int xGrid = col*gridSize;
                    int yGrid = row*gridSize;

                    if ( xGrid == 0 || yGrid == 0 ) {
                        size *= 0.9;
                    }

                    xGrid = Math.max(xGrid, (int)(0.3*size));
                    yGrid = Math.max(yGrid, (int)(0.3*size));

                    int xPos = pos[i][0];
                    int yPos = pos[i][1];

                    String value = String.valueOf( tmp.get(i) );

                    g2.setColor(Color.GRAY);

                    Font myFont = new Font("Comic Sans", Font.PLAIN, size);
                    g2.setFont(myFont);
                    g2.drawString(value, xGrid+xPos+(int)(0.2*size), yGrid+yPos+(int)(1*size));
                }
            }
        }

    }

    public void removeComment(int number, int row, int column) {
        for ( int i = 0; i < 9; i++ ) {
            // row
            if ( this.data.get(row).get(i).contains(number) ) {
                this.data.get(row).get(i).remove((Integer) number);
            }

            // column
            if ( this.data.get(i).get(column).contains(number) ) {
                this.data.get(i).get(column).remove((Integer) number);
            }
        }

        int rowStart = row/3;
        int colStart = column/3;
        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                if ( this.data.get(rowStart+i).get(colStart+j).contains(number) ) {
                    this.data.get(rowStart+i).get(colStart+j).remove((Integer) number);
                }
            }
        }

        this.repaint();
    }

    /**
     * method to add a comment used to add a new comment to the data that
     * will be later displayed
     * @param number the number of the comment to be added
     * @param row the row describing the commented sudoku cell
     * @param column the column describing the commented sudoku cell
     */
    public void addComment( int number, int row, int column ) {
        if ( !this.data.get(row).get(column).contains(number) ) {
            if ( !this.commentChecker(number, row, column) ) return;
            this.data.get(row).get(column).add(number);
            this.repaint();
        }
    }

    /**
     * method used to clear the comments for the given cell
     * for example whenever you input a number there
     * @param row row of the grid cell that we want to clear
     * @param column column of the grid cell that we want to clear
     */
    public void clearComment(int row, int column) {
        this.data.get(row).get(column).clear();
    }

    /**
     * method to check if the given comment makes sense,
     * meaning that the current board doesn't already have
     * that number in it, and they would collide
     * @param number the number that the user comments
     * @param row row of the cell the user comments
     * @param column column of the cell the user comments
     * @return true if it makes sense, otherwise false
     */
    private boolean commentChecker(int number, int row, int column) {
        int[][] currBoard = this.game.getCurrBoard();
        for ( int i = 0; i < 9; i++ ) {
            // row
            if( currBoard[row][i] == number ) {
                return false;
            }

            // column
            if ( currBoard[i][column] == number ) {
                return false;
            }
        }

        int rowStart = row/3;
        int colStart = column/3;
        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                if ( currBoard[rowStart+i][colStart+j] == number ) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * a generator for an gridPositions array that will be used to
     * determine the next positions of the comments
     * @return a int[][] array of positions where arr[i][0] is x value of position
     * and arr[i][y] is y value of position
     */
    private static int[][] gridPositionsGenerator() {
        int cellSize = Game.boardSize/9;
        int gridSize = cellSize/3;

        int[][] res = {
                {0, 0},
                {gridSize, 0},
                {2*gridSize, 0},
                {0, gridSize},
                {gridSize, gridSize},
                {2*gridSize, gridSize},
                {0, 2*gridSize},
                {gridSize, 2*gridSize},
                {2*gridSize, 2*gridSize}
        };

        return res;
    }

    /**
     * method used to set comments mode on/off
     * @param nBoolean the new value of comments mode
     */
    public void setCommentsMode(boolean nBoolean) { this.commentsMode = nBoolean; }

    /**
     * getter for the boolean commentsMode, which determines
     * if the comments mode is on
     * @return this.commentsMode of type boolean
     */
    public boolean getCommentsMode() { return this.commentsMode; }
}
