package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Arrays;

/**
 * class where user plays the game
 * implementation:
 * - game board -
 * - on left side numbers from 1 - 9/button clear/...
 */
public class Game extends JLabel {
    static int boardX;
    static int boardY;
    static int boardSize;
    int[][] currBoard;
    int[][] startingBoard;
    int[][] usersBoard;
    JFrame frame;
    Game(JFrame frame) {
        boardSize = (int)(Play.screenWidth/3);
        boardX = (int)(Play.screenWidth/3);
        boardY = (int)(Play.screenHeight/6);

        this.frame = frame;

        this.currBoard = Random.randomSudoku();
        //TODO JOptionPane to ask for difficulty
        double difficulty = 0.5d;
        this.startingBoard = setUpStartingBoard(difficulty, this.currBoard);

        //TODO some kind of checker stuff (is fully done?, correctly?)
        this.usersBoard = this.startingBoard.clone();

        //TODO learn how to access position/placements of a certain grid cell

        this.setBounds(0,0, Play.screenWidth, Play.screenHeight);
        this.setVisible(true);
        this.add(new SudokuBoard(this.startingBoard));

        this.frame.add(this);
    }

    /**
     * method to decide which numbers and how many will be shown on starting grid
     * @param difficulty - probability/amount of number shown in range (0,1)
     *                   for example = 0.5 means that ~ 40 nums will be shown
     * @param board - whole, completed board
     * @return - board to be displayed, zeros on positions not to be displayed, actual numbers on other
     */
    private int[][] setUpStartingBoard(double difficulty, int[][] board) {
        int[][] startingBoard = new int[9][9];

        for ( int row = 0; row < 9; row++ ) {
            for ( int column = 0; column < 9; column++ ) {
                double currentProbability = Math.random();
                if ( difficulty > currentProbability ) {
                    startingBoard[row][column] = board[row][column];
                }
            }
        }

        System.out.println(Arrays.deepToString(board));
        System.out.println(Arrays.deepToString(startingBoard));

        return startingBoard;
    }

    /**
     *  a method that can access Games visibility
     * @param newVisible - boolean that games visibility is set to
     */
    public void setVisibility(boolean newVisible) {
        this.setVisible(newVisible);
    }
}

/**
 * label that will be added to the game, where the sudoku board will be displayed
 */
class SudokuBoard extends JPanel {
    private int[][] startingBoard;
    SudokuBoard(int[][] nStartingBoard) {
        this.setBounds(Game.boardX, Game.boardY, Game.boardSize, Game.boardSize);
        this.setBackground(Color.WHITE);
        this.setOpaque(true); // for the testing

        this.startingBoard = nStartingBoard;
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        int size = Game.boardSize/9;
        int startX = 0;
        int startY = 0;

        g2.setStroke(new BasicStroke(6));
        g2.setColor(Color.BLACK);

        int fontSize = 80;
        Font f = new Font("Comic Sans MS", Font.PLAIN, fontSize);
        g2.setFont(f);

        for ( int row = 0; row < 9; row++ ) {
            for ( int column = 0; column < 9; column++ ) {
                int currX = row*size;
                int currY = column*size;

                Point2D startPoint = new Point2D.Double(startX + currX, startY + currY);
                Point2D topRightPoint = new Point2D.Double(startX + currX + size, startY + currY);
                Point2D bottomLeftPoint = new Point2D.Double(startX + currX, startY + currY + size);
                Point2D bottomRightPoint = new Point2D.Double(startX + currX + size, startY + currY + size);

                Line2D topLine = new Line2D.Double(startPoint, topRightPoint);
                Line2D rightLine = new Line2D.Double(topRightPoint, bottomRightPoint);
                Line2D bottomLine = new Line2D.Double(bottomLeftPoint, bottomRightPoint);
                Line2D leftLine = new Line2D.Double(startPoint, bottomLeftPoint);

                g2.draw(topLine);
                g2.draw(rightLine);
                g2.draw(bottomLine);
                g2.draw(leftLine);

                if ( this.startingBoard[column][row] != 0 ) {
                    g2.drawString(String.valueOf(this.startingBoard[column][row]), startX + currX + size + 10, startY + currY + size - 5);
                }
            }
        }
    }
}

