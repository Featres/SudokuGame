package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

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
    JFrame frame;
    Game(JFrame frame) {
        boardSize = (int)(Play.screenWidth/3);
        boardX = (int)(Play.screenWidth/3);
        boardY = (int)(Play.screenHeight/6);

        this.frame = frame;

        this.currBoard = Random.randomSudoku();

        this.setBounds(0,0, Play.screenWidth, Play.screenHeight);
        this.setVisible(true);
        this.add(new SudokuBoard());

        this.frame.add(this);
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
    SudokuBoard() {
        this.setBounds(Game.boardX, Game.boardY, Game.boardSize, Game.boardSize);
        this.setBackground(Color.WHITE);
        this.setOpaque(true); // for the testing
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        int size = Game.boardSize/9;
        //int startX = Game.boardX;
        //int startY = Game.boardY;
        int startX = 0;
        int startY = 0;

        g2.setStroke(new BasicStroke(6));
        g2.setColor(Color.BLACK);

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
            }
        }
    }
}


/**
 * label that will be added to the game, where the numbers will be displayed to be chosen
 */
class Numbers extends JPanel {
    Numbers() {

    }
}
