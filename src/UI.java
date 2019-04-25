import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI extends JPanel {
    private Board board;

    private class ScrollableButtonGrid extends JPanel {
        private JScrollPane scrollPane;
        private Panel  gridPanel;

        public ScrollableButtonGrid(int w, int h) {
            gridPanel = new Panel();
            gridPanel.setLayout(new GridLayout(h, w));
            scrollPane = new JScrollPane(gridPanel);
            scrollPane.createHorizontalScrollBar();
            scrollPane.createVerticalScrollBar();
            add(scrollPane);
        }

        public void addToGrid(JButton btn) {
            gridPanel.add(btn);
            //scrollPane.setViewportView(gridPanel);
        }
    }

    private class Controls extends JPanel {
        private class NextGenBtnListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                board.evolve();
            }
        }

        private class ClearBtnListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                board.clear();
            }
        }
        public Controls(){
            JButton nextGen = new JButton("Evolve state");
            JButton clear = new JButton("Clear board");
            nextGen.addActionListener(new NextGenBtnListener());
            clear.addActionListener(new ClearBtnListener());
            add(nextGen);
            add(clear);
        }
    }

    public UI() {
        JFrame jgol = new JFrame("JGoL");
        int width = Integer.parseInt(JOptionPane.showInputDialog(null, "What is the width of your board?"));
        int height = Integer.parseInt(JOptionPane.showInputDialog(null, "What is the height of your board?"));
        board = new Board(width, height);
        // creating the grid
        ScrollableButtonGrid scrollableButtonGrid = new ScrollableButtonGrid(width, height);
        for(int i = 0; i<width; i++){
            for( int j = 0; j<height; j++){
                scrollableButtonGrid.addToGrid(board.getCellButton(i,j));
                board.getCellButton(i,j).setPreferredSize(new Dimension(10,10));
            }
        }
        add(scrollableButtonGrid);
        add(new Controls());
    }
}
