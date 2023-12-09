package info3.game.assets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import info3.game.Vec2;

public class Label extends Paintable {
	private char[] chars;
	static Font font = new Font("sans-serif", Font.BOLD, 15);

	public Label(String text) {
		super(text);
		this.chars = text.toCharArray();
	}

	private static final long serialVersionUID = -4481581749224198365L;

	@Override
	public void tick(long elapsed) {
	}

	@Override
	public void paint(Graphics g, Vec2 screenCoords, Vec2 scale) {
		g.setFont(font);
		g.setColor(Color.white);
		g.drawChars(chars, 0, chars.length, (int) screenCoords.getX(), (int) screenCoords.getY());
	}

	@Override
	public Paintable duplicateFromPath(String path) {
		return new Label(path);
	}

	@Override
	public void load() {
		return;
	}
}
