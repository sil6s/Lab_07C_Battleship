import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Cell extends JButton {
    private static final int DEFAULT_CELL_SIZE = 40;
    private static final Color HIT_COLOR = new Color(255, 0, 0, 128);
    private static final Color MISS_COLOR = new Color(0, 0, 255, 128);

    private final int row;
    private final int col;
    private boolean occupied;
    private boolean isHit;
    private Ship ship;
    private BufferedImage blankImage;
    private ImageIcon hitIcon;
    private ImageIcon missIcon;

    public Cell(int row, int col, Board board) {
        this.row = row;
        this.col = col;
        this.occupied = false;
        this.isHit = false;

        setPreferredSize(new Dimension(DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setContentAreaFilled(false);
        setOpaque(false);

        loadImages();
        addActionListener(e -> fireAtCell(board));
        reset();
    }

    private void loadImages() {
        try {
            blankImage = loadBlankImage();
            setIcon(new ImageIcon(blankImage));
            hitIcon = createOverlayIcon(HIT_COLOR);
            missIcon = createOverlayIcon(MISS_COLOR);
        } catch (IOException e) {
            System.err.println("Error loading images: " + e.getMessage());
            setBackground(Color.WHITE);
            setOpaque(true);
        }
    }

    private BufferedImage loadBlankImage() throws IOException {
        File blankFile = new File("resources/blank.png");
        if (blankFile.exists()) {
            BufferedImage original = ImageIO.read(blankFile);
            BufferedImage scaled = new BufferedImage(DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaled.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(original, 0, 0, DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE, null);
            g2d.dispose();
            return scaled;
        } else {
            BufferedImage blank = new BufferedImage(DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = blank.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE);
            g2d.dispose();
            return blank;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(blankImage, 0, 0, getWidth(), getHeight(), this);
        if (isHit) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(occupied ? HIT_COLOR : MISS_COLOR);
            int padding = 5;
            g2d.fillOval(padding, padding, getWidth() - (2 * padding), getHeight() - (2 * padding));
            g2d.dispose();
        }
    }

    private ImageIcon createOverlayIcon(Color color) {
        BufferedImage img = new BufferedImage(DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        int padding = 5;
        g2d.fillOval(padding, padding, DEFAULT_CELL_SIZE - (2 * padding), DEFAULT_CELL_SIZE - (2 * padding));
        g2d.dispose();
        return new ImageIcon(img);
    }

    public void fireAtCell(Board board) {
        if (isHit) {
            return;
        }

        isHit = true;
        if (occupied && ship != null) {
            ship.recordHit();
            board.recordHit(ship);
        } else {
            board.recordMiss();
        }
        repaint();
        setEnabled(true);
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
        this.occupied = (ship != null);
    }

    public void reset() {
        occupied = false;
        isHit = false;
        ship = null;
        repaint();
        setEnabled(true);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
