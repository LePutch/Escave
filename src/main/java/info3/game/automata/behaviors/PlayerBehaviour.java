package info3.game.automata.behaviors;

import info3.game.Inventory;
import info3.game.Model;
import info3.game.assets.AnimatedImage;
import info3.game.automata.Category;
import info3.game.automata.Direction;
import info3.game.entities.Entity;
import info3.game.entities.Player;
import info3.game.physics.RigidBody;

public class PlayerBehaviour extends Behaviour {

	public Entity ret;
	private long jumpElapsed = 0;
	private long estElapsed = 0;
	private long westElapsed = 0;

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
		// pas besoin pour player
		return false;
	}

	@Override
	public boolean closest(Entity e, Category c, Direction d, int diam_vision) {
		int diam = 320; // en pixel
		return super.closest(e, c, d, diam);
	}

	@Override
	public boolean gotStuff(Entity e) {
		// pas besoin pour player
		return false;
	}

	@Override
	public void wizz(Entity e, Direction d) {
		// wizz=jump
		/*
		 * RigidBody p = new RigidBody(e, 1, 10); p.getSpeed().setY(-120);
		 */
//		((RigidBody) e).getSpeed().setY(-270);
//		return;
		// wizz = prise de contrôle de la statue
		// e.setCategory(Category.SOMETHING);

		// action vide pour aller dans l'état statue
		e.playAnimation("fige", 6, 200, 0, 0, false);
	}

	@Override
	public void pop(Entity e, Direction d) {
		// pop=hit à faire

		/*
		 * if (cell(e, d, Category.ADVERSAIRE)) { ret.getBehaviour().protect(ret, d,
		 * e.degatEpee); } else if (cell(e, d, Category.JUMPABLE)) {
		 * ret.getBehaviour().wizz(ret, d); // peut etre à ajuster } return;
		 */
		Player p = (Player) e;
		Inventory inv = p.getInventory();
		inv.mousePos = p.mousePos;
		inv.getBehaviour().pop(inv, d);

	}

	@Override
	public void move(Entity e, Direction d) {

		RigidBody p = (RigidBody) e;
		// new RigidBody(e, 1, 10);
		AnimatedImage anim = (AnimatedImage) e.getPaintable();
		switch (d) {
		case EST:
			if (System.currentTimeMillis() - estElapsed > 655) {
				if (e instanceof Player) {
					e.getController().viewFor(((Player) e).getColor()).playSound(11);
				}
				estElapsed = System.currentTimeMillis();
			}
			p.getSpeed().setX(190 * p.speedFactor);
			if (anim.isCancellable() || anim.isFinished() || anim.isLoop())
				p.playAnimation("walk-right", 5, 200, 0, -4, false, true);
			p.setDirection(Direction.EST);
			break;
		case WEST:
			if (System.currentTimeMillis() - westElapsed > 655) {
				if (e instanceof Player) {
					e.getController().viewFor(((Player) e).getColor()).playSound(11);
				}
				westElapsed = System.currentTimeMillis();
			}
			p.getSpeed().setX(-190);
			if (anim.isCancellable() || anim.isFinished() || anim.isLoop())
				p.playAnimation("walk-left", 5, 200, 0, -4, false, true);
			p.setDirection(Direction.WEST);
			break;
		case SOUTH:
			p.playAnimation("dig", 21, 100, -32, -32, false);
			p.setCategory(Category.SOMETHING);
			p.setBehaviour(new DoublureBehaviour());
			break;
		default:
			break;
		}

		return;
	}

	@Override
	public void move(Entity e) {
		// pas besoin pour player

	}

	@Override
	public void jump(Entity e) {
		// jump=wizz donc fait dans wizz
		((RigidBody) e).getSpeed().setY(-550);

		if (System.currentTimeMillis() - jumpElapsed > 200) {
			if (e instanceof Player) {
				e.getController().viewFor(((Player) e).getColor()).playSound(0);
			}
			jumpElapsed = System.currentTimeMillis();
		}

	}

	@Override
	public void hit(Entity e) {
		// pop=hit donc fait dans pop
	}

	@Override
	public void pick(Entity e) {
		// pas besoin pour le player
	}

	@Override
	public void throw_(Entity e) {
		// pas besoin pour le player

	}

	@Override
	public void store(Entity e) {
		// pas besoin pour le player

	}

	@Override
	public void get(Entity e) {
		// pas besoin pour le player

	}

	@Override
	public void power(Entity e) {
		// jamais utilisé

	}

	@Override
	public void explode(Entity e) {
		// pas besoin pour le player

	}

	@Override
	public void egg(Entity e, Direction d) {
		// pas besoin pour le player
		// passage en mode doublure

	}

	@Override
	public void protect(Entity e, Direction d, int dmg) {
		e.setPointsDeVie(e.getPointsDeVie() - dmg);
		RigidBody p = (RigidBody) e;
		switch (p.getDirection()) {
		case EST:
			p.playAnimation("protect-right", 2, 300, 0, 0, false);
			break;
		case WEST:
			p.playAnimation("protect-left", 2, 300, 0, 0, false);
			break;
		default:
			break;
		}
		if (e.getPointsDeVie() <= 0) {
			System.out.println("mort du joueur");
			if (p instanceof Player) {
				Player pp = (Player) p;
				pp.gameOver();
				e.getController().viewFor(((Player) e).getColor()).playSound(5);
			} else {
				Model.deleteEntity(p);
			}
		}
		switch (d) {
		case SOUTH:
			p.getSpeed().setY(-240);
			break;
		case EST:
			p.getSpeed().setX(240 * p.speedFactor);
			break;
		case WEST:
			p.getSpeed().setX(-240);
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
		return;
	}

}
