package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class HighlightLabel extends JLabel {
    private boolean[] currentHighlight;
    public HighlightLabel() {
        this.currentHighlight = new boolean[] {false, false, false, false,
                                        false, false, false, false, false};

        this.setBounds(Game.boardX, (int)(Game.boardY/4),
                Game.boardSize, (int)(Game.boardSize/9));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        this.setBackground(Color.WHITE);

        this.setOpaque(true);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int width = this.getWidth()-1;
        int height = this.getHeight()-1;

        Point2D pointTop1 = new Point2D.Double(0, 0);
        Point2D pointTop2 = new Point2D.Double(width, 0);
        Line2D topLine = new Line2D.Double(pointTop1, pointTop2);

        Point2D pointBot1 = new Point2D.Double(0, height);
        Point2D pointBot2 = new Point2D.Double(width, height);
        Line2D bottomLine = new Line2D.Double(pointBot1, pointBot2);

        Line2D leftLine = new Line2D.Double(pointTop1, pointBot1);
        Line2D rightLine = new Line2D.Double(pointTop2, pointBot2);

        g2.setStroke(new BasicStroke(3));

        g2.draw(topLine);
        g2.draw(bottomLine);
        g2.draw(leftLine);
        g2.draw(rightLine);

        for ( int i = 1; i <= 9; i++ ) {

            int x = i*(int)(width/9);

            Point2D point1 = new Point2D.Double(x, 0);
            Point2D point2 = new Point2D.Double(x, height);

            Line2D line = new Line2D.Double(point1, point2);

            g2.draw(line);

            String num = String.valueOf(i);
            Font myFont = new Font("Comic Sans", Font.PLAIN, this.getHeight());
            g2.setFont(myFont);
            int numX = (x - (int)(width/9)) + width/50;
            int numH = height - width/90;
            System.out.println(num);
            g2.drawString(num, numX, numH);

        }


    }
}
