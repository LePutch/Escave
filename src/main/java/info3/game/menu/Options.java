package info3.game.menu;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Options extends State implements Statemethods {

	private OptionButton[] buttons = new OptionButton[5];
	private BufferedImage backgroundImg, playerspng, boxpng;
	private int menuX, menuY, menuWidth, menuHeight, playerX, playerY, boxX, boxY;
	private NumberSelector selector;
	public static String ip = "127.0.0.1";

	public Options(GameJerem game) {
		super(game);
		this.selector = new NumberSelector(game);
		loadButtons();
		loadPlayer();
		loadBox();
		loadBackground();
	}

	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		menuWidth = (int) (backgroundImg.getWidth() * GameJerem.SCALE);
		menuHeight = (int) (backgroundImg.getHeight() * GameJerem.SCALE);
		menuX = GameJerem.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (45 * GameJerem.SCALE);
	}

	private void loadPlayer() {
		playerspng = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_PLAYERS);
		playerX = GameJerem.GAME_WIDTH / 2 - 90;
		playerY = (int) (150 * GameJerem.SCALE);
	}

	private void loadBox() {
		boxpng = LoadSave.GetSpriteAtlas(LoadSave.BOX);
		boxX = GameJerem.GAME_WIDTH / 2 - 25;
		boxY = (int) (185 * GameJerem.SCALE);
	}

	private void loadButtons() {

		buttons[0] = new OptionButton(GameJerem.GAME_WIDTH / 2 + 40, (int) (230 * GameJerem.SCALE), 1, () -> {
			Menu.sound.play(14);
			JFrame mainwindow = this.getGame().getMainFrame();
			new AutomataSelectionPage(mainwindow);
		}, LoadSave.OPTIONS_AUTOMATES, LoadSave.OPTIONS_AUTOMATES1);
		buttons[1] = new OptionButton(GameJerem.GAME_WIDTH / 2 + 100, (int) (270 * GameJerem.SCALE), 2, () -> {
			Menu.sound.play(14);
			JFrame mainwindow = this.getGame().getMainFrame();
			new IPSelectionPage(mainwindow);
		}, LoadSave.OPTIONS_IP, LoadSave.OPTIONS_IP1);
		buttons[2] = new OptionButton(GameJerem.GAME_WIDTH / 2 + 70, (int) (310 * GameJerem.SCALE), 2, () -> {
			Menu.sound.play(14);
			Gamestate.state = Gamestate.MENU;
		}, LoadSave.BUTTON_QUIT, LoadSave.BUTTON_QUIT1);
		buttons[3] = new OptionButton(GameJerem.GAME_WIDTH / 2 + 50, (int) (190 * GameJerem.SCALE), 2, () -> {
			Menu.sound.play(14);
			GameOptions.instance.playerCount--;
			if (GameOptions.instance.playerCount < 1)
				GameOptions.instance.playerCount = 8;
		}, LoadSave.FLECHE_GAUCHE, LoadSave.FLECHE_GAUCHE1);
		buttons[4] = new OptionButton(GameJerem.GAME_WIDTH / 2 + 205, (int) (190 * GameJerem.SCALE), 2, () -> {
			Menu.sound.play(14);
			GameOptions.instance.playerCount++;
			if (GameOptions.instance.playerCount > 8)
				GameOptions.instance.playerCount = 1;
		}, LoadSave.FLECHE_DROITE, LoadSave.FLECHE_DROITE1);
	}

	@Override
	public void update() {
		for (OptionButton mb : buttons)
			mb.update();
	}

	@Override
	public void draw(Graphics g) {

		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
		g.drawImage(playerspng, playerX, playerY, playerspng.getWidth() * 2, playerspng.getHeight() * 2, null);
		g.drawImage(boxpng, boxX, boxY, boxpng.getWidth() * 2, boxpng.getHeight() * 2, null);
		this.selector.draw(g);
		for (OptionButton mb : buttons)
			mb.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (OptionButton mb : buttons) {
			if (isIn(e, mb)) {
				mb.setMousePressed(true);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (OptionButton mb : buttons) {
			if (isIn(e, mb)) {
				if (mb.isMousePressed())
					mb.action.exec();
				break;
			}
		}

		resetButtons();
	}

	private void resetButtons() {
		for (OptionButton mb : buttons)
			mb.resetBools();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (OptionButton mb : buttons)
			mb.setMouseOver(false);

		for (OptionButton mb : buttons)
			if (isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
	}

}
