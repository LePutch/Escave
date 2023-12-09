package info3.game;

import java.awt.Graphics;
import java.io.Serializable;

import info3.game.assets.AnimatedImage;
import info3.game.assets.AssetServer;
import info3.game.assets.Paintable;
import info3.game.entities.Block;

public class Avatar implements Serializable {
	private static final long serialVersionUID = -7965867647047274240L;
	int id;
	int layer;
	boolean fixed;
	Vec2 position;
	Vec2 scale;
	Vec2 offset = new Vec2(0);
	Paintable image;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Vec2 getPosition() {
		return position;
	}

	private static transient final float DUPLICATE_RANGE = Block.SIZE * 20;

	public transient Avatar[] duplicates;

	private void updateDuplicates(Vec2 pos) {
		if (this.duplicates == null || this.fixed) {
			return;
		}
		int width = Model.getMap().width * Block.SIZE;
		int height = Model.getMap().height * Block.SIZE;

		boolean top = pos.getY() < DUPLICATE_RANGE;
		boolean bottom = pos.getY() > height - DUPLICATE_RANGE;
		boolean left = pos.getX() < DUPLICATE_RANGE;
		boolean right = pos.getX() > width - DUPLICATE_RANGE;

		float x = pos.getX();
		float y = pos.getY();
		float rightX = width + x;
		float bottomY = height + y;
		float leftX = x - width;
		float topY = y - height;

		this.duplicate(top && left, 0, new Vec2(rightX, bottomY));
		this.duplicate(top, 1, new Vec2(x, bottomY));
		this.duplicate(top && right, 2, new Vec2(leftX, bottomY));

		this.duplicate(left, 3, new Vec2(rightX, y));
		this.duplicate(right, 4, new Vec2(leftX, y));

		this.duplicate(bottom && left, 5, new Vec2(rightX, topY));
		this.duplicate(bottom, 6, new Vec2(x, topY));
		this.duplicate(bottom && right, 7, new Vec2(leftX, topY));
	}

	private void duplicate(boolean matches, int i, Vec2 pos) {
		if (matches) {
			if (this.duplicates[i] == null) {
				this.duplicates[i] = new AvatarBuilder(this.image.clone()).duplicate(false).position(pos)
						.scale(this.scale).build(Controller.controller);
			}
			Controller.controller.updateAvatar(this.duplicates[i].getId(), pos);
		} else {
			if (this.duplicates[i] != null) {
				Controller.controller.deleteAvatar(this.duplicates[i].getId());
				this.duplicates[i] = null;
			}
		}
	}

	public void setPosition(Vec2 pos) {
		synchronized (this.position) {
			this.updateDuplicates(pos);
			this.position = pos;
		}
	}

	public Vec2 getScale() {
		return scale;
	}

	public void setScale(Vec2 scale) {
		this.scale = scale;
	}

	Avatar() {
		this.position = new Vec2(0.0f, 0.0f);
		this.scale = new Vec2(2.0f, 2.0f);
		this.duplicates = new Avatar[8];
	}

	/**
	 * Crée un nouvel avatar.
	 * 
	 * @param img L'asset pour cet avatar
	 */
	public Avatar(int id, Paintable img, boolean dup) {
		this.id = id;
		this.position = new Vec2(0.0f, 0.0f);
		this.scale = new Vec2(2.0f, 2.0f);
		this.image = AssetServer.load(img);
		if (dup) {
			this.duplicates = new Avatar[8];
		}
	}

	/**
	 * Mets à jour le temps écoulé pour savoir si on doit passer à l'image suivante
	 * de l'animation ou non.
	 * 
	 * @param elapsed Le nombre de millisecondes qui se sont écoulées depuis le
	 *                dernier tick.
	 */
	public void tick(long elapsed) {
		this.image.tick(elapsed);
	}

	/**
	 * Dessine l'image sur l'écran.
	 * 
	 * @param g         La toile sur laquelle on dessine
	 * @param cameraPos La position de la caméra dans le monde
	 */
	public void paint(Graphics g, Vec2 cameraPos) {
		Vec2 screenCoords;
		if (!this.fixed) {
			screenCoords = this.position.add(this.offset).globalToScreen(cameraPos);
		} else {
			screenCoords = this.position.add(this.offset);
		}
		this.image.paint(g, screenCoords, scale);
	}

	public void setPaintablePath(String path) {
		this.image = this.image.duplicateFromPath(path);
	}

	public void setPaintable(Paintable p) {
		this.image = AssetServer.load(p);
		if (this.image instanceof AnimatedImage) {
			AnimatedImage anim = (AnimatedImage) this.image;
			if (anim.isFinished())
				anim.restart();
		}
	}

	public Paintable getPaintable() {
		return this.image;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Avatar && this.id == ((Avatar) other).id;
	}

	public Vec2 getOffset() {
		return this.offset;
	}

	public void setOffset(Vec2 offset) {
		this.offset = offset;
	}
}
