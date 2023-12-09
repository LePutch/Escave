package info3.game.network;

import info3.game.Vec2;
import info3.game.menu.GameOptions;

public class JoinGame extends NetworkMessage {
	private static final long serialVersionUID = 3365083741581125257L;

	public Vec2 screenSize;

	public GameOptions options;

	public JoinGame(Vec2 screen, GameOptions options) {
		this.screenSize = screen;
		this.options = options;
	}
}
