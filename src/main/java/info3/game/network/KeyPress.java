package info3.game.network;

public class KeyPress extends NetworkMessage {
	private static final long serialVersionUID = -1013068884208780503L;

	public int code;

	public KeyPress(int code) {
		this.code = code;
	}
}
