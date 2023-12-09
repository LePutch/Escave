package info3.game.automata.actions;

import info3.game.entities.Entity;

public class Explode implements IAction {

	@Override
	public void apply(Entity e) {
		e.getBehaviour().explode(e);
	}

	public String toString() {
		return "Explode()";
	}

}
