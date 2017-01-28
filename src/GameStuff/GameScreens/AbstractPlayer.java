package GameStuff.GameScreens;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public abstract class AbstractPlayer extends AbstractScreen{
	
	Scorecard score;
	DiceManager dice;
	
	int rolls = 0;
	int turns = 0;
	
	boolean gameFinished = false;
	
	String name;
	
	int WIDTH = 600;
	int HEIGHT = 600;
	
	boolean debug;
	
	AbstractPlayer(String name, Color backColor, boolean debug){
		super(backColor);
		
		dice = new DiceManager(30, 175, 325, 375, Color.YELLOW, debug);
		score = new Scorecard(390, 120, 180, 425, Color.BLACK);
		
		this.name = name;
		this.debug = debug;
		
		this.setSize(WIDTH, HEIGHT);
		this.setVisible(true);
	}
	
	public void addDiceMouseListeners(){
		this.addMouseListener(dice);
		this.addMouseMotionListener(dice);
		
		dice.addDiceModMouseListeners(this);
	}
	
	public void addScoreMouseListeners(){
		this.addMouseListener(score);
		this.addMouseMotionListener(score);
	}
	
	public void removeDiceMouseListeners(){
		this.removeMouseListener(dice);
		this.removeMouseMotionListener(dice);
		
		dice.removeDiceModMouseListeners(this);
	}
	
	public void removeScoreMouseListeners(){
		this.removeMouseListener(score);
		this.removeMouseMotionListener(score);
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isFinished(){
		return gameFinished;
	}
	
	public int getTotalScore(){
		return score.getTotal();
	}
	
	public Scorecard getScorecard(){
		return score;
	}
	
	abstract public void startTurn();
	abstract public void endGame();
}
