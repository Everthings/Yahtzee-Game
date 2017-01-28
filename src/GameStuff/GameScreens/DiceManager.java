package GameStuff.GameScreens;
import java.awt.Color;
import java.awt.Font;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Utilities.GraphicsUtils;

 
public class DiceManager implements MouseListener, MouseMotionListener{
	
	private int[] diceContents = new int[6];
	private int numConsecutive = 0;
	
	private int straightStartIndex;//exclusive
	private int straightEndIndex;//inclusive
	
	//coordinates and dimensions of the dice rolling area
	private int x;
	private int y;
	private int width;
	private int height;
	private Color areaColor;
	
	private boolean moveDone;
	
	private int maxRolls = 3;
	
	//for graphics
	private Dice[] diceRolls = new Dice[5];
	private BufferedImage[] diceImages = new BufferedImage[6];
	
	private int numRolls;

	private DiceModifier dmod;
	private DiceModifierListener dModClickListener;
	
	private ArrayList<DiceListener> listenerList = new ArrayList<DiceListener>();
	private ArrayList<DiceModifierListener> modListenerList = new ArrayList<DiceModifierListener>();
	private int clickedDiceIndex;
	
	private Timer t;
	private boolean timerScheduled = false;
	
	private boolean debug;
	private boolean rollingEnabled = true;
	
	private Timer timer;
	
	public DiceManager(int x, int y, int width, int height, Color color, boolean debug){
		this.x = x;
		this.y = y;
		
		this.debug = debug;
		
		this.width = width;
		this.height = height;
		
		this.areaColor = color;
		
		dmod = new DiceModifier(x, y);
		
		t = new Timer();
		timer = new Timer();
		
		dModClickListener = new DiceModifierListener(){

			@Override
			public void diceClicked() {
				
			}

			@Override
			public void mouseExited() {
				dmod.setEnable(false);
				fireExited();
			}

			@Override
			public void diceSelected(int num) {
				dmod.setEnable(false);
				Dice d = diceRolls[clickedDiceIndex];
				diceRolls[clickedDiceIndex] = new Dice(diceImages[num - 1], d.getX(), d.getY(), num, d.getRotation(), d.getRotationChange(), d.getMX(), d.getMY());
				createDiceContentArray();
				fireDoneRolling();
				fireSelected(num);
			}
		};
		
		dmod.addDiceModifierListener(dModClickListener);
		
		initImages();
		
		resetDice();
	}
	

	public void addDiceModifierListener(DiceModifierListener d) {
		modListenerList.add(d);
	}
		
	public void removeDiceModifierListener(DiceModifierListener d) {
		 modListenerList.remove(d);
	}
	
	protected void fireSelected(int value) {
	    for(DiceModifierListener dl: modListenerList){
	    	dl.diceSelected(value);
	    }
	}
	
	protected void fireClicked() {
	    for(DiceModifierListener dl: modListenerList){
	    	dl.diceClicked();
	    }
	}
	
	protected void fireExited() {
	    for(DiceModifierListener dl: modListenerList){
	    	dl.mouseExited();
	    }
	}
	
	public void addDiceModMouseListeners(JPanel p){
		p.addMouseListener(dmod);
		p.addMouseMotionListener(dmod);
	}
	
	public void removeDiceModMouseListeners(JPanel p){
		p.removeMouseListener(dmod);
		p.removeMouseMotionListener(dmod);
	}
	
	public void addDiceListener(DiceListener d) {
	  listenerList.add(d);
	}
	
	public void removeDiceListener(DiceListener d) {
	  listenerList.remove(d);
	}
	
    protected void fireRolled() {
    	for(DiceListener dl: listenerList){
    		dl.rolled();
    	}
	}
    
    protected void fireDoneRolling() {
    	for(DiceListener dl: listenerList){
    		dl.doneRolling();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void enableRolling(boolean value){
		rollingEnabled = value;
	}
	
	public int getNumRolls(){
		return numRolls;
	}
	
	public boolean getMoveDone(){
		return moveDone;
	}
	
	public Dice[] getDice(){
		return diceRolls;
	}
	
	public void resetDice(){
		
		initDice();
		
		for(int i = 0; i < diceRolls.length; i++){
			diceRolls[i].unhold();
		}
		
		numRolls = 0;
		moveDone = true;
		
	}
	
	public void initDice(){
		
		Random rand = new Random();
		
		for(int i = 0; i < diceRolls.length; i++){
			diceRolls[i] = new Dice(diceImages[0], rand.nextInt(width), rand.nextInt(height), 0, 0, 0, 0, 0);
		}
	}
	
	public void rollDice(){
		
		Random rand = new Random();
		
		if(moveDone){
			if(numRolls < maxRolls){
		
				moveDone = false;
				
				timer.scheduleAtFixedRate(new TimerTask(){
					
					@Override
					public void run(){
						int numDone = 0;
						
						for(int i = 0; i < diceRolls.length; i++){
							
							Dice d = diceRolls[i];
							
							d.updateRotation();
							d.updatePosition(width, height);
							handleCollisions();
							if(d.getRotationChange() == 0 && d.getMX() == 0 && d.getMY() == 0)
								numDone++;
						}
						
						if(numDone == diceRolls.length){
							moveDone = true;
							fireDoneRolling();
							this.cancel();
							timer.purge();
						}
					}
				}, 0, 20);
				
				for(int i = 0; i < diceRolls.length; i++){
					
					Dice oldDice = diceRolls[i];
					
					if(!oldDice.isHeld()){
						int num = rand.nextInt(6) + 1;
						diceRolls[i] = new Dice(diceImages[num - 1], oldDice.getX(), oldDice.getY(), num, 
								oldDice.getRotation(), rand.nextInt(100) + 50, rand.nextInt(50) - 25, rand.nextInt(50) - 25);
					}
				}
				
				numRolls++;
				createDiceContentArray();
				fireRolled();
			}
		}
	}
	
	public void handleCollisions(){
	    double xDist, yDist;
	    for(int i = 0; i < diceRolls.length; i++){
	        Dice d1 = diceRolls[i];
	        for(int j = i + 1; j < diceRolls.length; j++){
	        	
	            Dice d2 = diceRolls[j];
	            
	            xDist = d1.getX() - d2.getX();
	            yDist = d1.getY() - d2.getY();
	            
	            double distSquared = xDist*xDist + yDist*yDist;
	            
	            double d1Radius = Math.sqrt(Math.pow(d1.getWidth()/2, 2) + Math.pow(d1.getHeight()/2, 2));
	            double d2Radius = Math.sqrt(Math.pow(d2.getWidth()/2, 2) + Math.pow(d2.getHeight()/2, 2));
	            
	            //Check the squared distances instead of the the distances, same result, but avoids a square root.
	            if(distSquared <= (d1Radius + d2Radius)*(d1Radius + d2Radius)){
	                double xVelocity = d2.getMX() - d1.getMX();
	                double yVelocity = d2.getMY() - d1.getMY();
	                double dotProduct = xDist*xVelocity + yDist*yVelocity;
	                //dot product to check if the dice are moving toward each other - problems occurred before where the dice would stick
	                if(dotProduct > 0){
	                    double collisionScale = dotProduct / distSquared;
	                    double xCollision = xDist * collisionScale;
	                    double yCollision = yDist * collisionScale;
	                    //The Collision vector is the speed difference projected on the Dist vector,
	                    //thus it is the component of the speed difference needed for the collision.
	                    
	                    //Note: the collision weights are not included as the masses of the dice are the same
	                    d1.addMx(xCollision);
	                    d1.addMy(yCollision);
	                    d2.addMx(-xCollision);
	                    d2.addMy(-yCollision);
	                }
	            }
	        }
	    }
	}
	
	public void setRolls(int value){
		numRolls = value;
	}
	
	public int getMaxRolls(){
		return maxRolls;
	}

	public void createDiceContentArray(){
		for(int i = 0; i < diceContents.length; i++){
			diceContents[i] = 0;
		}
	
		for(int i = 0; i < diceRolls.length; i++){
			diceContents[diceRolls[i].getValue() - 1]++;
		}
	
		handleStraights();
	}
	
	public void handleStraights(){
		//for straights
		numConsecutive = 0;
		
		int tempStartIndex = 0;
		straightStartIndex = 0;
		straightEndIndex = 0;
		
		int tempNumConsecutive = 0;
		for(int i = 0; i < diceContents.length; i++){
			if(diceContents[i] >= 1){
				tempNumConsecutive++;
				
				if(tempNumConsecutive > numConsecutive){
					numConsecutive = tempNumConsecutive;
					straightStartIndex = tempStartIndex;
					straightEndIndex = i;
				}
			}else{
				tempNumConsecutive = 0;
				tempStartIndex = i;
			}
		}
	}
	
	public int getStraightStartIndex(){//exclusive
		return straightStartIndex;
	}
	
	public int getStraightEndIndex(){//inclusive
		return straightEndIndex;
	}
	
	public boolean hasNumber(int num){
		if(diceContents[num - 1] >= 1)
			return true;

		return false;
	}
	
	public boolean isThreeKind(){
		for(int i = 0; i < diceContents.length; i++){
			if(diceContents[i] >= 3)
				return true;
		}
		
		return false;
	}

	public boolean isFourKind(){
		for(int i = 0; i < diceContents.length; i++){
			if(diceContents[i] >= 4)
				return true;
		}
		
		return false;
	}
	
	public boolean isFullHouse(){
		boolean threeKind = false;
		boolean twoKind = false;
		
		for(int i = 0; i < diceContents.length; i++){
			if(diceContents[i] == 3){
				threeKind = true;
			}else if(diceContents[i] == 2){
				twoKind = true;
			}
		}
		
		if(threeKind && twoKind){
			return true;
		}
		
		return false;
	}
	
	public boolean isSmallStraight(){
		if(numConsecutive >= 4){
			return true;
		}
		
		return false;
	}
	
	public boolean isLargeStraight(){
		if(numConsecutive >= 5){
			return true;
		}
		
		return false;
	}
	
	public boolean isYahtzee(){
		for(int i = 0; i < diceContents.length; i++){
			if(diceContents[i] == 5)
				return true;
		}
		
		return false;
	}

	public int determineScore(int scoreIndex){
		int score = 0;
		
		if(scoreIndex <= 5){// ones through sixes
			if(hasNumber(scoreIndex + 1))
				score += diceContents[scoreIndex] * (scoreIndex + 1);// adds 1 because score index is always one less than the value of the die
		}else if(scoreIndex == 6){// three of a kind
			if(isThreeKind()){
				for(int i = 0; i < diceRolls.length; i++){
					score += diceRolls[i].getValue();
				}
			}
		}else if(scoreIndex == 7){// four of a kind
			if(isFourKind()){
				for(int i = 0; i < diceRolls.length; i++){
					score += diceRolls[i].getValue();
				}
			}
		}else if(scoreIndex == 8){// chance
			for(int i = 0; i < diceRolls.length; i++){
				score += diceRolls[i].getValue();
			}
		}else if(scoreIndex == 9){//full house
			if(isFullHouse())
				score += 25;
		}else if(scoreIndex == 10){//small straight
			if(isSmallStraight())
				score += 30;
		}else if(scoreIndex == 11){//large straight
			if(isLargeStraight())
				score += 40;
		}else if(scoreIndex == 12){//yahtzee
			if(isYahtzee())
				score += 50;
		}
		
		return score;
	}
	
	public void draw(Graphics g, boolean greyScale){
		if(greyScale)
			g.setColor(GraphicsUtils.getGreyScale(areaColor));
		else
			g.setColor(areaColor);
		g.fillRoundRect(x, y, width, height, width/5, width/5);

		g.setColor(Color.BLACK);
		g.setFont(new Font("Italic", Font.ITALIC, 12));
		String text;
		
		if(!moveDone)
			text = "Rolling! Please wait.";
		else if(numRolls < maxRolls)
			text = "Click here to roll.";
		else
			text = "Cannot roll...";
		g.drawString(text, (int)(x + (((double)width / 2.0) - (6 * ((double)text.length() / 2.0)))), height/2 + y);
		
		g.setFont(new Font("Italic", Font.BOLD, 50));
		
		for(int i = 0; i < diceRolls.length; i++){
			
			diceRolls[i].draw(g, x, y, greyScale);
			
			if(diceRolls[i].isHeld()){
				if(greyScale)
					g.setColor(GraphicsUtils.getGreyScale(Color.RED));
				else
					g.setColor(Color.RED);
				g.drawString(diceRolls[i].getValue() + "", x + (i * 40) + (width - diceRolls.length * 40)/2, y - 10);
			}else if(moveDone){
				g.setColor(Color.BLACK);
				g.drawString(diceRolls[i].getValue() + "", x + (i * 40) + (width - diceRolls.length * 40)/2, y - 10);
			}
		}

		dmod.draw(g);
	}
	
	public void printDice(){
		for(int i = 0; i < diceRolls.length; i++){
			System.out.print(diceRolls[i] + "  ");
		}
		
		System.out.println();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(!dmod.isEnabled()){
			if(mx > x && mx < x + width && my > y && my < y + height){
				boolean noDiceFound = true;
				
				for(int i = 0; i < diceRolls.length; i++){
					
					Dice d = diceRolls[i];
		
					if(mx > d.getX() + x - d.getWidth()/2  && mx < diceRolls[i].getX() + x + diceRolls[i].getWidth()/2 
							&& my > d.getY() + y - d.getHeight()/2  && my < diceRolls[i].getY() + y + diceRolls[i].getHeight()/2){
						
						if(debug){
							clickedDiceIndex = i;
							
							timerScheduled = true;
							
							t.schedule(new TimerTask(){
								
								@Override
								public void run() {
									dmod.setDice(d);
									dmod.setEnable(true);
									fireClicked();
									timerScheduled = false;
								}
							}, 500);
						}else{
							if(d.isHeld()){
								d.unhold();
								d.setHighlight(false);
							}else
								d.hold();
						}
		
						noDiceFound = false;
					}
						
				}
				
				if(noDiceFound && !dmod.isEnabled() && rollingEnabled)
					rollDice();
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(timerScheduled && debug){
			t.cancel();
			
			timerScheduled = false;
			
			t = new Timer();
			
			if(diceRolls[clickedDiceIndex].isHeld()){
				diceRolls[clickedDiceIndex].unhold();
				diceRolls[clickedDiceIndex].setHighlight(false);
			}else
				diceRolls[clickedDiceIndex].hold();
		}
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
		
		if(!dmod.isEnabled()){
			if(mx > x && mx < x + width && my > y && my < y + height){
				for(int i = 0; i < diceRolls.length; i++){
					Dice d = diceRolls[i];
					if(mx > d.getX() + x - d.getWidth()/2  && mx < diceRolls[i].getX() + x + diceRolls[i].getWidth()/2 
							&& my > d.getY() + y - d.getHeight()/2  && my < diceRolls[i].getY() + y + diceRolls[i].getHeight()/2){
						d.setHighlight(true);
					}else{
						d.setHighlight(false);
					}
				}
			}
		}
	}
}