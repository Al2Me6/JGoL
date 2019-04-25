import javax.swing.*;

public class JGoL {
    public static void main(String[] args){
        JFrame frame = new JFrame("JGoL");
        UI ui = new UI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ui);
        frame.pack();
        frame.setVisible(true);
    }
}
