package info3.game.automata.behaviors;

import info3.game.Model;
import info3.game.Vec2;
import info3.game.automata.Category;
import info3.game.automata.Direction;
import info3.game.entities.Block;
import info3.game.entities.Entity;

public class BlockBehaviour extends Behaviour {

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
		// jamais utilisé
		return false;
	}

	@Override
	public boolean closest(Entity e, Category c, Direction d, int diam_vision) {
		return false;
	}

	@Override
	public boolean gotPower(Entity e) {
		if (e.getPointsDeVie() > 0) {
			return true;
		} else {
			Vec2 coords = new Vec2(e.getPosition()).divide(Block.SIZE);
			Model.deleteBlock((int) coords.getX(), (int) coords.getY());
		}

		return false;

	}

	@Override
	public boolean gotStuff(Entity e) {
		// pas besoin
		return false;
	}

	@Override // pas normal les speed
	public void wizz(Entity e, Direction d) {
		// wizz=coup_reçu
		e.setPointsDeVie(e.getPointsDeVie() - 1); // à ajuster (dmg)
	}

	@Override
	public void pop(Entity e, Direction d) {
		// TODO pas besoin
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
