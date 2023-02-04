package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class HighlightLabel extends JLabel {
    private boolean[] currentHighlight;
    private final FunctionalPanel functionalPanel;
    public HighlightLabel(FunctionalPanel panel) {
        this.currentHighlight = new boolean[] {false, false, false, false,
                                        false, false, false, false, false};
        this.functionalPanel = panel;

        this.setBounds(Game.boardX, (int)(Game.boardY/4),
                Game.boardSize, (int)(Game.boardSize/9));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        this.setBackground(Color.WHITE);

        HighlightMouseListener highlightMouseListener = new HighlightMouseListener(this);
        this.addMouseListener(highlightMouseListener);

        this.setOpaque(true);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int width = this.getWidth()-1;
        int height = this.getHeight()-1;

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.BLACK);

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

        Font myFont = new Font("Comic Sans", Font.PLAIN, (int)(this.getHeight()*0.8));
        g2.setFont(myFont);
        for ( int i = 1; i <= 9; i++ ) {

            int x = i*(int)(width/9);

            Point2D point1 = new Point2D.Double(x, 0);
            Point2D point2 = new Point2D.Double(x, height);

            Line2D line = new Line2D.Double(point1, point2);

            g2.draw(line);

            String num = String.valueOf(i);

            int numX = (x - (int)(width/9)) + width/30;
            int numH = height - width/50;

            g2.drawString(num, numX, numH);

            if ( this.currentHighlight[i-1] ) {
                g2.drawOval(x-height, 0, height, height);
            }
        }
    }

    /**
     * method to change current highlight of the given
     * index, note that index 0 corresponds to number 1 etc.
     * @param index index of the number to be given (note method's docs)
     */
    public void changeCurrentHighlight(int index) {
        for ( int i = 0; i < this.currentHighlight.length; i++ ) {
            if ( i == index ) continue;
            this.currentHighlight[i] = false;
        }

        SudokuBoard sudokuBoard = this.functionalPanel.getGame().getSudokuBoard();
        if ( this.currentHighlight[index] ) {
            this.currentHighlight[index] = false;
            sudokuBoard.setHighlight(0);
        } else {
            this.currentHighlight[index] = true;
            sudokuBoard.setHighlight(index+1);
        }

        this.repaint();
    }

    /**
     * class responsible for reading clicks on highlight label
     * and changing the show/or not to show
     */
    static class HighlightMouseListener implements MouseListener {
        private final HighlightLabel label;
        public HighlightMouseListener(HighlightLabel label) { this.label = label; }
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mousePressed(MouseEvent e) {
            int width = this.label.getWidth()/9;
            int click = e.getX();

            int index = click/width;
            this.label.changeCurrentHighlight(index);
        }
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }

}
