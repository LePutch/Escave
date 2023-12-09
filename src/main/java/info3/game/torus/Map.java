package info3.game.torus;

import java.util.ArrayList;
import java.util.Collections;

import info3.game.entities.Block;

public class Map extends Torus<Block> {
	private ArrayList<Block> array;

	public Map(int w, int h) {
		super(w, h);
		this.array = new ArrayList<Block>(Collections.nCopies(w * h, null));
	}

	@Override
	protected Block getNoWrap(int x, int y) {
		return this.array.get(y * this.height + x);
	}

	@Override
	protected void setNoWrap(int x, int y, Block value) {
		this.array.set(y * this.height + x, value);
	}
}