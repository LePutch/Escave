package info3.game.automata.actions;

import info3.game.automata.Direction;
import info3.game.entities.Entity;

public class Egg implements IAction {

	Direction d;
	
	public Egg(Direction dir) {
		this.d = dir; 
	}
	
	@Override
	public void apply(Entity e) {
		e.getBehaviour().egg(e, d);
	}

	public String toString() {
		return "Egg()";
	}

}
