package bsu.fpmi.educational_practice2019;

public class AnswerEvent extends java.util.EventObject {
    public static final int MESS = 0; 
    protected int id;                             
    public AnswerEvent(Object source, int id) {
	super(source);
	this.id = id;
    }
    public int getID() { return id; }            
}

