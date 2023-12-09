package info3.game;

import java.util.Arrays;

import info3.game.assets.AssetServer;
import info3.game.assets.Paintable;
import info3.game.entities.Player;

public class AvatarBuilder {
	private Avatar a;

	public AvatarBuilder(Paintable p) {
		this.a = new Avatar();
		this.a.setPaintable(p);
	}

	public AvatarBuilder scale(Vec2 scale) {
		this.a.scale = scale;
		return this;
	}

	public AvatarBuilder fixed() {
		return this.fixed(true);
	}

	public AvatarBuilder fixed(boolean f) {
		this.a.fixed = true;
		return this;
	}

	public Avatar build(Controller c) {
		this.a.image = AssetServer.load(this.a.image);
		return c.createAvatar(this.a);
	}

	public AvatarBuilder position(Vec2 pos) {
		this.a.setPosition(pos);
		return this;
	}

	public AvatarBuilder duplicate(boolean dup) {
		if (dup) {
			this.a.duplicates = new Avatar[8];
			Arrays.fill(this.a.duplicates, null);
		} else {
			this.a.duplicates = null;
		}
		return this;
	}

	public AvatarBuilder layer(int i) {
		this.a.layer = i;
		return this;
	}

	public AvatarBuilder duplicate() {
		return this.duplicate(true);
	}

	public AvatarBuilder offset(Vec2 off) {
		this.a.offset = off;
		return this;
	}

	public AvatarBuilder offsetX(int xOff) {
		this.a.offset.setX(xOff);
		return this;
	}

	public AvatarBuilder offsetY(int yOff) {
		this.a.offset.setY(yOff);
		return this;
	}

	public Avatar buildFor(LocalController controller, Player player) {
		return controller.createAvatarFor(this.a, player.getColor());
	}
}
