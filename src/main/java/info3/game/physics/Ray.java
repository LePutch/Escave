package info3.game.physics;

import info3.game.Vec2;

public class Ray {
	Vec2 origin;
	Vec2 direction;

	Ray(float x, float y) {
		this.origin = new Vec2(x, y);
		this.direction = new Vec2(0, 1);
	}

	Ray(Vec2 origin, Vec2 direction) {
		this.origin = origin;
		this.direction = direction;
	}

	public Vec2 intersect(Line line) {
		float x1 = line.pt1.getX();
		float y1 = line.pt1.getY();
		float x2 = line.pt2.getX();
		float y2 = line.pt2.getY();

		float x3 = this.origin.getX();
		float y3 = this.origin.getY();
		float x4 = this.origin.getX() + this.direction.getX();
		float y4 = this.origin.getY() + this.direction.getY();

		float denom = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		if (denom == 0)
			return null;
		float t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denom;
		float u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / denom;

		if (t > 0 && t < 1 && u > 0) {
			Vec2 intersect = new Vec2(x1 + t * (x2 - x1), y1 + t * (y2 - y1));
			return intersect;
		} else
			return null;

	}

	public Vec2 getOrigin() {
		return this.origin;
	}

	public Vec2 getDirection() {
		return this.direction;
	}

}
