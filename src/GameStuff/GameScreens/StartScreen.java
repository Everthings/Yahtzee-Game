package GameStuff.GameScreens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import GameStuff.GameScreens.Buttons.*;

public class StartScreen extends AbstractScreen{

	int bWidth = 200;
	int bHeight = 100;

	private BufferedImage dice;
	private double theta = 0;
	
	private ArrayList<OptionButton> buttons = new ArrayList<OptionButton>();
	private ArrayList<ButtonClickListener> listenerList = new ArrayList<ButtonClickListener>();

	public StartScreen(){
		super(Color.ORANGE);
		
		OptionButton playButton = new OptionButton((int)((double)WIDTH / 4 - ((double)bWidth / 2)), (int)((double)HEIGHT / 2.0 - ((double)bHeight / 2)) - bHeight + 50, bWidth, bHeight, "Play", Mode.GAME_SCREEN, this);
		OptionButton settingsButton = new OptionButton((int)((3 * (double)WIDTH / 4) - ((double)bWidth / 2)), (int)((double)HEIGHT / 2.0 - ((double)bHeight / 2)) - bHeight + 50, bWidth, bHeight, "Settings", Mode.SETTINGS_SCREEN, this);
		OptionButton exitButton = new OptionButton((int)((double)WIDTH / 2 - ((double)bWidth / 2)), (int)((double)HEIGHT / 2.0 - ((double)bHeight / 2)) + 100 + bHeight, bWidth, bHeight, "Exit", Mode.EXIT, this);
		buttons.add(playButton);
		buttons.add(settingsButton);
		buttons.add(exitButton);

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
		
		settingsButton.addClickListener(new ButtonClickListener(){
			@Override
			public void clicked(Mode m) {
				//propagate event upward
				fireClicked(Mode.SETTINGS_SCREEN);
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
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		g.setFont(new Font("Courier", Font.BOLD, 64));
		g.setColor(Color.YELLOW);
		g.drawString("YAHTZEE", 170, 100);
		
		AffineTransform original = g2.getTransform();
		AffineTransform diceRotation1 = new AffineTransform();
		diceRotation1.translate(90, 45);
		diceRotation1.rotate(Math.toRadians(theta), dice.getWidth(null)/2, dice.getHeight(null)/2);
		AffineTransform diceRotation2 = new AffineTransform();
		diceRotation2.translate(440, 45);
		diceRotation2.rotate(Math.toRadians(-theta), dice.getWidth(null)/2, dice.getHeight(null)/2);
		g2.setTransform(diceRotation1);
		g2.drawImage(dice, 0, 0, null);
		g2.setTransform(diceRotation2);
		g2.drawImage(dice, 0, 0, null);
		g2.setTransform(original);
		
		for(OptionButton ob: buttons){
			ob.draw(g);
		}	
		
		theta += 4;
	}	
}