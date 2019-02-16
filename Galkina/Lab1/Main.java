import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom. AffineTransform;
import java.awt.geom.Ellipse2D;
public class Main{

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 500;
    public static final int xBegin = 100;
    private static final int xEnd = 500;
    public static final int y = 100;
    private static Color figureColor;
    private static Color borderColor;
    private static int borderWidth;

    private  static int angle=1,step=1, x = 0;

    public static void main(String[] args)  {

        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setTitle("Animation");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        frame.setBounds(dimension.width / 6, dimension.height / 5, WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Figure figure=new Figure(angle,step);
        borderColor=Color.black;
        figureColor=Color.lightGray;
        borderWidth=3;

        JPanel panel = new JPanel() {
            @Override
             protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.pink);
                Graphics2D g2d = (Graphics2D)g;
                g2d.drawLine(xBegin,Main.y,xEnd, Main.y);
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(borderWidth));
                g2d.draw(figure.changeAngle(angle,x));
                g2d.setColor(figureColor);
                g2d.fill(figure.changeAngle(angle,x));
                }
            };
        frame.add(panel);

        Timer timer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!(angle>=360))
                    angle+=5;
                else angle=1;
                x+=step;
                if(x==400 || x ==0)
                    step=-step;
                panel.repaint();
            }
        });
        timer.start();
        }
};


