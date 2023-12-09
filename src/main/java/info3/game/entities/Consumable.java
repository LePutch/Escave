package info3.game.entities;

import info3.game.LocalController;

public abstract class Consumable extends Tool {

	public Consumable(LocalController c, Player owner) {
		super(c, owner);
		this.special = false;

	}
}
