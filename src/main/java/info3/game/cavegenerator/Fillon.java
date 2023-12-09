package info3.game.cavegenerator;

import info3.game.torus.IntTorus;

public class Fillon {
	static void generateFilons(IntTorus map, int idToChange, int[] ids) {
		for (int i = 0; i < map.width; i++) {
			for (int j = 0; j < map.height; j++) {
				for (int k = 0; k < ids.length; k++) {
					if (map.get(i, j) == ids[k]) {
						map.set(i, j, idToChange);
						filonRec(map, i, j, idToChange, ids[k], 3);
					}
				}
			}
		}
	}

	private static void filonRec(IntTorus map, int i, int j, int oldBlock, int newBlock, double proba) {
		if (map.get(i, j) != oldBlock) {
			return;
		}

		if (Math.random() >= proba) {
			return;
		}

		map.set(i, j, newBlock);
		double newProba = proba / 9;
		filonRec(map, i - 1, j, oldBlock, newBlock, newProba);
		filonRec(map, i, j - 1, oldBlock, newBlock, newProba);
		filonRec(map, i, j + 1, oldBlock, newBlock, newProba);
		filonRec(map, i + 1, j, oldBlock, newBlock, newProba);
	}
}
