package info3.game.physics;

import info3.game.Vec2;

public class Line {
	public Vec2 pt1;
	public Vec2 pt2;

	public Line(Vec2 pt1, Vec2 pt2) {
		this.pt1 = pt1;
		this.pt2 = pt2;
	}

	public Line(float x1, float y1, float x2, float y2) {
		this.pt1 = new Vec2(x1, y1);
		this.pt2 = new Vec2(x2, y2);
	}

	public float length() {
		return pt1.sub(pt2).length();
	}
}
