package Project;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Arrays;

// TODO game finish
// TODO comments
// TODO green bars when completing a row/column/square
// TODO delete dashes from the documentation

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
    // positions that are uneditable - they are from starting grid
    int[][] startingPositions;
    // current board updated while playing
    int[][] usersBoard;
    // percentage of the board being displayed to the user
    double difficulty;
    // users difficulty choice
    int userChoice;
    // main frame of the game
    JFrame frame;
    private final SudokuBoard sudokuBoard;
    private final FunctionalPanel functionalPanel;
    private final MusicPlayer musicPlayer;
    private final CorrectFlare correctFlare;
    Game(JFrame frame, int userChoice)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        boardSize = (int)(Play.screenWidth/3);
        boardX = (int)(Play.screenWidth/3);
        boardY = (int)(Play.screenHeight/6);

        this.frame = frame;

        this.currBoard = Random.randomSudoku();
        System.out.println(Arrays.deepToString(this.currBoard));

        this.userChoice = userChoice;
        this.difficulty = customDifficulty(this.userChoice);
        System.out.println("Difficulty: "+difficulty);
        this.startingBoard = setUpStartingBoard(difficulty, this.currBoard);

        this.startingPositions = setUpStartingPositions(this.startingBoard);

        this.usersBoard = this.startingBoard.clone();

        this.setBounds(0,0, Play.screenWidth, Play.screenHeight);
        this.setVisible(true);

        CorrectFlare correctFlare = new CorrectFlare(this);
        this.add(correctFlare);
        this.correctFlare = correctFlare;


        SudokuBoard sudokuBoard = new SudokuBoard(this.startingBoard, this);
        this.add(sudokuBoard);
        this.sudokuBoard = sudokuBoard;

        FunctionalPanel functionalPanel = new FunctionalPanel(this);
        this.add(functionalPanel);
        this.functionalPanel = functionalPanel;

        MusicPlayer musicPlayer = new MusicPlayer(this);
        this.add(musicPlayer);
        this.musicPlayer = musicPlayer;

        JLabel musicPlayerTitle = new JLabel("Music Player", SwingConstants.CENTER);
        Font myFont = new Font("Comic Sans", Font.BOLD, 30);
        musicPlayerTitle.setFont(myFont);
        int width = this.getWidth();
        int height = this.getHeight();
        musicPlayerTitle.setBounds((int)(width*0.425), (int)(height*0.77), (int)(width*0.15), (int)(height*0.03));
        musicPlayerTitle.setOpaque(false);
        musicPlayerTitle.setVisible(true);
        this.add(musicPlayerTitle);

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
     * method to set starting positions of numbers, so that they are uneditable
     * @param board - starting board for the game
     * @return - poisitons that are uneditable
     */
    public int[][] setUpStartingPositions(int[][] board) {
        int count = 0;
        for ( int i = 0; i < 9; i++ ) {
            for ( int j = 0; j < 9; j++ ) {
                if ( board[i][j] != 0 ) count++;
            }
        }
        int[][] res = new int[count][2];
        count = 0;
        for ( int i = 0; i < 9; i++ ) {
            for ( int j = 0; j < 9; j++ ) {
                if ( board[i][j] != 0 ) {
                    res[count][0] = i;
                    res[count][1] = j;
                    count++;
                }
            }
        }
        System.out.println("Reserved positions: " + Arrays.deepToString(res));
        return res;
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
        this.correctFlare.updateCorrectFlare(row, column);
//        System.out.println("repaint called "+ Arrays.deepToString(this.usersBoard));
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
     * setter for the current board
     * @param nCurrBoard - new board, that is going to be set as current board in game object
     */
    public void setCurrBoard(int[][] nCurrBoard) { this.usersBoard = nCurrBoard; }

    /**
     * getter for the JFrame frame
     * @return - frame of the game
     */
    public JFrame getFrame() { return this.frame; }

    /**
     * getter for the sudoku board
     * @return - current, used sudoku board object
     */
    public SudokuBoard getSudokuBoard() { return this.sudokuBoard; }

    /**
     * getter for the functional panel
     * @return - current, used functional panel object
     */
    public FunctionalPanel getFunctionalPanel() { return this.functionalPanel; }

    /**
     * getter fot the music player object
     * @return this.musicPlayer of type MusicPlayer
     */
    public MusicPlayer getMusicPlayer() { return  this.musicPlayer; }
}

/**
 * label that will be added to the game, where the sudoku board will be displayed
 */
class SudokuBoard extends JPanel {
    private final int[][] startingBoard;
    private final Game boardLabel;
    private int[][] collidingPoints;
    private final NumberListener numberListener;
    SudokuBoard(int[][] nStartingBoard, Game label) {
        this.setBounds(Game.boardX, Game.boardY, Game.boardSize, Game.boardSize);
        this.setBackground(Color.WHITE);
        this.setOpaque(true); // for the testing

        this.boardLabel = label;

        NumberListener nl = new NumberListener(this, this.boardLabel);
        this.addMouseListener(nl);
        this.numberListener = nl;

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

        FunctionalPanel functionalPanel = this.boardLabel.getFunctionalPanel();
        functionalPanel.updateSideCounter();
    }
    /**
     * checks if its legal to add a certain number to a certain spot
     * @param row - row that we want to add
     * @param column - column that we want to add
     * @param number - number that we want to add
     * @return true if possible, else false
     */
    public boolean canAddNumber(int row, int column, int number) {
        int[][] reservedPositions = this.boardLabel.startingPositions.clone();
        for ( int[] position : reservedPositions ) {
            if ( row == position[0] && column == position[1] ) return false;
        }
        int[][] tempBoard = this.boardLabel.getCurrBoard().clone();
        tempBoard[row][column] = number;
        boolean allowance = Calculator.isCompleted(tempBoard);
        tempBoard[row][column] = 0;
        return allowance;
    }

    /**
     * paints the lines of the game board and the numbers from startingBoard
     * @param g  the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        final int SIZE = Game.boardSize/9;
        final int STARTX = 0;
        final int STARTY = 0;

        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLACK);

        int fontSize = 60;
        Font f = new Font("Comic Sans MS", Font.PLAIN, fontSize);
        g2.setFont(f);

        for ( int row = 0; row < 9; row++ ) {
            for ( int column = 0; column < 9; column++ ) {
                int currX = row*SIZE;
                int currY = column*SIZE;

                Point2D startPoint = new Point2D.Double(STARTX + currX, STARTY + currY);
                Point2D topRightPoint = new Point2D.Double(STARTX + currX + SIZE, STARTY + currY);
                Point2D bottomLeftPoint = new Point2D.Double(STARTX + currX, STARTY + currY + SIZE);
                Point2D bottomRightPoint = new Point2D.Double(STARTX + currX + SIZE, STARTY + currY + SIZE);

                Line2D topLine = new Line2D.Double(startPoint, topRightPoint);
                Line2D rightLine = new Line2D.Double(topRightPoint, bottomRightPoint);
                Line2D bottomLine = new Line2D.Double(bottomLeftPoint, bottomRightPoint);
                Line2D leftLine = new Line2D.Double(startPoint, bottomLeftPoint);

                g2.draw(topLine);
                g2.draw(rightLine);
                g2.draw(bottomLine);
                g2.draw(leftLine);

                // draw numbers
                if ( this.startingBoard[column][row] != 0 ) {
                    g2.drawString(String.valueOf(this.startingBoard[column][row]), STARTX + currX + 20, STARTY + currY + SIZE - 10 );
                }
            }
        }

        // lines around the board will be bigger
        g2.setStroke(new BasicStroke(15));

        Point2D startPoint = new Point2D.Double(STARTX, STARTY);
        Point2D topRightPoint = new Point2D.Double(STARTX + 9*SIZE, STARTY);
        Point2D bottomLeftPoint = new Point2D.Double(STARTX, STARTY + 9*SIZE);
        Point2D bottomRightPoint = new Point2D.Double(STARTX + 9*SIZE, STARTY + 9* SIZE);

        Line2D.Double topLine = new Line2D.Double(startPoint, topRightPoint);
        Line2D.Double rightLine = new Line2D.Double(topRightPoint, bottomRightPoint);
        Line2D.Double bottomLine = new Line2D.Double(bottomLeftPoint, bottomRightPoint);
        Line2D.Double leftLine = new Line2D.Double(startPoint, bottomLeftPoint);

        g2.draw(topLine);
        g2.draw(rightLine);
        g2.draw(bottomLine);
        g2.draw(leftLine);

        // lines around squares will be bigger
        g2.setStroke(new BasicStroke(6));
        for ( int i = 1; i <= 3; i++ ) {
            Point2D pointA = new Point2D.Double(STARTX + 3*SIZE*i, STARTY);
            Point2D pointB = new Point2D.Double(STARTX + 3*SIZE*i, STARTY + SIZE*9);

            Line2D lineA = new Line2D.Double(pointA, pointB);

            Point2D pointC = new Point2D.Double(STARTX, STARTY + 3*SIZE*i);
            Point2D pointD = new Point2D.Double(STARTX + SIZE*9, STARTY + 3*SIZE*i);

            Line2D lineB = new Line2D.Double(pointC, pointD);

            g2.draw(lineA);
            g2.draw(lineB);
        }

        int[][] collidingPos = this.collidingPoints;

        if ( collidingPos != null ) {
            g2.setColor(Color.RED);
            System.out.println(Arrays.deepToString(collidingPos));
            for ( int[] pos : collidingPos ) {
                int circleX = pos[0]*SIZE;
                int circleY = pos[1]*SIZE;

                Ellipse2D circle = new Ellipse2D.Double(circleX, circleY, SIZE, SIZE);

                g2.draw(circle);
            }
            int delay = 2000;
            CircleTimerActionListener circleTimerAL = new CircleTimerActionListener(this);

            Timer circleTimer = new Timer(delay, circleTimerAL);
            circleTimer.setRepeats(false);
            circleTimer.start();
        }

        g2.setColor(Color.BLACK);
        this.setCollidingPoints(null);
    }

    /**
     * class that will be used to 'hide' the circles after a reasonable time,
     * so they don't stay there forever
     */
    static class CircleTimerActionListener implements ActionListener{
        private final SudokuBoard sudokuBoard;
        public CircleTimerActionListener(SudokuBoard sudokuBoard) {
            this.sudokuBoard = sudokuBoard;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            SudokuBoard sudokuboard = this.sudokuBoard;
            sudokuboard.setCollidingPoints(null);

            Game myGame = sudokuboard.getGame();
            myGame.repaint();
        }
    }

    /**
     * setter for the colliding points data in sudokuboard
     * @param nCollidingPoints - the new value that the colliding points will be replaced with
     */
    public void setCollidingPoints(int[][] nCollidingPoints) { this.collidingPoints = nCollidingPoints; }

    /**
     * method that will be called every time that something
     * collides, it will run a searching algorithm and update the
     * colliding points variable
     * @param board - the sudoku board that the user is working on
     * @param num - number that the user tried to input
     * @param row - row that the user tried to input the number to
     * @param column - column that the user tried to input the number to
     */
    public void updateCollidingPoints(int[][] board, int num, int row, int column) {
        this.collidingPoints = Calculator.findCollidingPoints(board, num, row, column);
    }

    /**
     * getter for the current, used Game object
     * @return currents game Game data type object
     */
    public Game getGame() { return this.boardLabel; }

    /**
     * getter for the current, used NumberListener object
     * @return this.numberListener of type NumberListener
     */
    public NumberListener getNumberListener() { return this.numberListener; }
}

// listener used on board to add numbers
class NumberListener implements MouseListener {
    private final SudokuBoard sudokuBoard;
    private final Game game;
    private boolean gettingAHint;
    private boolean brushMode;
    NumberListener(SudokuBoard sudokuBoard, Game game) {
        this.sudokuBoard = sudokuBoard;
        this.game = game;
        this.gettingAHint = false;
        this.brushMode = false;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int xCLick = e.getX();
        int yClick = e.getY();

        // detect which grid cell was clicked
        int xGrid = (int) ((9*xCLick)/Game.boardSize);
        int yGrid = (int) ((9*yClick)/Game.boardSize);

        if ( gettingAHint ) {
            setGettingAHint(false);

            int[][] currBoard = this.game.getCurrBoard();
            if ( currBoard[yGrid][xGrid] != 0 ) {
                Play.message("You already have that grid figured out");
                return;
            }

            int[][] currBoardCopy = new int[9][9];
            for ( int i = 0; i < 9; i++ ) {
                for ( int j = 0; j < 9; j++ ) {
                    currBoardCopy[i][j] = currBoard[i][j];
                }
            }
            int[][] solutionBoard = null;
            try {
                solutionBoard = Calculator.calculateSudoku(currBoardCopy);
            } catch(Exception ex) {
                Play.message("Your current sudoku board is impossible to solve, you made some errors ;(");
                return;
            }

            int correctValue = solutionBoard[yGrid][xGrid];
            this.sudokuBoard.addNumber(yGrid, xGrid, correctValue);

            return;
        }

        int[][] startingPositions = this.game.startingPositions;

        if ( this.brushMode ) {
            for ( int[] position : startingPositions ) {
                if ( yGrid == position[0] && xGrid == position[1] ) {
                    Play.message("This is a starting cell - you can't brush it");
                    return;
                }
            }

            this.sudokuBoard.addNumber(yGrid, xGrid, 0);

            return;
        }

//        System.out.println("checking starting pos "+Arrays.deepToString(startingPositions));
        for ( int[] position : startingPositions ) {
            if ( yGrid == position[0] && xGrid == position[1] ) {
                Play.message("This is a starting cell - you can't edit it");
                return;
            }
        }

        // create a JFrame with buttons 1-9
        JFrame askFrame = new JFrame("Choose number");
        askFrame.setLayout(new GridLayout(3,3));

        for ( int i = 1; i <= 9; i++ ) {
            JButton currButton = new JButton(String.valueOf(i));
            final int num = i;
            currButton.addActionListener(event -> {
                // check if the chosen button's number is possible on that place in board
                if ( this.sudokuBoard.canAddNumber(yGrid, xGrid, num) ) {
                    this.sudokuBoard.addNumber(yGrid, xGrid, num);
                    askFrame.dispose();
                } else {
                    Play.message("You cannot use that number here");

                    int[][] board = this.game.getCurrBoard();
                    this.sudokuBoard.updateCollidingPoints(board, num, yGrid, xGrid);
                    this.game.repaint();

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

    /**
     * method to change the value of the variable getting a hint
     * @param nBoolean the new value of getting a hint
     */
    public void setGettingAHint(boolean nBoolean) { this.gettingAHint = nBoolean; }

    /**
     * method to change the value of the variable brush mode
     * @param nBoolean the new value of brush mode
     */
    public void setBrushMode(boolean nBoolean) { this.brushMode = nBoolean; }
}
