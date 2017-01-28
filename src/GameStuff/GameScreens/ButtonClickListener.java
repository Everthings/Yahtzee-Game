package GameStuff.GameScreens;

import java.util.EventListener;

import GameStuff.GameScreens.Buttons.Mode;

public interface ButtonClickListener extends EventListener{
	void clicked(Mode m);
}
