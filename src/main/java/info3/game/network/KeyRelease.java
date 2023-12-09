package info3.game.network;

public class KeyRelease extends NetworkMessage {
	private static final long serialVersionUID = 1601465573725411833L;

	public int code;

	public KeyRelease(int code) {
		this.code = code;
	}
}
