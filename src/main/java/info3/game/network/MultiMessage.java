package info3.game.network;

import java.util.Collection;

public class MultiMessage extends NetworkMessage {

	private static final long serialVersionUID = 5472038992208628708L;

	public NetworkMessage[] messages;

	public MultiMessage(Collection<NetworkMessage> collection) {
		this.messages = collection.toArray(new NetworkMessage[collection.size()]);
	}
}
