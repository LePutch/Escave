package info3.game;

import java.util.HashMap;

import info3.game.entities.PlayerColor;
import info3.game.menu.GameOptions;

public abstract class View {
	static View view;

	/**
	 * Chaque vue affiche un joueur particulier.
	 */
	private PlayerColor player;

	protected Controller controller;

	protected HashMap<Integer, Avatar> avatars;

	protected Camera camera;

	private Vec2 dimensions;

	public GameOptions options;

	protected View() {
		this.avatars = new HashMap<Integer, Avatar>();
		this.camera = new Camera(null, this);
		View.view = this;
	}

	public abstract void createAvatar(Avatar av);

	public void deleteAvatar(int id) {
		synchronized (this.avatars) {
			Avatar av = this.avatars.remove(id);
			if (av != null && av.duplicates != null) {
				for (Avatar dup : av.duplicates) {
					if (dup != null) {
						this.controller.deleteAvatar(dup.getId());
					}
				}
			}
		}
	}

	public void updateAvatar(int id, Vec2 pos) {
		Avatar a = this.avatars.get(id);
		if (a != null) {
			synchronized (a) {
				a.setPosition(pos);
			}
		}
	}

	public void updateAvatar(int id, String path) {
		Avatar a = this.avatars.get(id);
		if (a != null) {
			synchronized (a) {
				a.setPaintablePath(path);
			}
		}
	}

	public abstract void setController(Controller c);

	protected Avatar getAvatar(int avatarId) {
		return this.avatars.get(avatarId);
	}

	public PlayerColor getPlayer() {
		return player;
	}

	public void setPlayer(PlayerColor player) {
		this.player = player;
	}

	public void setFollowedAvatar(Avatar a) {
		this.camera.setAvatar(a);
	}

	protected int getWidth() {
		// return (int) this.dimensions.getX();
		return 1920;
	}

	protected int getHeight() {
		// return (int) this.dimensions.getY();
		return 1080;
	}

	public abstract void updateAvatar(Avatar av);

	public void setDimensions(Vec2 size) {
		// this.dimensions = size;
	}

	public Vec2 getDimensions() {
		// return this.dimensions;
		return new Vec2(1920, 1080);
	}

	protected abstract void syncCamera(Avatar syncWith);

	public abstract void playSound(int idx);

	public abstract void close();
}
