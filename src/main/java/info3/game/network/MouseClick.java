package info3.game.network;

import info3.game.Vec2;

public class MouseClick extends NetworkMessage {
	private static final long serialVersionUID = -2288738363125030763L;
	public Vec2 position;

	public MouseClick(int x, int y) {
		this.position = new Vec2(x, y);
	}
}
