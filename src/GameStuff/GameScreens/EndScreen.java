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

import javax.imageio.ImageIO;

import GameStuff.Statistics;
import GameStuff.GameScreens.Buttons.AbstractButton;
import GameStuff.GameScreens.Buttons.Button;
import GameStuff.GameScreens.Buttons.ButtonClickInterface;
import GameStuff.GameScreens.Buttons.ButtonDrawInterface;
import GameStuff.GameScreens.Buttons.Mode;
import GameStuff.GameScreens.Buttons.OptionButton;

public class EndScreen extends AbstractScreen{

	private int bWidth = 150;
	private int bHeight = 100;
	
	private ArrayList<AbstractButton> buttons = new ArrayList<AbstractButton>();
	
	private Statistics stats = new Statistics(5);
	private int displayIndex = 0;
	
	private BufferedImage arrow;
	
	private Scorecard[] scores = null;
	private String[] names = null;
	private int[] newHighScorePositions = null;;

	public EndScreen(){
		super(Color.ORANGE);
		
		this.setSize(WIDTH, HEIGHT);
		this.setVisible(true);
		
		OptionButton playButton = new OptionButton((int)(((double)1/6) * WIDTH - (double)bWidth/2), HEIGHT - bHeight - 50, bWidth, bHeight, "Play Again", Mode.GAME_SCREEN, this);
		OptionButton settingsButton = new OptionButton((int)((1 * (double)WIDTH / 3) + ((double)1/6) * WIDTH - (double)bWidth/2), HEIGHT - bHeight - 50, bWidth, bHeight, "Settings", Mode.SETTINGS_SCREEN, this);
		OptionButton exitButton = new OptionButton((int)((2 * (double)WIDTH / 3) + ((double)1/6) * WIDTH - (double)bWidth/2), HEIGHT - bHeight - 50, bWidth, bHeight, "Exit", Mode.EXIT, this);
		
		buttons.add(playButton);
		buttons.add(settingsButton);
		buttons.add(exitButton);

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
		
		try {
			arrow = ImageIO.read(new File("/Users/XuMan/Documents/BB&N/Yahtzee Game/src/Images/Arrow.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Button nextButton = new Button(new ButtonClickInterface(){
			
			@Override
			public void buttonClicked() {
				displayIndex = ++displayIndex % (scores.length + 1);
			}
			
		}, new ButtonDrawInterface(){

			@Override
			public void draw(Graphics g) {
				
				Graphics2D g2 = (Graphics2D) g;
				
				AffineTransform original = g2.getTransform();
				AffineTransform diceRotation1 = new AffineTransform();
				diceRotation1.translate(115, 65);
				g2.setTransform(diceRotation1);
				g2.drawImage(arrow, 0, 0, null);
				g2.setTransform(original);
			}
			
		}, 100, 50, 50, 50, 10, 10, this);
		
		Button previousButton = new Button(new ButtonClickInterface(){
			@Override
			public void buttonClicked() {
				displayIndex--;
				
				if(displayIndex < 0){
					displayIndex = scores.length;
				}
			}
			
		},  new ButtonDrawInterface(){

			@Override
			public void draw(Graphics g) {
				
				Graphics2D g2 = (Graphics2D) g;
				
				AffineTransform original = g2.getTransform();
				AffineTransform diceRotation1 = new AffineTransform();
				diceRotation1.translate(65, 65);
				diceRotation1.rotate(Math.toRadians(180), arrow.getWidth()/2, arrow.getHeight()/2);
				g2.setTransform(diceRotation1);
				g2.drawImage(arrow, 0, 0, null);
				g2.setTransform(original);
			}
			
		}, 50, 50, 50, 50, 10, 10, this);
		
		buttons.add(nextButton);
		buttons.add(previousButton);
		
		repaint();
	}
	
	public void setData(Scorecard[] scores, String[] names, int[] newHighScorePositions){
	this.newHighScorePositions = newHighScorePositions;
		
		this.scores = scores;
		this.names = names;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		for(AbstractButton ob: buttons){
			ob.draw(g);
		}
		
		if(displayIndex == 0){
			stats.draw(g, WIDTH, HEIGHT);
		}else if(displayIndex > 0){
			
			Font myFont = new Font("Courier", Font.PLAIN, 30);
			AffineTransform stringTransform = new AffineTransform();
			stringTransform.translate(430, 15);
			stringTransform.rotate(Math.toRadians(90), 0, 0);
			myFont = myFont.deriveFont(stringTransform);
			g.setFont(myFont);
			g2.drawString(names[displayIndex - 1], 0, 0);	
			
			stringTransform =  new AffineTransform();
			stringTransform.translate(400, 15);
			stringTransform.rotate(Math.toRadians(90), 0, 0);
			myFont = myFont.deriveFont(stringTransform);
			g.setFont(myFont);
			g2.drawString("Score Rank: " + newHighScorePositions[displayIndex - 1], 0, 0);
			
			scores[displayIndex - 1].setX(210);
			scores[displayIndex - 1].setY(15);
			scores[displayIndex - 1].draw(g, false);
			
		}
	}
}
