package info3.game.automata.conditions;

import info3.game.entities.Entity;

public class Key implements ICondition {

	int touche;

	public Key(int touche) {
		this.touche = touche;
	}

	@Override
	public boolean eval(Entity e) {
		return e.getBehaviour().key(e, touche);

	}

	public String toString() {
		return "Key(" + touche + ")";
	}

}
