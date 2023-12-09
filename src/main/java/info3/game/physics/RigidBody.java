package info3.game.physics;

import info3.game.LocalController;
import info3.game.Vec2;
import info3.game.automata.Direction;
import info3.game.entities.Block;
import info3.game.entities.Entity;

public class RigidBody extends Entity {
	private Vec2 force;
	private Vec2 speed;
	public float speedFactor = 1;
	private float mass;
	public RbState type;
	private Direction direction;

	public RigidBody(Entity e, float mass, int points) {
		super(e.getController(), points);
		force = Vec2.nullVector();
		speed = Vec2.nullVector();
		this.mass = mass;
		this.collider = new BoxCollider(Block.SIZE - 3, Block.SIZE - 3, 1, 1);
		this.type = RbState.DYNAMIC;
		this.direction = Direction.EST;
	}

	public void setState(RbState type) {
		this.type = type;
	}

	public RigidBody(float mass, LocalController c, int points) {
		super(c, points);
		force = Vec2.nullVector();
		speed = Vec2.nullVector();
		this.mass = mass;
		this.collider = new BoxCollider(Block.SIZE, Block.SIZE, 0, 0);
		this.direction = Direction.EST;
	}

	public void addSpeed(Vec2 speed) {
		this.speed = this.speed.add(speed);
	}

	public void addForce(Vec2 force) {
		this.force = this.force.add(force);
	}

	public CollisionType isColliding(Entity other) throws Exception {
		return this.getCollider().isColliding(this.getPosition().wrapCoords(), other.getCollider(),
				other.getPosition().wrapCoords());
	}

	/*
	 * Getters & Setters
	 */

	// mass
	public void setMass(float mass) {
		this.mass = mass;
	}

	public final float getMass() {
		return this.mass;
	}

	// speed
	public void setSpeed(Vec2 speed) {
		this.speed = speed.multiply(this.speedFactor);
	}

	public final Vec2 getSpeed() {
		return this.speed;
	}

	// force
	public void setForce(Vec2 force) {
		this.force = force;
	}

	public final Vec2 getForce() {
		return this.force;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return this.direction;
	}

}
