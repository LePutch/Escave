package info3.game.automata.actions;

import info3.game.automata.Direction;
import info3.game.entities.Entity;

public class Move implements IAction {

	Direction d;

	public Move(Direction dir) {
		this.d = dir;
	}

	@Override
	public void apply(Entity e) {
		e.getBehaviour().move(e, d);
	}

	public String toString() {
		return "Move(" + d.name() + ")";
	}

}
