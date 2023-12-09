package info3.game;

import info3.game.entities.PlayerColor;
import info3.game.network.Close;
import info3.game.network.CreateAvatar;
import info3.game.network.DeleteAvatar;
import info3.game.network.NetworkMessage;
import info3.game.network.PlaySound;
import info3.game.network.SyncCamera;
import info3.game.network.UpdateAvatar;
import info3.game.network.Welcome;

public class RemoteView extends View {
	private ClientThread client;

	/**
	 * Constructeur réservé aux tests
	 */
	public RemoteView() {
		super();
	}

	public RemoteView(ClientThread client) {
		super();
		this.controller = client.controller;
		this.client = client;
	}

	@Override
	public void createAvatar(Avatar av) {
		this.avatars.put(av.getId(), av);
		this.send(new CreateAvatar(av));
	}

	@Override
	public void deleteAvatar(int id) {
		super.deleteAvatar(id);
		this.send(new DeleteAvatar(id));
	}

	@Override
	public void setController(Controller c) {
		this.controller = c;
	}

	@Override
	public void updateAvatar(int id, Vec2 pos) {
		super.updateAvatar(id, pos);
		this.send(new UpdateAvatar(id, pos));
	}

	@Override
	public void updateAvatar(int id, String path) {
		super.updateAvatar(id, path);
		this.send(new UpdateAvatar(id, path));
	}

	@Override
	public void updateAvatar(Avatar av) {
		if (av != null) {
			this.send(new UpdateAvatar(av));
		}
	}

	@Override
	protected void syncCamera(Avatar syncWith) {
		this.setFollowedAvatar(syncWith);
		this.send(new SyncCamera(syncWith));
	}

	public void playSound(int idx) {
		this.send(new PlaySound(idx));
	}

	@Override
	public void close() {
		this.send(new Close());
	}

	private void send(NetworkMessage msg) {
		if (this.client != null) {
			this.client.send(msg);
		}
	}

	public void actualSend() {
		if (this.client != null) {
			this.client.actualSend();
		}
	}

	@Override
	public void setPlayer(PlayerColor p) {
		super.setPlayer(p);
		this.send(new Welcome(p));
	}
}
