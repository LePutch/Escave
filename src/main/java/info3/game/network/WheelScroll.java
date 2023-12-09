package info3.game.network;

public class WheelScroll extends NetworkMessage {
	private static final long serialVersionUID = 4020837385876872111L;

	public boolean up;

	public WheelScroll(boolean up) {
		this.up = up;
	}
}
