import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Board extends JPanel {
    private Cell[][] cells = new Cell[10][10];
    private Ship[] ships;
    private Player player;
    private JLabel missCounterLabel, strikeCounterLabel, totalMissCounterLabel, totalHitCounterLabel;
    private int missCount = 0;
    private int strikeCount = 0;
    private int totalMisses = 0;
    private int totalHits = 0;

    public Board() {
        setLayout(new BorderLayout());
        player = new Player();
        initializeCells();
        placeShips();
        initializeCounters();
        setBackground(Color.LIGHT_GRAY);
    }

    private void initializeCells() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(10, 10));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                cells[row][col] = new Cell(row, col, this);
                gridPanel.add(cells[row][col]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
    }

    private void placeShips() {
        ships = new Ship[]{new Ship(5), new Ship(4), new Ship(3), new Ship(3), new Ship(2)};
        Random rand = new Random();
        for (Ship ship : ships) {
            boolean placed = false;
            while (!placed) {
                boolean horizontal = rand.nextBoolean();
                int row = rand.nextInt(10);
                int col = rand.nextInt(10);
                placed = placeShip(ship, row, col, horizontal);
            }
        }
    }

    private boolean placeShip(Ship ship, int row, int col, boolean horizontal) {
        if (horizontal) {
            if (col + ship.getLength() > 10) return false;
            for (int i = 0; i < ship.getLength(); i++) {
                if (cells[row][col + i].isOccupied()) return false;
            }
            for (int i = 0; i < ship.getLength(); i++) {
                cells[row][col + i].setOccupied(true);
                cells[row][col + i].setShip(ship);
            }
        } else {
            if (row + ship.getLength() > 10) return false;
            for (int i = 0; i < ship.getLength(); i++) {
                if (cells[row + i][col].isOccupied()) return false;
            }
            for (int i = 0; i < ship.getLength(); i++) {
                cells[row + i][col].setOccupied(true);
                cells[row + i][col].setShip(ship);
            }
        }
        return true;
    }

    private void initializeCounters() {
        JPanel counterPanel = new JPanel(new GridLayout(4, 2));
        counterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        missCounterLabel = new JLabel("MISS Counter: 0");
        strikeCounterLabel = new JLabel("STRIKE Counter: 0");
        totalMissCounterLabel = new JLabel("TOTAL MISS: 0");
        totalHitCounterLabel = new JLabel("TOTAL HIT: 0");

        counterPanel.add(missCounterLabel);
        counterPanel.add(strikeCounterLabel);
        counterPanel.add(totalMissCounterLabel);
        counterPanel.add(totalHitCounterLabel);

        add(counterPanel, BorderLayout.SOUTH);
    }

    public void recordHit(Ship ship) {
        totalHits++;
        totalHitCounterLabel.setText("TOTAL HIT: " + totalHits);
        JOptionPane.showMessageDialog(this, "Hit!");

        if (ship.isSunk()) {
            JOptionPane.showMessageDialog(this, "You sank a ship!");
        }
        checkWinCondition();
    }

    public void recordMiss() {
        missCount++;
        totalMisses++;
        missCounterLabel.setText("MISS Counter: " + missCount);
        totalMissCounterLabel.setText("TOTAL MISS: " + totalMisses);

        // Show miss dialog
        JOptionPane.showMessageDialog(this, "Miss!");

        if (missCount == 5) {
            strikeCount++;
            strikeCounterLabel.setText("STRIKE Counter: " + strikeCount);
            missCount = 0;
            JOptionPane.showMessageDialog(this, "Strike " + strikeCount + "!");
        }

        if (strikeCount == 3) {
            JOptionPane.showMessageDialog(this, "You lost! Game Over!");
            resetGame();
        }
    }

    private void checkWinCondition() {
        boolean allSunk = true;
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                allSunk = false;
                break;
            }
        }
        if (allSunk) {
            JOptionPane.showMessageDialog(this, "You sunk all ships! You win!");
            resetGame();
        }
    }

    private void resetGame() {
        int response = JOptionPane.showConfirmDialog(this, "Would you like to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            resetCounters();
            resetCells();
            placeShips();
        } else {
            System.exit(0);
        }
    }

    private void resetCounters() {
        missCount = 0;
        strikeCount = 0;
        totalMisses = 0;
        totalHits = 0;
        missCounterLabel.setText("MISS Counter: 0");
        strikeCounterLabel.setText("STRIKE Counter: 0");
        totalMissCounterLabel.setText("TOTAL MISS: 0");
        totalHitCounterLabel.setText("TOTAL HIT: 0");
    }

    private void resetCells() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                cells[row][col].reset();
            }
        }
    }
}