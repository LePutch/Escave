package info3.game.assets;

import java.awt.Graphics;

import info3.game.Vec2;

public abstract class Paintable extends Asset implements Cloneable {
	public Paintable(String path) {
		super(path);
	}

	public abstract void tick(long elapsed);

	public abstract Paintable duplicateFromPath(String path);

	public Paintable clone() {
		try {
			Paintable c = (Paintable) super.clone();
			if (this.loaded) {
				c.load();
			}
			return c;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public abstract void paint(Graphics g, Vec2 screenCoords, Vec2 scale);
}
