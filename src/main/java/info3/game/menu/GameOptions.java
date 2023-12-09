package info3.game.menu;

import java.io.Serializable;
import java.util.HashMap;

public class GameOptions implements Serializable {
	public int playerCount = 1;
	public static GameOptions instance = new GameOptions();
	public HashMap<String, String> automates = new HashMap<String, String>();
	public String fichierGal;
}
