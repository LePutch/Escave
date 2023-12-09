package info3.game.automata.behaviors;

import info3.game.Model;
import info3.game.Vec2;
import info3.game.automata.Category;
import info3.game.automata.Direction;
import info3.game.entities.Entity;
import info3.game.entities.Socle;

public class SocleBehaviour extends Behaviour {

	private boolean atDest = false;

	@Override
	public boolean true_(Entity e) {
		return true;
	}

	@Override
	public boolean key(Entity e, int keyCode) {
		// pas besoin
		return false;
	}

	@Override
	public boolean myDir(Entity e, Direction d) {
		// pas besoin
		return false;
	}

	@Override
	public boolean closest(Entity e, Category c, Direction d, int diam_vision) {
		// pas besoin
		return false;
	}

	@Override
	public boolean gotPower(Entity e) {
		// pas besoin
		return false;
	}

	@Override
	public boolean gotStuff(Entity e) {
		// pas besoin
		return false;
	}

	@Override
	public void wizz(Entity e, Direction d) {
		super.ret = null;
		if (cell(e, Direction.NORTH, Category.TEAM) || cell(e, Direction.NORTH, Category.PLAYER)) {
			if (super.ret.getBehaviour() == null || super.ret.getBehaviour() instanceof StatueBehaviour) {
				if (super.ret.getBehaviour() != null) {
					super.ret.getBehaviour().explode(super.ret);
					super.ret.setPosition(e.getPosition().add(new Vec2(0, -96)));
				}
				if (!atDest) {
					e.getController().playSound(6);
					atDest = true;
				}
				((Socle) e).isActivated = true;
				Model.incrementActivatedSocles();
			}
		}
	}

	@Override
	public void pop(Entity e, Direction d) {
		// pas besoin

	}

	@Override
	public void move(Entity e, Direction d) {
		// pas besoin

	}

	@Override
	public void protect(Entity e, Direction d, int dmg) {
		// pas besoin

	}

	@Override
	public void move(Entity e) {
		// pas besoin

	}

	@Override
	public void jump(Entity e) {
		// pas besoin

	}

	@Override
	public void hit(Entity e) {
		// pas besoin

	}

	@Override
	public void pick(Entity e) {
		// pas besoin

	}

	@Override
	public void throw_(Entity e) {
		// pas besoin

	}

	@Override
	public void store(Entity e) {
		// pas besoin

	}

	@Override
	public void get(Entity e) {
		// pas besoin

	}

	@Override
	public void power(Entity e) {
		// pas besoin

	}

	@Override
	public void explode(Entity e) {
		// pas besoin

	}

	@Override
	public void egg(Entity e, Direction d) {
		// pas besoin

	}

}
