package info3.game;

import java.util.concurrent.atomic.AtomicInteger;

import info3.game.entities.PlayerColor;
import info3.game.network.KeyPress;
import info3.game.network.KeyRelease;
import info3.game.network.MouseClick;
import info3.game.network.WheelScroll;

public abstract class Controller {
	static Controller controller;

	protected Controller() {
		Controller.controller = this;
	}

	public abstract void keyPressed(PlayerColor p, KeyPress e);

	public abstract void keyReleased(PlayerColor p, KeyRelease e);

	public abstract void windowResize(PlayerColor p, Vec2 size);

	public abstract void tick(long elapsed);

	public abstract void addView(View v);

	static AtomicInteger avatarID = new AtomicInteger(0);

	protected abstract void removeView(RemoteView view);

	protected abstract void mouseClick(PlayerColor player, MouseClick mouseClick);

	protected abstract void mouseScroll(PlayerColor p, WheelScroll wheelScroll);

	public abstract void deleteAvatar(int avatarId);

	protected abstract void updateAvatar(int i, Vec2 pos);

	protected abstract Avatar createAvatar(Avatar a);

	protected abstract Avatar createAvatarFor(Avatar a, PlayerColor p);
}
