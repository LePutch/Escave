package info3.game.entities;

import info3.game.LocalController;
import info3.game.automata.Direction;

public abstract class Tool extends Entity {

	private String name;
	protected Player owner;
	// les objets spéciaux sont ceux qui ne peuvent pas quitter l'inventaire
	protected boolean special;

	public Tool(LocalController c, Player owner) {
		super(c, 0);
		this.owner = owner;
	}

	public abstract boolean useTool(Direction d);

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSpecial() {
		return this.special;
	}
}
