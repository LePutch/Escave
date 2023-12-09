package info3.game.automata.actions;

import info3.game.entities.Entity;

public class Get implements IAction {

	@Override
	public void apply(Entity e) {
		e.getBehaviour().get(e);
	}

	public String toString() {
		return "Get()";
	}

}
