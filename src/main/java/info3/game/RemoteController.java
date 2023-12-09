package info3.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UTFDataFormatException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;

import info3.game.entities.PlayerColor;
import info3.game.network.Close;
import info3.game.network.CreateAvatar;
import info3.game.network.DeleteAvatar;
import info3.game.network.JoinGame;
import info3.game.network.KeyPress;
import info3.game.network.KeyRelease;
import info3.game.network.MouseClick;
import info3.game.network.MultiMessage;
import info3.game.network.NetworkMessage;
import info3.game.network.PlaySound;
import info3.game.network.SyncCamera;
import info3.game.network.UpdateAvatar;
import info3.game.network.Welcome;
import info3.game.network.WheelScroll;
import info3.game.network.WindowResize;

public class RemoteController extends Controller {
	Socket sock;
	String ip;
	int port;
	NetworkSenderThread networkSender;
	NetworkReceiverThread networkReceiver;
	protected LocalView view;

	public RemoteController(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;

		this.networkSender = new NetworkSenderThread();
		this.networkReceiver = new NetworkReceiverThread(this);
	}

	@Override
	public void keyPressed(PlayerColor p, KeyPress e) {
		this.networkSender.send(p, e);
	}

	@Override
	public void keyReleased(PlayerColor p, KeyRelease e) {
		this.networkSender.send(p, e);
	}

	@Override
	public void tick(long elapsed) {
		// Le serveur tick de lui-même, pas besoin de lui signaler qu'il faut ticker
		// cependant pour que les animations aillent à la bonne vitesse, il faut les
		// faire tick deux fois (dont une dans le controlleur)
		synchronized (this.view.avatars) {
			for (Avatar a : this.view.avatars.values()) {
				a.tick(elapsed);
			}
		}
	}

	@Override
	public void addView(View v) {
		if (v instanceof LocalView && this.view == null) {
			try {
				this.sock = new Socket(ip, port);
				this.networkReceiver.setSocket(this.sock);
				this.networkSender.setSocket(this.sock);
				this.networkReceiver.start();
				this.networkSender.start();

				this.view = (LocalView) v;
				System.out.println("send joingame");
				this.networkSender.send(null, new JoinGame(v.getDimensions(), v.options));
			} catch (ConnectException ce) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.addView(v);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Avatar createAvatar(Avatar av) {
		int id = Controller.avatarID.incrementAndGet();
		av.id = id;
		this.view.createAvatar(av);
		return av;
	}

	@Override
	public Avatar createAvatarFor(Avatar av, PlayerColor p) {
		return this.createAvatar(av);
	}

	@Override
	protected void removeView(RemoteView view) {
		// Ne doit jamais être appelé normalement
	}

	@Override
	protected void mouseScroll(PlayerColor p, WheelScroll wheelScroll) {
		this.networkSender.send(p, wheelScroll);
	}

	@Override
	protected void mouseClick(PlayerColor player, MouseClick mouseClick) {
		this.networkSender.send(player, mouseClick);
	}

	@Override
	public void deleteAvatar(int avatarId) {
		this.view.deleteAvatar(avatarId);
	}

	@Override
	protected void updateAvatar(int i, Vec2 pos) {
		this.view.updateAvatar(i, pos);
	}

	@Override
	public void windowResize(PlayerColor p, Vec2 size) {
		this.networkSender.send(p, new WindowResize(size));
	}
}

class NetworkSenderThread extends Thread {
	Socket socket;
	ObjectOutputStream stream;
	ArrayBlockingQueue<NetworkMessage> queue;

	public NetworkSenderThread() {
		this.queue = new ArrayBlockingQueue<NetworkMessage>(10);
		this.setName("Sender");
	}

	public void setSocket(Socket sock) throws UnknownHostException, IOException {
		this.socket = sock;
		this.stream = new ObjectOutputStream(this.socket.getOutputStream());
	}

	public void send(PlayerColor p, NetworkMessage msg) {
		try {
			msg.player = p;
			this.queue.put(msg);
		} catch (InterruptedException e) {
			// TODO: do something with it?
			System.out.println("[ERROR] NetworkThread.send");
		}
	}

	@Override
	public void run() {
		System.out.println("sender started");
		while (true) {
			try {
				NetworkMessage msg = this.queue.take();
				stream.writeObject(msg);
			} catch (Exception e) {
				try {
					this.socket.close();
				} catch (IOException ioex) {
					ioex.printStackTrace();
				}
			}
		}
	}
}

class NetworkReceiverThread extends Thread {
	Socket socket;
	ObjectInputStream stream;
	RemoteController controller;

	public NetworkReceiverThread(RemoteController c) {
		this.controller = c;
		this.setName("Receiver");
	}

	public void setSocket(Socket sock) {
		this.socket = sock;
	}

	@Override
	public void run() {
		try {
			System.out.println("receiver started");
			this.stream = new ObjectInputStream(this.socket.getInputStream());
			while (true) {
				Object msg = this.stream.readObject();
				this.handleMessage(msg);
			}
		} catch (StreamCorruptedException | UTFDataFormatException ex) {
			System.out.println("[WARN] Corrupted stream");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.socket.close();
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
		}
	}

	private void handleMessage(Object msg) {
		if (msg instanceof CreateAvatar) {
			CreateAvatar ca = (CreateAvatar) msg;
			ca.avatar.image.load();
			this.controller.view.createAvatar(ca.avatar);
		} else if (msg instanceof UpdateAvatar) {
			UpdateAvatar ua = (UpdateAvatar) msg;
			try {
				this.controller.view.isPainting.acquire();
				if (ua.newPaintable != null) {
					Avatar av = new Avatar();
					av.setId(ua.avatarId);
					av.setOffset(ua.offset);
					av.position = ua.position;
					av.setPaintable(ua.newPaintable);
					this.controller.view.updateAvatar(av);
				}
				if (ua.position != null) {
					this.controller.view.updateAvatar(ua.avatarId, ua.position);
				}
				if (ua.newPath != null) {
					this.controller.view.updateAvatar(ua.avatarId, ua.newPath);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				this.controller.view.isPainting.release();
			}
		} else if (msg instanceof MultiMessage) {
			MultiMessage mm = (MultiMessage) msg;
			for (NetworkMessage m : mm.messages) {
				this.handleMessage(m);
			}
		} else if (msg instanceof SyncCamera) {
			SyncCamera sc = (SyncCamera) msg;
			System.out.println("sync cam for " + this.controller.view.getPlayer() + " " + sc.avatarId);
			this.controller.view.camera.setAvatar(this.controller.view.getAvatar(sc.avatarId));
		} else if (msg instanceof Welcome) {
			Welcome w = (Welcome) msg;
			this.controller.view.setPlayer(w.yourColor);
		} else if (msg instanceof DeleteAvatar) {
			DeleteAvatar da = (DeleteAvatar) msg;
			this.controller.view.deleteAvatar(da.id);
		} else if (msg instanceof PlaySound) {
			PlaySound ps = (PlaySound) msg;
			this.controller.view.playSound(ps.idx);
		} else if (msg instanceof Close) {
			this.controller.view.close();
		} else {
			System.out.println("[WARN] Unknown message type: " + msg.getClass().getName());
		}
	}
}