package lab3;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.RandomAccessFile;

public class MyPrintable implements Printable {


    public MyPrintable(final String fileName) {
        try {
            sourceFile = new RandomAccessFile(fileName, "r");
        } catch (Exception e) {
            rememberedEOF = true;
        }
    }

    private final int FONT_SIZE = 14;
    private final int LINE_SPACING = 5;
    final Font FONT = new Font("Arial", Font.PLAIN, 14);

    private RandomAccessFile sourceFile;
    private int rememberedPageIndex = 0;
    private long rememberedFilePointer = 0;
    private boolean rememberedEOF = false;
    private boolean tempLineIsOver = true;
    private String tempLine = "";


    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {

        final int width = (int) pageFormat.getImageableWidth();
        final int height = (int) pageFormat.getImageableHeight();
        final Point CENTER = new Point(width / 2, height * 3 / 4);
        final String nameOfLine = "Линия Верзьера";


        try {
            if (pageIndex == 0) {
                final Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.PINK);
                g2d.fillRect(0, 0, width, height);
                final int centerX = width / 2;
                final int centerY = height / 2;
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(0, centerY, centerX * 2 - 5, centerY);
                g2d.drawLine(centerX, 0, centerX, centerY * 2 - 5);
                g2d.drawString(nameOfLine, CENTER.x - g.getFontMetrics().stringWidth(nameOfLine) / 3, CENTER.y + 180);
                g2d.setColor(Color.RED);
                final LineShape p = new LineShape(centerX, centerY, 150);
                g2d.setStroke(new MyStroke(3));
                g2d.draw(p);
                g2d.setFont(FONT);
                return Printable.PAGE_EXISTS;
            }

            if (pageIndex != rememberedPageIndex) {
                rememberedPageIndex = pageIndex;
                if (rememberedEOF) {
                    System.out.println("Printing complete");
                    return Printable.NO_SUCH_PAGE;
                }
                rememberedFilePointer = sourceFile.getFilePointer();
            } else
                sourceFile.seek(rememberedFilePointer);

            g.setFont(FONT);
            final int x = (int) pageFormat.getImageableX();
            int y = (int) pageFormat.getImageableY();
            final FontMetrics fontMetrics = g.getFontMetrics();
            y += FONT_SIZE;

            while (y + FONT_SIZE + LINE_SPACING < pageFormat.getImageableY() + pageFormat.getImageableHeight()) {
                if (tempLineIsOver) {
                    final String line = sourceFile.readLine();
                    if (line == null) {
                        rememberedEOF = true;
                        break;
                    }
                    if (fontMetrics.stringWidth(line) < width) {
                        g.drawString(line, x, y);
                        y += FONT_SIZE + LINE_SPACING;
                    } else {
                        tempLineIsOver = false;
                        for (int i = line.length() - 1; i >= 0; i--) {
                            if (line.charAt(i) == ' ' || line.charAt(i) == '.') {
                                if (fontMetrics.stringWidth(line.substring(0, i)) < width) {
                                    g.drawString(line.substring(0, i), x, y);
                                    y += FONT_SIZE + LINE_SPACING;
                                    if (i == line.length()) {
                                        tempLineIsOver = true;
                                        tempLine = "";
                                    } else {
                                        tempLine = line.substring(i, line.length());
                                    }
                                    break;
                                }
                            }
                        }
                    }

                } else {
                    final String line = tempLine;
                    if (fontMetrics.stringWidth(line) < width) {
                        tempLineIsOver = true;
                        tempLine = "";
                        g.drawString(line, x, y);
                        y += FONT_SIZE + LINE_SPACING;
                        continue;
                    }
                    for (int i = line.length() - 1; i >= 0; i--) {
                        if (line.charAt(i) == ' ' || line.charAt(i) == '.') {
                            if (fontMetrics.stringWidth(line.substring(0, i)) < width) {
                                g.drawString(line.substring(0, i), x, y);
                                y += FONT_SIZE + LINE_SPACING;
                                if (i == line.length()) {
                                    tempLineIsOver = true;
                                    tempLine = "";
                                } else {
                                    tempLine = line.substring(i, line.length());
                                }
                                break;
                            }
                        }
                    }

                }
            }
            return Printable.PAGE_EXISTS;
        } catch (Exception e) {
            return Printable.NO_SUCH_PAGE;
        }
    }


    public static void print() {
        final PrinterJob printerJob = PrinterJob.getPrinterJob();
        final PageFormat page = new PageFormat();
        page.setOrientation(PageFormat.LANDSCAPE);
        printerJob.setPrintable(new MyPrintable("C:\\\\Users\\\\Katty\\\\Desktop\\\\Versbera.java"), page);
        PrintRequestAttributeSet parameters = new HashPrintRequestAttributeSet();
        parameters.add(Sides.DUPLEX);
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}


