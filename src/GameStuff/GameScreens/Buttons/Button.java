package GameStuff.GameScreens.Buttons;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Button extends AbstractButton{

	ButtonClickInterface bi;
	ButtonDrawInterface bd;
	
	public Button(ButtonClickInterface bi, ButtonDrawInterface bd, int x, int y, int width, int height, int arcWidth, int arcHeight, JPanel jp) {
		super(x, y, width, height, arcWidth, arcHeight, new Color(224, 113, 6), Color.YELLOW, jp);
		
		this.bi = bi;
		this.bd = bd;
	}

	@Override
	public void draw(Graphics g) {
		highlightButton(g);
		drawButtonOutline(g);
		
		bd.draw(g);
	}
	
	public void drawButtonOutline(Graphics g){
		g.setColor(hc);
		g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	void Do() {
		bi.buttonClicked();
	}
	
	public int getX(){
		return x;
	}
	
	
	public int getY(){
		return y;
	}
}
