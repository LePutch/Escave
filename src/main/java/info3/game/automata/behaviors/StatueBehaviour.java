package info3.game.automata.behaviors;

import info3.game.Model;
import info3.game.automata.Category;
import info3.game.automata.Direction;
import info3.game.entities.Entity;
import info3.game.entities.Player;
import info3.game.entities.Statue;
import info3.game.physics.RigidBody;

public class StatueBehaviour extends Behaviour {

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
		// activer la statue et le il y a un transfert d'automate
		Statue s = (Statue) e;
		Player p = s.getPlayer();
		s.playAnimation("statue-transfert", 12, 200, -16, -104, false);
		e.getController().viewFor(p.getColor()).playSound(6);
		s.setCategory(Category.PLAYER);
		s.setAutomata(Model.getAutomata("Player"));
		p.setCategory(Category.SOMETHING);
		p.setControlledEntity(e);
		s.getPlayer().getBehaviour().wizz(s.getPlayer(), d);
		s.getCurrentState().block(1000);
	}

	@Override
	public void pop(Entity e, Direction d) {
		// pop=hit
	}

	@Override
	public void move(Entity e, Direction d) {
		RigidBody p = (RigidBody) e;
		switch (d) {
		case EST:
			p.getSpeed().setX(140 * p.speedFactor);
			break;
		case WEST:
			p.getSpeed().setX(-140);
			break;
		default:
			break;
		}

		return;

	}

	@Override
	public void protect(Entity e, Direction d, int dmg) {
		Statue s = (Statue) e;
		Player p = s.getPlayer();
		s.getSpeed().setX(0);
		s.setCategory(Category.TEAM);
		s.setAutomata(Model.getAutomata("Statue"));
		p.setCategory(Category.PLAYER);
		p.setAutomata(Model.getAutomata("Player"));
		p.setControlledEntity(p);
		e.playAnimation("statue-idle", 4, 1000, -16, -64, true);
	}

	@Override
	public void move(Entity e) {
		// pas besoin

	}

	@Override
	public void jump(Entity e) {
		((RigidBody) e).getSpeed().setY(-350);
		return;
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
		this.protect(e, null, 0);
		e.setAutomata(null);
		e.setBehaviour(null);
	}

	@Override
	public void egg(Entity e, Direction d) {
		// TODO Auto-generated method stub

	}

}
