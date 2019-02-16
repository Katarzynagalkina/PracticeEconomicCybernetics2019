import java.awt.geom.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
public class Figure implements Shape {

    private Shape point;
    private Shape jog;
    private Area figureArea = new Area();

    Figure(double angle, int step){
        point=new Ellipse2D.Double(-10,-10,20,20);
        jog=new Rectangle(-2,0,4,70);

        AffineTransform at = AffineTransform.getRotateInstance(angle*Math.PI/180,Main.xBegin+step,Main.y);
        at.translate(Main.xBegin+step,Main.y);
        jog = at.createTransformedShape(jog);
        point=at.createTransformedShape(point);

        if(!figureArea.isEmpty())
            figureArea.reset();
        figureArea.add(new Area(point));
        figureArea.add(new Area(jog));
    }

public Figure changeAngle(double angle,int step){
   Figure figure= new Figure(angle,step);
       return figure;
    }

    public boolean contains(double x, double y) {
        return figureArea.contains(x, y);
    } @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return figureArea.getPathIterator(at);
    } @Override
    public Rectangle getBounds() {
        return figureArea.getBounds();
    } @Override
    public boolean contains(Point2D p) {
        return figureArea.contains(p);
    } @Override
    public boolean intersects(Rectangle2D r) {
        return figureArea.intersects(r);
    }
    public boolean intersects(double a,double b, double c, double d){
        return figureArea.intersects(a,b,c,d);
    } @Override
    public boolean contains(Rectangle2D r) {
        return figureArea.contains(r);
    } @Override
    public boolean contains(double x, double y, double w, double h) {
        return figureArea.contains(x,y,w,h);
    } @Override
    public Rectangle2D getBounds2D() {
        return figureArea.getBounds2D();
    } @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return figureArea.getPathIterator(at,flatness);
    }
}