package lab2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


public class Main {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 500;
    private static final int BORDERWIDTH = 10;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setVisible(true);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        frame.setBounds(dimension.width / 6, dimension.height / 5, WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                BufferedImage bufferedImage = new BufferedImage(getWidth() / 2, getHeight(), BufferedImage.TYPE_INT_RGB);
                final Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setPaint(Color.PINK);
                g2d.setStroke(new BasicStroke(BORDERWIDTH));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                Figure figure = new Figure(25, 100, g2d, this);

                final AffineTransform shadowTransform = AffineTransform.getShearInstance(-2, 0);
                shadowTransform.scale(1, 0.4);
                g2d.setPaint(Color.DARK_GRAY);
                g2d.translate(220, 168);
                g2d.fill(shadowTransform.createTransformedShape(figure));
                g2d.translate(-220, -168);

                g2d.setPaint(Color.BLUE);
                g2d.draw(figure);
                final GradientPaint gradient = new GradientPaint(100, 100, Color.GRAY, 100, 300, Color.WHITE, true);
                g2d.setPaint(gradient);
                g2d.fill(figure);
                g.drawImage(bufferedImage, 0, 0, null);

                final AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
                tx.translate(-bufferedImage.getWidth(null), 0);
                final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                bufferedImage = op.filter(bufferedImage, null);
                g.drawImage(bufferedImage, bufferedImage.getWidth(), 0, null);
            }
        };
        frame.add(panel);
    }
}
