package GameStuff.GameScreens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.event.EventListenerList;

import Utilities.GraphicsUtils;
import Utilities.UserInputUtils;

public class Scorecard implements MouseListener, MouseMotionListener{
	
	//score indexes:
	// 1 - 6 is the ones through sixes
	// 7 and 8 are three and four of a kind
	//9 is chance
	//10 is full house
	//11 and 12 are small and large straight
	//13 is yahtzee
	//subtract theses indices by 1 for location in scores array

	private int x;
	private int y;
	private int width;
	private int height;
	private Color lineColor;
	
	private int numItems = 13;
	
	private int[] scores = new int[13];
	
	private int numYahtzees = 0;
	
	private String[] cardLabels = {"Ones", "Twos", "Threes", "Fours", "Fives", "Sixes", "Sum", "Bonus", "Three of a Kind", "Four of a Kind", "Chance", "Full House", "Small Straight", "Large Straight", "YAHTZEE", "Total"};

	private int processingItemIndex;
	
	private int sum;
	private boolean bonus;
	private int total;
	
	private int mouseOverElement = -1;
	
	private double lineHeight;
	
	private ArrayList<ClickListener> listenerList = new ArrayList<ClickListener>();
	private ArrayList<Choice> choices = null;
	
	private boolean markingEnabled = true;
	
	public Scorecard(int x, int y, int width, int height, Color color){
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
		this.lineColor = color;
		
		lineHeight = (double)height/(numItems + 3);
		
		initScorecard();
	}
	
	public void resetChoices(){
		choices = new ArrayList<Choice>();
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
    
    public void setMarkingEnabled(boolean value){
    	markingEnabled = value;
    }
	
	public void initScorecard(){
		for(int i = 0; i < scores.length; i++){
			scores[i] = -1;
		}
	}
	
	public int getNumYahtzees(){
		return numYahtzees;
	}
	
	public void markJoker(){
		scores[12] += 100;
		numYahtzees++;
	}
	
	public boolean markScore(int scoreIndex, int score){
		if(isEligibleScore(scoreIndex)){
			scores[scoreIndex] = score;
			
			if(scoreIndex == 12 && score != 0){//yahtzee
				numYahtzees++;
			}
		
			calculateTotalScore();
			resetChoices();
			
			return true;
		}
		
		return false;
	}
	
	public boolean isEligibleScore(int scoreIndex){
		if(scoreIndex >= 0 && scoreIndex < scores.length)
			if(scores[scoreIndex] == -1)
					return true;
		
		return false;
	}
	
	public void setX(int value){
		x = value;
	}
	
	public void setY(int value){
		y = value;
	}
	
	public void setMouseOverElement(int value){
		mouseOverElement = value;
	}
	
	public void printCard(int numPerLine){
		
		int index = 0;
		
		while(true){
			
			int numPrint = cardLabels.length - index;
			
			if(numPrint > numPerLine){
				numPrint = numPerLine;
			}
			
			for(int i = 0; i < numPrint; i++){
				int length = cardLabels[index + i].length();
				System.out.printf("%" + (Integer.toString(length + 3)) + "s", cardLabels[index + i]);
			}
			System.out.println();
			
			for(int i = 0; i < numPrint; i++){
				int length = cardLabels[index + i].length();
				System.out.printf("%" + (Integer.toString(length + 3)) + "s", scores[index + i]);
			}
			System.out.println();
			
			index += numPerLine;
			
			if(index > scores.length)
				break;
		}
		System.out.println();
	}
	
	public void draw(Graphics g, boolean greyScale){
		drawOutline(g);
		
		if(markingEnabled){
			if(mouseOverElement >= 0){
				if(greyScale)
					g.setColor(GraphicsUtils.getGreyScale(Color.GREEN));
				else
					g.setColor(Color.GREEN);
				g.fillRect((int)(x + (((double)2 / 3) * width)), (int)(y + (mouseOverElement * lineHeight)), (int)(((double)1 / 3) * width) - 2, (int)lineHeight);
			}
		}
		
		drawScore(g);
	}
	
	public void drawScore(Graphics g){

		g.setFont(new Font("Italic", Font.ROMAN_BASELINE, 12));
		
		int scoreCounter = 0;
		for(int i = 0; i < cardLabels.length; i++){
			g.setColor(Color.BLACK);
			g.drawString(cardLabels[i], x + 5, (int)(y + (lineHeight * (i)) + lineHeight/2) + 2);
			if(i != 6 && i != 7 && i != 15){
				int score = scores[scoreCounter];
				if(score != -1){
					g.setColor(Color.BLACK);
					g.drawString(score + "", (int)(x + (((double)2 / 3) * width) + ((double)1/6) * width) - String.valueOf(score).length() * 6, (int)(y + (lineHeight * (i)) + lineHeight/2) + 2);
				}else if(containsElement(choices, scoreCounter) != null){
					g.setColor(Color.RED);
					score = containsElement(choices, scoreCounter).getValue();
					g.drawString(score + "", (int)(x + (((double)2 / 3) * width) + ((double)1/6) * width) - String.valueOf(score).length() * 6, (int)(y + (lineHeight * (i)) + lineHeight/2) + 2);
				}
					
				scoreCounter++;
			}
		}
		
		g.drawString(sum + "", (int)(x + (((double)2 / 3) * width) + ((double)1/6) * width) - String.valueOf(sum).length() * 6, (int)(y + (lineHeight * (6)) + lineHeight/2) + 2);
		if(bonus)
			g.drawString("35", (int)(x + (((double)2 / 3) * width) + ((double)1/6) * width) - String.valueOf(35).length() * 6, (int)(y + (lineHeight * (7)) + lineHeight/2) + 2);
		else
			g.drawString("0", (int)(x + (((double)2 / 3) * width) + ((double)1/6) * width) - String.valueOf(0).length() * 6, (int)(y + (lineHeight * (7)) + lineHeight/2) + 2);
		g.drawString(total + "", (int)(x + (((double)2 / 3) * width) + ((double)1/6) * width) - String.valueOf(total).length() * 6, (int)(y + (lineHeight * (15)) + lineHeight/2) + 2);
	}
	
	public void drawOutline(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(lineColor);
		g2.setStroke(new BasicStroke(5));
		g2.drawRect(x, y, width, height);

		for(int i = 1; i <= numItems + 3; i++){
			
			if(i == 6 || i == 8 || i == 15){
				g2.setStroke(new BasicStroke(5));
			}else{
				g2.setStroke(new BasicStroke(1));
			}
			g2.drawLine(x, (int)(y + (lineHeight * i)), x + width, (int)(y + (lineHeight * i)));
		}

		g2.drawLine((int)(x + (((double)2/3) * width)), y, (int)(x + (((double)2/3) * width)), y + height);
	}
	
	public Choice containsElement(ArrayList<Choice> list, int index){
		if(list != null){
			for(Choice c: list){
				if(c.getIndex() == index)
					return c;
			}
		}
		
		return null;
	}
	
	public int getTotal(){
		return total;
	}
	
	public void calculateTotalScore(){
		
		total = 0;
		sum = 0;
		
		for(int i  = 0; i < 6; i++){
			int score = scores[i];
			if(score < 0)
				score = 0;
			
			sum += score;
		}
		
		total += sum;
		
		if(sum >= 63){
			total += 35;
			bonus = true;
		}
		
		for(int i  = 6; i < scores.length; i++){
			int score = scores[i];
			if(score < 0)
				score = 0;
			
			total += score;
		}
	}
	
	public int getScoreIndex(){
		return processingItemIndex;
	}
	
	public void setChoices(ArrayList<Choice> list){
		choices = list;
	}
	
	private int getMouseOverIndex(int my){
		
		double lineHeight = (double)height/(numItems + 3);
		
		return  (int)((my - y)/lineHeight);	
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		int mx = e.getX();
		int my = e.getY();
		
		if(mx > x && mx < x + width && my > y && my < y + height){
			mouseOverElement = getMouseOverIndex(my);
		}else{
			mouseOverElement = -1;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(markingEnabled){
			if(mx > x && mx < x + width && my > y && my < y + height){
	
				processingItemIndex = getMouseOverIndex(my);
				
				if(processingItemIndex >= 8){
					processingItemIndex -= 2;
					fireClicked();
				}else if(processingItemIndex < 6){
					fireClicked();
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
}


