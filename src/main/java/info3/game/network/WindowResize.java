package info3.game.network;

import info3.game.Vec2;

public class WindowResize extends NetworkMessage {

	private static final long serialVersionUID = 5520574902286099797L;

	public Vec2 size;

	public WindowResize(Vec2 size) {
		this.size = size;
	}
}
