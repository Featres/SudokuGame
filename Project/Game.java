package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    //starting point of the board label
    static int boardX;
    // starting point of the board label
    static int boardY;
    // size of the sudoku board
    static int boardSize;
    // completed board used to current game
    int[][] currBoard;
    // board displayed at the beginning
    int[][] startingBoard;
    // current board updated while playing
    int[][] usersBoard;
    // percentage of the board being displayed to the user
    double difficulty;
    // users difficulty choice
    int userChoice;
    // main frame of the game
    JFrame frame;
    Game(JFrame frame, int userChoice) {


        boardSize = (int)(Play.screenWidth/3);
        System.out.println(boardSize);
        boardX = (int)(Play.screenWidth/3);
        boardY = (int)(Play.screenHeight/6);

        this.frame = frame;

        this.currBoard = Random.randomSudoku();
        System.out.println(Arrays.deepToString(this.currBoard));

        this.userChoice = userChoice;
        this.difficulty = customDifficulty(this.userChoice);
        System.out.println("Difficulty: "+difficulty);
        this.startingBoard = setUpStartingBoard(difficulty, this.currBoard);

        //TODO some kind of checker stuff (is fully done?, correctly?)
        this.usersBoard = this.startingBoard.clone();

        //TODO learn how to access position/placements of a certain grid cell

        this.setBounds(0,0, Play.screenWidth, Play.screenHeight);
        this.setVisible(true);
        this.add(new SudokuBoard(this.startingBoard, this));

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

//        System.out.println(Arrays.deepToString(board));
        System.out.println("Starting board: "+Arrays.deepToString(startingBoard));

        return startingBoard;
    }

    /**
     * method to change users choice in Joptionpane to a actual difficulty
     * @param userChoice - users choice in JOptionPane difficulty window
     * @return - double of percentage of shown digits
     */
    private double customDifficulty(int userChoice) {
        switch(userChoice) {
            case 0:
                return Math.random()/5 + 0.45;
            case 1:
                return Math.random()/5 + 0.30;
            case 2:
                return Math.random()/5 + 0.20;
            default:
                return 0.3d;
        }
    }

    /**
     * changes the board and adds a number on it
     * @param row - row on the board to add a number
     * @param column - column on the board to add a number
     * @param number - number to add
     */
    public void addNumber(int row, int column, int number) {
        this.usersBoard[row][column] = number;
        this.repaint();
    }

    /**
     * getter for currently stated board
     * @return - users board
     */
    public int[][] getCurrBoard() {
        return this.usersBoard;
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
    private final Game boardLabel;
    SudokuBoard(int[][] nStartingBoard, Game label) {
        this.setBounds(Game.boardX, Game.boardY, Game.boardSize, Game.boardSize);
        this.setBackground(Color.WHITE);
        this.setOpaque(true); // for the testing

        this.boardLabel = label;

        MouseListener nl = new NumberListener(this);
        this.addMouseListener(nl);

        this.startingBoard = nStartingBoard;
    }

    /**
     * changes the board and adds a number on it and calls a brother method in Game
     * @param row - row on the board to add a number
     * @param column - column on the board to add a number
     * @param number - number to add
     */
    public void addNumber(int row, int column, int number) {
        this.startingBoard[row][column] = number;
        this.boardLabel.addNumber(row, column, number);
    }
    /**
     * checks if its legal to add a ceratin number to a certain spot
     * @param row - row that we want to add
     * @param column - column that we want to add
     * @param number - number that we want to add
     * @return true if possible, else false
     */
    public boolean canAddNumber(int row, int column, int number) {
        int[][] tempBoard = this.boardLabel.getCurrBoard().clone();
        tempBoard[row][column] = number;
        return Calculator.isCompleted(tempBoard);
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
                    g2.drawString(String.valueOf(this.startingBoard[column][row]), startX + currX + 10, startY + currY + size - 5);
                }
            }
        }
    }
}

class NumberListener implements MouseListener {
    private SudokuBoard sudokuBoard;
    NumberListener(SudokuBoard sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        //TODO set uneditable boxes (starting board)
        int xCLick = e.getX();
        int yClick = e.getY();

        int xGrid = (int) ((9*xCLick)/Game.boardSize);
        int yGrid = (int) ((9*yClick)/Game.boardSize);

        JFrame askFrame = new JFrame("Choose number");
        askFrame.setLayout(new GridLayout(3,3));

        for ( int i = 1; i <= 9; i++ ) {
            JButton currButton = new JButton(String.valueOf(i));
            final int num = i;
            currButton.addActionListener(e1 -> {
                if ( this.sudokuBoard.canAddNumber(yGrid, xGrid, num) ) {
                    this.sudokuBoard.addNumber(yGrid, xGrid, num);
                    askFrame.dispose();
                } else {
                    Play.message("You cannot use that number here");
                    //TODO maybe some animation of colliding number
                    askFrame.dispose();
                }

            });
            askFrame.add(currButton);
        }

        askFrame.setSize(250,250);
        askFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        askFrame.setVisible(true);
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
