package GameStuff.GameScreens;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import GameStuff.GameScreens.Buttons.Mode;

public class AbstractScreen extends JPanel{
	final int WIDTH = 600;
	final int HEIGHT = 600;
	
	private ArrayList<ButtonClickListener> listenerList = new ArrayList<ButtonClickListener>();
	
	protected Color backColor;
	
	protected AbstractScreen(Color backColor){
		this.setSize(WIDTH, HEIGHT);
		this.setVisible(true);
		
		this.backColor = backColor;
		
		this.setBackground(backColor);
	}
	
	public void addClickListener(ButtonClickListener l) {
		  listenerList.add(l);
	}
		
	public void removeClickListener(ButtonClickListener l) {
	  listenerList.remove(l);
	}
	
	protected void fireClicked(Mode mode) {
		for(ButtonClickListener cl: listenerList){
			cl.clicked(mode);
		}
	}
}
