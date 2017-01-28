package GameStuff;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import GameStuff.GameScreens.Score;

public class Statistics {
	
	int numScores;
	
	ArrayList<Score> scores;
	
	public Statistics(int numScores){
		this.numScores = numScores;
		
		scores = new ArrayList<Score>();
	}

	public void printHighScores(){
		FileReader fr;
		try {
			fr = new FileReader("High-Scores.txt");
			BufferedReader bRead = new BufferedReader(fr);
			Scanner sc = new Scanner(bRead);
			
			for(int i = 0; i < numScores; i++)
				System.out.println((i + 1) + ": " + sc.nextInt() + " - " + sc.nextLine().trim()); 
		
			bRead.close();
			sc.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g, int frameWidth, int frameHeight){
		g.setFont(new Font("Courier", Font.PLAIN, 30));
		g.setColor(Color.YELLOW);
		
		FileReader fr;
		try {
			fr = new FileReader("High-Scores.txt");
			BufferedReader bRead = new BufferedReader(fr);
			Scanner sc = new Scanner(bRead);
			
			String text = "High Scores";
			g.drawString(text, (int)((1 * (double)frameWidth/2) - text.length()/2 * 15) - 15, frameHeight/2 - 150);
			
			for(int i = 0; i < numScores; i++){
				
				int yOffset = i * 30;
				
				int score = sc.nextInt();
				String name = sc.nextLine().trim();
				
				g.drawString(score + "", (int)((1 * (double)frameWidth/3) - Integer.toString(score).length()/2 * 15), frameHeight/2 + yOffset - 100);
				g.drawString(name, (int)((2 * (double)frameWidth/3) - name.length()/2 * 15), frameHeight/2 + yOffset - 100);
			}
		
			bRead.close();
			sc.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int[] updateHighScores(Score[] newScores){

		BufferedWriter writer;
		int[] highScorePosition = new int[newScores.length];
		
		try{
			if(!new File("High-Scores.txt").exists()) { 
				writer = new BufferedWriter(new PrintWriter("High-Scores.txt", "UTF-8"));
				for(int i = 0; i < numScores; i++){
					writer.write("0 sampleText");
					writer.newLine();
				}
				writer.close();
			}
			
			FileReader fr = new FileReader("High-Scores.txt");
			BufferedReader bRead = new BufferedReader(fr);
			Scanner sc = new Scanner(bRead);
			
			while(sc.hasNextLine()){
				
				int score = sc.nextInt();
				String name = sc.nextLine().trim();
				
				scores.add(new Score(score, name)); 
			}
		
			sc.close();
			bRead.close();
			
			highScorePosition = replaceHighScores(newScores);
			
			writer = new BufferedWriter(new PrintWriter("High-Scores.txt", "UTF-8"));
			for(int i = 0; i < scores.size(); i++){
				writer.write(scores.get(i).getScore() + " " + scores.get(i).getName());
				writer.newLine();
			}
			writer.close();

		}catch(IOException e){
			e.printStackTrace();
		}
		
		return highScorePosition;
	}
	
	private int[] replaceHighScores(Score[] newScores){

		int[] NewToOldIndices = QuickSort.sortGreatest(newScores);//sort the array and return where the indices went
		
		int index = 0;
		int[] retValue = new int[newScores.length];
		
		for(int i = 0; i < scores.size(); i++){
			if(scores.get(i).getScore() < newScores[index].getScore()){
				scores.add(i, newScores[index]);
				retValue[index] = i + 1;
				index++;
				
				if(index >= newScores.length)
					break;
			}
		}
		
		int[] retValueClone = (int[])retValue.clone();
		
		for(int i = 0; i < retValueClone.length; i++){
			retValue[NewToOldIndices[i]] = retValueClone[i];// unsort the return values so that they were in the order they were before
		}
		
		while(index < newScores.length){
			scores.add(newScores[index]);
			retValue[index] = scores.size();
			index++;
		}
		
		return retValue;
	}
}

class QuickSort{

	public static int[] sortGreatest(Score[] a){// greatest to least
		
		int[] changedIndices = new int[a.length];
		
		for(int i = 0; i < changedIndices.length; i++){
			changedIndices[i] = i;
		}
		
		quickSort(a, 0, a.length - 1, changedIndices);
		
		return changedIndices;
	}
	
	private static void quickSort(Score[] a, int start, int end, int[] indices){
		if(start < end){	
			int partitionIndex = partition(a, start, end, indices);
			
			quickSort(a, start, partitionIndex - 1, indices);
			quickSort(a, partitionIndex + 1, end, indices);
		}
	}
	
	private static int partition(Score[] a, int start, int end, int[] indices){
		
		int pIndex = start;
		
		Score pivot = a[end];
		
		for(int i = start; i <= end; i++){
			if(a[i].getScore() > pivot.getScore()){
				swap(a, pIndex, i, indices);
				pIndex++;
			}	
		}
		
		swap(a, pIndex, end, indices);
		
		return pIndex;	
	}
	
	private static void swap(Score[] a, int indexOne, int indexTwo, int[] indices){
		Score placeHolder = a[indexOne];
		a[indexOne] = a[indexTwo];
		a[indexTwo] = placeHolder;
		
		int tempInt = indices[indexOne];
		indices[indexOne] = indices[indexTwo];
		indices[indexTwo] = tempInt;
	}
}
