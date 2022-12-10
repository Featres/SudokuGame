package Project;

import javax.swing.*;
import java.awt.*;

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
        this.add(new BoardLines());
    }
}

/**
 * lines that will be used on sudoku board
 */
class BoardLines extends JComponent {

}


/**
 * label that will be added to the game, where the numbers will be displayed to be chosen
 */
class Numbers extends JLabel {
    Numbers() {

    }
}
