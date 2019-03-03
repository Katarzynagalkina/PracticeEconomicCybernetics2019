package lab2;

import javax.swing.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.awt.*;

public class Figure implements Shape {

    private Area figureArea = new Area();
    private final String LINE = "GO";
    private final int RECRANGLE_WIDTH = 300;
    private final int RECTANGLE_HEIGHT = 170;
    private static final int FONT_SIZE = 100;

    Figure(final int x, final int y, final Graphics2D graphics2D) {
        final Shape rectangle = new Rectangle(x, y, RECRANGLE_WIDTH, RECTANGLE_HEIGHT);
        final AffineTransform transform = AffineTransform.getTranslateInstance(
                ((Rectangle) rectangle).x, ((Rectangle) rectangle).y);
        transform.translate(((Rectangle) rectangle).width / 3, 2 * ((Rectangle) rectangle).height / 3);
        final FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
        final Font font = new Font(Font.MONOSPACED, Font.BOLD, FONT_SIZE);
        final TextLayout textLayout = new TextLayout(LINE, font, fontRenderContext);
        final Shape line = textLayout.getOutline(transform);
        graphics2D.setColor(Color.BLUE);
        graphics2D.fill(line);
        figureArea.add(new Area(rectangle));
        figureArea.subtract(new Area(line));
    }

    public Figure changeAngle(final int x, final int y, final Graphics2D graphics2D, final JPanel panel) {
        return new Figure(x, y, graphics2D);
    }

    public boolean contains(final double x, final double y) {
        return figureArea.contains(x, y);
    }

    @Override
    public PathIterator getPathIterator(final AffineTransform at) {
        return figureArea.getPathIterator(at);
    }

    @Override
    public Rectangle getBounds() {
        return figureArea.getBounds();
    }

    @Override
    public boolean contains(final Point2D p) {
        return figureArea.contains(p);
    }

    @Override
    public boolean intersects(final Rectangle2D r) {
        return figureArea.intersects(r);
    }

    public boolean intersects(final double a, final double b, final double c, final double d) {
        return figureArea.intersects(a, b, c, d);
    }

    @Override
    public boolean contains(final Rectangle2D r) {
        return figureArea.contains(r);
    }

    @Override
    public boolean contains(final double x, final double y, final double w, final double h) {
        return figureArea.contains(x, y, w, h);
    }

    @Override
    public Rectangle2D getBounds2D() {
        return figureArea.getBounds2D();
    }

    @Override
    public PathIterator getPathIterator(final AffineTransform at, final double flatness) {
        return figureArea.getPathIterator(at, flatness);
    }

}
