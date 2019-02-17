package lab1;

import java.awt.geom.*;
import java.awt.*;

public class Figure implements Shape {

    private Area figureArea = new Area();

    Figure(double angle, int step) {
        Shape point;
        Shape jog;
        point = new Ellipse2D.Double(-10, -10, 20, 20);
        jog = new Rectangle(-2, 0, 4, 70);
        final AffineTransform at = AffineTransform.getRotateInstance(angle * Math.PI / 180, Main.X_BEGIN + step, Main.Y);
        at.translate(Main.X_BEGIN + step, Main.Y);
        jog = at.createTransformedShape(jog);
        point = at.createTransformedShape(point);
        if (!figureArea.isEmpty()) {
            figureArea.reset();
        }
        figureArea.add(new Area(point));
        figureArea.add(new Area(jog));
    }

    public Figure changeAngle(final double angle, final int step) {
        return new Figure(angle, step);
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