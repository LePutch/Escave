package info3.game;

import info3.game.automata.Category;
import info3.game.automata.Direction;
import info3.game.automata.behaviors.InventaireBehaviour;
import info3.game.entities.Block;
import info3.game.entities.Entity;
import info3.game.entities.Food;
import info3.game.entities.Pickaxe;
import info3.game.entities.Player;
import info3.game.entities.Sword;
import info3.game.entities.Tool;
import info3.game.entities.Water;

public class Inventory extends Entity {
	public final static short INVENTORY_SIZE = 5; // pickaxe, sword, water,food, block
	private InventoryCouple tools[]; // tableau de couples (objet : quantité)
	private int currentToolIndex; // index de l'objet en main
	private int size; // le nombre de catégories d'objets présentes dans l'inventaire
	LocalController controller;
	private Player owner;

	public Inventory(LocalController c, Player owner) {
		super(c, 1);
		this.setCategory(Category.TEAM);
		this.setAutomata(Model.getAutomata("Inventaire"));
		this.setBehaviour(new InventaireBehaviour());
		this.currentToolIndex = 0;
		this.tools = new InventoryCouple[INVENTORY_SIZE];
		this.controller = c;
		this.owner = owner;
	}

	public Tool getCurrentTool() {
		return toolAt(currentToolIndex);
	}

	// plusieurs façons de se déplacer dans l'inventaire

	public void selectCurrentTool(int i) {
		this.owner.hud.unselect(this.currentToolIndex);

		this.currentToolIndex = i % INVENTORY_SIZE; // et pour les nombres négatifs ?
		this.owner.hud.select(this.currentToolIndex);
	}

	// décale la selection d'un cran vers la droite
	public void moveRCurrentTool() {
		this.selectCurrentTool((this.currentToolIndex + 1) % INVENTORY_SIZE);
		System.out.println(getCurrentTool());
	}

	// décale la selection d'un cran vers la gauche
	public void moveLCurrentTool() {
		if (currentToolIndex == 0) {
			this.selectCurrentTool(INVENTORY_SIZE - 1);
		} else {
			this.selectCurrentTool(this.currentToolIndex - 1);
		}
	}

	// verifie la validité de l'indice de l'objet en main
	public void checkCurrentTool() {
		while (currentToolIndex >= size) {
			currentToolIndex--;
		}
	}

	public InventoryCouple[] getTools() {
		return this.tools;
	}

	public boolean pick(Tool t) {
		InventoryCouple couple = getCouple(t);

		if (couple == null)
			return this.addCouple(new InventoryCouple(t, 1));

		boolean res = couple.add();
		this.owner.hud.setCounter(couple.getIndex(), couple.getNumber());
		return res;
	}

	public boolean drop() {
		// se débarasser d'un objet

		if (this.isEmpty())
			return false;

		InventoryCouple toDrop = this.coupleAt(currentToolIndex);

		if (toDrop == null)
			return false;

		this.checkCurrentTool();
		boolean res = toDrop.sub();
		this.owner.hud.setCounter(currentToolIndex, toDrop.getNumber());
		return res;
	}

	public boolean use(Direction d) {
		// utiliser l'objet en main

		InventoryCouple couple = this.coupleAt(currentToolIndex);
		Tool current = couple.getTool();

		if (current == null)
			return false;

		if (!current.isSpecial()) {
			if (couple.getNumber() > 0) {
				if (current.useTool(d)) {
					this.drop(); // enlève 1 à la qtté
					return true;
				}
			}
		} else {
			current.useTool(d);
			return true;
		}
		return false;
	}

	// l'inventaire est dit vide s'il ne contient aucun consumable
	// vraiment utile ?
	public boolean isEmpty() {
		for (int i = 0; i < this.size; i++) {
			InventoryCouple tmp = tools[i];
			if (!tmp.getTool().isSpecial() && tmp.getNumber() > 0)
				return false;
		}
		return true;
	}

	public boolean rest1place() {
		int compteur = 0;
		for (int i = 0; i < this.size; i++) {
			InventoryCouple tmp = tools[i];
			if (!tmp.getTool().isSpecial() && tmp.getNumber() > 0)
				compteur++;
		}
		if (compteur == INVENTORY_SIZE - 1) {
			return true;
		}
		return false;
	}

	public InventoryCouple coupleAt(int i) {
		if (i >= 0 && i < this.size)
			return tools[i];
		return null;
	}

	public Tool toolAt(int i) {
		return coupleAt(i).getTool();
	}

	public InventoryCouple getCouple(Tool t) {
		for (int i = 0; i < this.size; i++) {
			InventoryCouple tmp = coupleAt(i);
			if (tmp.getTool().getClass() == t.getClass())
				return tmp;
		}
		return null;
	}

	public Tool getTool(Tool t) {
		if (getCouple(t) == null)
			return null;
		return getCouple(t).getTool();
	}

	public int indexOf(Tool t) {
		for (int i = 0; i < this.size; i++) {
			Tool tmp = toolAt(i);
			if (tmp.getClass() == t.getClass())
				return i;
		}
		return -1;
	}

	public boolean addCouple(InventoryCouple c) {
		if (this.size >= Inventory.INVENTORY_SIZE)
			return false;
		c.setIndex(this.size);
		this.tools[size] = c;
		this.size++;
		return true;
	}

	public void printInventory() {
		System.out.print("[ Inventory : ");
		for (int i = 0; i < this.size; i++) {
			if (coupleAt(i) != null)
				coupleAt(i).printCouple();

		}
		if (this.getCurrentTool() != null)
			System.out.print("CURRENT = " + this.getCurrentTool().getName());
		System.out.println("]");

	}

	public static Inventory createInventory(LocalController c, Player owner) {
		Inventory inv = new Inventory(c, owner);
		inv.addCouple(new InventoryCouple(new Pickaxe(c, owner), 1));
		inv.addCouple(new InventoryCouple(new Sword(c, owner), 1));
		inv.addCouple(new InventoryCouple(new Water(c, owner), 1));
		inv.addCouple(new InventoryCouple(new Food(c, owner), 10));
		inv.addCouple(new InventoryCouple(new Block(c, owner), 10));
		return inv;
	}
}
