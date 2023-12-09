package info3.game.automata.behaviors;

import info3.game.automata.Category;
import info3.game.automata.Direction;
import info3.game.entities.Entity;

public class DoublureBehaviour extends Behaviour {

	@Override
	public boolean true_(Entity e) {
		return true;
	}

	@Override
	public boolean key(Entity e, int keyCode) {
		return e.getController().isKeyPressed(e, keyCode);
	}

	@Override
	public boolean myDir(Entity e, Direction d) {
		// non utilisée
		return false;
	}

	@Override
	public boolean gotPower(Entity e) {
		// non utilisée
		return false;
	}

	@Override
	public boolean gotStuff(Entity e) {
		// non utilisée
		return false;
	}

	@Override
	public void wizz(Entity e, Direction d) {
		// non utilisée

	}

	@Override
	public void pop(Entity e, Direction d) {
		if (d == Direction.NORTH) {
			e.playAnimation("spawn", 9, 200, 0, -10, false);
			e.setCategory(Category.PLAYER);
			e.setBehaviour(new PlayerBehaviour());
		}
	}

	@Override
	public void move(Entity e, Direction d) {
		// est remplacée par pop()

	}

	@Override
	public void protect(Entity e, Direction d, int dmg) {
		// non utilisée
	}

	@Override
	public void move(Entity e) {
		// non utilisée
	}

	@Override
	public void jump(Entity e) {
		// non utilisée
	}

	@Override
	public void hit(Entity e) {
		// non utilisée
	}

	@Override
	public void pick(Entity e) {
		// non utilisée
	}

	@Override
	public void throw_(Entity e) {
		// non utilisée
	}

	@Override
	public void store(Entity e) {
		// non utilisée
	}

	@Override
	public void get(Entity e) {
		// non utilisée
	}

	@Override
	public void power(Entity e) {
		// non utilisée
	}

	@Override
	public void explode(Entity e) {
		// non utilisée
	}

	@Override
	public void egg(Entity e, Direction d) {
		// non utilisée
	}

}
