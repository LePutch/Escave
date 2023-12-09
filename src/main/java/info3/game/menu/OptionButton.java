package info3.game.menu;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class OptionButton {
	public static final int B_WIDTH_DEFAULT = 140;
	public static final int B_HEIGHT_DEFAULT = 56;

	public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * 2);
	public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * 2);
	private int xPos, yPos, index;
	private int xOffsetCenter = B_WIDTH / 2;
	ButtonCallback action;
	private BufferedImage imgs[];
	private String im1, im2;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;

	public OptionButton(int xPos, int yPos, int rowIndex, ButtonCallback action, String im1, String im2) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.action = action;
		this.im1 = im1;
		this.im2 = im2;
		loadImgs();
		initBounds();
	}

	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos, (int) (imgs[0].getWidth() * GameJerem.SCALE + 1),
				(int) (imgs[0].getHeight() * GameJerem.SCALE + 1));

	}

	private void loadImgs() {
		imgs = new BufferedImage[2];
		BufferedImage image1 = LoadSave.GetSpriteAtlas(this.im1);
		BufferedImage image2 = LoadSave.GetSpriteAtlas(this.im2);
		imgs[0] = image1;
		imgs[1] = image2;
	}

	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, (int) (imgs[index].getWidth() * GameJerem.SCALE + 1),
				(int) (imgs[index].getHeight() * GameJerem.SCALE + 1), null);
	}

	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

}
