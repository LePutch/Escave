package info3.game.network;

import info3.game.Avatar;

public class SyncCamera extends NetworkMessage {
	private static final long serialVersionUID = 8811547359177873107L;

	public int avatarId;

	public SyncCamera(Avatar a) {
		this.avatarId = a.getId();
	}
}
