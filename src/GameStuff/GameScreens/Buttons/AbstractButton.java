package GameStuff.GameScreens.Buttons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import GameStuff.GameScreens.ClickListener;


public abstract class AbstractButton implements MouseMotionListener, MouseListener{
	
	int i = 0;
	
	boolean highlight;
	
	int x;
	int y;
	int width;
	int height;
	int arcWidth;
	int arcHeight;
	
	Color hc;
	Color bc;

	AbstractButton(int x, int y, int width, int height, int arcWidth, int arcHeight, Color bc, Color hc, JPanel jp){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.arcWidth = arcWidth;
		this.arcHeight = arcHeight;
		this.hc = hc;
		this.bc = bc;
		
		jp.addMouseMotionListener(this);
		jp.addMouseListener(this);
	}
	
	public abstract void draw(Graphics g);
	
	public void highlightButton(Graphics g){
		if(highlight == true){
			g.setColor(hc);
			g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
		}else{
			g.setColor(bc);
			g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
		}
	}
	
	abstract void Do();

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getX() >= x && e.getX() < x + width){
			if(e.getY() >= y && e.getY() < y + height){
				Do();
			}
		}	
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getX() >= x && e.getX() < x + width && e.getY() >= y && e.getY() < y + height){
			highlight = true;
		}else{
			highlight = false;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
