package info3.game.menu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class LoadSave {

	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String OPTIONS_PLAYERS = "button_players.png";
	public static final String OPTIONS_AUTOMATES = "button_automates.png";
	public static final String OPTIONS_AUTOMATES1 = "button_automates1.png";
	public static final String OPTIONS_IP = "button_ip.png";
	public static final String OPTIONS_IP1 = "button_ip1.png";
	public static final String BUTTON_QUIT = "button_quit.png";
	public static final String BUTTON_QUIT1 = "button_quit1.png";
	public static final String FLECHE_GAUCHE = "fleche_gauche.png";
	public static final String FLECHE_GAUCHE1 = "fleche_gauche1.png";
	public static final String FLECHE_DROITE = "fleche_droite.png";
	public static final String FLECHE_DROITE1 = "fleche_droite1.png";
	public static final String BOX = "box.png";
	public static final String NUMBERS = "numbers.png";

	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = null;
		try {
			is = new FileInputStream(new File("src/main/resources/UI/" + fileName));
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}

}