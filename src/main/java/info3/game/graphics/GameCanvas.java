/*
 * Copyright (C) 2020  Pr. Olivier Gruber
 * Educational software for a basic game development
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Created on: March, 2020
 *      Author: Pr. Olivier Gruber
 */
package info3.game.graphics;
/*
 * Copyright (C) 2020  Pr. Olivier Gruber
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.Timer;

import info3.game.sound.AudioPlayer;
import info3.game.sound.AudioPlayerListener;

/**
 * A game-oriented canvas. It creates the JFrame in which it will added so that
 * it is aware of when the corresponding window is opened or closed.
 * 
 * We extended the AWT canvas to have an efficient double buffering. You can
 * read https://en.wikipedia.org/wiki/Multiple_buffering as an introduction.
 * 
 * We also manage an audio player for audio streams in the OGG format. The
 * player is automatically stopped when the window is closed.
 * 
 * @see info3.game.CanvasListener.GameCanvasListener to know how to get at the
 *      mouse and keyboard events, the timer events, and the paint requests.
 * 
 *      NOTA BENE: this is not a class that you are supposed to change. you may
 *      only want to change the FPS rate (frames per second). and the tick
 *      period.
 */
public class GameCanvas extends Canvas {

	static final int TICK_PERIOD = 1; // tick every milli-second.
	/*
	 * We want to target 24 frame per seconds (fps), which is the following period
	 * in milliseconds period = (1000.0 / 24.0)
	 */

	static final double FPS = 60.0;
	static final int REPAINT_DELAY = (int) (1000.0 / FPS);

	public GameCanvas(GameCanvasListener l) {

		/*
		 * This is ugly, but this is the only way to turn off the drawing of the Canvas
		 * background before the update/repaint methods are called, causing an annoying
		 * flickering.
		 */
		System.setProperty("sun.awt.noerasebackground", "true");

		this.listener = l;
		setBackground(Color.gray);
		addKeyListener(l);
		addMouseListener(l);
		addMouseMotionListener(l);
		addMouseWheelListener(l);
		setFocusable(true);
		requestFocusInWindow();

	}

	/**
	 * Gives the current width of the canvas, in pixels. The horizontal X-axis grows
	 * from left to right.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gives the current height of the canvas, in pixels. The vertical Y-axis grows
	 * downward.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Play the given audio stream for the specified duration, stopping any
	 * previously playing music (not playing sounds). A duration of 0 requests to
	 * play the audio stream up to the end. The end of play of an audio stream is
	 * always notified to the canvas listener.
	 * 
	 * @param the       name must be unique. if the name is already playing, the
	 *                  playing is stopped and restarted.
	 * 
	 * @param duration, in milliseconds. 0 means until the end of the audio stream.
	 * @param volume,   as a percentage (.10 for 10%, .80 for 80%)
	 */
	public void playMusic(final InputStream is, long duration, float vol) {
		if (!is.markSupported())
			throw new IllegalArgumentException("Input stream must support mark/reset");
		_playMusic(is, duration, vol);
	}

	public void stopMusic(String name) {
		_stopMusic(name);
	}

	/**
	 * Play the given audio stream for the specified duration as a sound over the
	 * background music. A duration of 0 requests to play the audio stream up to the
	 * end. The end of play of an audio stream is always notified to the canvas
	 * listener.
	 * 
	 * @param the       name must be unique. if the name is already playing, the
	 *                  playing is stopped and restarted.
	 * 
	 * @param duration, in milliseconds. 0 means until the end of the audio stream.
	 * @param volume,   as a percentage (.10 for 10%, .80 for 80%)
	 */
	public void playSound(String name, final InputStream is, long duration, float vol) {
		_playSound(name, is, duration, vol);
	}

	/**
	 * This method sets the unique timer to the given delay. Until the timer
	 * expires, you may not set another timer, but you may cancel the current one
	 * and set a new one.
	 * 
	 * @param delay
	 * @param listener
	 * @throws IllegalStateException if you try to set a second timer.
	 */
	public void setTimer(int delay) {
		_setTimer(delay);
	}

	/**
	 * Cancel the previously-set timer, calling its cancelled You may set a new
	 * timer after calling this method. It is safe to call this method even if there
	 * is no timer currently set.
	 */
	public void cancelTimer() {
		_cancelTimer();
	}

	/*
	 * ================================================================ None of the
	 * methods below are to be called by your game. None of the fields are of
	 * interest. ================================================================
	 */

	private static final long serialVersionUID = 1L;

	private Image buffer1, buffer2;
	private Image renderBuffer;
	private Image drawBuffer;
	private int width, height;
	private boolean swap;
	private GameCanvasListener listener;

	private void initDoubleBuffering(int width, int height) {

		if (this.width != width || this.height != height) {
			this.width = width;
			this.height = height;
			buffer1 = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
			buffer2 = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);

			Graphics gc = buffer1.getGraphics();
			gc.setColor(getBackground());
			gc.fillRect(0, 0, width, height);
			gc = buffer2.getGraphics();
			gc.setColor(getBackground());
			gc.fillRect(0, 0, width, height);
			renderBuffer = buffer2;
			drawBuffer = buffer1;
		}
	}

	private void swap() {
		if (renderBuffer == buffer1) {
			renderBuffer = buffer2;
			drawBuffer = buffer1;
		} else {
			renderBuffer = buffer1;
			drawBuffer = buffer2;
		}
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		if (width > 0 && height > 0) {
			initDoubleBuffering(width, height);
			if (timer == null)
				createTimer();
		}
	}

	@Override
	public final void paint(Graphics g) {
		if (swap) {
			swap();
			swap = false;
		}
		g.drawImage(renderBuffer, 0, 0, this);
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public final void update(Graphics g) {
		paint(g);
	}

	/*
	 * Let's create a timer, it is the heart of the simulation, ticking periodically
	 * so that we can simulate the passing of time.
	 */
	Timer timer;
	long elapsed;
	long lastRepaint;
	long lastTick;
	long lastSec;
	int nTicks;
	int fps;
	int npaints;

	float tickPeriod;

	public int getFPS() {
		return fps;
	}

	public float getTickPeriod() {
		return tickPeriod;
	}

	private void createTimer() {
		int tick = TICK_PERIOD;
		lastTick = System.currentTimeMillis();
		timer = new Timer(tick, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				tick();
			}
		});
		timer.start();
	}

	/*
	 * This is the period tick callback. We compute the elapsed time since the last
	 * tick. We inform the model of the current time.
	 */
	private void tick() {
		long now = System.currentTimeMillis();
		long elapsed = (now - lastTick);
		this.elapsed += elapsed;
		lastTick = now;
		nTicks++;

		// compute the number of frame paints
		// during the last second
		if (now - lastSec > 1000L) {
			fps = npaints;
			lastSec = now;
			npaints = 0;
		}

		if (listener != null)
			try {
				listener.tick(elapsed);
			} catch (Throwable th) {
				th.printStackTrace(System.err);
			}
		elapsed = now - lastRepaint;
		if (elapsed > REPAINT_DELAY) {
			tickPeriod = (float) this.elapsed / (float) nTicks;
			tickPeriod = ((int) (tickPeriod * 10.0F)) / 10.0F;
			this.elapsed = 0;
			nTicks = 0;
			lastRepaint = now;

			// repainting off-screen and asking for a swap
			if (drawBuffer != null) {
				Graphics g = drawBuffer.getGraphics();
				listener.paint(g);
				swap = true;
				npaints++;
				repaint();
			}
		}
	}

	public class RunnableEvent extends AWTEvent implements Runnable {
		private static final long serialVersionUID = 1L;
		public static final int EVENT_ID = AWTEvent.RESERVED_ID_MAX + 1;
		Runnable runnable;

		RunnableEvent(Object target, Runnable runnable) {
			super(target, EVENT_ID);
			this.runnable = runnable;
		}

		public void run() {
			runnable.run();
		}
	}

	public void post(Runnable r) {
		eventQueue.postEvent(new RunnableEvent(this, r));
	}

	/*
	 * Over load processEvent which is inherited from class java.awt.Window Our
	 * defined events will be handled here.
	 */
	protected void processEvent(AWTEvent event) {
		if (event instanceof RunnableEvent) {
			RunnableEvent tr = (RunnableEvent) event;
			tr.run();
		} else
			super.processEvent(event);
	}

	WindowFrame frame;
	java.awt.EventQueue eventQueue;

	class WindowFrame extends JFrame {
		private static final long serialVersionUID = 1L;

		WindowFrame() {
			enableEvents(RunnableEvent.EVENT_ID);
		}

		// DO NOT OVERRIDE THOSE, IT CUTS THE REPAINT
		// OF THE SWING/AWT COMPONENTS AND CONTAINERS AROUND THE CANVAS
//    @Override
//    public final void update(Graphics g) {
//    }

//    @Override
//    public final void paint(Graphics g) {
//    }
	}

	public JFrame createFrame(Dimension d) {

		java.awt.Toolkit tk = Toolkit.getDefaultToolkit();
		eventQueue = tk.getSystemEventQueue();

		frame = new WindowFrame();

		frame.setSize(d);
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);

		frame.addComponentListener(this.listener);
		frame.addWindowListener(new WindowListener());
		return frame;
	}

	private class WindowListener implements java.awt.event.WindowListener {

		WindowListener() {
		}

		@Override
		public void windowOpened(WindowEvent e) {
			listener.windowOpened();
		}

		@Override
		public void windowClosing(WindowEvent e) {
			GameCanvasListener l = listener;
			// cut any further invocation of the listener
			// (in particular, cut any further ticks),
			// but do the last call to exit()
			listener = null;
			_stopPlayers();
			try {
				if (l != null)
					l.exit();
			} catch (Throwable th) {
			}
			System.exit(0);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			System.exit(0);
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}
	}

	AudioPlayerListener apl = new AudioPlayerListener() {
		@Override
		public void endOfPlay(AudioPlayer player, final String name) {
			post(new Runnable() {
				@Override
				public void run() {
					// System.out.println("End of play for " + name);
					if (listener != null)
						listener.endOfPlay(name);
				}
			});
		}
	};

	float musicVol;
	InputStream musicInputStream;
	AudioPlayer musicPlayer;

	private void _playMusic(final InputStream is, long duration, float vol) {
		if (musicPlayer != null)
			musicPlayer.stop();
		musicPlayer = new OggPlayer(this);
		musicVol = vol;
		musicInputStream = is;
		musicInputStream.mark(Integer.MAX_VALUE);
		musicPlayer.playMusic("music", is, duration, vol);
		return;
	}

	private void _stopMusic(String name) {
		if (musicPlayer != null) {
			musicPlayer.stop();
			musicPlayer = null;
		}
	}

	AudioPlayer players[] = new AudioPlayer[10];
	int nplayers;

	public void stopped(AudioPlayer player) {
		for (int i = 0; i < nplayers; i++) {
			if (player == players[i]) {
				players[i].stop();
				for (i++; i < nplayers; i++)
					players[i - 1] = players[i];
				players[i - 1] = null;
				nplayers--;
			}
		}
	}

	private void _playSound(String name, final InputStream is, long duration, float vol) {
		AudioPlayer player;
		if (nplayers >= players.length) {
			player = players[0];
			player.stop();
			for (int i = 1; i < nplayers; i++)
				players[i - 1] = players[i];
			players[nplayers - 1] = null;
			nplayers--;
		}
		player = new OggPlayer(this);
		players[nplayers++] = player;
		player.playSound(name, is, duration, vol, apl);
		return;
	}

	private void _stopPlayers() {
		if (musicPlayer != null) {
			musicPlayer.stop();
			musicPlayer = null;
		}
		for (int i = 0; i < nplayers; i++) {
			players[i].stop();
			players[i] = null;
		}
		nplayers = 0;
	}

	Timer delayTimer;

	private void _setTimer(int delay) {
		if (delayTimer != null)
			throw new IllegalStateException("You may not set another timer, the current timer has not expired.");
		delayTimer = new Timer(delay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean canceled;
				synchronized (this) {
					if (delayTimer != null) {
						delayTimer.stop();
						delayTimer = null;
						canceled = false;
					} else
						canceled = true;
				}
				if (!canceled) {
					if (listener != null)
						listener.expired();
				}
			}
		});
		delayTimer.setRepeats(false);
		delayTimer.start();
	}

	private void _cancelTimer() {
		synchronized (this) {
			if (delayTimer != null) {
				delayTimer.stop();
				delayTimer = null;
			}
		}
	}

}
