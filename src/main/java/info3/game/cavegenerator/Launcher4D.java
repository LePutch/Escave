package info3.game.cavegenerator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import info3.game.Vec2;
import info3.game.torus.IntTorus;

public class Launcher4D {
	private static final int nbPlayers = 2;
	static int width;
	static int height;

	public static void coloration(int[][] values, int a, int color, BufferedImage image) {

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (values[x][y] == a) {
					image.setRGB(x, y, color);
				}
			}
		}
	}

	public static void colorationBlocs(List<Vec2> coords, BufferedImage image) {
		for (Vec2 coordsBloc : coords) {
			int x = (int) coordsBloc.getX();
			int y = (int) coordsBloc.getY();
			image.setRGB(x, y, 0xFFA500);
		}
	}

	public static void colorationStatue(List<Vec2> coords, BufferedImage image) {
		for (Vec2 coordsBloc : coords) {
			int x = (int) coordsBloc.getX();
			int y = (int) coordsBloc.getY();
			image.setRGB(x, y, 0xFFFF00);
		}
	}

	public static void main(String[] args) throws IOException {
		SpawnGenerator4D generationMap = new SpawnGenerator4D();
		int[][] values1 = generationMap.spawnStatueTotal(nbPlayers);
		List<Vec2> blocs = generationMap.listSpawnBlocsStatues;
		List<Vec2> statues = generationMap.listSpawnStatues;

		IntTorus torus = DecorationGenerator.decorate(values1);
		int[][] values = torus.toArray();

		/* Test des fillons */
		// Fillon fillonMap = new Fillon();
		// int[][] values = fillonMap.noiseFillonGen(nbPlayers);
		// int[] mineraux = { 100, 101, 102, 103, 104, 105 };
		width = values.length;
		height = values[0].length;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		/*
		 * for (int x = 0; x < width; x++) { for (int y = 0; y < height; y++) {
		 * image.setRGB(x, y, ~(values[x][y] * 0xFFFFFF)); } }
		 */

		coloration(values, -3, 0xFF0000, image);
		coloration(values, -4, 0x00FF00, image);
		coloration(values, -5, 0x0000FF, image);
		coloration(values, -6, 0x00FFFF, image);
		colorationBlocs(blocs, image);
		coloration(values, -7, 0xE389B9, image);
		colorationStatue(statues, image);

		// Coloration blocs speciaux
		coloration(values, 22, 0xC52929, image);
		coloration(values, 2, 0X29C2C5, image);
		coloration(values, 3, 0xC57729, image);
		coloration(values, 4, 0x2963C5, image);
		coloration(values, 5, 0xC5B329, image);
		coloration(values, 6, 0x7029C5, image);
		coloration(values, 7, 0x71C529, image);
		coloration(values, 8, 0xC029C5, image);
		coloration(values, 9, 0x689581, image);
		coloration(values, 10, 0x687490, image);
		coloration(values, 11, 0x684690, image);
		coloration(values, 12, 0x684631, image);
		coloration(values, 13, 0x682716, image);
		coloration(values, 14, 0x6899CD, image);
		coloration(values, 15, 0x689976, image);
		coloration(values, 16, 0x999449, image);
		coloration(values, 17, 0x995DA0, image);
		coloration(values, 18, 0x0ABA1D, image);
		coloration(values, 19, 0xBA4B4E, image);
		coloration(values, 20, 0xEDA376, image);
		coloration(values, 21, 0xE2806A, image);
		coloration(values, 23, 0x99d4a0, image);
		coloration(values, 26, 0xd4d399, image);
		coloration(values, 27, 0x71d499, image);
		coloration(values, 28, 0x715d99, image);
		coloration(values, 29, 0x3e5771, image);
		coloration(values, 100, 0xf7e4aa, image);
		coloration(values, 101, 0x174f00, image);
		coloration(values, 102, 0x3e414f, image);
		coloration(values, 103, 0xee00f7, image);
		coloration(values, 104, 0x7410ff, image);
		coloration(values, 105, 0x3e5771, image);
		coloration(values, 0, 0x000000, image);
		coloration(values, 1, 0xffffff, image);

		ImageIO.write(image, "png", new File("yaha.png"));
	}

}
