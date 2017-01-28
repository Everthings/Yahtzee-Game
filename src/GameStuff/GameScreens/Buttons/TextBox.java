package GameStuff.GameScreens.Buttons;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import GameStuff.GameScreens.ClickListener;

public class TextBox implements MouseListener{
	
	private String toolTip;
	private int boxX, boxY, boxWidth, boxHeight, submitX, submitY, submitWidth, submitHeight;
	
	private boolean isSelected = false;
	
	private ArrayList<TextListener> listenerList = new ArrayList<TextListener>();
	
	private String text = "";
	
	private Color boxColor, boxSelectedColor, submitColor;
	private JPanel panel;
	
	private int maxCharacters;
	
	public TextBox(String toolTip, int boxX, int boxY, int boxWidth, int boxHeight, int submitX, int submitY, int submitWidth, int submitHeight, Color boxColor, Color boxSelectedColor, Color submitColor, int maxCharacters, JPanel panel){
		this.toolTip = toolTip;
		
		this.boxX = boxX;
		this.boxY = boxY;
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
		
		this.submitX = submitX;
		this.submitY = submitY;
		this.submitWidth = submitWidth;
		this.submitHeight = submitHeight;
		
		this.boxColor = boxColor;
		this.boxSelectedColor = boxSelectedColor;
		this.submitColor = submitColor;
		
		panel.addMouseListener(this);

		this.panel = panel;
		this.maxCharacters = maxCharacters;
		
		setupKeyBindings();
	}
	
	public void addTextListener(TextListener l) {
		  listenerList.add(l);
	}
		
	public void removeTextListener(TextListener l) {
	  listenerList.remove(l);
	}
		
	protected void fireEntered() {
	  	for(TextListener tl: listenerList){
	  		tl.entered(text.trim());
	  	}
	}
	
	public void setupKeyBindings(){
		
		InputMap inMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actMap = panel.getActionMap();
			
		inMap.put(KeyStroke.getKeyStroke('a'), "aKey");
		inMap.put(KeyStroke.getKeyStroke('b'), "bKey");
		inMap.put(KeyStroke.getKeyStroke('c'), "cKey");
		inMap.put(KeyStroke.getKeyStroke('d'), "dKey");
		inMap.put(KeyStroke.getKeyStroke('e'), "eKey");
		inMap.put(KeyStroke.getKeyStroke('f'), "fKey");
		inMap.put(KeyStroke.getKeyStroke('g'), "gKey");
		inMap.put(KeyStroke.getKeyStroke('h'), "hKey");
		inMap.put(KeyStroke.getKeyStroke('i'), "iKey");
		inMap.put(KeyStroke.getKeyStroke('j'), "jKey");
		inMap.put(KeyStroke.getKeyStroke('k'), "kKey");
		inMap.put(KeyStroke.getKeyStroke('l'), "lKey");
		inMap.put(KeyStroke.getKeyStroke('m'), "mKey");
		inMap.put(KeyStroke.getKeyStroke('n'), "nKey");
		inMap.put(KeyStroke.getKeyStroke('o'), "oKey");
		inMap.put(KeyStroke.getKeyStroke('p'), "pKey");
		inMap.put(KeyStroke.getKeyStroke('q'), "qKey");
		inMap.put(KeyStroke.getKeyStroke('r'), "rKey");
		inMap.put(KeyStroke.getKeyStroke('s'), "sKey");
		inMap.put(KeyStroke.getKeyStroke('t'), "tKey");
		inMap.put(KeyStroke.getKeyStroke('u'), "uKey");
		inMap.put(KeyStroke.getKeyStroke('v'), "vKey");
		inMap.put(KeyStroke.getKeyStroke('w'), "wKey");
		inMap.put(KeyStroke.getKeyStroke('x'), "xKey");
		inMap.put(KeyStroke.getKeyStroke('y'), "yKey");
		inMap.put(KeyStroke.getKeyStroke('z'), "zKey");
		
		inMap.put(KeyStroke.getKeyStroke('A'), "AKey");
		inMap.put(KeyStroke.getKeyStroke('B'), "BKey");
		inMap.put(KeyStroke.getKeyStroke('C'), "CKey");
		inMap.put(KeyStroke.getKeyStroke('D'), "DKey");
		inMap.put(KeyStroke.getKeyStroke('E'), "EKey");
		inMap.put(KeyStroke.getKeyStroke('F'), "FKey");
		inMap.put(KeyStroke.getKeyStroke('G'), "GKey");
		inMap.put(KeyStroke.getKeyStroke('H'), "HKey");
		inMap.put(KeyStroke.getKeyStroke('I'), "IKey");
		inMap.put(KeyStroke.getKeyStroke('J'), "JKey");
		inMap.put(KeyStroke.getKeyStroke('K'), "KKey");
		inMap.put(KeyStroke.getKeyStroke('L'), "LKey");
		inMap.put(KeyStroke.getKeyStroke('M'), "MKey");
		inMap.put(KeyStroke.getKeyStroke('N'), "NKey");
		inMap.put(KeyStroke.getKeyStroke('O'), "OKey");
		inMap.put(KeyStroke.getKeyStroke('P'), "PKey");
		inMap.put(KeyStroke.getKeyStroke('Q'), "QKey");
		inMap.put(KeyStroke.getKeyStroke('R'), "RKey");
		inMap.put(KeyStroke.getKeyStroke('S'), "SKey");
		inMap.put(KeyStroke.getKeyStroke('T'), "TKey");
		inMap.put(KeyStroke.getKeyStroke('U'), "UKey");
		inMap.put(KeyStroke.getKeyStroke('V'), "VKey");
		inMap.put(KeyStroke.getKeyStroke('W'), "WKey");
		inMap.put(KeyStroke.getKeyStroke('X'), "XKey");
		inMap.put(KeyStroke.getKeyStroke('Y'), "YKey");
		inMap.put(KeyStroke.getKeyStroke('Z'), "ZKey");
		
		inMap.put(KeyStroke.getKeyStroke('0'), "0Key");
		inMap.put(KeyStroke.getKeyStroke('1'), "1Key");
		inMap.put(KeyStroke.getKeyStroke('2'), "2Key");
		inMap.put(KeyStroke.getKeyStroke('3'), "3Key");
		inMap.put(KeyStroke.getKeyStroke('4'), "4Key");
		inMap.put(KeyStroke.getKeyStroke('5'), "5Key");
		inMap.put(KeyStroke.getKeyStroke('6'), "6Key");
		inMap.put(KeyStroke.getKeyStroke('7'), "7Key");
		inMap.put(KeyStroke.getKeyStroke('8'), "8Key");
		inMap.put(KeyStroke.getKeyStroke('9'), "9Key");
		
		inMap.put(KeyStroke.getKeyStroke("SPACE"), "spaceKey");

		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "delKey");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enterKey");

		
		actMap.put("aKey", new AddAction('a'));
		actMap.put("bKey", new AddAction('b'));
		actMap.put("cKey", new AddAction('c'));
		actMap.put("dKey", new AddAction('d'));
		actMap.put("eKey", new AddAction('e'));
		actMap.put("fKey", new AddAction('f'));
		actMap.put("gKey", new AddAction('g'));
		actMap.put("hKey", new AddAction('h'));
		actMap.put("iKey", new AddAction('i'));
		actMap.put("jKey", new AddAction('j'));
		actMap.put("kKey", new AddAction('k'));
		actMap.put("lKey", new AddAction('l'));
		actMap.put("mKey", new AddAction('m'));
		actMap.put("nKey", new AddAction('n'));
		actMap.put("oKey", new AddAction('o'));
		actMap.put("pKey", new AddAction('p'));
		actMap.put("qKey", new AddAction('q'));
		actMap.put("rKey", new AddAction('r'));
		actMap.put("sKey", new AddAction('s'));
		actMap.put("tKey", new AddAction('t'));
		actMap.put("uKey", new AddAction('u'));
		actMap.put("vKey", new AddAction('v'));
		actMap.put("wKey", new AddAction('w'));
		actMap.put("xKey", new AddAction('x'));
		actMap.put("yKey", new AddAction('y'));
		actMap.put("zKey", new AddAction('z'));
		
		actMap.put("AKey", new AddAction('A'));
		actMap.put("BKey", new AddAction('B'));
		actMap.put("CKey", new AddAction('C'));
		actMap.put("DKey", new AddAction('D'));
		actMap.put("EKey", new AddAction('E'));
		actMap.put("FKey", new AddAction('F'));
		actMap.put("GKey", new AddAction('G'));
		actMap.put("HKey", new AddAction('H'));
		actMap.put("IKey", new AddAction('I'));
		actMap.put("JKey", new AddAction('J'));
		actMap.put("KKey", new AddAction('K'));
		actMap.put("LKey", new AddAction('L'));
		actMap.put("MKey", new AddAction('M'));
		actMap.put("NKey", new AddAction('N'));
		actMap.put("OKey", new AddAction('O'));
		actMap.put("PKey", new AddAction('P'));
		actMap.put("QKey", new AddAction('Q'));
		actMap.put("RKey", new AddAction('R'));
		actMap.put("SKey", new AddAction('S'));
		actMap.put("TKey", new AddAction('T'));
		actMap.put("UKey", new AddAction('U'));
		actMap.put("VKey", new AddAction('V'));
		actMap.put("WKey", new AddAction('W'));
		actMap.put("XKey", new AddAction('X'));
		actMap.put("YKey", new AddAction('Y'));
		actMap.put("ZKey", new AddAction('Z'));
		
		actMap.put("0Key", new AddAction('0'));
		actMap.put("1Key", new AddAction('1'));
		actMap.put("2Key", new AddAction('2'));
		actMap.put("3Key", new AddAction('3'));
		actMap.put("4Key", new AddAction('4'));
		actMap.put("5Key", new AddAction('5'));
		actMap.put("6Key", new AddAction('6'));
		actMap.put("7Key", new AddAction('7'));
		actMap.put("8Key", new AddAction('8'));
		actMap.put("9Key", new AddAction('9'));
		
		actMap.put("spaceKey", new AddAction(' '));
		
		actMap.put("delKey", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isSelected)
					remove();
			}
		});
		
		actMap.put("enterKey", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isSelected)
					fireEntered();
			}
		});
	}


	public void draw(Graphics g){
		
		if(isSelected){
			g.setColor(boxSelectedColor);
		}else{
			g.setColor(boxColor);
		}
		g.fillRect(boxX, boxY, boxWidth, boxHeight);
		
		g.setColor(Color.BLACK);
		if(text.length() == 0){
			g.setFont(new Font("Italic", Font.ITALIC, 30));
			g.drawString(toolTip, boxX + 20, boxY + boxHeight/2 + 7);
		}else{
			g.setFont(new Font("Plain", Font.PLAIN, 30));
			g.drawString(text, boxX + 20, boxY + boxHeight/2 + 7);
		}
		
		g.setColor(submitColor);
		g.fillRect(submitX, submitY, submitWidth, submitHeight);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Roman", Font.ROMAN_BASELINE, 30));
		String text = "Enter";
		g.drawString(text, (int)(submitX + (((double)submitWidth / 2.0) - (15 * ((double)text.length() / 2.0)))), (int)(submitY + ((double)submitHeight / 2.0)) + 7);
	}
	
	public void remove(){
		if(text.length() == 1){
			text = "";
		}else if(text.length() >= 1){
			text = text.substring(0, text.length() - 1);
		}
	}
	
	public void add(char c){
		if(text.length() < maxCharacters){
			text = text + c;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(mx > submitX && mx < submitX + submitWidth && my > submitY && my < submitY + submitHeight){
			fireEntered();
		}
		
		if(mx > boxX && mx < boxX + boxWidth && my > boxY && my < boxY + boxHeight){
			isSelected = true;
		}else{
			isSelected = false;
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
	
	class AddAction extends AbstractAction{

		char c;
		
		AddAction(char c){
			this.c = c;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(isSelected){
				add(c);
			}
		}
	}
}