package info3.game.network;

import info3.game.entities.PlayerColor;

public class Welcome extends NetworkMessage {

	private static final long serialVersionUID = 1984830357315026450L;

	public PlayerColor yourColor;

	public Welcome(PlayerColor color) {
		this.yourColor = color;
	}
}
