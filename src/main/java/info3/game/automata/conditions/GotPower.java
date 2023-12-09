package info3.game.automata.conditions;

import info3.game.entities.Entity;

public class GotPower implements ICondition {

	@Override
	public boolean eval(Entity e) {
		return e.getBehaviour().gotPower(e);
	}

	public String toString() {
		return "GotPower()";
	}

}
