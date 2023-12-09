package info3.game.physics;

import info3.game.Vec2;

public abstract class Collider {
	float offsetX;
	float offsetY;

	Collider(float offX, float offY) {
		this.offsetX = offX;
		this.offsetY = offY;
	}

	public abstract CollisionType isColliding(Vec2 pos, Collider other, Vec2 otherPos) throws Exception;
}
