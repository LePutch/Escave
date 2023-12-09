package info3.game.automata.behaviors;

import info3.game.Inventory;
import info3.game.automata.Direction;
import info3.game.entities.Entity;

public class InventaireBehaviour extends Behaviour {

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
	public boolean gotPower(Entity e) {
		// pas besoin
		return false;
	}

	@Override
	public boolean gotStuff(Entity e) {
		Inventory p = (Inventory) e;
		if (p.rest1place()) {
			return true;
		}
		return false;
	}

	@Override
	public void wizz(Entity e, Direction d) {
		// wizz=mettre en réserve
		Inventory p = (Inventory) e;
		// p.pick()

	}

	@Override
	public void pop(Entity e, Direction d) {
		// utiliser l'objet donc si c une arme,on appelle le hit de playerbehaviour
		Inventory p = (Inventory) e;
		p.use(d);
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
		// jete l'objet de l'inventaire
		Inventory p = (Inventory) e;
		p.drop();

	}

	@Override
	public void store(Entity e) {
		// store=wizz

	}

	@Override
	public void get(Entity e) {
		// se déplaçer dans l'inventaire
		// mouseScroll(Player e, WheelScroll wheelScroll);
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
