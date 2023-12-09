package info3.game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import info3.game.assets.AssetServer;

public class Server {
	public static void main(String[] args) {
		AssetServer.init(false);
		// TODO: parse parameters
		LocalController controller = new LocalController();
		Server.run(controller);
	}

	public static void run(LocalController controller) {
		try {
			ServerThread server = new ServerThread(controller);
			TickerThread ticker = new TickerThread(controller, server);
			ticker.start();
			server.start();

			server.join();
			for (ClientThread ct : server.clients) {
				ct.join();
			}
			ticker.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class TickerThread extends Thread {
	LocalController controller;
	private ServerThread serverThread;

	public TickerThread(LocalController c, ServerThread st) {
		this.controller = c;
		this.serverThread = st;
	}

	public void run() {
		long start = System.currentTimeMillis();
		while (true) {
			try {
				long end = System.currentTimeMillis();
				this.controller.tick(end - start);
				// Sync the clients
				synchronized (this.controller.views) {
					List<View> views = new ArrayList<>(this.controller.views);
					for (View view : views) {
						if (view instanceof RemoteView) {
							RemoteView rv = (RemoteView) view;
							rv.actualSend();
						}
					}
					if (Model.options != null && views.isEmpty() && Model.getPlayers().size() > 0) {
						this.serverThread.sock.close();
						return;
					}
				}
				long execTime = System.currentTimeMillis() - end;
				start = end;
				Thread.sleep(Math.max(0, 30 - execTime));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class ServerThread extends Thread {
	ServerSocket sock;
	Controller controller;
	ArrayList<ClientThread> clients;

	public ServerThread(Controller c) throws IOException {
		this.controller = c;
		this.clients = new ArrayList<>();
		this.sock = new ServerSocket(1906);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket client = this.sock.accept();
				ClientThread thread = new ClientThread(client, this.controller);
				this.clients.add(thread);
				thread.start();
			} catch (SocketException e) {
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}