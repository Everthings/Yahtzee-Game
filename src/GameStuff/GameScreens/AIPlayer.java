package GameStuff.GameScreens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import GameStuff.GameScreens.Buttons.AbstractButton;
import GameStuff.GameScreens.Buttons.Button;
import GameStuff.GameScreens.Buttons.ButtonClickInterface;
import GameStuff.GameScreens.Buttons.ButtonDrawInterface;
import Utilities.AIUtils;

public class AIPlayer extends AbstractPlayer{

	private AIUtils utils;
	private ArrayList<Action> aiActions = new ArrayList<Action>();
	private ArrayList<ClickListener> listenerList = new ArrayList<ClickListener>();
	
	private double[] categoryWeights = {0.3, 0.5, 1, 1, 1.25, 1.5, 1, 1, 0.5, 1, 1, 1, 1};// must be length 13
	
	private boolean done = false;
	
	private boolean play = true;
	
	private BufferedImage playImage;
	private BufferedImage pauseImage;
	
	private ArrayList<AbstractButton> buttons = new ArrayList<AbstractButton>();
	
	private Timer timer;
	
	public AIPlayer(String name, Color backColor, AIUtils utils, boolean debug) {
		super(name, backColor, debug);
		
		timer = new Timer();
		
		DiceListener diceListener = new DiceListener(){

			@Override
			public void rolled() {
		
			}

			@Override
			public void doneRolling() {
				
				aiActions.add(new Action(){

					@Override
					public void run() {
						//get the dice rolled
						if(holdDice() || dice.getNumRolls() >= dice.getMaxRolls()){// if all the dice are held or the ai has rolled 3 times, mark the score and end the turn
							
							while(aiActions.size() > 1){
								aiActions.remove(1);
							}
							
							aiActions.add(new Action(){

								@Override
								public void run() {
									done = true;
								}
								
							});	
						}else{// if the if has not finished roll again
							aiActions.add(new Action(){

								@Override
								public void run() {
									dice.rollDice();
								}
							
							});
						}
					}
					
				});
			}
		};
		
		try {
			playImage = ImageIO.read(new File("/Users/XuMan/Documents/BB&N/Yahtzee Game/src/Images/Play.png"));
			pauseImage = ImageIO.read(new File("/Users/XuMan/Documents/BB&N/Yahtzee Game/src/Images/Pause.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buttons.add(new Button(new ButtonClickInterface(){
			
			@Override
			public void buttonClicked() {
				play = !play;
			}
			
		}, new ButtonDrawInterface(){

			@Override
			public void draw(Graphics g) {
				
				Graphics2D g2 = (Graphics2D) g;
				
				AffineTransform original = g2.getTransform();
				AffineTransform diceRotation1 = new AffineTransform();
				diceRotation1.translate(25, 25);
				g2.setTransform(diceRotation1);
				if(play)
					g2.drawImage(pauseImage, 0, 0, null);
				else
					g2.drawImage(playImage, 0, 0, null);
				g2.setTransform(original);
				
			}
			
		}, 10, 10, 50, 50, 10, 10, this));
	
		
		if(debug){
			DiceModifierListener diceModListener = new DiceModifierListener(){
	
				@Override
				public void diceSelected(int num) {
					
					while(aiActions.size() > 1){
						aiActions.remove(1);
					}
					
					if(holdDice() || dice.getNumRolls() >= dice.getMaxRolls()){// if all the dice are held or the ai has rolled 3 times, mark the score and end the turn
						aiActions.add(new Action(){

							@Override
							public void run() {
								done = true;
							}
							
						});	
					}else{// if the if has not finished roll again
						aiActions.add(new Action(){

							@Override
							public void run() {
								dice.rollDice();
							}
						
						});
					}
				}
	
				@Override
				public void diceClicked() {
					
				}
	
				@Override
				public void mouseExited() {
					
				}
				
			};
			
			dice.enableRolling(false);
			
			dice.addDiceModifierListener(diceModListener);
		
			addDiceMouseListeners();
		}
		
		dice.addDiceListener(diceListener);
		
		this.utils = utils;
	}

	public boolean holdDice(){
		Dice[] myDice = dice.getDice();
		
		int[] myDiceValues = new int[myDice.length];
		for(int i = 0; i < myDice.length; i++){
			myDiceValues[i] = myDice[i].getValue();
		}
	
		boolean[] hold = getBestMove(myDiceValues);
	
		boolean allHeld = true;
		
		for(int i = 0; i < myDice.length; i++){// holds the ai dice
			
			int index = i;
			
			if(hold[i]){
				if(!myDice[i].isHeld()){
					aiActions.add(new Action(){// adds hold action so the ai can hold sequentially
	
						@Override
						public void run() {
							myDice[index].hold();
						}
						
					});
				}
			}else{
				if(myDice[i].isHeld()){
					aiActions.add(new Action(){// adds unhold action so ai can unhold sequentially
	
						@Override
						public void run() {
							myDice[index].unhold();
						}
						
					});
				}
				allHeld = false;
			}
		}
			
		return allHeld;
	}
	
	public void addClickListener(ClickListener l) {
		listenerList.add(l);
	}
		
	public void removeClickListener(ClickListener l) {
		listenerList.remove(l);
	}
		
	protected void fireClicked() {
	  	for(ClickListener cl: listenerList){
	  		cl.clicked();
	  	}
	}
	
	@Override
	public void startTurn() {
		dice.resetDice();
		dice.rollDice();
		
		handleTurn();
	}
	
	public void handleTurn(){
		//pre: handles the ai player's turn; sets a new timer which is responsible for running the ai's actions
		int timeBetweenTurns = 500;//milliseconds
		done = false;
		
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				if(!done){
						
					if(play){
						if(!aiActions.isEmpty()){
							aiActions.get(0).run();// executes the actions
							aiActions.remove(0); // deletes the action to run the next one
						}
					}
	
				}else{
					aiActions.clear();

					//mark score and tell the game to move to the next player
					
					if(dice.isYahtzee() && score.getNumYahtzees() >= 1){
						score.markJoker();
					}
					
					int category = getHighestChoice();
					score.markScore(category, dice.determineScore(category));
					
					//highlight the new score
					if(category > 5){
						category += 2;
					}
					score.setMouseOverElement(category);
					
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//unhighlight the new score
					score.setMouseOverElement(-1);
					fireClicked();
					
					this.cancel();
					timer.purge();
				}
			}
		}, 0, timeBetweenTurns);
		
		turns++;
    	
		if(turns >= 13){
			gameFinished = true;
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		this.setBackground(backColor);
		
		super.paintComponent(g);

		g.setFont(new Font("Courier", Font.BOLD, 64));
		g.setColor(Color.YELLOW);
		g.drawString(name, (int)(((double)WIDTH / 2.0) - (39 * (double)name.length() / 2.0)), 100);
		
		for(AbstractButton ab: buttons){
			ab.draw(g);
		}
		
		dice.draw(g, false);
		score.draw(g, false);
	}
	
	public boolean[] getBestMove(int[] dice){
		
		boolean[] bestHold = null;
		double bestValue = -1;
		
		for(int a = 0; a < 2; a++){
			for(int b = 0; b < 2; b++){
				for(int c = 0; c < 2; c++){
					for(int d = 0; d < 2; d++){
						for(int e = 0; e < 2; e++){
							
							boolean[] hold = {a == 1, b == 1, c == 1, d == 1, e == 1};
							
							double value = getExpectedValue(hold, dice);
							
							if(value > bestValue){
								bestValue = value;
								bestHold = hold;
							}
						}
					}
				}
			}
		}
		
		return bestHold;
	}
	
	private double getWeightedValue(double value, int scoreIndex){
		return value * categoryWeights[scoreIndex];
	}
	
	private double getExpectedValue(boolean[] hold, int[] dice){
		
		int value = 0;
		double probability;
		
		ArrayList<Integer> rerollIndices = new ArrayList<Integer>();
		
		for(int i = 0; i < hold.length; i++){
			if(!hold[i]){
				rerollIndices.add(i);
			}
		}
		
		int numReroll = rerollIndices.size();
		probability = 1.0/(Math.pow(6, numReroll));

		if(numReroll == 0){
			value = calculateZeroRerollExpectation(dice);
		}else if(numReroll == 1){// one reroll
			value = calculateOneRerollExpectation(rerollIndices, dice);
		}else if(numReroll == 2){ // two rerolls
			value = calculateTwoRerollExpectation(rerollIndices, dice);
		}else if(numReroll == 3){ // three rerolls
			value = calculateThreeRerollExpectation(rerollIndices, dice);
		}else if(numReroll == 4){ // four rerolls
			value = calculateFourRerollExpectation(rerollIndices, dice);
		}else if(numReroll == 5){ // five rerolls
			value = calculateFiveRerollExpectation(rerollIndices, dice);
		}
		
		return value * probability;
	}
	
	private int calculateZeroRerollExpectation(int[] dice){
		
		int value = 0;
		
		int[] tempDice = (int[])dice.clone();
		
		tempDice = convertToDiceContents(tempDice);
		
		for(int a = 0; a < 13; a++){
			if(score.isEligibleScore(a))// only add eligible categories
				value += getWeightedValue(utils.get(a, tempDice), a);
			else if(a == 12)
				value += getWeightedValue(utils.get(a, tempDice), a);// always add yahtzee chance	
		}
		
		return value;
	}
	
	private int calculateOneRerollExpectation(ArrayList<Integer> rerollIndices, int[] dice){
		
		int value = 0;
		
		for(int i = 1; i <= 6; i++){// six possible values to reroll so i = 1 through 6
			for(int a = 0; a < 13; a++){
				if(score.isEligibleScore(a)){// only add eligible categories
					int[] tempDice = (int[])dice.clone();
					
					tempDice[rerollIndices.get(0)] = i;
					
					tempDice = convertToDiceContents(tempDice);
					
					value += getWeightedValue(utils.get(a, tempDice), a);
				}else if(a == 12){// always add yahtzee chance
					int[] tempDice = (int[])dice.clone();
					
					tempDice[rerollIndices.get(0)] = i;
					
					tempDice = convertToDiceContents(tempDice);
					
					value += getWeightedValue(utils.get(a, tempDice), a);
				}
			}
		}
		
		return value;
	}
	
	private int calculateTwoRerollExpectation(ArrayList<Integer> rerollIndices, int[] dice){
		
		int value = 0;
		
		for(int i = 1; i <= 6; i++){// six possible values to reroll so i = 1 through 6
			for(int j = 1; j <= 6; j++){
				for(int a = 0; a < 13; a++){
					if(score.isEligibleScore(a)){// only add eligible categories
						int[] tempDice = (int[])dice.clone();
						
						tempDice[rerollIndices.get(0)] = i;
						tempDice[rerollIndices.get(1)] = j;
						
						tempDice = convertToDiceContents(tempDice);
						
						value += getWeightedValue(utils.get(a, tempDice), a);
					}else if(a == 12){// always add yahtzee chance
						int[] tempDice = (int[])dice.clone();
						
						tempDice[rerollIndices.get(0)] = i;
						tempDice[rerollIndices.get(1)] = j;
						
						tempDice = convertToDiceContents(tempDice);
						
						value += getWeightedValue(utils.get(a, tempDice), a);
					}
				}
			}
		}
		
		return value;
	}
	
	private int calculateThreeRerollExpectation(ArrayList<Integer> rerollIndices, int[] dice){
		
		int value = 0;
		
		for(int i = 1; i <= 6; i++){// six possible values to reroll so i = 1 through 6
			for(int j = 1; j <= 6; j++){
				for(int k = 1; k <= 6; k++){
					for(int a = 0; a < 13; a++){
						if(score.isEligibleScore(a)){// only add eligible categories
							int[] tempDice = (int[])dice.clone();
							
							tempDice[rerollIndices.get(0)] = i;
							tempDice[rerollIndices.get(1)] = j;
							tempDice[rerollIndices.get(2)] = k;
							
							tempDice = convertToDiceContents(tempDice);
							
							value += getWeightedValue(utils.get(a, tempDice), a);
						}else if(a == 12){// always add yahtzee chance
							int[] tempDice = (int[])dice.clone();
							
							tempDice[rerollIndices.get(0)] = i;
							tempDice[rerollIndices.get(1)] = j;
							tempDice[rerollIndices.get(2)] = k;
							
							tempDice = convertToDiceContents(tempDice);
							
							value += getWeightedValue(utils.get(a, tempDice), a);
						}
					}
				}
			}
		}
		
		return value;
	}
	
	private int calculateFourRerollExpectation(ArrayList<Integer> rerollIndices, int[] dice){
		
		int value = 0;
		
		for(int i = 1; i <= 6; i++){// six possible values to reroll so i = 1 through 6
			for(int j = 1; j <= 6; j++){
				for(int k = 1; k <= 6; k++){
					for(int l = 1; l <= 6; l++){
						for(int a = 0; a < 13; a++){
							if(score.isEligibleScore(a)){// only add eligible categories
								int[] tempDice = (int[])dice.clone();
								
								tempDice[rerollIndices.get(0)] = i;
								tempDice[rerollIndices.get(1)] = j;
								tempDice[rerollIndices.get(2)] = k;
								tempDice[rerollIndices.get(3)] = l;
								
								tempDice = convertToDiceContents(tempDice);
								
								value += getWeightedValue(utils.get(a, tempDice), a);
							}else if(a == 12){// always add yahtzee chance
								int[] tempDice = (int[])dice.clone();
								
								tempDice[rerollIndices.get(0)] = i;
								tempDice[rerollIndices.get(1)] = j;
								tempDice[rerollIndices.get(2)] = k;
								tempDice[rerollIndices.get(3)] = l;
								
								tempDice = convertToDiceContents(tempDice);
								
								value += getWeightedValue(utils.get(a, tempDice), a);
							}
						}
					}
				}
			}
		}
		
		return value;
	}
	
	private int calculateFiveRerollExpectation(ArrayList<Integer> rerollIndices, int[] dice){
		
		int value = 0;
		
		for(int i = 1; i <= 6; i++){// six possible values to reroll so i = 1 through 6
			for(int j = 1; j <= 6; j++){
				for(int k = 1; k <= 6; k++){
					for(int l = 1; l <= 6; l++){
						for(int m = 1; m <= 6; m++){
							for(int a = 0; a < 13; a++){
								if(score.isEligibleScore(a)){// only add eligible categories
									int[] tempDice = (int[])dice.clone();
									
									tempDice[rerollIndices.get(0)] = i;
									tempDice[rerollIndices.get(1)] = j;
									tempDice[rerollIndices.get(2)] = k;
									tempDice[rerollIndices.get(3)] = l;
									tempDice[rerollIndices.get(4)] = m;
									
									tempDice = convertToDiceContents(tempDice);
									
									value += getWeightedValue(utils.get(a, tempDice), a);
								}else if(a == 12){// always add yahtzee chance
									int[] tempDice = (int[])dice.clone();
									
									tempDice[rerollIndices.get(0)] = i;
									tempDice[rerollIndices.get(1)] = j;
									tempDice[rerollIndices.get(2)] = k;
									tempDice[rerollIndices.get(3)] = l;
									tempDice[rerollIndices.get(4)] = m;
									
									tempDice = convertToDiceContents(tempDice);
									
									value += getWeightedValue(utils.get(a, tempDice), a);
								}
							}
						}
					}
				}
			}
		}
		
		return value;
	}
	
	private int[] convertToDiceContents(int[] dice){//converts to a length-6 array which is sorted by {# ones, # twos, # threes, # fours, # fives, # sixes}
		int[] diceContents = new int[6];
		
		for(int i = 0; i < dice.length; i++){
			diceContents[dice[i] - 1]++;
		}
		
		return diceContents;
	}
	
	private int getHighestChoice(){

		int highestValue = -1;
		int highestChoice = -1;
		
		for(int i = 3; i <= 6; i++){// threes through sixes
			if(dice.hasNumber(i) && score.isEligibleScore(i - 1)){
				if(dice.determineScore(i - 1) > highestValue){
					highestValue = dice.determineScore(i - 1);
					highestChoice = i - 1;
				}
			}
		}
		
		if(dice.isFourKind() && score.isEligibleScore(7)){// four of a kind
			if(dice.determineScore(7) > highestValue){
				highestValue = dice.determineScore(7);
				highestChoice = 7;
			}
		}
		
		if(dice.isThreeKind() && score.isEligibleScore(6)){// three of a kind
			if(dice.determineScore(6) > highestValue){
				highestValue = dice.determineScore(6);
				highestChoice = 6;
			}
		}
		
		if(dice.isFullHouse() && score.isEligibleScore(9)){// full house
			if(dice.determineScore(9) > highestValue){
				highestValue = dice.determineScore(9);
				highestChoice = 9;
			}
		}
		
		if(dice.isSmallStraight() && score.isEligibleScore(10)){// small straight
			if(dice.determineScore(10) > highestValue){
				highestValue = dice.determineScore(10);
				highestChoice = 10;
			}
		}
		
		if(dice.isLargeStraight() && score.isEligibleScore(11)){// large straight
			if(dice.determineScore(11) > highestValue){
				highestValue = dice.determineScore(11);
				highestChoice = 11;
			}
		}
		
		if(dice.isYahtzee()){// yahtzee
			if(score.isEligibleScore(12)){
				if(dice.determineScore(12) > highestValue){
					highestValue = dice.determineScore(12);
					highestChoice = 12;
				}
			}
		}
		
		if(highestChoice == -1){// if there has not been a score chosen, pick chance; or pick a random value if chance has already been chosen
			
			for(int i = 1; i <= 2; i++){// ones and twos
				if(dice.hasNumber(i) && score.isEligibleScore(i - 1)){
					highestChoice = i - 1;
				}
			}
			
			if(highestChoice == -1){// if there still hasn't been a choosen score...
				if(score.isEligibleScore(8)){//chance
					if(dice.determineScore(8) > highestValue){
						highestValue = dice.determineScore(8);
						highestChoice = 8;
					}
				}else{
					for(int i = 0; i < 13; i++){
						if(score.isEligibleScore(i)){
							highestChoice = i;
							break;
						}
					}
				}
			}
		}

		return highestChoice;
	}
	
	private interface Action{
		void run();
	}

	@Override
	public void endGame() {
		if(debug)
			removeDiceMouseListeners();
	}
}
