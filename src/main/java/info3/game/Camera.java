package info3.game;

public class Camera {
	Avatar followedAvatar;
	View view;

	public Camera(Avatar a, View v) {
		this.followedAvatar = a;
		this.view = v;
	}

	public Vec2 getPos() {
		if (this.followedAvatar != null) {
			return this.followedAvatar.getPosition()
					.add(new Vec2(-this.view.getWidth() / 2, -this.view.getHeight() / 2));
		} else {
			return new Vec2(0);
		}
	}

	public void setAvatar(Avatar avatar) {
		this.followedAvatar = avatar;
	}
}
