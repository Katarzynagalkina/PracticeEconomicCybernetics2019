package lab3;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 500;
    private static final int PARAM = 150;

    private static int centerX;
    private static int centerY;

    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        frame.setVisible(true);
        final  Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension dimension = toolkit.getScreenSize();
        frame.setBounds(dimension.width / 6, dimension.height / 5, WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                setBackground(Color.PINK);
                final Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                centerX = getWidth() / 2;
                centerY = getHeight() / 2;
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(0, centerY + 10, centerX * 2, centerY + 10);
                g2d.drawLine(centerX, 0, centerX, centerY * 2 + 20);
                g2d.setColor(Color.RED);
                final LineShape p = new LineShape(centerX, centerY, PARAM);
                g2d.setStroke(new MyStroke(3));
                g2d.draw(p);
            }
        };
        frame.add(panel);
    }
}
