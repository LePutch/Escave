package info3.game.automata.conditions;

import info3.game.entities.Entity;

public class Not implements ICondition {

	private ICondition expr;

	public Not(ICondition e) {
		this.expr = e;
	}

	@Override
	public boolean eval(Entity e) {
		return !this.expr.eval(e);
	}

}
