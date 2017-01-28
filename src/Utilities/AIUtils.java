package Utilities;

import java.util.Arrays;

import GameStuff.GameScreens.Dice;

public class AIUtils {
	private short[][][][][][][] diceCombinations = new short[13][6][6][6][6][6][6];
	private int numConsecutive;
	
	//testing
	/*
	public static void main(String args[]){
		new AIUtils();
	}
	*/
	
	public AIUtils(){
		initCombinations();
	}
	
	public double get(int scoreIndex, int[] combinationIndices){
		return diceCombinations[scoreIndex][combinationIndices[0]][combinationIndices[1]][combinationIndices[2]][combinationIndices[3]][combinationIndices[4]][combinationIndices[5]];
	}
	
	//scores:
	// 1 - 6 is the ones through sixes
	// 7 and 8 are three and four of a kind
	//9 is chance
	//10 is full house
	//11 and 12 are small and large straight
	//13 is yahtzee
	//subtract theses indices by 1 for location in scores array
	
	private void initCombinations(){
		
		int num = 0;
		
		// ones  twos  threes  fours  fives  sixes
		//  5     0      0       0      0      0
		//  0     5      0       0      0      0
		//etc
		for(int a = 0; a < 6; a++){
			//java initializes the value to 0 by default
			int[] combos = new int[6];// must be length 6
			
			combos[a] = 5;
			
			storeDiceCombinationValues(combos);
			
			num++;
		}
		
		// ones  twos  threes  fours  fives  sixes
		//  4     1      0       0      0      0
		//  0     4      1       0      0      0
		//etc
		for(int a = 0; a < 6; a++){
			for(int b = 0; b < 6; b++){
				if(a != b){
					int[] combos = new int[6];// must be length 6
					combos[a] = 4;
					combos[b] = 1;
					
					storeDiceCombinationValues(combos);
					
					num++;
				}
			}
		}
		
		// ones  twos  threes  fours  fives  sixes
		//  3     2      0       0      0      0
		//  0     3      2       0      0      0
		//etc
		for(int a = 0; a < 6; a++){
			for(int b = 0; b < 6; b++){
				if(a != b){
					int[] combos = new int[6];// must be length 6
					combos[a] = 3;
					combos[b] = 2;
					
					storeDiceCombinationValues(combos);
					
					num++;
				}
			}
		}
		
		// ones  twos  threes  fours  fives  sixes
		//  3     1      1       0      0      0
		//  0     3      1       1      0      0
		//etc
		for(int a = 0; a < 6; a++){
			for(int b = 0; b < 6; b++){
				for(int c = b + 1; c < 6; c++){
					if(a != b && a != c){
						int[] combos = new int[6];// must be length 6
						combos[a] = 3;
						combos[b] = 1;
						combos[c] = 1;
						
						storeDiceCombinationValues(combos);
						
						num++;
					}
				}
			}
		}
		
		// ones  twos  threes  fours  fives  sixes
		//  2     2      1       0      0      0
		//  0     2      2       1      0      0
		//etc
		for(int a = 0; a < 6; a++){
			for(int b = 0; b < 6; b++){
				for(int c = b + 1; c < 6; c++){
					if(a != b && a != c){
						int[] combos = new int[6];// must be length 6
						combos[a] = 1;
						combos[b] = 2;
						combos[c] = 2;
						
						storeDiceCombinationValues(combos);
						
						num++;
					}
				}
			}
		}
		
		// ones  twos  threes  fours  fives  sixes
		//  2     1      1       1      0      0
		//  0     2      1       1      1      0
		//etc
		for(int a = 0; a < 6; a++){
			for(int b = 0; b < 6; b++){
				for(int c = b + 1; c < 6; c++){
					for(int d = c + 1; d < 6; d++){
						if(a != b && a != c && a != d){
							int[] combos = new int[6];// must be length 6
							combos[a] = 2;
							combos[b] = 1;
							combos[c] = 1;
							combos[d] = 1;
							
							storeDiceCombinationValues(combos);
							
							num++;
						}
					}
				}
			}
		}
		
		// ones  twos  threes  fours  fives  sixes
		//  1     1      1       1      1      0
		//  0     1      1       1      1      1
		//etc
		for(int a = 0; a < 6; a++){
			int[] combos = new int[6];// must be length 6
			Arrays.fill(combos, 1);
			combos[a] = 0;
			
			storeDiceCombinationValues(combos);
			
			num++;
		}
		
		//System.out.println(num);//testing
	}
	
	private void storeDiceCombinationValues(int[] combinationIdices){// goes through all the score categories with one set of dice combinations
		
		handleStraights(combinationIdices);
		
		for(int i = 0; i < 13; i++){
			diceCombinations[i]
					[combinationIdices[0]]
							[combinationIdices[1]]
									[combinationIdices[2]]
											[combinationIdices[3]]
													[combinationIdices[4]]
															[combinationIdices[5]] = determineScore(i, combinationIdices);
		}
	}
	

	private short determineScore(int scoreIndex, int[] combinationIndices){
		short score = 0;
		
		if(scoreIndex <= 5){// ones through sixes
				score += combinationIndices[scoreIndex] * (scoreIndex + 1);// adds 1 because score index is always one less than the value of the die
		}else if(scoreIndex == 6){// three of a kind
			if(isThreeKind(combinationIndices)){
				for(int i = 0; i < combinationIndices.length; i++){
					score += combinationIndices[i] * (i + 1);// adds 1 because score index is always one less than the value of the die
				}
			}
		}else if(scoreIndex == 7){// four of a kind
			if(isFourKind(combinationIndices)){
				for(int i = 0; i < combinationIndices.length; i++){
					score += combinationIndices[i] * (i + 1);// adds 1 because score index is always one less than the value of the die
				}
			}
		}else if(scoreIndex == 8){// chance
			for(int i = 0; i < combinationIndices.length; i++){
				score += combinationIndices[i] * (i + 1);// adds 1 because score index is always one less than the value of the die
			}
		}else if(scoreIndex == 9){//full house
			if(isFullHouse(combinationIndices))
				score += 25;
		}else if(scoreIndex == 10){//small straight
			if(isSmallStraight())
				score += 30;
		}else if(scoreIndex == 11){//large straight
			if(isLargeStraight())
				score += 40;
		}else if(scoreIndex == 12){//yahtzee
			if(isYahtzee(combinationIndices))
				score += 50;
		}
		
		return score;
	}
	
	
	public void handleStraights(int[] combinationIndices){
		//for straights
		numConsecutive = 0;
		int tempNumConsecutive = 0;
		
		for(int i = 0; i < combinationIndices.length; i++){
			if(combinationIndices[i] >= 1){
				tempNumConsecutive++;
				
				if(tempNumConsecutive > numConsecutive){
					numConsecutive = tempNumConsecutive;
				}
			}else{
				tempNumConsecutive = 0;
			}
		}
	}
	
	public boolean isThreeKind(int[] combinationIndices){
		for(int i = 0; i < combinationIndices.length; i++){
			if(combinationIndices[i] >= 3)
				return true;
		}
		
		return false;
	}

	public boolean isFourKind(int[] combinationIndices){
		for(int i = 0; i < combinationIndices.length; i++){
			if(combinationIndices[i] >= 4)
				return true;
		}
		
		return false;
	}
	
	public boolean isFullHouse(int[] combinationIndices){
		boolean threeKind = false;
		boolean twoKind = false;
		
		for(int i = 0; i < combinationIndices.length; i++){
			if(combinationIndices[i] == 3){
				threeKind = true;
			}else if(combinationIndices[i] == 2){
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
	
	public boolean isYahtzee(int[] combinationIndices){
		for(int i = 0; i < combinationIndices.length; i++){
			if(combinationIndices[i] == 5)
				return true;
		}
		
		return false;
	}
}
