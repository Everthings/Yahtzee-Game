package Utilities;
import java.util.ArrayList;
import java.util.Scanner;

public class UserInputUtils {
	//utility class
	
	static Scanner sc = new Scanner(System.in);
	
	public static void welcomeUser(String gameName){
		
		String[] welcomeMessages = {"Hope you enjoy!", "Have a blast!", "Especially made for you!", "Hand made with love!", "Don't lose..", ""};
		
		System.out.println("Welcome to " + gameName + ". " + welcomeMessages[(int)(Math.random() * 6)]);
	}
	
	public static String getUserName(){
		System.out.println("Enter a name?: ");
		String ret = sc.nextLine();
		if(ret.equals(""))
			return sc.nextLine();
		else
			return ret;
	}
	
	public static String askStringQuestion(String question){
		System.out.println(question + ": ");
		String ret = sc.nextLine();
		if(ret.equals(""))
			return sc.nextLine();
		else
			return ret;
	}
	
	public static int askIntQuestion(String question){
		
		boolean isValid = false;
		int input = 0;
		
		do{
			System.out.println(question + ": ");
			
			try{
				input = Integer.parseInt(sc.next());
				isValid = true;
			}catch(NumberFormatException e){
				System.out.println("Invalid number!");
			}
		}while(!isValid);

		return input;
	}
	
	public static ArrayList<Integer> askIntArrayQuestion(String question){
		
		int input = 0;
		
		ArrayList<Integer> ret = new ArrayList<Integer>();

		boolean isValid = false;
		
		do{
			
			isValid = false;
			
			do{
				System.out.println(question + "(-n to quit): ");
				
				try{
					input = Integer.parseInt(sc.next());
					isValid = true;
				}catch(NumberFormatException e){
					System.out.println("Invalid number!");
				}
			}while(!isValid);
			
			ret.add(input);
		}while(input >= 0);
		
		ret.remove(ret.size() - 1);
		
		return ret;
	}
}
