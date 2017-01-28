package GameStuff.GameScreens.Buttons;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;


public class SettingsButton extends AbstractButton{
	
	int[] values;
	Color[] colors;
	int index;
	
	String text;
	
	Color textColor = Color.YELLOW;
	
	Color highlightedTextColor = new Color(224, 113, 6);
	
	Font myFont = new Font("Serif", Font.BOLD, 30);
	
	final int COLOR = 1;
	final int INT = 0;
	int type;

	public SettingsButton(int x, int y, int width, int height, JPanel jp, int[] values, int startingIndex, String text){
		super(x, y, width, height, (int)(double)(width / 5.0), (int)(double)(width / 5.0), new Color(224, 113, 6), Color.YELLOW, jp);
		
		this.text = text;
		this.values = values;
		this.index = startingIndex;
		
		type = INT;
	}
	
	public SettingsButton(int x, int y, int width, int height, JPanel jp, Color[] colors, int startingIndex, String text){
		super(x, y, width, height, (int)(double)(width / 5.0), (int)(double)(width / 5.0), new Color(224, 113, 6), Color.YELLOW, jp);

		this.text = text;
		this.colors = colors;
		this.index = startingIndex;
		
		type = COLOR;
	}

	@Override
	public void draw(Graphics g) {
		highlightButton(g);
		
		if(type == INT){
			
			drawButtonOutline(g);
			
			if(highlight){
				g.setColor(highlightedTextColor);
			}else{
				g.setColor(textColor);
			}
			g.setFont(myFont);
			g.drawString(values[index] + "", (int)(x + (((double)width / 2.0) - (18 * (1 / 2.0)))), (int)(y + ((double)height / 2.0)) + 7);
		}else if(type == COLOR){
			g.setColor(colors[index]);
			g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
		}
			
		g.setFont(new Font("Serif", Font.BOLD, 15));
		g.setColor(textColor);
		g.drawString(text, (int)(x + ((width - (text.length() * 7)) / 2.0)), y - 10);
	}
	
	public void drawButtonOutline(Graphics g){
		g.setColor(hc);
		g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	void Do(){
		index++;
		
		if(type == INT){
			if(index >= values.length){
				index = index % values.length;
			}
		}
		
		if(type == COLOR){
			if(index >= colors.length){
				index = index % colors.length;
			}
		}
	}
	
	public Object getValue(){
		if(type == INT){
			return values[index];
		}else{
			return colors[index];
		}
	}
}
