package lab6;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.*;
import java.util.ArrayList;


public class Main extends JComponent  implements
        DragGestureListener, // For recognizing the start of drags
        DragSourceListener,  // For processing drag source events
        DropTargetListener,  // For processing drop target events
        MouseListener,      // For processing mouse clicks
        MouseMotionListener   // For processing mouse drags
{
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 500;
    private static final int PARAM = 150;
    private static final int LINE_WIDTH = 3;
    private static final BasicStroke LINE_STYLE = new BasicStroke(LINE_WIDTH);
    private static final Border NORMAL_BORDER = new BevelBorder(BevelBorder.LOWERED);
    private static final Border DROP_BORDER = new BevelBorder(BevelBorder.RAISED);

    private ArrayList<LineShape> lineShapes = new ArrayList<>();
    private LineShape currentScribble;
    private LineShape beingDragged;
    private DragSource dragSource;
    private boolean dragMode;

    private static int centerX;
    private static int centerY;

    final JFrame frame = new JFrame();
    JPanel panel;



    private Main() {
        setBorder(NORMAL_BORDER);
        addMouseListener(this);
        addMouseMotionListener(this);
        dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
        DropTarget dropTarget = new DropTarget(this, this);
        this.setDropTarget(dropTarget);
    }

    private void setDragMode(boolean dragMode) {
        this.dragMode = dragMode;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new MyStroke(2));
        for (LineShape lineShape : lineShapes) {
            g2.draw(lineShape);
        }
        g2.setStroke(LINE_STYLE);
    }
    public void mousePressed(MouseEvent e) {
        if (dragMode) {
            return;
        }
        currentScribble = new LineShape(e.getX(), e.getY(), PARAM);
        lineShapes.add(currentScribble);
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (dragMode) {
            return;
        }
        currentScribble = new LineShape(e.getX(), e.getY(), PARAM);
        lineShapes.add(currentScribble);
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
    }

    public void dragGestureRecognized(DragGestureEvent e) {

        if (!dragMode) {
            return;
        }
        MouseEvent inputEvent = (MouseEvent) e.getTriggerEvent();
        int x = inputEvent.getX();
        int y = inputEvent.getY();

        Rectangle rectangle = new Rectangle(x - LINE_WIDTH, y - LINE_WIDTH, LINE_WIDTH * 4, LINE_WIDTH * 4);
        int numScribbles = lineShapes.size();
        for (int i = 0; i < numScribbles; i++) {  // Loop through the scribbles
            LineShape s = (LineShape) lineShapes.get(i);
            if (s.intersects(rectangle)) {
                beingDragged = s;
                LineShape dragScribble = (LineShape) s.clone();
                dragScribble.translate(-x, -y);
                Cursor cursor;
                switch (e.getDragAction()) {
                    case DnDConstants.ACTION_COPY:
                        cursor = DragSource.DefaultCopyDrop;
                        break;
                    case DnDConstants.ACTION_MOVE:
                        cursor = DragSource.DefaultMoveDrop;
                        break;
                    default:
                        return;
                }
                if (DragSource.isDragImageSupported()) {
                    Rectangle scribbleBox = dragScribble.getBounds();
                    Image dragImage = this.createImage(scribbleBox.width,
                            scribbleBox.height);
                    Graphics2D g = (Graphics2D) dragImage.getGraphics();
                    g.setColor(new Color(0, 0, 0, 0));  // transparent background
                    g.fillRect(0, 0, scribbleBox.width, scribbleBox.height);
                    g.setColor(Color.black);
                    g.setStroke(LINE_STYLE);
                    g.translate(-scribbleBox.x, -scribbleBox.y);
                    g.draw(dragScribble);
                    Point hotspot = new Point(-scribbleBox.x, -scribbleBox.y);
                    e.startDrag(cursor, dragImage, hotspot, dragScribble, this);
                } else {
                    e.startDrag(cursor, dragScribble, this);
                }

                return;
            }
        }
    }

    public void dragDropEnd(DragSourceDropEvent e) {
        if (!e.getDropSuccess())
            return;
        int action = e.getDropAction();
        if (action == DnDConstants.ACTION_MOVE) {
            lineShapes.remove(beingDragged);
            beingDragged = null;
            repaint();
        }
    }

    public void dragEnter(DragSourceDragEvent e) {
    }

    public void dragExit(DragSourceEvent e) {
    }

    public void dropActionChanged(DragSourceDragEvent e) {
    }

    public void dragOver(DragSourceDragEvent e) {
    }

    public void dragEnter(DropTargetDragEvent e) {
        if (e.isDataFlavorSupported(LineShape.decDataFlavor) || e.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            e.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
            this.setBorder(DROP_BORDER);
        }
    }

    public void dragExit(DropTargetEvent e) {
        this.setBorder(NORMAL_BORDER);
    }

    public void drop(DropTargetDropEvent e) {
        this.setBorder(NORMAL_BORDER);
        if (e.isDataFlavorSupported(LineShape.decDataFlavor) || e.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        } else {
            e.rejectDrop();
            return;
        }

        Transferable t = e.getTransferable();
        LineShape droppedScribble;
        try {
            droppedScribble = (LineShape) t.getTransferData(LineShape.decDataFlavor);
        } catch (Exception ex) {
            try {
                String s = (String) t.getTransferData(DataFlavor.stringFlavor);
                droppedScribble = LineShape.getFromString(s);
            } catch (Exception ex2) {
                return;
            }
        }
        Point p = e.getLocation();
        droppedScribble.translate((int) p.getX(), (int) p.getY());
        lineShapes.add(droppedScribble);
        repaint();
        e.dropComplete(true);
    }
    public void dragOver(DropTargetDragEvent e) {
    }

    public void dropActionChanged(DropTargetDragEvent e) {
    }



    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            System.err.println(e.getMessage());
        }

        JFrame frame = new JFrame("Lab6");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        final Main scribblePane = new Main();
        frame.getContentPane().add(scribblePane, BorderLayout.CENTER);
        JToolBar toolbar = new JToolBar();
        ButtonGroup group = new ButtonGroup();
        JToggleButton draw = new JToggleButton("Draw");
        JToggleButton drag = new JToggleButton("Drag");
        draw.addActionListener(e -> scribblePane.setDragMode(false));
        drag.addActionListener(e -> scribblePane.setDragMode(true));
        group.add(draw);
        group.add(drag);
        toolbar.add(draw);
        toolbar.add(drag);
        frame.getContentPane().add(toolbar, BorderLayout.NORTH);
        draw.setSelected(true);
        scribblePane.setDragMode(false);
        frame.setSize(700, 400);
        frame.setVisible(true);
    }
}