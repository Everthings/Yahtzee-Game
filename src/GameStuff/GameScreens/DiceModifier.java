package GameStuff.GameScreens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Utilities.GraphicsUtils;

public class DiceModifier implements MouseMotionListener, MouseListener{
	
	private BufferedImage[] diceImages = new BufferedImage[6];
	private ArrayList<DiceModifierListener> listenerList = new ArrayList<DiceModifierListener>();
	
	private Dice dice;
	
	private final int OVAL_WIDTH = 200;
	private final int OVAL_HEIGHT = 150;
	private final int ARC_WIDTH = 50;
	private final int ARC_HEIGHT = 50;
	
	private int offsetX;
	private int offsetY;
	private final int pixelsAboveDice = 40;
	
	private Dice[] displayDice = new Dice[6];
	private boolean enableModifier = false;
	
	DiceModifier(int offsetX, int offsetY){
		
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		
		initImages();	

		for(int i = 0; i < 6; i++){
			displayDice[i] = new Dice(diceImages[i], 0, 0, i + 1, 0, 0, 0, 0);
		}
	}

	public void initImages(){
		try {
			diceImages[0] = ImageIO.read(new File("/Users/XuMan/Documents/BB&N/Yahtzee Game/src/Images/Dice_1.png"));	
			diceImages[1] = ImageIO.read(new File("/Users/XuMan/Documents/BB&N/Yahtzee Game/src/Images/Dice_2.png"));	
			diceImages[2] = ImageIO.read(new File("/Users/XuMan/Documents/BB&N/Yahtzee Game/src/Images/Dice_3.png"));	
			diceImages[3] = ImageIO.read(new File("/Users/XuMan/Documents/BB&N/Yahtzee Game/src/Images/Dice_4.png"));	
			diceImages[4] = ImageIO.read(new File("/Users/XuMan/Documents/BB&N/Yahtzee Game/src/Images/Dice_5.png"));	
			diceImages[5] = ImageIO.read(new File("/Users/XuMan/Documents/BB&N/Yahtzee Game/src/Images/Dice_6.png"));	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addDiceModifierListener(DiceModifierListener d) {
		listenerList.add(d);
	}
		
	public void removeDiceModifierListener(DiceModifierListener d) {
		 listenerList.remove(d);
	}
		
	protected void fireSelected(int value) {
	    for(DiceModifierListener dl: listenerList){
	    	dl.diceSelected(value);
	    }
	}
	
	protected void fireExited() {
	    for(DiceModifierListener dl: listenerList){
	    	dl.mouseExited();
	    }
	}
	
	public void setEnable(boolean value){
		enableModifier = value;
	}
	
	public boolean isEnabled(){
		return enableModifier;
	}
	
	public void setDice(Dice d){
		this.dice = d;
		
		for(int i = 0; i < 3; i++){
			displayDice[i].setX(d.getX() + offsetX - OVAL_WIDTH/2 + d.getWidth() + i * 60);
			displayDice[i].setY(d.getY() - d.getHeight()/2 + offsetY - pixelsAboveDice - 3 * OVAL_HEIGHT/4);
		}
		
		for(int i = 0; i < 3; i++){
			displayDice[i + 3].setX(d.getX() + offsetX - OVAL_WIDTH/2 + d.getWidth() + i * 60);
			displayDice[i + 3].setY(d.getY() - d.getHeight()/2 + offsetY - pixelsAboveDice - OVAL_HEIGHT/4);
		}
	}
	
	public void positionDiceToRight(){
		for(int i = 0; i < 6; i++){
			displayDice[i].setX(dice.getX() + offsetX + (i % 3) * 60);
		}
	}
	
	public void draw(Graphics g){
		if(enableModifier){
			if(dice.getX() + offsetX - OVAL_WIDTH/2 > 0){
				g.setColor(Color.WHITE);
				g.fillRoundRect(dice.getX() + offsetX - OVAL_WIDTH/2, dice.getY() - dice.getHeight()/2 + offsetY - pixelsAboveDice - OVAL_HEIGHT, OVAL_WIDTH, OVAL_HEIGHT, ARC_WIDTH, ARC_HEIGHT);
			}else{
				positionDiceToRight();
				g.setColor(Color.WHITE);
				g.fillRoundRect(dice.getX() + offsetX - dice.getWidth(), dice.getY() - dice.getHeight()/2 + offsetY - pixelsAboveDice - OVAL_HEIGHT, OVAL_WIDTH, OVAL_HEIGHT, ARC_WIDTH, ARC_HEIGHT);
			}
			
			Graphics2D g2 = (Graphics2D) g;
			
			for(int i = 0; i < 6; i++){
				displayDice[i].draw(g, 0, 0, false);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		int mx = e.getX();
		int my = e.getY();
		
		if(dice != null){
			if(enableModifier){
				if(dice.getX() + offsetX - OVAL_WIDTH/2 > 0){
					if(mx > dice.getX() + offsetX - OVAL_WIDTH/2 && mx < dice.getX() + offsetX + OVAL_WIDTH/2 && 
							my > dice.getY() + offsetY - OVAL_HEIGHT - pixelsAboveDice - dice.getHeight()/2 && my < dice.getY() + offsetY + dice.getHeight()/2){
						for(int i = 0; i < displayDice.length; i++){
							Dice d = displayDice[i];
							if(mx > d.getX() - d.getWidth()/2  && mx < displayDice[i].getX() + displayDice[i].getWidth()/2 
									&& my > d.getY() - d.getHeight()/2  && my < displayDice[i].getY() + displayDice[i].getHeight()/2){
								fireSelected(d.getValue());
							}
						}
					}
				}else{
					if(mx > dice.getX() + offsetX - dice.getWidth() && mx < dice.getX() + offsetX - dice.getWidth() + OVAL_WIDTH && 
							my > dice.getY() + offsetY - OVAL_HEIGHT - pixelsAboveDice - dice.getHeight()/2 && my < dice.getY() + offsetY + dice.getHeight()/2){
						for(int i = 0; i < displayDice.length; i++){
							Dice d = displayDice[i];
							if(mx > d.getX() - d.getWidth()/2  && mx < displayDice[i].getX() + displayDice[i].getWidth()/2 
									&& my > d.getY() - d.getHeight()/2  && my < displayDice[i].getY() + displayDice[i].getHeight()/2){
								fireSelected(d.getValue());
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {

		int mx = e.getX();
		int my = e.getY();
		
		if(dice != null){
			if(enableModifier){
				if(dice.getX() + offsetX - OVAL_WIDTH/2 > 0){
					if(mx > dice.getX() + offsetX - OVAL_WIDTH/2 && mx < dice.getX() + offsetX + OVAL_WIDTH/2 && 
							my > dice.getY() + offsetY - OVAL_HEIGHT - pixelsAboveDice - dice.getHeight()/2 && my < dice.getY() + offsetY + dice.getHeight()/2){
						for(int i = 0; i < displayDice.length; i++){
							Dice d = displayDice[i];
							if(mx > d.getX() - d.getWidth()/2 && mx < displayDice[i].getX() + displayDice[i].getWidth()/2 
									&& my > d.getY() - d.getHeight()/2  && my < displayDice[i].getY() + displayDice[i].getHeight()/2){
								d.setHighlight(true);
							}else{
								d.setHighlight(false);
							}
						}
					}else{
						fireExited();
					}
				}else{
					if(mx > dice.getX() + offsetX - dice.getWidth() && mx < dice.getX() + offsetX - dice.getWidth() + OVAL_WIDTH && 
							my > dice.getY() + offsetY - OVAL_HEIGHT - pixelsAboveDice - dice.getHeight()/2 && my < dice.getY() + offsetY + dice.getHeight()/2){
						for(int i = 0; i < displayDice.length; i++){
							Dice d = displayDice[i];
							if(mx > d.getX() - d.getWidth()/2 && mx < displayDice[i].getX() + displayDice[i].getWidth()/2 
									&& my > d.getY() - d.getHeight()/2  && my < displayDice[i].getY() + displayDice[i].getHeight()/2){
								d.setHighlight(true);
							}else{
								d.setHighlight(false);
							}
						}
					}else{
						fireExited();
					}
				}
			}
		}
	}
}
