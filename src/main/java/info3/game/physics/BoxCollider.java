package info3.game.physics;

import info3.game.Vec2;

public class BoxCollider extends Collider {
	public float width;
	public float height;

	public BoxCollider(float width, float height, float offX, float offY) {
		super(offX, offY);
		this.width = width;
		this.height = height;
	}

	@Override
	public CollisionType isColliding(Vec2 pos, Collider other, Vec2 otherPos) throws Exception {
		Vec2 colliderPos = pos.add(new Vec2(this.offsetX, this.offsetY));
		if (other instanceof BoxCollider) {
			return testCollision(colliderPos, (BoxCollider) other, otherPos);
		}
		throw new Exception("Collision not implemented");
	}

	public CollisionType testCollision(Vec2 pos, BoxCollider other, Vec2 otherPos) {
		if ((otherPos.getX() >= pos.getX() + this.width) // trop à droite
				|| (otherPos.getX() + other.width <= pos.getX()) // trop à gauche
				|| (otherPos.getY() >= pos.getY() + this.height) // trop en bas
				|| (otherPos.getY() + other.height <= pos.getY())) // trop en haut
			// No collision
			return CollisionType.NONE;
		else
		// Collision
		if (pos.getX() < otherPos.getX()) { // Collision RIGHT
			if (pos.getY() <= otherPos.getY()) { // Collision DOWN RIGHT
				// * Decide DOWN / RIGHT
				float diffX = pos.getX() + this.width - otherPos.getX();
				float diffY = pos.getY() + this.height - otherPos.getY();
				if (diffY == 0)
					return CollisionType.DOWN;
				if (diffX == diffY)
					return CollisionType.NONE;
				return diffX < diffY ? CollisionType.RIGHT : CollisionType.DOWN;
			} else { // Collision UP RIGHT
				// * Decide UP / RIGHT
				float diffX = pos.getX() + this.width - otherPos.getX();
				float diffY = otherPos.getY() + other.height - pos.getY();
				if (diffX == diffY)
					return CollisionType.NONE;
				return diffX < diffY ? CollisionType.RIGHT : CollisionType.UP;
			}
		} else { // Collision LEFT
			if (pos.getY() <= otherPos.getY()) { // Collision DOWN LEFT
				// * Decide DOWN / LEFT
				float diffX = otherPos.getX() + other.width - pos.getX();
				float diffY = pos.getY() + this.height - otherPos.getY();
				if (diffY == 0)
					return CollisionType.DOWN;
				if (diffX == diffY)
					return CollisionType.NONE;
				return diffX < diffY ? CollisionType.LEFT : CollisionType.DOWN;
			} else { // Collision UP LEFT
				// * Decide UP / LEFT
				float diffX = otherPos.getX() + other.width - pos.getX();
				float diffY = otherPos.getY() + other.height - pos.getY();
				if (diffX == diffY)
					return CollisionType.NONE;
				return diffX < diffY ? CollisionType.LEFT : CollisionType.UP;
			}
		}
	}

	public CollisionType testCollision(Vec2 pos, CircleCollider other, Vec2 otherPos) {
		return null;
	}
}
