package info3.game.entities;

import info3.game.LocalController;
import info3.game.Vec2;
import info3.game.automata.Direction;

public class Water extends Consumable {
	private int waterValue;

	public Water(LocalController c, Player owner) {
		super(c, owner);
		this.setName("Water");
		this.waterValue = 1;
	}

	public Water(LocalController c, Vec2 pos, Player owner) {
		this(c, owner);
		this.position = pos;
	}

	public Water(LocalController c, Vec2 pos, Player owner, int waterVal) {
		this(c, pos, owner);
		this.waterValue = waterVal;
	}

	@Override
	public boolean useTool(Direction d) {
		owner.water(waterValue);
		this.getController().viewFor(owner.getColor()).playSound(3);
		return true;
	}
}
