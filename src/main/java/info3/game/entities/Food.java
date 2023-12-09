package info3.game.entities;

import info3.game.LocalController;
import info3.game.Vec2;
import info3.game.automata.Direction;

public class Food extends Consumable {

	private int feedValue;

	public Food(LocalController c, Player owner) {
		super(c, owner);
		this.setName("Food");
		this.feedValue = 1;
	}

	public Food(LocalController c, Vec2 pos, Player owner) {
		this(c, owner);
		this.position = pos;
	}

	public Food(LocalController c, Vec2 pos, Player owner, int feedVal) {
		this(c, pos, owner);
		this.feedValue = feedVal;
	}

	@Override
	public boolean useTool(Direction d) {
		this.getController().viewFor(owner.getColor()).playSound(8);
		owner.feed(feedValue);
		return true;
	}

}
