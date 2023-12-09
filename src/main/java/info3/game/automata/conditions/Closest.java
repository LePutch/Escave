package info3.game.automata.conditions;

import info3.game.automata.Category;
import info3.game.automata.Direction;
import info3.game.entities.Entity;

public class Closest implements ICondition {
	Direction d;
	Category c;

	public Closest(Category c, Direction d) {
		this.d = d;
		this.c = c;
	}

	@Override
	public boolean eval(Entity e) {
		return e.getBehaviour().closest(e, c, d, 0); //0arbitraire
	}

	public String toString() {
		return "Closest(" + c.name() + ", " + d.name() + ")";
	}

}
