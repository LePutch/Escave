package info3.game.menu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2703227226283234010L;
	private MouseInputs mouseInputs;
	private GameJerem game;
	private Image bg;

	public GamePanel(GameJerem game2) {
		mouseInputs = new MouseInputs(this);
		this.game = game2;
		setOpaque(false);
		setPanelSize();
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		this.bg = new ImageIcon("src/main/resources/UI/bg_menu.png").getImage();
	}

	private void setPanelSize() {
		Dimension size = new Dimension(game.getGameWidth(), game.getGameHeight());
		setPreferredSize(size);

	}

	public void updateGame() {

	}

	public void paintComponent(Graphics g) {
		g.drawImage(bg, 0, 0, this.getWidth(), this.getHeight(), null);
		game.render(g);
	}

	public GameJerem getGame() {
		return game;
	}

}