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
 *
 */
public class Game extends JLabel {
    Game() {
        this.setBounds(0,0, Play.screenWidth, Play.screenHeight);
        this.add(new SudokuBoard());
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
class SudokuBoard extends JLabel {
    SudokuBoard() {
        this.setBounds((int)(Play.screenWidth/3), (int)(Play.screenHeight/6), (int)(Play.screenWidth/3), (int)(Play.screenWidth/3));
        this.setBackground(Color.WHITE);
        this.setOpaque(true); // for the testing

        BoardLines boardLines = new BoardLines((int)(Play.screenWidth/3), (int)(Play.screenHeight/6), (int)(Play.screenWidth/3));
        this.add(boardLines);
    }
}

/**
 * lines that will be used on sudoku board
 * @Params:
 * startX, startY - coordinates where the sudoku board starts
 * bound - length of a side of the board
 */
class BoardLines extends JComponent {
    private final int startX;
    private final int startY;
    private final int size;
    BoardLines(int startX, int startY, int bound) {
        this.startX = startX;
        this.startY = startY;
        this.size = bound/9;
        this.setBounds(this.startX, this.startY, bound, bound);
        System.out.println("Im here A");
    }

    @Override
    public void paintComponent(Graphics g) {
        System.out.println("Im here");

        Graphics2D g2 = (Graphics2D) g;

        this.drawMyself(g2);
    }

    private void drawMyself(Graphics2D g2) {
        for ( int row = 0; row < 9; row++ ) {
            for ( int column = 0; column < 9; column++ ) {
                int currX = row*this.size;
                int currY = column*this.size;

                Point2D startPoint = new Point2D.Double(this.startX + currX, this.startY + currY);
                Point2D topRightPoint = new Point2D.Double(this.startX + currX + this.size, this.startY + currY);
                Point2D bottomLeftPoint = new Point2D.Double(this.startX + currX, this.startY + currY + this.size);
                Point2D bottomRightPoint = new Point2D.Double(this.startX + currX + this.size, this.startY + currY + this.size);

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
class Numbers extends JLabel {
    Numbers() {

    }
}
