package GameStuff;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Utilities.AIUtils;
import GameStuff.GameScreens.AIPlayer;
import GameStuff.GameScreens.AbstractPlayer;
import GameStuff.GameScreens.ButtonClickListener;
import GameStuff.GameScreens.ClickListener;
import GameStuff.GameScreens.NameScreen;
import GameStuff.GameScreens.Score;
import GameStuff.GameScreens.Scorecard;
import GameStuff.GameScreens.UserPlayer;
import GameStuff.GameScreens.Buttons.Mode;
import GameStuff.GameScreens.Buttons.TextListener;


public class Game{
	
	Statistics stats = new Statistics(5);
	ArrayList<AbstractPlayer> players = new ArrayList<AbstractPlayer>();
	
	JFrame window;
	
	JPanel currentPanel = null;
	int index = 0;
	
	private String[] names = null;
	private int namesIndex = 0;
	private Scorecard[] scores = null;
	private int[] newPositions = null;
	
	private ArrayList<ButtonClickListener> listenerList = new ArrayList<ButtonClickListener>();
	
	private AIUtils aiUtils;//only one of this should be created

	public Game(int numUsers, int numAI, boolean debug, JFrame window){
		
		if(numAI > 0){
			aiUtils = new AIUtils();
		}
		
		names = new String[numUsers + numAI];
		scores = new Scorecard[numUsers + numAI];
		
		String nameText = "Enter a Name:";
		
		TextListener nameListener = new TextListener(){

			@Override
			public void entered(String text) {
				
				names[namesIndex] = text;
				namesIndex++;
				
				if(namesIndex < numUsers + numAI){
					getNextName(nameText, this);
				}else{
					initPlayers(numUsers, numAI, debug);
					nextPlayer();
				}
			}
		};
		
		this.window = window;
		
		if(numAI + numUsers > 0)
			getNextName(nameText, nameListener);
	}
	
	public void initPlayers(int numUsers, int numAI, boolean debug){
		
		ClickListener playerListener = new ClickListener(){
			public void clicked() {
				nextPlayer();
			}
		};
		
		for(int i = 0; i < numUsers; i++){
			UserPlayer p = new UserPlayer(names[i], Color.ORANGE, debug);
			players.add(p);
		}
		
		for(int i = 0; i < numAI; i++){
			AIPlayer p = new AIPlayer(names[i + numUsers], Color.ORANGE, aiUtils, debug);
			players.add(p);
		}

		for(int i = 0; i < numUsers; i++){
			UserPlayer p = (UserPlayer)players.get(i);
			p.addClickListener(playerListener);
		}
		
		for(int i = numUsers; i < numAI + numUsers; i++){
			AIPlayer p = (AIPlayer)players.get(i);
			p.addClickListener(playerListener);
		}
	}
	
	public void getNextName(String text, TextListener tl){
		
		if(currentPanel != null)
			window.remove(currentPanel);
		
		NameScreen nm = new NameScreen(Color.ORANGE, text);
		nm.addTextListener(tl);
		
		window.add(nm);
		window.repaint();

		currentPanel = nm;
	}
	
	public void addClickListener(ButtonClickListener l) {
		  listenerList.add(l);
	}
		
	public void removeClickListener(ButtonClickListener l) {
	  listenerList.remove(l);
	}
	
    protected void fireClicked() {
    	for(ButtonClickListener cl: listenerList){
    		cl.clicked(Mode.END_SCREEN);
    	}
	}
	
	public void nextPlayer(){// changes to the next player; setup the ending screen and statistics if the last player has finished
		if(!players.isEmpty()){

			if(currentPanel != null)
				window.remove(currentPanel);
			
			if(!players.get(players.size() - 1).isFinished()){
				
				window.add(players.get(index));
				players.get(index).startTurn();
				window.repaint();

				currentPanel = players.get(index);
				
				index++;
				
				index = index % players.size();
			}else{
				
				Score[] scores = new Score[players.size()];
				
				for(int i = 0; i < players.size(); i++){
					players.get(i).endGame();
					players.get(i).getScorecard().setMouseOverElement(-1);
					scores[i] = new Score(players.get(i).getTotalScore(), players.get(i).getName());
				}
				
				newPositions = stats.updateHighScores(scores);
				
				compileScores();
				
				fireClicked();
			}
		}
	}
	
	public void compileScores(){
		int index = 0;
		
		for(AbstractPlayer ap: players){
			scores[index] = ap.getScorecard();
			index++;
		}
	}
	
	public Scorecard[] getScoreArray(){
		return scores;
	}
	
	public String[] getNameArray(){
		return names;
	}
	
	public int[] getPosions(){
		return newPositions;
	}
}
