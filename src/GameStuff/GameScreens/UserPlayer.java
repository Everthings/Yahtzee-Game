package GameStuff.GameScreens;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import Utilities.GraphicsUtils;

public class UserPlayer extends AbstractPlayer implements MouseListener{
	
	private ArrayList<ClickListener> listenerList = new ArrayList<ClickListener>();
	private ArrayList<Choice> list;
	
	private boolean showButton = false;//done button after move has been made
	private int width = 200;//button width
	private int height = 100;//button height
	
	public UserPlayer(String name, Color backColor, boolean debug) {
		super(name, backColor, debug);
		
		DiceListener diceListener = new DiceListener(){

			@Override
			public void rolled() {
				score.setChoices(null);
			}

			@Override
			public void doneRolling() {
				list = compileChoices();
				score.setChoices(list);
			}
		};
		
		DiceModifierListener diceModListener = new DiceModifierListener(){

			@Override
			public void diceSelected(int num) {
				score.setMarkingEnabled(true);
			}

			@Override
			public void diceClicked() {
				score.setMarkingEnabled(false);
			}

			@Override
			public void mouseExited() {
				score.setMarkingEnabled(true);
			}
			
		};
		
		ClickListener clickListener = new ClickListener(){
			public void clicked() {
				if(dice.getNumRolls() > 0 && !showButton){
					if(dice.getMoveDone()){
						
						if(dice.isYahtzee() && score.getNumYahtzees() >= 1){
							score.markJoker();
						}
						
						int scoreIndex = score.getScoreIndex();
						if(score.markScore(score.getScoreIndex(), dice.determineScore(score.getScoreIndex())) == true){
							handleUserVariables();
						}
					}
				}
			}
		};
	
		score.addClickListener(clickListener);
		dice.addDiceListener(diceListener);
		dice.addDiceModifierListener(diceModListener);
		
		addDiceMouseListeners();
		addScoreMouseListeners();
		
		this.addMouseListener(this);
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
	public void startTurn(){
		dice.resetDice();
		dice.rollDice();
	}
    
    public void handleUserVariables(){
    	
    	turns++;
    	
		if(turns >= 13){
			gameFinished = true;
		}
		
		showButton = true;
		
		dice.setRolls(dice.getMaxRolls());
    }
    
	public boolean containsElement(ArrayList<Choice> list, int index){
		for(Choice c: list){
			if(c.getIndex() == index)
				return true;
		}
		
		return false;
	}
	
	public ArrayList<Choice> compileChoices(){
		
		ArrayList<Choice> possibleChoices = new ArrayList<Choice>();
		
		//System.out.println("You have the following choices: ");
		
		for(int i = 1; i <= 6; i++){
			if(dice.hasNumber(i) && score.isEligibleScore(i - 1)){
				//System.out.println("Enter " + i + " for the " + i + "s catagory: ");
				possibleChoices.add(new Choice(i - 1, dice.determineScore(i - 1)));
			}
		}
		
		if(dice.isThreeKind() && score.isEligibleScore(6)){
			//System.out.println("Enter 7 for Three Kind: ");
			possibleChoices.add(new Choice(6, dice.determineScore(6)));
		}
		
		if(dice.isFourKind() && score.isEligibleScore(7)){
			//System.out.println("Enter 8 for Four Kind: ");
			possibleChoices.add(new Choice(7, dice.determineScore(7)));
		}
		
		if(dice.isFullHouse() && score.isEligibleScore(9)){
			//System.out.println("Enter 10 for Full House: ");
			possibleChoices.add(new Choice(9, dice.determineScore(9)));
		}
		
		if(dice.isSmallStraight() && score.isEligibleScore(10)){
			//System.out.println("Enter 11 for Small Straight: ");
			possibleChoices.add(new Choice(10, dice.determineScore(10)));
		}
		
		if(dice.isLargeStraight() && score.isEligibleScore(11)){
			//System.out.println("Enter 12 for Large Straight: ");
			possibleChoices.add(new Choice(11, dice.determineScore(11)));
		}
		
		if(dice.isYahtzee()){
			if(score.isEligibleScore(12)){
				//System.out.println("Enter 13 for YAHTZEE: ");
				possibleChoices.add(new Choice(12, dice.determineScore(12)));
			}
		}
		
		if(score.isEligibleScore(8)){//chance
			//System.out.println("Enter 9 for Chance: ");
			possibleChoices.add(new Choice(8, dice.determineScore(8)));
		}
		
		return possibleChoices;
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		this.setBackground(determineColor(backColor));
		
		super.paintComponent(g);

		g.setFont(new Font("Courier", Font.BOLD, 64));
		g.setColor(determineColor(Color.YELLOW));
		g.drawString(name, (int)(((double)WIDTH / 2.0) - (39 * (double)name.length() / 2.0)), 100);
		
		score.draw(g, showButton);
		dice.draw(g, showButton);
		
		if(showButton){
			g.setColor(Color.BLACK);
			g.drawRect(WIDTH/2 - width/2, HEIGHT/2 - height/2, width, height);
			g.setColor(Color.GREEN);
			g.fillRect(WIDTH/2 - width/2, HEIGHT/2 - height/2, width, height);
			
			g.setColor(Color.BLACK);
			g.setFont(new Font("Italic", Font.BOLD, 50));
			g.drawString("Done", 230, 315);
		}
	}
	
	public Color determineColor(Color c){
		if(showButton)
			return GraphicsUtils.getGreyScale(c);
		
		return c;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(showButton){
			if(mx > WIDTH/2 - width/2 && mx < WIDTH/2 + width/2 && my > HEIGHT/2 - height/2 && my < HEIGHT/2 + height/2){
				fireClicked();
				showButton = false;
			}
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endGame() {
		removeDiceMouseListeners();
		removeScoreMouseListeners();
	}
}

class Choice{
	private int index;
	private int value;
	
	Choice(int index, int value){
		this.index = index;
		this.value = value;
	}
	
	public int getIndex(){
		return index;
	}
	
	public int getValue(){
		return value;
	}
}
