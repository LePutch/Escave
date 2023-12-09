package info3.game.automata.actions;

import info3.game.automata.Direction;
import info3.game.entities.Entity;

public class Protect implements IAction {

	Direction d;

	public Protect(Direction dir) {
		this.d = dir;
	}

	@Override
	public void apply(Entity e) {
		e.getBehaviour().protect(e, d , 0);
	}

	public String toString() {
		return "Protect(" + d.name() + ")";
	}

}
