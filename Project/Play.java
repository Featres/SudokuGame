package Project;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/**
 * class with main function of the project
 */
public class Play {
    /**
     * easily accessible screen size width and height
     */
    static int screenWidth;
    static int screenHeight;

    /**
     * updating screen size and opening the game
     */
    public static void main(String[] args) {

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int)size.getWidth();
        screenHeight = (int)size.getHeight();

        new Menu();
    }

    /**
     * method that is useful to inform the user about something
     * a JOptionPane window pops up with the given message
     * @param message message that we will be shown the user
     */
    public static void message(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message);
    }


    /**
     * method used when creating the sudoku board to determine
     * the difficulty of the board
     * @return a integer value:
     *      0 - easy,
     *      1 - medium,
     *      2 - hard
     */
    public static int difficultyAsker() {
        return JOptionPane.showOptionDialog(new JFrame(), "Choose difficulty:", "Set up Your Sudoku", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] {"Easy (~45 cells)", "Medium (~35 cells)", "Hard (~25 cells)"}, 1);
    }
}


/**
 * menu window pops up when user opens the game.
 * - background as a photo
 * - game title
 */
class Menu extends JFrame {
    Menu() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Game of Sudoku");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        try {
            // C:\Users\piort\OneDrive\Documents\GitHub\SudokuGame\Project\Images\MenuBackground.png
            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("Images/BackGround/MenuBackground.png")))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.add(new MenuLabel(this));

        String logoPath = "Images/Logo/Logo.png";
        ImageIcon logo1 = new ImageIcon(logoPath);
        Image logo = logo1.getImage();
        this.setIconImage(logo);

        this.setVisible(true);
    }

}

/**
 * label used to control all the labels on menu panel
 */
class MenuLabel extends JLabel {
    private final JFrame frame;
    MenuLabel(JFrame frame) {
        this.frame = frame;
        this.setBounds(0,0, Play.screenWidth, Play.screenHeight);
        this.add(new TitleLabel());
        this.add(new PlayLabel(this, this.frame));

        boolean showInstructions = false;
        if ( showInstructions ) this.add(new InstructionLabel());
    }
}

/**
 * title label added to menu of the game. white label with text "SUDOKU"
 */
class TitleLabel extends JLabel {
    TitleLabel() {
        this.setBounds((int)(Play.screenWidth/12), (int)(Play.screenHeight/10), (int)(Play.screenWidth/6), (int)(Play.screenHeight/14));
        this.setText("SUDOKU");
        this.setFont(new Font("SansSerif", Font.BOLD, 65));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setBackground(Color.WHITE);
        this.setOpaque(true);
        LineBorder line = new LineBorder(Color.BLACK, 2, true);
        this.setBorder(line);
    }
}

/**
 * play button for the game added to the menu - starts the game
 */
class PlayLabel extends JLabel {
    PlayLabel(JLabel label, JFrame frame) {
        this.setBounds((int)(Play.screenWidth/10), (int)(Play.screenHeight/3), (int)(Play.screenWidth/10), (int)(Play.screenHeight/18));
        this.setText("Play");
        this.setFont(new Font("SansSerif", Font.BOLD, 45));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setBackground(Color.WHITE);
        this.setOpaque(true);
        this.addMouseListener(new ClickListener(frame, label));
        LineBorder line = new LineBorder(Color.GRAY, 1, true);
        this.setBorder(line);
    }
}

/**
 * in menu there will be instructions for the game displayed. to be implemented
 */
class InstructionLabel extends JLabel {
    InstructionLabel() {
        this.setBounds((int)(Play.screenWidth*0.55), (int)(Play.screenHeight*0.125), (int)(Play.screenWidth*0.3125), (int)(Play.screenHeight*0.45));
        this.setText("Instructions");
        this.setFont(new Font("SansSerif", Font.BOLD, 35));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.TOP);
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        InstructionText instructionText = new InstructionText(this);
        this.add(instructionText);

        this.setOpaque(true);
        this.setVisible(true);
    }

    /**
     * class that will be responsible for all the text
     * in the instruction label
     */
    static class InstructionText extends JLabel {
        public InstructionText(InstructionLabel instructionLabel) {
            int width = instructionLabel.getWidth();
            int height = instructionLabel.getHeight();

            Font myFont = new Font("SansSerif", Font.PLAIN, 20);
            this.setFont(myFont);

            this.setBounds(0, (int)(height*0.1),
                    width, (int)(height*0.8));
            this.setVerticalAlignment(CENTER);
            this.setHorizontalAlignment(CENTER);

            String[] lines = { "\n                                            by Piotr Marciniak",
                                "This is a simple program to allow the user to play a game of ",
                                "sudoku. I will not explain the rules since, they are general ",
                                " knowledge. You have got a timer that measures Your time. You ",
                                "also have a Counter, that gives you information about the amount ",
                                "of different digits on the board. You can always use a brush, ",
                                "when you click on it and confirm that You want to use it, You ",
                                "can erase any inputted number just by clicking on it (only the ",
                                "numbers, that you added, not from the starting puzzle). You also ",
                                "have a hint, if You take it, You can click on any empty cell and ",
                                "the correct number will be added. On top of that You can use bot, ",
                                "which will automatically finish Your current problem. ",
                                "Please note: You can run into a wrong way of solving, and then ",
                                "Your board doesn't have a solution. Then the Hint and the Bot ",
                                "won't work. "};
            String text = "<html>";
            for ( String line : lines ) text += "  " + line + "<br>";
            text += "</html>";
            this.setText(text);

            this.setOpaque(false);
            this.setVisible(true);
        }
    }
}

/**
 * click listener for play label to trigger the start of the game
 */
class ClickListener implements MouseListener {
    private final JLabel label;
    private final JFrame frame;
    ClickListener(JFrame frame, JLabel label) {
        this.label = label;
        this.frame = frame;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int userChoice = Play.difficultyAsker();

        this.label.setVisible(false);
        Game newGame = null;
        try {
            newGame = new Game(this.frame, userChoice);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.frame.add(newGame);
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
