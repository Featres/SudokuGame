package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * a class that will be responsible for doing green flares
 * whenever user completes a row/column/square
 * - object will be added
 * - update function called every time a new number is added
 * - if the 'checker' finds a competition, paint it
 */
public class CorrectFlare extends JPanel {
    private final Game game;
    private final ArrayList<Flare2D> toDoList;
    public CorrectFlare(Game game) {
        this.game = game;
        this.toDoList = new ArrayList<Flare2D>();

        this.setBounds(Game.boardX, Game.boardY, Game.boardSize, Game.boardSize);
        this.setOpaque(false);

        this.setVisible(true);
    }

    /**
     * function used to update and check the board
     * for any row/column/square competition. Will check
     * if there are any and if so, call repaint with the flare
     */
    public void updateCorrectFlare(int row, int column) {
        int[][] currBoard = this.game.getCurrBoard();
        int gridSize = (int)(Game.boardSize/9);
        int boardX = Game.boardX;
        int boardY = Game.boardY;

        // TODO this!
        if ( Calculator.checkRow(currBoard, row) ) {
            // mark row...
        }
        if ( Calculator.checkColumn(currBoard, column) ) {
            // mark column...
        }
        if ( Calculator.checkSquare(currBoard, row, column) ) {
            // mark square...
        }
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
    }
}

/**
 * a class that will be used to make changes to the Line2D, so that
 * it gets more needed functionality
 */
class Flare2D extends Line2D.Double {
    private final Timer myTimer;
    public Flare2D(int x1, int y1, int x2, int y2) {
        super(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));

        int delay = 10;
        TimerActionListener timerAL = new TimerActionListener(this);
        this.myTimer = new Timer(delay, timerAL);
        myTimer.setRepeats(true);
        this.myTimer.start();
    }



    /**
     * action listener that will lower the size of the
     */
    static class TimerActionListener implements ActionListener {
        private final Flare2D flare;
        TimerActionListener(Flare2D flare) {
            this.flare = flare;
        }

        /**
         * changes the points of flare by a given diff to each
         * other making the line shorter until it reaches the
         * maximum value
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Point2D firstPoint = flare.getP1();
            Point2D secondPoint = flare.getP2();

            final int maximum = 100;
            if ( calcDistance(firstPoint, secondPoint) < maximum ) {
                this.flare.myTimer.stop();
                return;
            }

            int x1 = (int) firstPoint.getX();
            int y1 = (int) firstPoint.getY();
            int x2 = (int) secondPoint.getX();
            int y2 = (int) secondPoint.getY();

            int lenX = Math.abs(x1 - x2);
            int lenY = Math.abs(y1 - y2);

            final double diff = 0.03;
            int diffX = (int)(lenX* diff);
            int diffY = (int)(lenY* diff);

            if ( x1 < x2 ) {
                x1 += diffX;
                x2 -= diffX;
            } else {
                x1 += diffX;
                x2 -= diffX;
            }

            if ( y1 < y2 ) {
                y1 += diffY;
                y2 -= diffY;
            } else {
                y1 += diffY;
                y2 -= diffY;
            }

            Point2D newFirstPoint = new Point2D.Double(x1, y1);
            Point2D newSecondPoint = new Point2D.Double(x2, y2);

            this.flare.setLine(x1, y1, x2, y2);
        }

        /**
         * a function to calculate the distance between two points
         * @param pointOne first point of type Point2D
         * @param pointTwo second points of type Point2D
         * @return distance between given points
         */
        private int calcDistance(Point2D pointOne, Point2D pointTwo) {
            int x1 = (int) pointOne.getX();
            int y1 = (int) pointOne.getY();

            int x2 = (int) pointTwo.getX();
            int y2 = (int) pointTwo.getY();

            if ( x1 == x2 && y1 == y2 ) return 0;

            int xDiff = Math.abs(x1 - x2);
            int yDiff = Math.abs(y1 - y2);

            int powerOfDistance = (int) (Math.pow(xDiff, 2) + Math.pow(yDiff, 2));

            return (int) Math.sqrt(powerOfDistance);
        }
    }
}
