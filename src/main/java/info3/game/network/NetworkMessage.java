package info3.game.network;

import java.io.Serializable;

import info3.game.entities.PlayerColor;

public abstract class NetworkMessage implements Serializable {
	public PlayerColor player;
}
