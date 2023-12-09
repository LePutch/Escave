package info3.game.automata.conditions;

import info3.game.entities.Entity;

public class True implements ICondition {

	@Override
	public boolean eval(Entity e) {
		return e.getBehaviour().true_(e);
	}

	public String toString() {
		return "True()";
	}

}
