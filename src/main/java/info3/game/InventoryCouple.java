package info3.game;

import info3.game.entities.Tool;

public class InventoryCouple {
	private int number;
	private Tool tool;
	private int index;

	public InventoryCouple(Tool tool, int number) {
		this.tool = tool;
		this.number = number;

	}

	public InventoryCouple(Tool tool) {
		this.tool = tool;
		this.number = 0;
	}

	public Tool getTool() {
		return this.tool;
	}

	public int getNumber() {
		return this.number;
	}

	public boolean add() {
		if (this.getTool().isSpecial()) {
			return false;
		}
		this.number++;
		return true;
	}

	public boolean add(int i) {
		if (this.getTool().isSpecial()) {
			return false;
		}
		this.number += i;
		return true;
	}

	public boolean sub() {
		if (this.getTool().isSpecial() || this.getNumber() <= 0) {
			return false;
		}
		this.number--;
		return false;
	}

	public boolean sub(int i) {
		if (this.getTool().isSpecial() || this.getNumber() <= 0) {
			return false;
		}
		this.number -= i;
		return true;
	}

	public void printCouple() {
		if (this.tool != null)
			System.out.print("( " + this.tool.getName() + " : " + this.getNumber() + " )");
	}

	public void setIndex(int idx) {
		this.index = idx;
	}

	public int getIndex() {
		return this.index;
	}
}