package GameStuff.GameScreens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import GameStuff.GameScreens.Buttons.TextListener;
import GameStuff.GameScreens.Buttons.TextBox;

public class NameScreen extends AbstractScreen{

	private TextBox tBox;
	
	private ArrayList<TextListener> listenerList = new ArrayList<TextListener>();
	
	private String text;
	
	public NameScreen(Color backColor, String text) {
		super(backColor);
		
		this.text = text;
		
		tBox = new TextBox("Enter a name: ", 100, 200, 400, 50, 250, 400, 100, 50, Color.YELLOW, Color.GREEN, Color.CYAN, 12, this);
		tBox.addTextListener(new TextListener(){
			@Override
			public void entered(String text) {
				if(text == "")
					text = "Some Human";
				
				fireEntered(text);
			}
			
		});
	}
	
	public void addTextListener(TextListener l) {
		  listenerList.add(l);
	}
		
	public void removeTextListener(TextListener l) {
	  listenerList.remove(l);
	}
		
	protected void fireEntered(String text) {
	  	for(TextListener tl: listenerList){
	  		tl.entered(text);
	  	}
	}
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Courier", Font.BOLD, 64));
		g.drawString(text, (int)(((double)WIDTH / 2.0) - (39 * (double)text.length() / 2.0)), 100);
		
		tBox.draw(g);
	}
}
