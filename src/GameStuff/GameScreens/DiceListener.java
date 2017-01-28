package GameStuff.GameScreens;

import java.util.EventListener;

interface DiceListener extends EventListener{
	void rolled();
	
	void doneRolling();
}
