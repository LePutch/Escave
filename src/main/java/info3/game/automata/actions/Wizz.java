package info3.game.automata.actions;

import info3.game.automata.Direction;
import info3.game.entities.Entity;

public class Wizz implements IAction {

	Direction d;

	public Wizz(Direction dir) {
		this.d = dir;
	}

	@Override
	public void apply(Entity e) { // d parfois inutile
		e.getBehaviour().wizz(e, d);
	}

	public String toString() {
		if (d == null)
			return "Wizz()";
		return "Wizz(" + d.name() + ")";
	}
}
