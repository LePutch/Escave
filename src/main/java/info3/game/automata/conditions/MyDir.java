package info3.game.automata.conditions;

import info3.game.automata.Direction;
import info3.game.entities.Entity;

public class MyDir implements ICondition {

	Direction d;

	public MyDir(Direction d) {
		this.d = d;
	}

	@Override
	public boolean eval(Entity e) {
		return e.getBehaviour().myDir(e, d);

	}

	public String toString() {
		return "MyDir(" + d.name() + ")";
	}

}
