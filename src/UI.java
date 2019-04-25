import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI extends JFrame {
    private Board board;

    public UI() {
        int width = Integer.parseInt(JOptionPane.showInputDialog(null, "Board width:"));
        int height = Integer.parseInt(JOptionPane.showInputDialog(null, "Board height:"));
        board = new Board(width, height);
        // creating the grid
        ScrollableButtonGrid scrollableButtonGrid = new ScrollableButtonGrid(width, height);
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                scrollableButtonGrid.addToGrid(board.getCellButton(i, j));
            }
        }
        setButtonSize(20);
        add(scrollableButtonGrid, BorderLayout.CENTER);
        add(new Controls(), BorderLayout.SOUTH);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void setButtonSize(int px) {
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                board.getCellButton(i, j).setPreferredSize(new Dimension(px, px));
            }
        }
    }

    private class ScrollableButtonGrid extends JPanel {
        private Panel gridPanel;
        private JScrollPane scrollPane;

        public ScrollableButtonGrid(int w, int h) {
            gridPanel = new Panel();
            gridPanel.setLayout(new GridLayout(h, w));
            scrollPane = new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            add(scrollPane, BorderLayout.CENTER);
        }

        public void addToGrid(JButton btn) {
            gridPanel.add(btn);
            // scrollPane.setViewportView(gridPanel);
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

        private class StartButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {

            }
        }

        public Controls() {
            JButton nextGen = new JButton("Evolve state");
            JButton clear = new JButton("Clear board");
            JButton autoEvolve = new JButton("Start");
            nextGen.addActionListener(new NextGenBtnListener());
            clear.addActionListener(new ClearBtnListener());
            autoEvolve.addActionListener(new StartButtonListener());
            add(nextGen);
            add(clear);
            add(autoEvolve);
        }
    }
}
