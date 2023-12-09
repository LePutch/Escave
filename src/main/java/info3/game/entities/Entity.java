package info3.game.entities;

import info3.game.Avatar;
import info3.game.LocalController;
import info3.game.Model;
import info3.game.Vec2;
import info3.game.assets.AnimatedImage;
import info3.game.assets.Paintable;
import info3.game.automata.Automata;
import info3.game.automata.AutomataState;
import info3.game.automata.Category;
import info3.game.automata.CurrentState;
import info3.game.automata.behaviors.Behaviour;
import info3.game.physics.BoxCollider;
import info3.game.physics.Collider;
import info3.game.torus.Map;

/**
 * Représente une entité du jeu.
 * 
 * Tout (ou presque) ce qui s'affiche à l'écran dans le jeu est une entité.
 * 
 * La logique des entités est contrôlée par un automate (pas encore implémenté).
 * 
 * Elle possèdent un `Avatar` qui contrôle comment elles doivent être affichées.
 *
 */
public abstract class Entity {
	/**
	 * La position de l'entité en coordonnées globales
	 */
	Vec2 position;

	/**
	 * L'avatar de l'entité
	 */
	protected Avatar avatar;
	protected Collider collider;
	float frictionFactor;
	protected Automata automata;
	protected CurrentState currentState;
	protected Behaviour behaviour;
	private int pointsDeVie;

	public int getPointsDeVie() {
		return pointsDeVie;
	}

	public void setPointsDeVie(int pointsDeVie) {
		this.pointsDeVie = pointsDeVie;
	}

	public int degatMob;
	public int degatEpee;
	public int degatPioche;
	protected Category category;
	public Vec2 mousePos;

	protected LocalController controller;

	public Vec2 getPosition() {
		return this.position;
	}

	public void setPosition(Vec2 pos) {
		if (this.position != null && this.position.equals(pos)) {
			return;
		}
		Map map = Model.getMap();

		// dépassement du haut
		float mapWidth = map.width * Block.SIZE;
		float mapHeight = map.height * Block.SIZE;
		if (pos.getX() >= 0 && pos.getX() <= mapWidth && pos.getY() < 0) {
			pos = new Vec2(pos.getX(), pos.getY() + mapHeight);
		}

		// dépassement du bas
		else if (pos.getX() >= 0 && pos.getX() <= mapWidth && pos.getY() > mapHeight) {
			pos = new Vec2(pos.getX(), pos.getY() - mapHeight);
		}

		// dépassement à droite
		else if (pos.getY() >= 0 && pos.getY() <= mapHeight && pos.getX() > mapWidth) {
			pos = new Vec2(pos.getX() - mapHeight, pos.getY());

		}

		// dépassement à gauche
		else if (pos.getY() >= 0 && pos.getY() <= mapHeight && pos.getX() < 0) {
			pos = new Vec2(pos.getX() + mapWidth, pos.getY());
		}

		this.position = pos;
		if (this.avatar != null) {
			this.avatar.setPosition(this.position);

			this.controller.updateAvatar(this.avatar.getId(), this.avatar.getPosition());
		}
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setAutomata(Automata automata) {
		this.automata = automata;
		if (this.automata != null) {
			this.currentState = new CurrentState(this.automata.getInitialState());
		}
	}

	public void setAutomata(Automata automata, AutomataState state) {
		this.automata = automata;
		this.currentState = new CurrentState(state);
	}

	public Automata getAutomata() {
		return this.automata;
	}

	public void setBehaviour(Behaviour behaviour) {
		this.behaviour = behaviour;
	}

	public Behaviour getBehaviour() {
		return this.behaviour;
	}

	public Entity(LocalController c, int points) {
		this.controller = c;
		this.collider = new BoxCollider(Block.SIZE, Block.SIZE, 0, 0);
		this.frictionFactor = 0.6f;
		this.pointsDeVie = points;
		this.automata = null;
		this.currentState = null;
		this.behaviour = null;
		this.degatMob = 0;
		this.degatEpee = 0;
		this.degatPioche = 0;
	}

	/**
	 * Cette fonction est appelée à chaque tick de jeu
	 * 
	 * @param elapsed Le nombre de millisecondes qui se sont écoulées depuis le
	 *                dernier tick
	 */
	public void tick(long elapsed) {
		if (this.avatar != null) {
			this.avatar.tick(elapsed);
		}
		if (this.automata != null && this.currentState != null) {
			this.automata.step(this, this.currentState.getState(), elapsed);
		}
	}

	public Avatar getAvatar() {
		return this.avatar;
	}

	public final Collider getCollider() {
		return this.collider;
	}

	public LocalController getController() {
		return this.controller;
	}

	public float getFrictionFactor() {
		return frictionFactor;
	}

	public void setCurrentState(CurrentState s) {
		this.currentState = s;
	}

	public void setPaintable(Paintable p) {
		if (p.getPath().equals(this.avatar.getPaintable().getPath())) {
			return;
		}
		this.avatar.setPaintable(p);
		Vec2 pos = this.getPosition();
		this.avatar.setPosition(pos);
		this.controller.updateAvatar(this.avatar);
	}

	public Paintable getPaintable() {
		return this.avatar.getPaintable();
	}

	public String animationDir() {
		return ".";
	}

	public void playAnimation(String name, int frameCount, int delay, int offsetX, int offsetY, boolean loop) {
		this.getAvatar().getOffset().setX(offsetX);
		this.getAvatar().getOffset().setY(offsetY);
		AnimatedImage anim = new AnimatedImage(this.animationDir() + "/" + name + ".png", frameCount, delay, loop);
		this.setPaintable(anim);
	}

	public void playAnimation(String name, int frameCount, int delay, int offsetX, int offsetY, boolean loop,
			boolean cancellable) {
		this.getAvatar().getOffset().setX(offsetX);
		this.getAvatar().getOffset().setY(offsetY);
		AnimatedImage anim = new AnimatedImage(this.animationDir() + "/" + name + ".png", frameCount, delay, loop,
				cancellable);
		this.setPaintable(anim);
	}

	public CurrentState getCurrentState() {
		return this.currentState;
	}
}
