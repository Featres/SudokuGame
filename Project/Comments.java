package Project;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Comments extends JLabel {
    private ArrayList<ArrayList<ArrayList<Integer>>> data;
    // data - 9 arraylists of 9 arraylists:
    // equal to 9 rows with 9 cells
    private final Game game;

    public Comments(Game game) {
        int boardX = Game.boardX;
        int boardY = Game.boardY;
        int boardSize = Game.boardSize;

        this.data = new ArrayList<>();
        for ( int i = 0; i < 9; i++ ) {
            ArrayList<ArrayList<Integer>> tmp = new ArrayList<>();
            for ( int j = 0; j < 9; j++ ) {
                tmp.add(new ArrayList<>());
            }
            this.data.add(tmp);
        }

        this.game = game;

        this.setBounds(boardX, boardY, boardSize, boardSize);
        this.setOpaque(false);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (this.data.get(row).get(col).size() == 0) continue;


            }
        }

    }
}
