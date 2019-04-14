/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import bsu.fpmi.educational_practice.My3DLine;
import javax.swing.JFrame;


public class Main extends JFrame{
    private static My3DLine line;
    
    public Main(){
        super(); 
        setBounds(100, 100, 500, 500); 
        line = new My3DLine(50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }
     
  public static void main(String[] args) { 
    Main window = new Main(); 
    line = new My3DLine(40);
    window.add(t);
    window.setVisible(true); 
  }
}
