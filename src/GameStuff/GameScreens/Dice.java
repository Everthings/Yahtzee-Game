package GameStuff.GameScreens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import Utilities.GraphicsUtils;


public class Dice{
	private int x;//x and y relative to the start of the dice bounds - (0, 0) is the top left of the rectangular area
	private int y;
	
	private int width;
	private int height;
	
	private int rotation;
	private int rotChange;// change in rotation
	private boolean isHeld = false;
	
	private BufferedImage image;
	private boolean highlight = false;
	
	private int value;
	
	private double mx;//movement x and y
	private double my;
	
	public Dice(BufferedImage image, int x, int y, int value, int rotation, int rotChange, double mx, double my){
		this.image = image;
		
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		this.x = x;
		this.y = y;
		this.mx = mx;
		this.my = my;
		
		this.value = value;
		
		this.rotChange = rotChange;
		this.rotation = rotation;
	}
	
	public void addMx(double value){
		mx += value;
	}
	
	public void addMy(double value){
		my += value;
	}
	
	public void updateRotation(){
		rotation = (rotChange + rotation) % 360;
		rotChange -= 2;
		if(rotChange < 0){
			rotChange = 0;
		}
	}
	
	public void updatePosition(int dmWidth, int dmHeight){
		x += mx;
		y += my;
		
		if(x - width/2 < 0){
			mx = -mx;
			x = width/2;
		}else if(x + width/2 > dmWidth){
			mx = -mx;
			x = dmWidth - width/2;
		}
		
		if(y - height/2 < 0){
			my = -my;
			y = height/2;
		}else if(y + height/2 > dmHeight){
			my = -my;
			y = dmHeight - height/2;
		}
		
		if(mx <= -1){
			mx++;
		}else if(mx >= 1){
			mx--;
		}else{
			mx = 0;
		}
		
		if(my <= -1){
			my++;
		}else if(my >= 1){
			my--;
		}else{
			my = 0;
		}
	}
	
	public boolean isHeld(){
		return isHeld;
	}
	
	public void hold(){
		isHeld = true;
	}
	
	public void unhold(){
		isHeld = false;
	}
	
	public int getValue(){
		return value;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setX(int value){
		x =  value;
	}
	
	public void setY(int value){
		y = value;
	}
	
	public double getMX(){
		return mx;
	}
	
	public double getMY(){
		return my;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getRotation(){
		return rotation;	
	}
	
	public int getRotationChange(){
		return rotChange;	
	}
	
	public void setHighlight(boolean value){
		highlight = value;	
	}
	
	public void draw(Graphics g, int dmX, int dmY, boolean greyScale){
		
		Graphics2D g2 = (Graphics2D) g;
		
		AffineTransform original = g2.getTransform();
		AffineTransform diceRotation1 = new AffineTransform();
		diceRotation1.translate(x + dmX - width/2, y + dmY - height/2);
		diceRotation1.rotate(Math.toRadians(rotation), width/2, height/2);
		g2.setTransform(diceRotation1);
		g2.drawImage(image, 0, 0, null);
		if(highlight || isHeld){
			if(greyScale)
				g2.setColor(GraphicsUtils.getGreyScale(Color.RED));
			else
				g2.setColor(Color.RED);
			g2.drawRect(0, 0, width, height);
		}
		g2.setTransform(original);
		
	}
}

