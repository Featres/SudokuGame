package Project;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * class that will manage all the labels around the sudoku board
 */
class FunctionalPanel extends JPanel {
    public static final int fontSize = Play.screenWidth/90;
    private final JFrame frame;
    private final Game game;
    private final SideCounter sideCounter;
    private final TimerLabel timerLabel;
    private final HelpLabel.InfoLabel infoLabel;
    private boolean able;
    public FunctionalPanel(Game game) {
        this.frame = game.getFrame();
        this.game = game;
        this.able = true;
        this.setLayout(null);

        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();

        this.setBounds(0, 0, frameWidth, frameHeight);

        IAmDoneLabel iAmDoneLabel = new IAmDoneLabel(this);
        this.add(iAmDoneLabel);

        SideCounter sideCounter = new SideCounter(this);
        this.add(sideCounter);
        this.sideCounter = sideCounter;

        TimerLabel timerLabel = new TimerLabel(this);
        this.add(timerLabel);
        this.timerLabel = timerLabel;

        BotLabel botLabel = new BotLabel(this);
        this.add(botLabel);

        BackToMainMenu backToMainMenu = new BackToMainMenu(this);
        this.add(backToMainMenu);

        HintLabel hintLabel = new HintLabel(this);
        this.add(hintLabel);

        BrushLabel brushLabel = new BrushLabel(this);
        this.add(brushLabel);

        CommentsLabel commentsLabel = new CommentsLabel(this);
        this.add(commentsLabel);

        HelpLabel.InfoLabel infoLabel = new HelpLabel.InfoLabel();
        this.add(infoLabel, 0);
        this.infoLabel = infoLabel;

        HelpLabel helpLabel = new HelpLabel(this);
        this.add(helpLabel);

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
     * @return current game class object
     */
    public Game getGame() { return this.game; }

    /**
     * method for an easier access of updating the side
     * counter numbers
     */
    public void updateSideCounter() { this.sideCounter.updateNumDataLabel(); }

    /**
     * method to set able of the panel
     * if its true it all works correctly (default)
     * when changed to false, (for example after bot solution),
     * it makes some functions (mouse listeners mostly) unreachable,
     * because they aren't needed (I am ready after bot...)
     * @param nAble boolean informing about availability, false paralyzed some functions
     */
    public void setAble(boolean nAble) { this.able = nAble; }

    /**
     * getter for the able boolean
     * @return boolean functionalPanel. able
     */
    public boolean getAble() { return this.able; }

    /**
     * getter for the infoLabel object
     * @return this.infoLabel of class HelpLabel.InfoLabel
     */
    public HelpLabel.InfoLabel getInfoLabel() { return this.infoLabel; }

    /**
     * method used for easier access of the method stop timer
     * in TimerLabel that stops the timer when the game ends
     */
    public void stopTimerTimerLabel() { this.timerLabel.stopTimer(); }

    /**
     * label that will be a button for the user to click,
     * whenever he is done and needs help from the algorithm,
     * to finish his work
     */
    static class BotLabel extends JLabel {
        public BotLabel(FunctionalPanel nPanel) {
            this.setBackground(Color.CYAN);
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
            this.setText("Help me! Let the bot finish");
            this.setHorizontalAlignment(CENTER);
            this.setVerticalAlignment(CENTER);
            this.setOpaque(true);

            Font myFont = new Font("Comic Sans", Font.PLAIN, FunctionalPanel.fontSize);
            this.setFont(myFont);

            int panelWidth = nPanel.getWidth();
            int panelHeight = nPanel.getHeight();
            this.setBounds((int)(panelWidth*0.1), (int)(panelHeight*0.75), (int)(panelWidth*0.2), (int)(panelHeight*0.07));

            BotSolvingMouseListener botSolvingMouseListener = new BotSolvingMouseListener(nPanel);
            this.addMouseListener(botSolvingMouseListener);

            this.setVisible(true);
        }

        /**
         * label that being triggered starts the input
         * of correct solution on the board
         * by the code in mouse listener
         */
        static class BotSolvingMouseListener implements MouseListener {
            private final FunctionalPanel panel;
            private final Game game;
            private final SudokuBoard sudokuBoard;
            BotSolvingMouseListener(FunctionalPanel nPanel) {
                this.panel = nPanel;

                this.game = this.panel.getGame();

                this.sudokuBoard = this.game.getSudokuBoard();
            }

            /**
             * firstly user is asked whether he is sure
             * then the current board is being read and a
             * goal board is created (solution)
             * (boards can have multiple solutions and users progress
             * might have influenced how the game will work
             * @param e the event to be processed
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                int[][] currBoard = this.game.getCurrBoard();
                boolean able = this.panel.getAble();

                if ( !able || Calculator.isFullyDone(currBoard) ) {
                    Play.message("Bot cannot perform the operations right now.");
                    return;
                }

                int areYouSure = JOptionPane.showOptionDialog(new JFrame(),
                        "Do you want the bot to solve the game for you?",
                        "Are you sure?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[] {"Yes, I am sure", "No, take me back"},
                        1);
                if ( areYouSure != 0 ) {
                    return;
                }
//                System.out.println("Running the bot");

                int[][] goalBoard = null;

                try {
                    int[][] currBoardCopy = new int[9][9];
                    for ( int i = 0; i < currBoardCopy.length; i++ ) {
                        for ( int j = 0; j < currBoardCopy[i].length; j++ ) {
                            currBoardCopy[i][j] = currBoard[i][j];
                        }
                    }
                    goalBoard = Calculator.calculateSudoku(currBoardCopy);
                } catch(Exception exception) {
                    Play.message("Your current sudoku board is impossible to solve, you made some errors ;(");
                    return;
                }

                int x = 0;
                int y = 0;
                while ( true ) {
                    this.game.setCurrBoard(currBoard);
                    if ( x == 9 ) {
                        x = 0;
                        y += 1;
                        if ( y == 9 ) break;
                    }

                    currBoard = this.game.getCurrBoard();
                    if ( currBoard[y][x] != 0 ) {
                        x++;
                        continue;
                    }

                    int tmp = goalBoard[y][x];
                    this.sudokuBoard.addNumber(y, x, tmp);

                    x++;
                }

                this.panel.setAble(false);
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

    /**
     * label that will show how much time it took the user
     */
    static class TimerLabel extends JLabel {
        private final Timer timer;
        public TimerLabel(FunctionalPanel panel) {
            TimerActionListener timerActionListener = new TimerActionListener(this);

            int delay = 1000; // in ms
            Timer myTimer = new Timer(delay, timerActionListener);
            myTimer.setRepeats(true);
            myTimer.start();

            this.timer = myTimer;

            this.setOpaque(true);
            this.setBackground(Color.WHITE);
            this.setText("00:00");
            this.setForeground(Color.BLACK);
            this.setHorizontalAlignment(CENTER);
            this.setVerticalAlignment(CENTER);

            this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,8));

            int panelWidth = panel.getWidth();
            int panelHeight = panel.getHeight();
            this.setBounds((int)(panelWidth*0.15), (int)(panelHeight*0.25), (int)(panelWidth*0.09), (int)(panelHeight*0.08));

            Font myFont = new Font("Comic Sans", Font.PLAIN, 2*FunctionalPanel.fontSize);
            this.setFont(myFont);

            this.setVisible(true);
        }

        /**
         * method used to stop the timer when the game ends
         */
        public void stopTimer() {
            this.timer.stop();
        }

        /**
         * action listener used for the time label,
         * has a variable of seconds, updates it every second,
         * and updates the text on the label
         */
        static class TimerActionListener implements ActionListener {
            private final JLabel showTimeLabel;
            private int seconds;
            TimerActionListener(JLabel nLabel) {
                this.showTimeLabel = nLabel;
                this.seconds = 0;
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                this.seconds += 1;
                // in 'mm:ss'
                String timeFormat = String.format("%02d", (int)(seconds/60)) + ":" + String.format("%02d", (seconds%60));
                this.showTimeLabel.setText(timeFormat);
            }
        }
    }

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
        ArrayList<JLabel> myLabels = new ArrayList<>();
        public SideCounter(FunctionalPanel panel) {
            this.game = panel.getGame();

            int[][] currBoard = game.getCurrBoard();

            this.numData = updateNumData(currBoard);

            int panelWidth = panel.getWidth();
            int panelHeight = panel.getHeight();

            int labelHeight = (int)(panelHeight*0.5);
            this.labelHeight = labelHeight;

            this.setBounds((int)(panelWidth*0.75), (int)(panelHeight*0.1), (int)(panelWidth*0.15), labelHeight);
            this.width = (int)(panelWidth*0.15);
            this.height = labelHeight;

            this.add(labels());

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

            Font titleFont = new Font("Comic Sans", Font.PLAIN, (int)(1.5*FunctionalPanel.fontSize));

            JLabel title = new JLabel("COUNTER", SwingConstants.CENTER);
            title.setBounds(0, 10, width, (int)(this.labelHeight/12));
            title.setFont(titleFont);
            title.setBackground(Color.RED);
            title.setForeground(Color.BLACK);
            title.setVisible(true);

            mainLabel.add(title);

            int width = this.width;
            int height = this.height;
            mainLabel.setBounds(0, 0, width, height);

            mainLabel.setBackground(Color.GRAY);
            mainLabel.setOpaque(true);

            final int XCORDINATE = 0;
            final int HEIGHTPART = (this.labelHeight /12);
            final int[] DATA = this.numData;

            for ( int i = 0; i < DATA.length; i++ ) {
                final int YCORDINATE = HEIGHTPART*(i+1)+15;
                final String s = comments[i] + DATA[i];

                Font myFont = new Font("Comic Sans MS", Font.PLAIN, FunctionalPanel.fontSize);

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
         * @param board current state of the board in the game
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
     * has a white, centered, big text "I am done"
     * positioned regarding frame size
     */
    static class IAmDoneLabel extends JLabel {
        public IAmDoneLabel(FunctionalPanel panel) {
            Font myFont = new Font("Comic Sans", Font.PLAIN, (int)(1.8*FunctionalPanel.fontSize));
            this.setFont(myFont);
            this.setHorizontalAlignment(SwingConstants.CENTER);
            this.setVerticalAlignment(SwingConstants.CENTER);
            this.setForeground(Color.WHITE);
            this.setText("I am done!");

            int panelWidth = panel.getWidth();
            int panelHeight = panel.getHeight();

            this.setBounds((int)(panelWidth*0.75), (int)(panelHeight*0.8), (int)(panelWidth*0.15), (int)(panelHeight*0.1));

            this.setBackground(Color.BLUE);

            IAmDoneListener myListener = new IAmDoneListener(panel);
            this.addMouseListener(myListener);

            this.setOpaque(true);
            this.setVisible(true);
        }
    }

    /**
     * mouse listener for I am done label in game view
     * whenever called, calls Calculator is done and complete
     * and gives feedback to the user
     */
    static class IAmDoneListener implements MouseListener {
        private final Game game;
        private final FunctionalPanel panel;
        public IAmDoneListener(FunctionalPanel panel) {
            this.panel = panel;
            this.game = this.panel.getGame();
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            boolean able = this.panel.getAble();
            if ( !able ) {
                Play.message("You can't perform this operation right now");
                return;
            }

            int[][] currBoard = game.getCurrBoard();

            boolean isDone = Calculator.isFullyDone(currBoard);
            boolean isDoneAndComplete = Calculator.isDoneAndComplete(currBoard);

            if ( !isDone ) Play.message("You didn't finish Your board yet! Good luck!");
            else if ( isDoneAndComplete ) {
//                System.out.println("Game ended");
                this.panel.stopTimerTimerLabel();

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

    static class BackToMainMenu extends JLabel {
        BackToMainMenu(FunctionalPanel panel) {
            int width = panel.getWidth();
            int height = panel.getHeight();

            this.setBounds(0,0, (int)(width*0.1), (int)(height*0.1));
            this.setText("Back to Menu");
            this.setVerticalAlignment(CENTER);
            this.setHorizontalAlignment(CENTER);
            this.setBackground(Color.WHITE);
            this.setOpaque(false);

            Font myFont = new Font("Comic Sans", Font.PLAIN, FunctionalPanel.fontSize);
            this.setFont(myFont);

            BackToMainMenuListener backToMainMenuListener = new BackToMainMenuListener(panel);
            this.addMouseListener(backToMainMenuListener);

            this.setVisible(true);
        }

        /**
         * class that will contain a listener
         * that will react on clicking 'back to main menu'
         */
        static class BackToMainMenuListener implements MouseListener {
            private final Game game;
            public BackToMainMenuListener(FunctionalPanel panel) {
                this.game = panel.getGame();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(new JFrame(), "If you return to main menu, you " +
                                "will lose all current progress.",
                        "Back to main menu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null);

                if ( choice == 0 ) {
                    JFrame mainFrame = this.game.getFrame();
                    mainFrame.dispose();

                    MusicPlayer currMusicPlayer = this.game.getMusicPlayer();
                    try {
                        currMusicPlayer.changeClipState("pause");
                    } catch (LineUnavailableException | IOException ex) {
                        ex.printStackTrace();
                    }

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

    /**
     * class that will be a label that will be
     * a hint for the user
     * - he will ask for a hint
     * - if he accepts
     * - he will click on a certain box
     * - the correct number pops
     */
    static class HintLabel extends JLabel {
        public HintLabel(FunctionalPanel panel) {
            int width = panel.getWidth();
            int height = panel.getHeight();

            this.setBounds((int)(width*0.2), (int)(height*0.65), (int)(width*0.1), (int)(height*0.07));
            this.setBackground(Color.ORANGE);
            this.setOpaque(true);
            this.setText("Get a HINT");

            Font myFont = new Font("Comic Sans", Font.PLAIN, FunctionalPanel.fontSize);
            this.setFont(myFont);

            this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
            this.setHorizontalAlignment(CENTER);
            this.setVerticalAlignment(CENTER);

            HintMouseListener hintMouseListener = new HintMouseListener(panel);
            this.addMouseListener(hintMouseListener);

            this.setVisible(true);
        }

        /**
         * class that will react on the clicking of the hint label
         * more detailed information in the label's documentation
         */
        static class HintMouseListener implements MouseListener {
            private final FunctionalPanel functionalPanel;
            public HintMouseListener(FunctionalPanel panel) {
                this.functionalPanel = panel;
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean questionWill = false;
                if ( questionWill ) {
                    int choice = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure to get a hint?",
                            "Question to get Hint", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null);
                    if ( choice == 1 ) return;
                }

                SudokuBoard sudokuBoard = this.functionalPanel.getGame().getSudokuBoard();
                NumberListener numberListener = sudokuBoard.getNumberListener();
                numberListener.setGettingAHint(true);

                Play.message("Click on the grid cell you want to get hint on");
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

    /**
     * label that will allow the user to brush off the
     * numbers that he wrote and doesn't want them anymore
     * - click on the label and accept it
     * - go into brush mode
     * - click on numbers to delete them (apart from starting positions)
     * - click on the label again to exit brush mode
     */
    static class BrushLabel extends JLabel {
        private boolean brushMode;
        private final NumberListener numberListener;
        public BrushLabel(FunctionalPanel panel) {
            this.brushMode = false;

            SudokuBoard sudokuBoard = panel.getGame().getSudokuBoard();
            this.numberListener = sudokuBoard.getNumberListener();

            BrushMouseListener brushML = new BrushMouseListener(this);
            this.addMouseListener(brushML);

            int width = panel.getWidth();
            int height = panel.getHeight();

            this.setBounds((int)(width*0.1), (int)(height*0.65), (int)(width*0.1), (int)(height*0.07));
            this.setBackground(Color.YELLOW);
            this.setOpaque(true);
            this.setText("Brush Mode");
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));


            Font myFont = new Font("Comic Sans", Font.PLAIN, FunctionalPanel.fontSize);
            this.setFont(myFont);
            this.setHorizontalAlignment(CENTER);
            this.setVerticalAlignment(CENTER);

            this.setVisible(true);
        }

        /**
         * method used to access and change the value of brush mode
         * will change BrushLabel.brushMode & NumberListener.brushMode
         * @param nBoolean the new value for the brushMode
         */
        public void setBrushMode(boolean nBoolean) {
            this.brushMode = nBoolean;
            this.numberListener.setBrushMode(nBoolean);
            if ( nBoolean ) this.setText("Brush ON");
            else this.setText("Brush Mode");
        }

        /**
         * getter for the brush mode, a boolean value, which
         * determines whether the user is in brush mode
         * @return this.brushMode of type boolean
         */
        public boolean getBrushMode() { return this.brushMode; }

        static class BrushMouseListener implements MouseListener {
            private final BrushLabel brushLabel;
            public BrushMouseListener(BrushLabel brushLabel) {
                this.brushLabel = brushLabel;
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if ( this.brushLabel.getBrushMode() ) {
                    this.brushLabel.setBrushMode(false);
                    return;
                }
                boolean questionWill = false;
                if ( questionWill ) {
                    int choice = JOptionPane.showConfirmDialog(new JFrame(), "Do you want to turn on Brush Mode?",
                            "Turning on Brush Mode", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null);
                    if ( choice == 1 ) return;

//                    Play.message("You are now in Brush Mode. You can click on the grid cells and remove" +
//                            " inputted values. To leave Brush Mode click on the Brush Mode button again." +
//                            " Note: you can't remove numbers from the starting grid!");
                }

                this.brushLabel.setBrushMode(true);
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

    static class CommentsLabel extends JLabel {
        private final FunctionalPanel panel;
        private final Comments comments;
        public CommentsLabel(FunctionalPanel panel) {
            this.panel = panel;
            Game game = this.panel.getGame();
            this.comments = game.getComments();

            this.setBackground(Color.RED);
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
            this.setText("Comments Mode");
            this.setHorizontalAlignment(CENTER);
            this.setVerticalAlignment(CENTER);
            this.setOpaque(true);

            Font myFont = new Font("Comic Sans", Font.PLAIN, FunctionalPanel.fontSize);
            this.setFont(myFont);

            int panelWidth = this.panel.getWidth();
            int panelHeight = this.panel.getHeight();
            this.setBounds((int)(panelWidth*0.1), (int)(panelHeight*0.55), (int)(panelWidth*0.2), (int)(panelHeight*0.07));

            CommentsMouseListener commentsMouseListener = new CommentsMouseListener(this);
            this.addMouseListener(commentsMouseListener);

            this.setVisible(true);
        }

        /**
         * getter for the comments object
         * @return this.comments of type Comments
         */
        public Comments getComments() { return this.comments; }

        static class CommentsMouseListener implements MouseListener {
            private final CommentsLabel commentsLabel;
            private final Comments comments;
            public CommentsMouseListener(CommentsLabel commentsLabel) {
                this.commentsLabel = commentsLabel;
                this.comments = commentsLabel.getComments();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if ( !commentsLabel.panel.getAble() ) return;

                if ( this.comments.getCommentsMode() ) {
                    this.comments.setCommentsMode(false);
                    this.commentsLabel.setText("Comments Mode");
                    return;
                }

                this.commentsLabel.setText("Comments Mode ON");
                this.comments.setCommentsMode(true);
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

    /**
     * label that will display a bit of help information for the user
     * about the game and the functionality
     */
    static class HelpLabel extends JLabel {
        private final InfoLabel helpInfo;
        public HelpLabel(FunctionalPanel panel) {
            helpInfo = panel.getInfoLabel();

            int width = panel.getWidth();
            int height = panel.getHeight();

            Font myFont = new Font("Comic Sans", Font.PLAIN, 2*FunctionalPanel.fontSize);
            this.setFont(myFont);
            this.setHorizontalAlignment(SwingConstants.CENTER);
            this.setVerticalAlignment(SwingConstants.CENTER);
            this.setForeground(Color.WHITE);
            this.setText("HELP");

            this.setBackground(Color.BLACK);
            this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
            this.setBounds((int)(width*0.75), (int)(height*0.65), (int)(width*0.15), (int)(height*0.1));

            HelpListener helpListener = new HelpListener(this);
            this.addMouseListener(helpListener);

            this.setOpaque(true);
            this.setVisible(true);
        }

        /**
         * method to set the parameter show, which determines if the
         * help info is displayed
         * @param nBoolean the new value of show
         */
        public void setShow(boolean nBoolean) { this.helpInfo.setShow(nBoolean); }

        static class HelpListener implements MouseListener {
            private final HelpLabel helpLabel;
            public HelpListener(HelpLabel helpLabel) {
                this.helpLabel = helpLabel;
            }
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) { this.helpLabel.setShow(true); }
            @Override
            public void mouseExited(MouseEvent e) { this.helpLabel.setShow(false); }
        }

        /**
         * a label that will display some text regarding instructions
         */
        static class InfoLabel extends JPanel {
            public InfoLabel() {

                this.setBounds(Play.screenWidth/4, Play.screenHeight/5,
                        Play.screenWidth/2, Play.screenHeight/2);
                this.setBackground(Color.WHITE);
                this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 15));
                this.setOpaque(true);

                Font myFont = new Font("Comic Sans", Font.PLAIN, FunctionalPanel.fontSize);
                String text = " Timer - Timer measures your time in mm:ss.\n" +
                        " Comments Mode - You can turn on comments mode and then whenever you add a number to a cell" +
                        " it appears as a note just for you. Note: it has to be a valid option for the given cell. " +
                        "Note: You can remove them with a brush.\n" +
                        " Brush Mode - as soon as You turn on brush mode, you can click on the numbers that You inputted" +
                        " and just delete them. Note: it doesn't work for the numbers that were the starting position.\n" +
                        " Hint - whenever you need a bit of help, You can ask a bot for a hint for the chosen cell. Note: " +
                        "if you already did some mistakes that make the sudoku impossible, the hint won't help you.\n" +
                        " Bot - whenever you feel like You are done and need help, You can ask a bot to solve the sudoku" +
                        " for you. Note: if you already did some mistakes that make the sudoku impossible, the bot won't " +
                        "help you. Note: some functionality is then blocked.\n"+
                        " Music Player - play/pause to turn the music on or off. Backwards for the previous song and " +
                        "forward for the next one.\n" +
                        " I am Done - as soon as You finish, click on that button to finish the game.";

                int rows = (int)(this.getHeight() / (1.25*myFont.getSize()));
                int columns = this.getWidth() / myFont.getSize();

                JTextArea helpInfo = new JTextArea(rows, columns);
                helpInfo.setLineWrap(true);
                helpInfo.setWrapStyleWord(true);
                helpInfo.setText(text);
                helpInfo.setBounds(0, 0, this.getWidth(), this.getHeight());
                helpInfo.setOpaque(true);
                helpInfo.setFont(myFont);

                helpInfo.setVisible(true);

                JScrollPane scrollHelp = new JScrollPane(helpInfo);
                helpInfo.setEditable(false);

                this.add(scrollHelp);
                this.setVisible(false);

            }

            /**
             * method to set the parameter show, which determines if the
             * help info is displayed
             * @param nBoolean the new value of show
             */
            public void setShow(boolean nBoolean) { this.setVisible(nBoolean); }
        }
    }
}
