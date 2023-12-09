package info3.game.automata.conditions;

import info3.game.automata.Category;
import info3.game.automata.Direction;
import info3.game.entities.Entity;

public class Cell implements ICondition {
	Direction d;
	Category c;

	public Cell(Direction d, Category c) {
		this.d = d;
		this.c = c;
	}

	@Override
	public boolean eval(Entity e) {
		return e.getBehaviour().cell(e, d, c);

	}

	public String toString() {
		return "Cell(" + d.name() + ", " + c.name() + ")";
	}
}
