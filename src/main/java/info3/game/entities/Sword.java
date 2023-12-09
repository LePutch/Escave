package info3.game.entities;

import java.util.ArrayList;

import info3.game.Inventory;
import info3.game.LocalController;
import info3.game.Model;
import info3.game.automata.Category;
import info3.game.automata.Direction;

public class Sword extends Weapon {
	int mobDmg = 50;
	int blockDmg = 0; // dmg à ajuster plus tard
	long elapsed = 0;

	public Sword(LocalController c, Player owner) {
		super(c, owner);
		this.setName("Sword");
	}

	public boolean useTool(Direction d) {
		if (System.currentTimeMillis() - elapsed > 300) {
			this.getController().viewFor(owner.getColor()).playSound(1);
			elapsed = System.currentTimeMillis();
		}

		Direction orientation = owner.getPosition().orientation(owner.mousePos);

		int decX, decY, width, height;

		switch (orientation) {
		case NORTH:
			decX = -Block.SIZE;
			decY = -Block.SIZE * 2;
			width = Block.SIZE * 3;
			height = Block.SIZE * 3;
			owner.playAnimation("attack-right", 5, 200, 0, -4, false);
			break;
		case SOUTH:
			decX = -Block.SIZE;
			decY = -Block.SIZE;
			width = Block.SIZE * 3;
			height = Block.SIZE * 2;
			owner.playAnimation("attack-right", 5, 200, 0, -4, false);
			break;
		case EST:
			decX = Block.SIZE;
			decY = -Block.SIZE;
			width = Block.SIZE * 2;
			height = Block.SIZE * 3;
			owner.playAnimation("attack-right", 5, 200, 0, -4, false);
			break;
		case WEST:
			decX = -Block.SIZE * 2;
			decY = -Block.SIZE;
			width = Block.SIZE * 2;
			height = Block.SIZE * 3;
			owner.playAnimation("attack-left", 5, 200, -32, -4, false);
			break;
		default:
			decX = 0;
			decY = 0;
			width = Block.SIZE;
			height = Block.SIZE;
			break;
		}

		System.out.println(orientation);
		ArrayList<Entity> nearEntities = Model.getNearEntities((int) (owner.getPosition().getX()) + decX,
				(int) (owner.getPosition().getY()) + decY, width, height);
		for (Entity e1 : nearEntities) {
			if (e1.getCategory() == Category.ADVERSAIRE) {
				if (e1.getPointsDeVie() - mobDmg <= 0) {
					Inventory inv = owner.getInventory();
					inv.pick(inv.toolAt(3));
					System.out.println("récupère de la food");

				}
				e1.getBehaviour().protect(e1, orientation, mobDmg);
				return true;
			}
		}

		return false;
	}

}
