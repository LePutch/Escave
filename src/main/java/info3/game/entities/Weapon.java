package info3.game.entities;

import info3.game.LocalController;

public abstract class Weapon extends Tool {

	public Weapon(LocalController c, Player owner) {
		super(c, owner);
		this.special = true;
	}

}
