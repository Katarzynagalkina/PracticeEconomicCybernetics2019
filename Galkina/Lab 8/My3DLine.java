/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bsu.fpmi.educational_practice;
import java.awt.*;

public class My3DLine extends Canvas 
{
    private int height;
    private int width=10;
    private int x = 200;
    private int y = 10;


    public My3DLine (int height) { this.height = height; }
    public My3DLine () {}

    public void setHeight(int height){
        this.height=height;
    }
    public int getHeight(){
        return height;
    }


    @Override
    public void paint(Graphics g) {
        g.setColor(Color.red);
        g.draw3DRect(x, y, width ,height, true);
        g.fill3DRect(x, y, width ,height, true);
    }

}
