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

//        this.setBounds(0,0,Game.boardSize,Game.boardSize);
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

        if ( Calculator.checkRow(currBoard, row) ) {
            int x = row*gridSize + gridSize/2;
            int y1 = gridSize/2;
            int y2 = (int)(gridSize*8.5);
            Flare2D myFlare = new Flare2D(this, y1, x, y2, x);
            this.toDoList.add(myFlare);
        }
        if ( Calculator.checkColumn(currBoard, column) ) {
            int x1 = gridSize/2;
            int x2 = (int) (gridSize*8.5);
            int y = column*gridSize + gridSize/2;
            Flare2D myFlare = new Flare2D(this, y, x1, y, x2);
            this.toDoList.add(myFlare);
        }
        if ( Calculator.checkSquare(currBoard, row, column) ) {
            int xGrid = 3*((int)(column/3));
            int yGrid = 3*((int)(row/3));

            int x1 = xGrid*gridSize;
            int y1 = yGrid*gridSize;
            int x2 = xGrid*gridSize + 3*gridSize;
            int y2 = yGrid*gridSize + 3*gridSize;

            Flare2D myFlare1 = new Flare2D(this, x1, y1, x2, y2);
            Flare2D myFlare2 = new Flare2D(this, x1, y2, x2, y1);

            this.toDoList.add(myFlare1);
            this.toDoList.add(myFlare2);
        }

        this.repaint();
    }

    /**
     * paint function to draw all the flares in the toDoList
     * and then clear it
     * @param g  the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for ( Flare2D flare : this.toDoList ) {
            flare.draw(g2);
        }
    }

    /**
     * method to be called whenever you need to remove given flare
     * from the toDoList of the correctFlare object variable
     * @param flare flare object to be removed
     */
    public void removeFlare(Flare2D flare) {
        this.toDoList.remove(flare);
        this.repaint();
    }
}

/**
 * a class that will be used to make changes to the Line2D, so that
 * it gets more needed functionality
 */
class Flare2D extends Line2D.Double {
    private final Timer myTimer;
    private final CorrectFlare correctFlare;
    private int x1;
    private int y1;
    private  int x2;
    private int y2;

    private int diffX = -1;
    private int diffY = -1;
    private final int delay = 5;

    public Flare2D(CorrectFlare correctFlare, int x1, int y1, int x2, int y2) {
        super(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
        this.correctFlare = correctFlare;

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        int delay = this.delay;
        TimerActionListener timerAL = new TimerActionListener(this);
        this.myTimer = new Timer(delay, timerAL);
        myTimer.setRepeats(true);
        this.myTimer.start();
    }

    /**
     * getter for the correct flare object
     * @return this.correctFlare of type CorrectFlare
     */
    public CorrectFlare getCorrectFlare() { return this.correctFlare; }

    /**
     * method to draw the flare
     * @param g2 a graphics 2d object
     */
    public void draw(Graphics2D g2) {
        int x1 = this.x1;
        int y1 = this.y1;
        int x2 = this.x2;
        int y2 = this.y2;

        Point2D first = new Point2D.Double(x1, y1);
        Point2D second = new Point2D.Double(x2, y2);

        Line2D copy = new Line2D.Double(first, second);

        g2.setColor(Color.GREEN);
        g2.setStroke(new BasicStroke(10));
        g2.draw(copy);
        g2.setColor(Color.BLACK);
    }

    /**
     * method to set new points/coordinates for the given flare
     * using four coordinates
     * @param x1 x coordinate of first point
     * @param y1 y coordinate of first point
     * @param x2 x coordinate of second point
     * @param y2 y coordinate of second point
     */
    public void setPoints(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * override of toString for better debugging
     * @return an easier to read debug
     */
    @Override
    public String toString() {
        String res = "flare";

        String x1 = String.valueOf(this.x1);
        String y1 = String.valueOf(this.y1);
        res += "("+x1+", "+y1+")";

        String x2 = String.valueOf(this.x2);
        String y2 = String.valueOf(this.y2);
        res += "("+x2+", "+y2+")";

        return res;
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
            int x1 = this.flare.x1;
            int y1 = this.flare.y1;
            int x2 = this.flare.x2;
            int y2 = this.flare.y2;

            Point2D firstPoint = new Point2D.Double(x1, y1);
            Point2D secondPoint = new Point2D.Double(x2, y2);

            final int maximum = 10;
            if ( calcDistance(firstPoint, secondPoint) < maximum ) {
                this.flare.myTimer.stop();
                CorrectFlare correctFlare = this.flare.getCorrectFlare();
                correctFlare.removeFlare(this.flare);
                return;
            }

            int lenX = Math.abs(x1 - x2);
            int lenY = Math.abs(y1 - y2);

            final double diff = 0.002;

            int diffX = Math.max((int)(lenX * diff), 1);
            if ( this.flare.diffX == -1 ) this.flare.diffX = diffX;
            else diffX = this.flare.diffX;

            int diffY = Math.max((int)(lenY* diff), 1);
            if ( this.flare.diffY == -1 ) this.flare.diffY = diffY;
            else diffY = this.flare.diffY;

            if ( x1 < x2 ) {
                x1 += diffX;
                x2 -= diffX;
            } else {
                x1 -= diffX;
                x2 += diffX;
            }

            if ( y1 < y2 ) {
                y1 += diffY;
                y2 -= diffY;
            } else {
                y1 -= diffY;
                y2 += diffY;
            }

            this.flare.setPoints(x1, y1, x2, y2);

            CorrectFlare correctFlare = this.flare.getCorrectFlare();
            correctFlare.repaint();
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
