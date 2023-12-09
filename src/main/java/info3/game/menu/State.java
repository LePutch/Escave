package info3.game.menu;

import java.awt.event.MouseEvent;

public class State {

	protected GameJerem game;

	public State(GameJerem game2) {
		this.game = game2;
	}

	public boolean isIn(MouseEvent e, MenuButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
	}

	public boolean isIn(MouseEvent e, OptionButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
	}

	public GameJerem getGame() {
		return game;
	}
}