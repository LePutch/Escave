package info3.game.assets;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import info3.game.Vec2;

public class AnimatedImage extends Paintable {

	private static final long serialVersionUID = -7309704678040085838L;
	transient BufferedImage[] frames;
	int frameCount;
	/**
	 * Indique à quel indice de l'animation on se trouve.
	 * 
	 * Reste à 0 dans le cas d'une image statique.
	 */
	int imageIndex;

	/**
	 * Indique combien de temps s'est écoulé depuis le dernier changement d'image.
	 */
	long imageElapsed = 0;

	/**
	 * Indique le temps après lequel on change d'image dans l'animation.
	 * 
	 * Si il vaut 0, on n'a pas d'animation, même si on a plusieurs images. Le
	 * contrôle se fait alors avec la méthode `nextFrame`.
	 */
	public long animationDelay;

	boolean loop;
	boolean cancellable;

	public AnimatedImage(String path, int fc, long delay, boolean loop) {
		super(path);
		this.frameCount = fc;
		this.animationDelay = delay;
		this.loop = loop;
		this.imageIndex = 0;
		this.cancellable = false;
	}

	public AnimatedImage(String path, int fc, long delay, boolean loop, boolean cancellable) {
		super(path);
		this.frameCount = fc;
		this.animationDelay = delay;
		this.loop = loop;
		this.imageIndex = 0;
		this.cancellable = cancellable;
	}

	@Override
	public void load() {
		if (this.loaded) {
			return;
		}
		Image img = AssetServer.load(new Image(this.getPath()));
		BufferedImage image = img.image;
		int width = image.getWidth() / this.frameCount;
		int height = image.getHeight();

		this.frames = new BufferedImage[this.frameCount];
		for (int i = 0; i < this.frameCount; i++) {
			int x = i * width;
			this.frames[i] = image.getSubimage(x, 0, width, height);
		}
		this.loaded = true;
	}

	@Override
	public void tick(long elapsed) {
		this.imageElapsed += elapsed;
		if (this.animationDelay > 0 && this.imageElapsed > this.animationDelay) {
			this.imageElapsed = 0;
			this.nextFrame();
		}
	}

	@Override
	public void paint(Graphics g, Vec2 screenCoords, Vec2 scale) {
		if (this.frames != null) {
			BufferedImage img = this.frames[this.imageIndex];
			if (img != null) {
				g.drawImage(img, (int) screenCoords.getX(), (int) screenCoords.getY(),
						((int) scale.getX()) * img.getWidth(), ((int) scale.getY()) * img.getHeight(), null);
			}
		}
	}

	/**
	 * Passe à l'image suivante de l'animation
	 */
	void nextFrame() {
		if (this.loop)
			this.imageIndex = (this.imageIndex + 1) % this.frameCount;
		else {
			if (this.imageIndex == this.frameCount - 1)
				return;
			this.imageIndex = this.imageIndex + 1;
		}
	}

	@Override
	public Paintable duplicateFromPath(String path) {
		AnimatedImage img = new AnimatedImage(path, this.frameCount, this.animationDelay, this.loop, this.cancellable);
		if (this.loaded) {
			img.load();
		}
		return img;
	}

	public boolean isFinished() {
		return !this.loop && this.imageIndex == this.frameCount - 1;
	}

	public boolean isLoop() {
		return this.loop;
	}

	public boolean isCancellable() {
		return this.cancellable;
	}

	public void restart() {
		this.imageIndex = 0;
	}
}
