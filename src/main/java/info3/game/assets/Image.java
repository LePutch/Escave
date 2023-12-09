package info3.game.assets;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import info3.game.Vec2;

public class Image extends Paintable {
	private static final long serialVersionUID = -1054334314973696343L;
	transient BufferedImage image;

	public Image(String path) {
		super(path);
	}

	@Override
	public void tick(long elapsed) {
	}

	@Override
	public void paint(Graphics g, Vec2 screenCoords, Vec2 scale) {
		if (this.image != null) {
			g.drawImage(this.image, (int) screenCoords.getX(), (int) screenCoords.getY(),
					((int) scale.getX()) * this.image.getWidth(), ((int) scale.getY()) * this.image.getHeight(), null);
		}
	}

	@Override
	public void load() {
		if (this.loaded) {
			return;
		}
		File imageFile = new File(AssetServer.baseDir + this.getPath());
		if (imageFile.exists()) {
			try {
				this.image = ImageIO.read(imageFile);
				this.loaded = true;
			} catch (IOException e) {
				System.out.println("[WARN] Couldn't read file: " + this.path);
				return;
			}
		} else {
			System.out.println("[WARN] Image file not found: " + this.path);
		}
	}

	@Override
	public Paintable duplicateFromPath(String path) {
		Image img = new Image(path);
		if (this.loaded) {
			img.load();
		}
		return img;
	}
}
