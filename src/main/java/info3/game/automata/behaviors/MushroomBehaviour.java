package info3.game.automata.behaviors;

import info3.game.Model;
import info3.game.Vec2;
import info3.game.automata.Category;
import info3.game.automata.Direction;
import info3.game.entities.Entity;
import info3.game.entities.Mushroom;
import info3.game.entities.Player;
import info3.game.physics.RigidBody;

public class MushroomBehaviour extends Behaviour {

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

		int diam = 320; // en pixel
		return super.closest(e, c, d, diam);
	}

	@Override
	public boolean gotStuff(Entity e) {
		// pas besoin
		return false;
	}

	@Override
	public void wizz(Entity e, Direction d) {
		// wizz=jump

		switch (d) {
		case NORTHEST:
			RigidBody p = (RigidBody) e;
			p.getSpeed().setY(-370);
			p.getSpeed().setX(150 * p.speedFactor);
			break;
		case NORTHWEST:
			RigidBody p1 = (RigidBody) e;
			p1.getSpeed().setY(-370);
			p1.getSpeed().setX(150 * p1.speedFactor);
			break;
		default:
			break;
		}

		return;
	}

	@Override
	public void pop(Entity e, Direction d) {
		// pop = hit
		if (cell(e, Direction.HERE, Category.PLAYER)) {
			System.out.println("Mushroom hit player?");
			if (ret instanceof Player) {
				e.getController().viewFor(((Player) ret).getColor()).playSound(7);
			}
			super.ret.getBehaviour().protect(ret, d, e.degatMob);
			switch (d) {
			case EST:
				e.playAnimation("attack-right", 4, 200, 0, -64, false);
				break;
			default:
				e.playAnimation("attack-left", 4, 200, 0, -64, false);
				break;
			}
		}
		return;
	}

	@Override
	public void move(Entity e, Direction d) {

		RigidBody p = (RigidBody) e;
		// new RigidBody(e, 1, 5);
		switch (d) {
		case EST:
			p.getSpeed().setX(50 * p.speedFactor);
			p.playAnimation("walk-right", 5, 400, 0, -64, false);
			p.setDirection(Direction.EST);
			break;
		case WEST:
			p.getSpeed().setX(-50);
			p.playAnimation("walk-left", 5, 400, 0, -64, false);
			p.setDirection(Direction.WEST);
			break;
		case SOUTH:
			p.getSpeed().setX(0);
			break;
		default:
			break;
		}

	}

	@Override
	public void protect(Entity e, Direction d, int dmg) {
		e.setPointsDeVie(e.getPointsDeVie() - dmg);
		System.out.println(e.getPointsDeVie());
		RigidBody p = (RigidBody) e;
		if (e.getPointsDeVie() <= 0) {
			if (ret instanceof Player) {
				e.getController().viewFor(((Player) ret).getColor()).playSound(4);
			}
			if (p.getDirection() == Direction.EST)
				p.playAnimation("death-right", 9, 200, 0, -60, false);
			else if (p.getDirection() == Direction.WEST)
				p.playAnimation("death-left", 9, 200, 0, -60, false);
			e.getBehaviour().egg(e, d);
			Model.deleteEntity(p);
			return;
		}
		if (p.getDirection() == Direction.EST)
			p.playAnimation("hurt-right", 4, 200, 0, -64, false);
		else if (p.getDirection() == Direction.WEST)
			p.playAnimation("hurt-left", 4, 200, 0, -64, false);
		switch (d) {
		case SOUTH:
			p.getSpeed().setY(-240);
			break;
		case EST:
			p.getSpeed().setX(100);
			p.getSpeed().setY(-100);
			break;
		case WEST:
			p.getSpeed().setX(-100);
			p.getSpeed().setY(-100);
			break;
		case NORTH:
			p.getSpeed().setY(240);
			break;
		case NORTHEST:
			p.getSpeed().setY(240);
			p.getSpeed().setX(240);
			break;
		case NORTHWEST:
			p.getSpeed().setY(240);
			p.getSpeed().setX(-240);
			break;
		case SOUTHEST:
			p.getSpeed().setY(-240);
			p.getSpeed().setX(240);
			break;
		case SOUTHWEST:
			p.getSpeed().setY(-240);
			p.getSpeed().setX(-240);
			break;
		default:
			break;
		}
		p.getCurrentState().block(300);
		return;
	}

	@Override
	public void move(Entity e) {
		// pas besoin
	}

	@Override
	public void jump(Entity e) {
		// wizz=jump donc fait dans wizz
	}

	@Override
	public void hit(Entity e) {
		// pop=hit donc fait dans pop

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
		Mushroom pere = (Mushroom) e;
		for (int c = 0; c <= pere.childRemain; c++) {
			int decX = 48;
			switch (d) {
			case EST:
				decX = 48;
				break;
			case WEST:
				decX = -48;
				break;
			default:
				decX = 0;
				break;
			}
			Vec2 childPos = new Vec2(pere.getPosition().getX() + decX * c, pere.getPosition().getY());
			Mushroom child = new Mushroom(pere.getController(), childPos, pere.childRemain - 1);
			Model.spawn(child);
		}
	}

}
