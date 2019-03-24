package lab6;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;

public class LineShape implements Shape, Cloneable, Transferable, Serializable {

    private int centerX;
    private int centerY;
    private int a;

    public LineShape(final int centerX, final int centerY, final int param) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.a = param;
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    public Rectangle2D getBounds2D() {
        return null;
    }

    @Override
    public boolean contains(final double x, final double y) {
        return false;
    }

    @Override
    public boolean contains(final Point2D p) {
        return false;
    }

    @Override
    public boolean intersects(final double x, final double y, final double w, final double h) {
        return false;
    }

    @Override
    public boolean intersects(final Rectangle2D r) {
        return false;
    }

    @Override
    public boolean contains(final double x, final double y, final double w, final double h) {
        return false;
    }

    @Override
    public boolean contains(final Rectangle2D r) {
        return false;
    }

    @Override
    public PathIterator getPathIterator(final AffineTransform at) {
        return new ShapeIterator();
    }

    @Override
    public PathIterator getPathIterator(final AffineTransform at, final double flatness) {
        return new ShapeIterator();
    }


    class ShapeIterator implements PathIterator {
        boolean done;
        double h = 10;
        boolean start = true;
        double t = centerX * (-1);

        @Override
        public int getWindingRule() {
            return 0;
        }

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public void next() {
            t += h;
        }

        @Override
        public int currentSegment(final float[] coordinate) {
            if (start) {
                coordinate[0] = (float) t + centerX;
                coordinate[1] = (float) ((a * a * a) / (a * a + t * t)) + centerY;
                start = false;
                return SEG_MOVETO;
            }
            if (t >= centerX) {
                done = true;
                return SEG_CLOSE;
            }
            coordinate[0] = (float) t + centerX;
            coordinate[1] = (float) ((a * a * a) / (a * a + t * t)) + centerY;
            return SEG_LINETO;
        }

        @Override
        public int currentSegment(final double[] coordinate) {
            if (start) {
                coordinate[0] = (float) t + centerX;
                coordinate[1] = (float) ((a * a * a) / (a * a + t * t)) + centerY;
                start = false;
                return SEG_MOVETO;
            }
            if (t >= centerX) {
                done = true;
                return SEG_CLOSE;
            }
            coordinate[0] = (float) t + centerX;
            coordinate[1] = (float) ((a * a * a) / (a * a + t * t)) + centerY;
            return SEG_LINETO;
        }
    }


    static DataFlavor decDataFlavor = new DataFlavor(LineShape.class, "LineShape");
    private static DataFlavor[] supportedFlavors = {decDataFlavor, DataFlavor.stringFlavor};

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return supportedFlavors.clone();
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        return (dataFlavor.equals(decDataFlavor) || dataFlavor.equals(DataFlavor.stringFlavor));
    }

    @Override
    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        if (dataFlavor.equals(decDataFlavor)) {
            return this;
        } else if (dataFlavor.equals(DataFlavor.stringFlavor)) {
            return toString();
        } else
            throw new UnsupportedFlavorException(dataFlavor);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) { // This should never happen
            return this;
        }
    }

    @Override
    public String toString() {
        return a + " " + centerX + " " + centerY ;
    }


    static LineShape getFromString(String line) {
        String[] arr = line.split(" ");
        return new LineShape(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]),
                Integer.parseInt(arr[2]));
    }

    void translate(int x, int y) {
        centerX += x;
        centerY += y;
    }

}
