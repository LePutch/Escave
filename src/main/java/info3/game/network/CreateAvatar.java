package info3.game.network;

import info3.game.Avatar;

public class CreateAvatar extends NetworkMessage {
	private static final long serialVersionUID = 2693943721618745832L;

	public Avatar avatar;

	public CreateAvatar(Avatar av) {
		this.avatar = av;
	}
}
