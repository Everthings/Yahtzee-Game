package Main;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GameStuff.*;
import GameStuff.GameScreens.*;
import GameStuff.GameScreens.Buttons.Mode;
import Utilities.UserInputUtils;

public class GameHandler{
	
	private int numUsers;
	private int numAI;
	private boolean debug;
	
	private JFrame window;
	private Game game;
	private StartScreen start;
	private SettingsScreen settings;
	private EndScreen end;
	private JPanel currentPanel;
	
	private ButtonClickListener listener;
	
	private Timer timer;
	
	GameHandler(){

		initWindow();
		
		timer = new Timer();
		
		listener = new ButtonClickListener(){
			@Override
			public void clicked(Mode m) {
				changeMode(m);
			}
		};

		start = new StartScreen();
		settings = new SettingsScreen();
		end = new EndScreen();
		
		//setupGame();
	}
	
	public void initWindow(){
		java.awt.EventQueue.invokeLater(new Runnable() {
		    public void run() {
		    	window = new JFrame();
		    	window.setTitle("Dice Game");
				window.setSize(600, 600);
				window.setLocationRelativeTo(null); 
				window.setBackground(Color.WHITE); 
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
				window.setResizable(false);
				window.revalidate();
				window.repaint();
				
				timer.scheduleAtFixedRate(new TimerTask(){

					@Override
					public void run() {
						window.repaint();
					}
					
				}, 0, 20);
		    }
		});
	}
	
	public void run(){
		window.add(start);
		currentPanel = start;
		
		addEventListeners();
	
		window.repaint();
		
		window.setVisible(true);
	}
	
	public void addEventListeners(){
		start.addClickListener(listener);
		settings.addClickListener(listener);
		end.addClickListener(listener);
	}
	
	public void changeMode(Mode m){
		
		window.remove(currentPanel);
		
		switch(m){
		case GAME_SCREEN:
			
			initGameValues();
			
			game = new Game(numUsers, numAI, debug, window);
			game.addClickListener(listener);
			
			break;
		case END_SCREEN:
			end.setData(game.getScoreArray(), game.getNameArray(), game.getPosions());
			window.add(end);
			currentPanel = end;
			break;
		case SETTINGS_SCREEN:
			window.add(settings);
			currentPanel = settings;
			break;
		case EXIT:
			window.dispose();
			System.exit(0);
		}
		
		window.repaint();
	}
	
	public void initGameValues(){
		numUsers = settings.getNumPlayers();
		numAI = settings.getNumAI();
		debug = settings.getDebug();
	}
}
