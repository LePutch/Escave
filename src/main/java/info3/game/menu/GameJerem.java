package info3.game.menu;

import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class GameJerem implements Runnable {

	private GamePanel gamePanel;
	public Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;

	private Playing playing;
	private Menu menu;
	private Options options;
	GameWindow gamewindow;

	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 2f;
	public final static int TILES_IN_WIDTH = 64;
	public final static int TILES_IN_HEIGHT = 64;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

	public final static int GAME_WIDTH = device.getDisplayMode().getWidth();
	public final static int GAME_HEIGHT = device.getDisplayMode().getHeight();

	public GameJerem() {
		initClasses();
		gamePanel = new GamePanel(this);

		gamewindow = new GameWindow(gamePanel);
		device.setFullScreenWindow(gamewindow);
		gamePanel.requestFocus();

		startGameLoop();

	}

	public int getGameWidth() {
		return GAME_WIDTH;
	}

	public GameWindow getMainFrame() {
		return gamewindow;
	}

	public int getGameHeight() {
		return GAME_HEIGHT;
	}

	private void initClasses() {
		menu = new Menu(this);
		playing = new Playing(this);
		options = new Options(this);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		switch (Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case OPTIONS:
			options.update();
			break;
		case QUIT:
		default:
			System.exit(0);
			break;

		}
	}

	public void render(Graphics g) {
		switch (Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		case OPTIONS:
			options.draw(g);
			break;
		default:
			break;
		}
	}

	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;

			}
		}

	}

	public void windowFocusLost() {
	}

	public Menu getMenu() {
		return menu;
	}

	public Options getOptions() {
		return options;
	}

	public Playing getPlaying() {
		return playing;
	}

}
