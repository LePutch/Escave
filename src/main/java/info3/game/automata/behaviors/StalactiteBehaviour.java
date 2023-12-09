package info3.game.automata.behaviors;

import java.util.ArrayList;

import info3.game.Model;
import info3.game.Vec2;
import info3.game.assets.Image;
import info3.game.automata.Category;
import info3.game.automata.Direction;
import info3.game.entities.Block;
import info3.game.entities.Entity;
import info3.game.entities.Player;
import info3.game.physics.RbState;
import info3.game.physics.RigidBody;

public class StalactiteBehaviour extends Behaviour {

	Entity ret;

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
		int diam = Block.SIZE * 3; // en pixel
		return super.closest(e, c, d, diam);

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
		// wizz=tomber
		RigidBody p = (RigidBody) e;
		p.setState(RbState.DYNAMIC);
		System.out.println("stala tombe");
		p.addSpeed(new Vec2(0, 0));
		p.setPaintable(new Image("classic_block/stalactite_chute.png"));
	}

	@Override
	public void pop(Entity e, Direction d) {
		// pop=exploser
		ArrayList<Entity> nearEntities = Model.getNearEntities((int) (e.getPosition().getX()) - Block.SIZE,
				(int) (e.getPosition().getY()) - Block.SIZE, Block.SIZE * 3, Block.SIZE * 3);
		for (Entity e1 : nearEntities) {
			Category cat = e1.getCategory();
			if (cat == Category.PLAYER || cat == Category.ADVERSAIRE) {
				System.out.println("stala explose");
				e1.getBehaviour().protect(e1, Direction.HERE, 1);
				if (e1 instanceof Player) {
					e.getController().viewFor(((Player) e1).getColor()).playSound(9);
				}
			}
		}
		return;
	}

	@Override
	public void move(Entity e, Direction d) {
		// pas besoin

	}

	@Override
	public void protect(Entity e, Direction d, int dmg) {
		// pas besoin
		RigidBody p = (RigidBody) e;
		e.setPaintable(new Image(""));
		Model.deleteEntity(p);
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
