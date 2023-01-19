package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

// TODO add counter: counter of ones and so on
// TODO add timer
// TODO give up - bot finish

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
    private SudokuBoard sudokuBoard;
    private FunctionalPanel functionalPanel;
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

        this.startingPositions = setUpStartingPositions(this.startingBoard);

        this.usersBoard = this.startingBoard.clone();

        //TODO learn how to access position/placements of a certain grid cell

        this.setBounds(0,0, Play.screenWidth, Play.screenHeight);
        this.setVisible(true);

        SudokuBoard sudokuBoard = new SudokuBoard(this.startingBoard, this);
        this.add(sudokuBoard);
        this.sudokuBoard = sudokuBoard;

        FunctionalPanel functionalPanel = new FunctionalPanel(this);
        this.add(functionalPanel);
        this.functionalPanel = functionalPanel;

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
     * getter for the jframe frame
     * @return - frame of the game
     */
    public JFrame getFrame() { return this.frame; }

    /**
     *  a method that can access Games visibility
     * @param newVisible - boolean that games visibility is set to
     */
    public void setVisibility(boolean newVisible) {
        this.setVisible(newVisible);
    }

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
}

/**
 * label that will be added to the game, where the sudoku board will be displayed
 */
class SudokuBoard extends JPanel {
    private final int[][] startingBoard;
    private final Game boardLabel;
    SudokuBoard(int[][] nStartingBoard, Game label) {
        this.setBounds(Game.boardX, Game.boardY, Game.boardSize, Game.boardSize);
        this.setBackground(Color.WHITE);
        this.setOpaque(true); // for the testing

        this.boardLabel = label;

        MouseListener nl = new NumberListener(this, this.boardLabel);
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
            //TODO somehow mark those positions, that collide
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
    }

    /**
     * getter for the starting board
     * @return starting board
     */
    public int[][] getStartingBoard() { return this.startingBoard; }
}

// listener used on board to add numbers
class NumberListener implements MouseListener {
    private final SudokuBoard sudokuBoard;
    private final Game game;
    NumberListener(SudokuBoard sudokuBoard, Game game) {
        this.sudokuBoard = sudokuBoard;
        this.game = game;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int xCLick = e.getX();
        int yClick = e.getY();

        // detect which grid cell was clicked
        int xGrid = (int) ((9*xCLick)/Game.boardSize);
        int yGrid = (int) ((9*yClick)/Game.boardSize);

        int[][] startingPositions = this.game.startingPositions;
        System.out.println("checking starting pos "+Arrays.deepToString(startingPositions));
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
            currButton.addActionListener(e1 -> {
                // check if the chosen button's number is possible on that place in board
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

/**
 * class that will manage all the labels around the sudoku board
 */
class FunctionalPanel extends JPanel {
    private final JFrame frame;
    private final Game game;
    private SideCounter sideCounter;
    public FunctionalPanel(Game game) {
        this.frame = game.getFrame();
        this.game = game;
        this.setLayout(null);

        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();

        this.setBounds(0, 0, frameWidth, frameHeight);

        IAmDoneLabel iAmDoneLabel = new IAmDoneLabel(this);
        this.add(iAmDoneLabel);

        SideCounter sideCounter = new SideCounter(this);
        this.add(sideCounter);
        this.sideCounter = sideCounter;

        this.setOpaque(false);
        this.setVisible(true);
    }

    /**
     * getter for width
     * @return width of the object
     */
    public int getWidth() { return this.frame.getWidth(); }

    /**
     * getter for height
     * @return width of the object
     */
    public int getHeight() { return this.frame.getHeight(); }

    /**
     * getter for the game
     * @return - current game class object
     */
    public Game getGame() { return this.game; }

    /**
     * method for an easier access of updating the side
     * counter numbers
     */
    public void updateSideCounter() { this.sideCounter.updateNumDataLabel(); }

    /**
     * class/label that will show how many ones, twos etc. user already has in the board
     */
    static class SideCounter extends JLabel {
        private final static String[] comments = {"To be completed: ", "Ones: ", "Twos: ", "Threes: ", "Fours: ",
                "Fives: ", "Sixes: ", "Sevens: ", "Eights: ", "Nines: "};
        private final Game game;
        private int[] numData; //[to be completed, num of ones, num of twos...]
        private final int labelHeight;
        private final int width;
        private final int height;
        ArrayList<JLabel> myLabels = new ArrayList<JLabel>();
        public SideCounter(FunctionalPanel panel) {
            this.game = panel.getGame();

            int[][] currBoard = game.getCurrBoard();

            this.numData = updateNumData(currBoard);

            int panelWidth = panel.getWidth();
            int panelHeight = panel.getHeight();

            int labelHeight = (int)(panelHeight*0.5);
            this.labelHeight = labelHeight;

            // TODO do the looks of the label

            this.setBounds((int)(panelWidth*0.75), (int)(panelHeight*0.1), (int)(panelWidth*0.15), labelHeight);
            this.width = (int)(panelWidth*0.15);
            this.height = labelHeight;

            this.add(labels());

            Font titleFont = new Font("Comic Sans", Font.PLAIN, 35);

            JLabel title = new JLabel("COUNTER", SwingConstants.CENTER);
            title.setBounds(0, 0, width, (int)(this.labelHeight/12));
            title.setFont(titleFont);
            title.setBackground(Color.RED);
            title.setForeground(Color.BLACK);
            title.setVisible(true);

            this.add(title);

            // TODO make title work

            this.setOpaque(true);
            this.setVisible(true);
        }

        /**
         * paint all the data:
         * - free cells
         * - number of cells of every number
         */
        private JLabel labels() {
            JLabel mainLabel = new JLabel();

            int width = this.width;
            int height = this.height;
            mainLabel.setBounds(0, 0, width, height);

            mainLabel.setBackground(Color.GRAY);
            mainLabel.setOpaque(true);

            final int XCORDINATE = 0;
            final int LABELHEIGHT = this.labelHeight;
            final int HEIGHTPART = (int)(LABELHEIGHT/12);
            final int[] DATA = this.numData;

            for ( int i = 0; i < DATA.length; i++ ) {
                final int YCORDINATE = HEIGHTPART*(i+1);
                final String s = comments[i] + DATA[i];

                int fontSize = 20;
                Font myFont = new Font("Comic Sans MS", Font.PLAIN, fontSize);

                JLabel currLabel = new JLabel(s, SwingConstants.CENTER);
                currLabel.setFont(myFont);
                currLabel.setForeground(Color.BLACK);
                currLabel.setBackground(Color.GRAY);
                currLabel.setBounds(XCORDINATE, YCORDINATE, width, HEIGHTPART);
                currLabel.setVisible(true);

                myLabels.add(currLabel);

                mainLabel.add(currLabel);
            }

            mainLabel.setVisible(true);
            return mainLabel;
        }

        /**
         * method to refresh the data of amount of different values
         * @param board - current state of the board in the game
         * @return new, updated int[] numData array
         */
        private int[] updateNumData(int[][] board) {
            int[] result = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            for ( int[] row : board ) {
                for ( int num : row ) {
                    result[num]++;
                }
            }
            return result;
        }

        /**
         * void method called to repaint the label with numbers using
         * new, correct data about the numbers, should be called
         * whenever user changes anything on the board
         */
        public void updateNumDataLabel() {
            int[][] board = this.game.getCurrBoard();
            this.numData = updateNumData(board);
            int[] data = this.numData;

            for ( int i = 0; i < myLabels.size(); i++ ) {
                String s = comments[i] + data[i];

                myLabels.get(i).setText(s);
            }
        }
    }

    /**
     * label, that clicked by the user triggers the checker,
     * has a white, centered, big text "i am done"
     * positioned regarding frame size
     */
    static class IAmDoneLabel extends JLabel {
        public IAmDoneLabel(FunctionalPanel panel) {
            Font myFont = new Font("Comic Sans", Font.PLAIN, 44);
            this.setFont(myFont);
            this.setHorizontalAlignment(SwingConstants.CENTER);
            this.setVerticalAlignment(SwingConstants.CENTER);
            this.setForeground(Color.WHITE);
            this.setText("I am done!");

            int panelWidth = panel.getWidth();
            int panelHeight = panel.getHeight();

            this.setBounds((int)(panelWidth*0.75), (int)(panelHeight*0.8), (int)(panelWidth*0.15), (int)(panelHeight*0.1));

            this.setBackground(Color.BLUE);

            Game currGame = panel.getGame();
            IAmDoneListener myListener = new IAmDoneListener(currGame);
            this.addMouseListener(myListener);

            this.setOpaque(true);
            this.setVisible(true);
        }
    }

    /**
     * mouse listener for i am done label in gameview
     * whenever called, calls Calculator is done and complete
     * and gives feedback to the user
     */
    static class IAmDoneListener implements MouseListener {
        private final Game game;
        public IAmDoneListener(Game game) { this.game = game; }
        @Override
        public void mouseClicked(MouseEvent e) {
            int[][] currBoard = game.getCurrBoard();

            boolean isDone = Calculator.isFullyDone(currBoard);
            boolean isDoneAndComplete = Calculator.isDoneAndComplete(currBoard);

            // TODO game finish (better)
            if ( !isDone ) Play.message("You didn't finish Your board yet! Good luck!");
            else if ( isDoneAndComplete ) {
                System.out.println("Game ended");
                Play.message("Congrats! You won the game!!!");

                JFrame currFrame = this.game.getFrame();
                currFrame.dispose();

                new Menu();
            }

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
}
