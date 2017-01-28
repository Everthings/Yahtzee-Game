package GameStuff.GameScreens.Buttons;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import GameStuff.GameScreens.ButtonClickListener;
import GameStuff.GameScreens.ClickListener;
import Main.Runner;

public class OptionButton extends AbstractButton{
	
	private String text;
	private Mode mode;
	
	private Color textColor = Color.YELLOW;
	
	private Color highlightedTextColor = new Color(224, 113, 6);
	
	private Font myFont = new Font("Serif", Font.BOLD, 30);
	
	private ArrayList<OptionButton> buttons = new ArrayList<OptionButton>();
	private ArrayList<ButtonClickListener> listenerList = new ArrayList<ButtonClickListener>();
	
	public OptionButton(int x, int y, int width, int height, String text, Mode mode, JPanel jp) {
		super(x, y, width, height, (int)(double)(width / 5.0), (int)(double)(width / 5.0), new Color(224, 113, 6), Color.YELLOW, jp);
		this.text = text;
		this.mode = mode;
	}
	
	public void addClickListener(ButtonClickListener l) {
		  listenerList.add(l);
	}
		
	public void removeClickListener(ButtonClickListener l) {
	  listenerList.remove(l);
	}
	
	protected void fireClicked() {
		for(ButtonClickListener cl: listenerList){
			cl.clicked(mode);
		}
	}

	@Override
	public void draw(Graphics g) {
		highlightButton(g);
		drawButtonOutline(g);
		
		if(highlight){
			g.setColor(highlightedTextColor);
		}else{
			g.setColor(textColor);
		}
		g.setFont(myFont);
		g.drawString(text, (int)(x + (((double)width / 2.0) - (13 * ((double)text.length() / 2.0)))), (int)(y + ((double)height / 2.0)) + 7);
	}
	
	public void drawButtonOutline(Graphics g){
		g.setColor(hc);
		g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	void Do() {
		fireClicked();
	}
}

