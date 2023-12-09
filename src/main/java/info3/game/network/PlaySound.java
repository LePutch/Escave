package info3.game.network;

public class PlaySound extends NetworkMessage {
	private static final long serialVersionUID = 4354803838639317149L;
	public int idx;

	public PlaySound(int i) {
		this.idx = i;
	}
}
