package lab3;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

public class MyStroke implements Stroke {
    private BasicStroke stroke;

    MyStroke(final float width) {
        this.stroke = new BasicStroke(width);
    }

    @Override
    public Shape createStrokedShape(Shape shape) {
        GeneralPath shapePath = new GeneralPath();

        float[] xy = new float[2];
        float[] pre_xy = new float[2];

        for (PathIterator i = shape.getPathIterator(null); !i.isDone(); i.next()) {
            int type = i.currentSegment(xy);

            switch (type) {
                case PathIterator.SEG_MOVETO:
                    shapePath.moveTo(xy[0], xy[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    double x1 = pre_xy[0];
                    double y1 = pre_xy[1];
                    double x2 = xy[0];
                    double y2 = xy[1];
                    double step = 4;

                    double dx = x2 - x1;
                    double dy = y2 - y1;
                    double length = Math.sqrt(dx * dx + dy * dy);
                    dx /= length;
                    dy /= length;
                    y1 += dx * step;

                    if (!Double.isInfinite(length)) {
                        x1 += dx * step;
                        y1 -= dx * step;
                        shapePath.lineTo(x1, y1);
                        y1 -= dx * step;
                        shapePath.lineTo(x1, y1);
                        x1 += dx * step;
                        shapePath.lineTo(x1, y1);
                        y1 += dx * step;
                        shapePath.lineTo(x1, y1);
                        x1 += dx * step;
                        y1 += dx * step;
                        shapePath.lineTo(x1, y1);
                    }
                    break;
            }
            pre_xy[0] = xy[0];
            pre_xy[1] = xy[1];
        }
        return stroke.createStrokedShape(shapePath);
    }
}