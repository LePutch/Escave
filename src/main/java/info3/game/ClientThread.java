package info3.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import info3.game.network.JoinGame;
import info3.game.network.KeyPress;
import info3.game.network.KeyRelease;
import info3.game.network.MouseClick;
import info3.game.network.MultiMessage;
import info3.game.network.NetworkMessage;
import info3.game.network.WheelScroll;
import info3.game.network.WindowResize;

public class ClientThread extends Thread {
	Socket sock;
	ObjectInputStream inputStream;
	ObjectOutputStream outputStream;
	Controller controller;
	ArrayList<NetworkMessage> messageQueue;
	RemoteView view;

	ClientThread(Socket client, Controller c) {
		this.sock = client;
		try {
			this.inputStream = new ObjectInputStream(this.sock.getInputStream());
			this.outputStream = new ObjectOutputStream(this.sock.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.controller = c;
		this.messageQueue = new ArrayList<NetworkMessage>();
		this.view = new RemoteView(this);
	}

	@Override
	public void run() {
		while (true) {
			Object msg;
			try {
				msg = this.inputStream.readObject();
				if (msg instanceof JoinGame) {
					JoinGame jg = (JoinGame) msg;
					this.view.options = jg.options;
					this.view.setDimensions(jg.screenSize);
					this.controller.addView(this.view);
				} else if (msg instanceof KeyPress) {
					KeyPress k = (KeyPress) msg;
					this.controller.keyPressed(this.view.getPlayer(), k);
				} else if (msg instanceof KeyRelease) {
					KeyRelease kr = (KeyRelease) msg;
					this.controller.keyReleased(this.view.getPlayer(), kr);
				} else if (msg instanceof WheelScroll) {
					WheelScroll w = (WheelScroll) msg;
					this.controller.mouseScroll(this.view.getPlayer(), w);
				} else if (msg instanceof MouseClick) {
					MouseClick mc = (MouseClick) msg;
					this.controller.mouseClick(this.view.getPlayer(), mc);
				} else if (msg instanceof WindowResize) {
					// WindowResize wr = (WindowResize) msg;
					// this.controller.windowResize(this.view.getPlayer(), wr.size);
				}
			} catch (ClassNotFoundException | IOException e) {
				this.disconnect();
				break;
			}
		}
	}

	public void send(NetworkMessage msg) {
		synchronized (this.messageQueue) {
			this.messageQueue.add(msg);
		}
	}

	protected void actualSend() {
		NetworkMessage msg;
		synchronized (this.messageQueue) {
			int count = this.messageQueue.size();
			if (count == 0) {
				return;
			} else if (count == 1) {
				msg = this.messageQueue.get(0);
			} else {
				msg = new MultiMessage(this.messageQueue);
			}
			this.messageQueue.clear();
		}
		try {
			this.outputStream.writeObject(msg);
			this.outputStream.reset();
			this.setName("Sender: " + getPlayerName());
		} catch (IOException e) {
			this.disconnect();
		}
	}

	private void disconnect() {
		System.out.println(String.format("[WARN] Client %s was disconnected", this.getPlayerName()));
		this.controller.removeView(this.view);
	}

	private String getPlayerName() {
		if (this.view == null || this.view.getPlayer() == null) {
			return "?";
		}
		return this.view.getPlayer().name();
	}
}