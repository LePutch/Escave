package info3.game.automata.actions;

import info3.game.automata.Direction;
import info3.game.entities.Entity;

public class Pop implements IAction {

	Direction d;

	public Pop(Direction dir) {
		this.d = dir;
	}

	@Override
	public void apply(Entity e) {
		e.getBehaviour().pop(e, d); // d pas tjrs utilisé
	}

	public String toString() {
		if (d == null)
			return "Pop()";
		return "Pop(" + d.name() + ")";
	}

}
