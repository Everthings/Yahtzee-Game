package GameStuff.GameScreens;


public class Score {
	String name;
	int score;
	
	public Score(int score, String name){
		this.name = name;
		this.score = score;
	}
	
	public int getScore(){
		return score;
	}
	
	public String getName(){
		return name;
	}
}