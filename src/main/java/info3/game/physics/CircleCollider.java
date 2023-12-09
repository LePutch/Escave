package info3.game.physics;

import info3.game.Vec2;

public class CircleCollider extends Collider {
	private float radius;

	CircleCollider(float radius, float offX, float offY) {
		super(offX, offY);
		this.radius = radius;
	}

	@Override
	public CollisionType isColliding(Vec2 pos, Collider other, Vec2 otherPos) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
