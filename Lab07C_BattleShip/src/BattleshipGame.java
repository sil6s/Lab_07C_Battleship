import javax.swing.*;

public class BattleshipGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Battleship Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600);
            frame.setContentPane(new Board());
            frame.setVisible(true);
        });
    }
}
