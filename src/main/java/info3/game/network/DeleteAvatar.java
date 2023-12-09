package info3.game.network;

public class DeleteAvatar extends NetworkMessage {
	private static final long serialVersionUID = 4915016037060585823L;
	public int id;

	public DeleteAvatar(int id) {
		this.id = id;
	}
}
