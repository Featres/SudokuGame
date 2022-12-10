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
            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("Project/Images/MenuBackground.png")))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.add(new MenuLabel(this));

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
        this.add(new InstructionLabel());
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
    private final JLabel label;
    private final JFrame frame;
    PlayLabel(JLabel label, JFrame frame) {
        this.frame = frame;
        this.label = label;
        this.setBounds((int)(Play.screenWidth/10), (int)(Play.screenHeight/3), (int)(Play.screenWidth/10), (int)(Play.screenHeight/18));
        this.setText("Play");
        this.setFont(new Font("SansSerif", Font.BOLD, 45));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setBackground(Color.WHITE);
        this.setOpaque(true);
        this.addMouseListener(new ClickListener(this.frame, this.label));
        LineBorder line = new LineBorder(Color.GRAY, 1, true);
        this.setBorder(line);
    }
}

/**
 * in menu there will be instructions for the game displayed. to be implemented
 */
class InstructionLabel extends JLabel {
    InstructionLabel() {
        this.setBounds((int)(Play.screenWidth*5/8), (int)(Play.screenHeight/8), (int)(Play.screenWidth*5/16), (int)(Play.screenHeight/4));
        this.setText("Instructions");
        this.setFont(new Font("SansSerif", Font.BOLD, 35));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.TOP);
        this.setBackground(Color.WHITE);
        this.setOpaque(true);
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
        System.out.println("WoW");
        this.label.setVisible(false);
        this.frame.add(new Game());

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
