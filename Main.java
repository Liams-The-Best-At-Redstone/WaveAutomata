import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import javax.swing.Timer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Main {
    int width;
    int height;

    float[] currentGrid;
    float[] previousGrid;
    boolean[] walls;
    BufferedImage image;
    int[] pixels;

    JLabel label;

    final float max_power = 16f;
    final float min_power = -16f;

    boolean hideHud = false;

    float damping;

    long lastTime = System.nanoTime();
    double fps = 0;

    float wavePower;

    public Main() {
        width = 1800;
        height = 1000;
        damping = 0.99f;
        wavePower = 1f;
    }

    public void start() {
        

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);

        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);

        width = frame.getWidth();
        height = frame.getHeight();

        currentGrid = new float[width * height];
        previousGrid = new float[width * height];
        walls = new boolean[width * height];

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        label = new JLabel(new ImageIcon(image));

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    addWave(e.getX(), e.getY(), wavePower, 1);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    addWall(e.getX(), e.getY(), 10);
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    removeAll(e.getX(), e.getY(), 10);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    addWave(e.getX(), e.getY(), wavePower, 1);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    addWall(e.getX(), e.getY(), 10);
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    removeAll(e.getX(), e.getY(), 10);
                }
            }
        };

        label.addMouseListener(mouseHandler);
        label.addMouseMotionListener(mouseHandler);
        frame.add(label);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    wavePower += 0.5;
                    if (wavePower > max_power) wavePower = max_power;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    wavePower -= 0.5;
                    if (wavePower < min_power) wavePower = min_power;
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    Arrays.fill(currentGrid, 0);
                    Arrays.fill(previousGrid, 0);
                    Arrays.fill(walls, false);
                } else if (e.getKeyCode() == KeyEvent.VK_U) {
                    damping += 0.01;
                    if (damping > 1) damping = 1;
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    damping -= 0.01;
                    if (damping < 0) damping = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_H) {
                    hideHud = !hideHud;
                }
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Timer timer = new Timer(16, e -> {
            long currentTime = System.nanoTime();
            double elapsedTime = (currentTime - lastTime) / 1_000_000_000.0;
            lastTime = currentTime;
            fps = 1.0 / elapsedTime;
            update();
            render();
            if (!hideHud) {
                drawOverlay();
            }
            label.repaint();
        });
        timer.start();
    }

    public void addWave(int centerX, int centerY, float value, int radius) {
        int radiusSq = radius * radius;
        for (int y = -radius; y <= radius; y++) {
            int ySq = y * y;
            for (int x = -radius; x <= radius; x++) {
                if (x * x + ySq <= radiusSq) {
                    int targetX = centerX + x;
                    int targetY = centerY + y;

                    if (targetX > 1 && targetX < width - 1 && targetY > 1 && targetY < height - 1) {
                        currentGrid[targetX + targetY * width] += value;
                    }
                }
            }
        }
    }

    public void addWall(int centerX, int centerY, int radius) {
        int radiusSq = radius * radius;
        for (int y = -radius; y <= radius; y++) {
            int ySq = y * y;
            for (int x = -radius; x <= radius; x++) {
                if (x * x + ySq <= radiusSq) {
                    int targetX = centerX + x;
                    int targetY = centerY + y;

                    if (targetX > 1 && targetX < width - 1 && targetY > 1 && targetY < height - 1) {
                        int rowOffset = targetY * width;
                        walls[targetX + rowOffset] = true;
                        currentGrid[targetX + rowOffset] = 0;
                        previousGrid[targetX + rowOffset] = 0;
                    }
                }
            }
        }
    }

    public void removeAll(int centerX, int centerY, int radius) {
        int radiusSq = radius * radius;
        for (int y = -radius; y <= radius; y++) {
            int ySq = y * y;
            for (int x = -radius; x <= radius; x++) {
                if (x * x + ySq <= radiusSq) {
                    int targetX = centerX + x;
                    int targetY = centerY + y;

                    if (targetX > 1 && targetX < width - 1 && targetY > 1 && targetY < height - 1) {
                        int rowOffset = targetY * width;
                        walls[targetX + rowOffset] = false;
                        currentGrid[targetX + rowOffset] = 0;
                        previousGrid[targetX + rowOffset] = 0;
                    }
                }
            }
        }
    }

    public void update() {
        float[] temp = previousGrid;
        previousGrid = currentGrid;
        currentGrid = temp;
        java.util.stream.IntStream.range(1, height - 1).parallel().forEach(y -> {
            int rowOffset = y * width;
            for (int x = 1; x < width - 1; x++) {
                if (walls[x + rowOffset]) {
                    currentGrid[x + rowOffset] = 0;
                } else {
                    float neighborSum = previousGrid[x - 1 + rowOffset] +
                                        previousGrid[x + 1 + rowOffset] +
                                        previousGrid[x + rowOffset - width] +
                                        previousGrid[x + rowOffset + width];
                    float nextValue = (neighborSum / 2.0f) - currentGrid[x + rowOffset];

                    nextValue *= damping;
                    currentGrid[x + rowOffset] = nextValue;
                }
            }
        });
    }

    public void render() {
        java.util.stream.IntStream.range(1, height - 1).parallel().forEach(y -> {
            int rowOffset = y * width;
            for (int x = 1; x < width - 1; x++) {
                int idx = x + rowOffset;
                int r, g, b;
                if (walls[idx]) {
                    r = 100;
                    g = 100;
                    b = 100;
                } else {
                    float amplitude = currentGrid[idx];
                    r = 0;
                    g = 0;
                    b = 0;

                    int colorValue = (int) (Math.abs(amplitude) * 255);
                    colorValue = Math.min(255, Math.max(0, colorValue));
                    r = colorValue;
                    g = colorValue;
                    b = colorValue;
                }

                pixels[idx] = (r << 16) | (g << 8) | b;
            }
        });
    }

    public void drawOverlay() {
        Graphics2D g2 = (Graphics2D) image.getGraphics();

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(20, 20, 310, 175);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.setColor(Color.CYAN);
        g2.drawString("Up arrow: Increase wave power", 35, 45);
        g2.drawString("Down arrow: Decrease wave power", 35, 60);
        g2.drawString(String.format("Current wave power: %.1f", wavePower), 35,75);
        g2.drawString("U: Increase dampening", 35, 90);
        g2.drawString("J: Decrease dampening", 35, 105);
        g2.drawString(String.format("Current dampening: %.2f", damping), 35,120);
        g2.drawString("Left click & drag: Emit waves", 35,135);
        g2.drawString("Right click & drag: Paint walls", 35,150);
        g2.drawString("Middle click & drag: Clear Tiles", 35,165);
        g2.drawString("H: Hide HUD", 35, 180);
        g2.drawString(String.format("FPS: %.1f", fps), 35, 195);;
        g2.drawString("Space: Reset grid | ESC: Exit", 35,210);

        g2.dispose();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }
}
