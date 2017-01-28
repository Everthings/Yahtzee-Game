package GameStuff.GameScreens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import GameStuff.GameScreens.Buttons.*;


public class SettingsScreen extends AbstractScreen{
	
	Random r = new Random();
	
	int selectionIndex = 0;
	
	int sWidth = 50;
	int sHeight = 50;
	
	int oWidth = 200;
	int oHeight = 100;
			
	ArrayList<SettingsButton> sButtons = new ArrayList<SettingsButton>();
	ArrayList<OptionButton> oButtons = new ArrayList<OptionButton>();
	
	int[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	Color[] debugColors = {Color.RED, Color.GREEN};
	
	private BufferedImage dice;
	
	private int theta = 0;
	
	public SettingsScreen(){
		super(Color.ORANGE);
		//first button = numPlayers
		//second button = numAI
		sButtons.add(new SettingsButton((int)((double)WIDTH / 3 - ((double)sWidth / 2)), 200, sWidth, sHeight, this, values, 1, "Number of Human Players"));
		sButtons.add(new SettingsButton((int)((2 * (double)WIDTH / 3) - ((double)sWidth / 2)), 200, sWidth, sHeight, this, values, 0, "Number of AI Player"));
		sButtons.add(new SettingsButton((int)(((double)WIDTH / 2) - ((double)sWidth / 2)), 300, sWidth, sHeight, this, debugColors, 0, "Debug"));
			
		OptionButton playButton = new OptionButton((int)(((double)WIDTH / 2.0) - oWidth - 20), HEIGHT - oHeight - 100, oWidth, oHeight, "Play", Mode.GAME_SCREEN, this);
		OptionButton exitButton = new OptionButton((int)(((double)WIDTH / 2.0) + 20), HEIGHT - oHeight - 100, oWidth, oHeight, "Exit", Mode.EXIT, this);
		
		oButtons.add(playButton);
		oButtons.add(exitButton);

		try {
			dice = ImageIO.read(new File("/Users/XuMan/Documents/BB&N/Yahtzee Game/src/Images/Black_Dice.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		playButton.addClickListener(new ButtonClickListener(){
			@Override
			public void clicked(Mode m) {
				//propagate event upward
				fireClicked(Mode.GAME_SCREEN);
			}
		});
		
		exitButton.addClickListener(new ButtonClickListener(){
			@Override
			public void clicked(Mode m) {
				//propagate event upward
				fireClicked(Mode.EXIT);
			}
		});
	
		repaint();
	}
	
	public int getNumPlayers(){
		return (int)sButtons.get(0).getValue();
	}
	
	public int getNumAI(){
		return (int)sButtons.get(1).getValue();
	}
	
	public boolean getDebug(){
		
		Color debug = (Color) sButtons.get(2).getValue();
		
		if(debug.equals(Color.GREEN)){
			return true;
		}
		
		return false;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		g.setFont(new Font("Courier", Font.BOLD, 64));
		g.setColor(Color.YELLOW);
		g.drawString("Settings", 150, 100);
		
		AffineTransform original = g2.getTransform();
		AffineTransform diceRotation1 = new AffineTransform();
		diceRotation1.translate(70, 45);
		diceRotation1.rotate(Math.toRadians(theta), dice.getWidth(null)/2, dice.getHeight(null)/2);
		AffineTransform diceRotation2 = new AffineTransform();
		diceRotation2.translate(460, 45);
		diceRotation2.rotate(Math.toRadians(-theta), dice.getWidth(null)/2, dice.getHeight(null)/2);
		g2.setTransform(diceRotation1);
		g2.drawImage(dice, 0, 0, null);
		g2.setTransform(diceRotation2);
		g2.drawImage(dice, 0, 0, null);
		g2.setTransform(original);
		
		for(AbstractButton b: sButtons){
			b.draw(g);
		}
		
		for(AbstractButton b: oButtons){
			b.draw(g);
		}
		
		theta += 4;
	}
}
