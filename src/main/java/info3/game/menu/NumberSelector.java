package info3.game.menu;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class NumberSelector {
	private BufferedImage numbers[];

	public NumberSelector(GameJerem game) {
		loadImgs();
	}

	private void loadImgs() {
		numbers = new BufferedImage[8];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.NUMBERS);
		for (int i = 0; i < numbers.length; i++)
			numbers[i] = temp.getSubimage(i * 10, 0, 10, 11);
	}

	public void draw(Graphics g) {
		g.drawImage(numbers[GameOptions.instance.playerCount - 1], GameJerem.GAME_WIDTH / 2 - 7,
				(int) (195 * GameJerem.SCALE), 20, 22, null);
	}
}
