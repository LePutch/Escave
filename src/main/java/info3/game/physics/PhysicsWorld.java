package info3.game.physics;

import java.util.ArrayList;
import java.util.HashSet;

import info3.game.Model;
import info3.game.Vec2;
import info3.game.entities.Block;

public class PhysicsWorld {
	public static final Vec2 GRAVITY = new Vec2(0.0f, 800f);
	public static final Vec2 MAXSPEED = new Vec2(200f, 5000f);
	private static final int RADIUS = 2;

	/**
	 * Calcule tous les changements de position dans le model dûs aux forces du
	 * monde et aux collisions
	 * 
	 * @param elapsed Temps écoulé depuis le dernier tick
	 * 
	 *
	 * @return void
	 */
	public void tick(long elapsed) {
		float elapsedSec = elapsed / 1000.0f;
		ArrayList<RigidBody> entities = new ArrayList<>(Model.getEntities());

		for (RigidBody rb : entities) {
			if (rb.type == RbState.STATIC)
				continue;
			step(rb, elapsedSec);
			Vec2 center = rb.getPosition().divide(Block.SIZE);

			HashSet<CollisionType> collisions = new HashSet<CollisionType>();
			Block[][] zone = Model.getMapZone((int) center.sub(RADIUS).getX(), (int) center.sub(RADIUS).getY(),
					RADIUS * 2, RADIUS * 2);
			for (int i = 0; i < zone.length; i++) {
				for (int j = 0; j < zone[i].length; j++) {
					Block currentBlock = zone[i][j];
					if (currentBlock == null) {
						continue;
					}

					try {
						CollisionType coll = rb.isColliding(currentBlock);
						if (coll == CollisionType.NONE)
							continue;
						collisions.add(coll);
						clearCoords(rb, currentBlock, coll);
					} catch (Exception e) {
						// Ne devrait jamais arriver
						e.printStackTrace();
						return;
					}

				}
			}
			if (!collisions.contains(CollisionType.DOWN)) {
				this.computeGravity(rb, elapsedSec);
			} else {
				// this.computeFrictionX(rb, floor);
			}
			for (CollisionType coll : collisions) {
				switch (coll) {
				case UP:
					if (rb.getSpeed().getY() < 0)
						rb.getSpeed().nullY();
					break;
				case DOWN:
					if (rb.getSpeed().getY() > 0)
						rb.getSpeed().nullY();
					break;
				case LEFT:
					if (rb.getSpeed().getX() < 0)
						rb.getSpeed().nullX();
					break;
				case RIGHT:
					if (rb.getSpeed().getX() > 0)
						rb.getSpeed().nullX();
					break;
				default:
					break;

				}
			}
			checkSpeed(rb);
		}
	}

	/**
	 * Ajout au RigidBody passé en paramètre la vitesse dû à la gravité que ce
	 * dernier à gagner pendant le temps elapsed
	 * 
	 * @param rb      RigidBody soumis à la gravité
	 * @param elapsed Temps écoulé depuis le dernier tick
	 * 
	 *
	 * @return void
	 */
	private void computeGravity(RigidBody rb, float elapsed) {
		Vec2 gravityForce = PhysicsWorld.GRAVITY.multiply(rb.getMass());
		rb.addForce(gravityForce);
		Vec2 speed = rb.getForce().divide(rb.getMass()).multiply(elapsed);
		rb.addSpeed(speed);
		rb.setForce(Vec2.nullVector());
	}

	/**
	 * Calcul la nouvelle vitesse selon l'axe X d'un RigidBody lorsqu'il est au
	 * contact d'une entité
	 * 
	 * @param rb RigidBody soumis à la friction
	 * @param e  Entity en friction avec un RigidBody
	 * 
	 *
	 * @return void
	 */
	private void computeFrictionX(RigidBody rb, Block e) {
		if (rb.getFrictionFactor() == 0 || e.getFrictionFactor() == 0)
			return;
		rb.getSpeed().setX(Math.round(rb.getSpeed().getX() * (rb.getFrictionFactor() + e.getFrictionFactor() / 2)));
	}

	/**
	 * Calcul et met à jour la nouvelle position d'une entité dynamique après un
	 * temps et selon les forces qui lui sont appliquées et sa vitesse initiale
	 * 
	 * @param rb      RigidBody dont on veut calculer la nouvelle position
	 * @param elapsed Temps écoulé depuis le dernier tick
	 * 
	 *
	 * @return void
	 */
	private void step(RigidBody rb, float elapsed) {
		Vec2 newPos = rb.getPosition().add(rb.getSpeed().multiply(elapsed));
		rb.setPosition(newPos);
	}

	private void clearCoords(RigidBody rb, Block bl, CollisionType coll) {
		switch (coll) {
		case UP:
			float diffUP = bl.getPosition().getY() + ((BoxCollider) bl.getCollider()).height - rb.getPosition().getY();
			rb.setPosition(rb.getPosition().add(new Vec2(0f, diffUP)));
			break;
		case DOWN:
			float diffDOWN = bl.getPosition().getY()
					- (rb.getPosition().getY() + ((BoxCollider) rb.getCollider()).height);
			rb.setPosition(rb.getPosition().add(new Vec2(0f, diffDOWN)));
			break;
		case LEFT:
			float diffLEFT = bl.getPosition().getX() + ((BoxCollider) bl.getCollider()).width - rb.getPosition().getX();
			rb.setPosition(rb.getPosition().add(new Vec2(diffLEFT, 0f)));
			break;
		case RIGHT:
			float diffRIGHT = rb.getPosition().getX() + ((BoxCollider) rb.getCollider()).width
					- bl.getPosition().getX();
			rb.setPosition(rb.getPosition().add(new Vec2(-diffRIGHT, 0f)));
			break;
		default:
			break;
		}
	}

	private void checkSpeed(RigidBody rb) {
		if (Math.abs(rb.getSpeed().getX()) > MAXSPEED.getX())
			rb.setSpeed(new Vec2(rb.getSpeed().getX() / Math.abs(rb.getSpeed().getX()) * MAXSPEED.getX(),
					rb.getSpeed().getY()));
	}
}
