package info3.game.cavegenerator;

import java.util.ArrayList;
import java.util.List;

import info3.game.Vec2;

public class SpawnGenerator4D {

	/*
	 * Cette classe permet de générer entièrement la map
	 * 
	 * On va faire dans l'ordre:
	 * 
	 * - Génération Simplex Noise - Spawn Player - Spawn Sortie - Spawn statue
	 * 
	 * 
	 * - automate cellulaire - Spawn murs
	 * 
	 */

	int width;
	int height;
	int limit;

	int nombreGeneration = 4;

	public ArrayList<Vec2> listSpawnPlayer = new ArrayList<Vec2>();
	public List<Vec2> listSpawnBlocsStatues = new ArrayList<Vec2>();
	public List<Vec2> listSpawnStatues = new ArrayList<Vec2>();

	public Vec2 exit = new Vec2(0);

	public int[][] classicGen(int nbPlayers) {
		long rand = (long) (Math.random() * System.currentTimeMillis());
		System.out.println(rand);
		SimplexNoise4D noise = new SimplexNoise4D();
		int[][] values = noise.generation(nbPlayers, 0.12);
		this.width = values.length;
		this.height = values[0].length;
		return values;
	}

	/**
	 * Prend le nombre de joueurs dans la partie et modifie le tableau de 0 et de 1
	 * pour le mettre en 2, signifiant un spawn. Un spawn d'un joueur est dans une
	 * zone différente des autres, la map et divisé par le nombre de joueur
	 * 
	 * Le spawn respecte certaines conditions (le spawn doit être éloigné des bords)
	 * 
	 * @param nombre de joueur
	 * @return le tableau de valeurs modifié avec un 2 aux coordonnées de spawn
	 */
	public int[][] spawnPlayer(int nbPlayers) {
		int[][] values = classicGen(nbPlayers);
		int depart = 0;
		boolean stop = true;
		int zoneLen = this.width / nbPlayers;
		for (int i = 0; i < nbPlayers; i++) {
			stop = true;
			while (stop) {
				int x = (int) (Math.random() * zoneLen + depart);
				int y = (int) (Math.random() * this.height);

				if (x > 15 && y < this.height - 15 && y > 15 && x < this.width - 15 && values[x][y + 1] == 1
						&& values[x - 3][y + 1] == 1 && values[x - 4][y + 1] == 1 && values[x - 2][y + 1] == 1
						&& values[x - 1][y + 1] == 1 && values[x + 1][y + 1] == 1 && values[x + 2][y + 1] == 1
						&& values[x + 3][y + 1] == 1 && values[x + 4][y + 1] == 1 && values[x - 3][y + 1] == 1) {
					stop = false;
					depart += zoneLen;
					values[x][y] = 0;
					listSpawnPlayer.add(new Vec2(x, y));
				}
			}
		}
		return values;
	}

	public int[][] zonePlayer(List<Vec2> listSpawnPlayer, int[][] values) {
		int len = listSpawnPlayer.size();
		for (int i = 0; i < len; i++) {
			Vec2 coords = listSpawnPlayer.get(i);
			values = zoneDestructionPlayer(values, coords);
		}
		return values;
	}

	public int[][] zoneDestructionPlayer(int[][] values, Vec2 coords) {
		int x = (int) coords.getX();
		int y = (int) coords.getY();
		// on creuse à droite et à gauche
		for (int i = -5; i < 6; i++) {
			values[x + i][y] = 0;
		}
		// ligne au dessus
		for (int i = -4; i < 6; i++) {
			values[x + i][y - 1] = 0;
		}
		// 2 lignes au dessus
		for (int i = -3; i < 5; i++) {
			values[x + i][y - 2] = 0;
		}
		// 3 lignes au dessus
		for (int i = -2; i < 4; i++) {
			values[x + i][y - 3] = 0;
		}
		// 4 lignes au dessus
		values[x][y - 4] = 0;
		values[x][y] = 0;
		return values;
	}

	public int[][] spawnPlayerTotal(int nbPlayers) {
		int[][] values = spawnPlayer(nbPlayers);
		values = zonePlayer(listSpawnPlayer, values);
		return values;
	}

	public int[][] spawnExit(int[][] values, int nbPlayers) {
		boolean stop = false;
		boolean find = true;
		if (nbPlayers == 1 || nbPlayers == 2)
			limit = 8;
		if (nbPlayers == 3 || nbPlayers == 4)
			limit = 30;
		if (nbPlayers == 5 || nbPlayers == 6)
			limit = 35;
		if (nbPlayers == 7 || nbPlayers == 8)
			limit = 40;

		int cpt = 0;
		int len = listSpawnPlayer.size();
		while (!stop) {
			cpt++;
			stop = true;
			find = true;
			boolean full = true;
			int x = (int) (Math.random() * (this.width - 50) + 15);
			int y = (int) (Math.random() * (this.height - 50) + 15);
			for (int i = 0; i < len; i++) {
				Vec2 coords = listSpawnPlayer.get(i);
				System.out.println("Player " + i + ": " + coords.getX() + " " + coords.getY() + " distance: "
						+ Math.abs(coords.getX() - x) + " " + Math.abs(coords.getY() - y));

				for (int j = -nbPlayers - 2; j < nbPlayers + 2; j++) {
					if (values[x + j][y + 1] == 0)
						full = false;
				}
				if (full == false || Math.abs(coords.getX() - x) < limit || Math.abs(coords.getY() - y) < limit / 2
						|| x < 15 || y > this.height - 15 || y < 15 || x > this.width - 15 || values[x - 3][y + 1] != 1
						|| values[x - 2][y + 1] != 1 || values[x - 1][y + 1] != 1 || values[x][y + 1] != 1
						|| values[x + 1][y + 1] != 1 || values[x + 2][y + 1] != 1 || values[x + 3][y + 1] != 1)
					find = false;

			}
			if (find == false) {
				stop = false;
			} else {
				values[x][y] = 0;
				exit.setX(x);
				exit.setY(y);
				System.out.println(exit.getX());
				System.out.println(exit.getY());
			}
			System.out.println("cpt " + cpt);

		}
		return values;
	}

	public int[][] zoneExit(Vec2 exit, int[][] values, int nbPlayers) {
		values = zoneDestructionExit(values, exit, nbPlayers);
		return values;
	}

	public int[][] zoneDestructionExit(int[][] values, Vec2 coords, int nbPlayers) {
		int x = (int) coords.getX();
		int y = (int) coords.getY();
		if (nbPlayers == 1 || nbPlayers == 2)
			values = zoneDestructionExit12(values, x, y);
		if (nbPlayers == 3 || nbPlayers == 4)
			values = zoneDestructionExit34(values, x, y);
		if (nbPlayers == 5 || nbPlayers == 6)
			values = zoneDestructionExit56(values, x, y);
		if (nbPlayers == 7 || nbPlayers == 8)
			values = zoneDestructionExit78(values, x, y);
		return values;
	}

	public int[][] zoneDestructionExit12(int[][] values, int x, int y) {
		for (int i = -7; i < 9; i++) {
			values[x + i][y] = 0;
		}
		for (int i = -7; i < 9; i++) {
			values[x + i][y - 1] = 0;
		}
		for (int i = -6; i < 7; i++) {
			values[x + i][y - 2] = 0;
		}
		for (int i = -5; i < 6; i++) {
			values[x + i][y - 3] = 0;
		}
		for (int i = -3; i < 4; i++) {
			values[x + i][y - 4] = 0;
		}
		values[x][y] = 0;
		return values;
	}

	public int[][] zoneDestructionExit34(int[][] values, int x, int y) {
		for (int i = -6; i < 8; i++) {
			values[x + i][y] = 0;
		}
		for (int i = -6; i < 7; i++) {
			values[x + i][y - 1] = 0;
		}
		for (int i = -5; i < 7; i++) {
			values[x + i][y - 2] = 0;
		}
		for (int i = -3; i < 4; i++) {
			values[x + i][y - 3] = 0;
		}
		for (int i = -3; i < 4; i++) {
			values[x + i][y - 4] = 0;
		}
		for (int i = 0; i < 3; i++) {
			values[x + i][y - 5] = 0;
		}
		values[x][y] = 0;
		return values;
	}

	public int[][] zoneDestructionExit56(int[][] values, int x, int y) {
		for (int i = -8; i < 9; i++) {
			values[x + i][y] = 0;
		}
		for (int i = -8; i < 9; i++) {
			values[x + i][y - 1] = 0;
		}
		for (int i = -7; i < 6; i++) {
			values[x + i][y - 2] = 0;
		}
		for (int i = -6; i < 6; i++) {
			values[x + i][y - 3] = 0;
		}
		for (int i = -5; i < 6; i++) {
			values[x + i][y - 4] = 0;
		}
		for (int i = -3; i < 4; i++) {
			values[x + i][y - 5] = 0;
		}
		for (int i = -2; i < 1; i++) {
			values[x + i][y - 6] = 0;
		}
		values[x][y] = 0;
		return values;
	}

	public int[][] zoneDestructionExit78(int[][] values, int x, int y) {
		for (int i = -10; i < 11; i++) {
			values[x + i][y] = 0;
		}
		for (int i = -10; i < 11; i++) {
			values[x + i][y - 1] = 0;
		}
		for (int i = -9; i < 10; i++) {
			values[x + i][y - 2] = 0;
		}
		for (int i = -9; i < 10; i++) {
			values[x + i][y - 3] = 0;
		}
		for (int i = -7; i < 8; i++) {
			values[x + i][y - 4] = 0;
		}
		for (int i = -4; i < 4; i++) {
			values[x + i][y - 5] = 0;
		}
		for (int i = -2; i < 2; i++) {
			values[x + i][y - 6] = 0;
		}
		values[x][y] = 0;
		return values;
	}

	private List<Vec2> spawnBlocsStatue(int nbPlayers) {

		int x = (int) exit.getX();
		int y = (int) exit.getY();
		System.out.println("Exit : " + x + " " + y);
		switch (nbPlayers) {
		case 8:
			listSpawnBlocsStatues.add(new Vec2(x - 8, y + 1));
		case 7:
			listSpawnBlocsStatues.add(new Vec2(x + 8, y + 1));
		case 6:
			listSpawnBlocsStatues.add(new Vec2(x - 6, y + 1));
		case 5:
			listSpawnBlocsStatues.add(new Vec2(x + 6, y + 1));
		case 4:
			listSpawnBlocsStatues.add(new Vec2(x - 4, y + 1));
		case 3:
			listSpawnBlocsStatues.add(new Vec2(x + 4, y + 1));
		case 2:
			listSpawnBlocsStatues.add(new Vec2(x - 2, y + 1));
		case 1:
			listSpawnBlocsStatues.add(new Vec2(x + 2, y + 1));
			break;
		}
		return listSpawnBlocsStatues;
	}

	public int[][] spawnExitTotal(int nbPlayers) {
		int[][] values = spawnPlayerTotal(nbPlayers);
		values = spawnExit(values, nbPlayers);
		values = zoneExit(this.exit, values, nbPlayers);
		spawnBlocsStatue(nbPlayers);
		return values;
	}

	private List<Vec2> spawnStatue(int nbPlayers, int[][] values) {
		int cpt = nbPlayers;
		while (cpt != 0) {
			int x = (int) (Math.random() * this.width);
			int y = (int) (Math.random() * this.height);
			Vec2 coords = new Vec2(x, y);
			if (checkBorder(coords) && checkPlacementStatue(coords) && checkZoneStatue(coords, values)) {
				cpt--;
				System.out.println("Statue : " + x + " " + y);
				listSpawnStatues.add(coords);
			}
		}
		return listSpawnStatues;
	}

	private boolean checkBorder(Vec2 coords) {
		int x = (int) coords.getX();
		int y = (int) coords.getY();
		if (x < 15 || y > this.height - 15 || y < 15 || x > this.width - 15) {
			return false;
		}
		return true;
	}

	public boolean checkPlacementStatue(Vec2 coords) {
		int x = (int) coords.getX();
		int y = (int) coords.getY();
		for (Vec2 coordsPlayer : listSpawnPlayer) {
			int xPlayer = (int) coordsPlayer.getX();
			int yPlayer = (int) coordsPlayer.getY();
			if (Math.abs(x - xPlayer) < limit && Math.abs(y - yPlayer) < limit) {
				return false;
			}
		}
		return true;
	}

	public boolean checkZoneStatue(Vec2 coords, int[][] values) {
		int x = (int) coords.getX();
		int y = (int) coords.getY();
		for (int i = x - 5; i < x + 6; i++) {
			for (int j = y - 5; j < y + 6; j++) {
				if (values[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	public int[][] destroyZoneStatue(List<Vec2> coordsStatues, int[][] values) {
		for (Vec2 coordsStatue : coordsStatues) {
			int x = (int) coordsStatue.getX();
			int y = (int) coordsStatue.getY();
			for (int i = -2; i < 3; i++) {
				for (int j = -2; j < 3; j++) {
					values[x + i][y + j] = 0;
				}
			}
			values[x - 4][y] = 0;
			values[x - 3][y] = 0;
			values[x + 4][y] = 0;
			values[x + 3][y] = 0;
			values[x][y - 4] = 0;
			values[x][y - 3] = 0;
			values[x][y + 4] = 0;
			values[x][y + 3] = 0;
			values[x - 3][y - 1] = 0;
			values[x - 3][y + 1] = 0;
			values[x + 3][y - 1] = 0;
			values[x + 3][y + 1] = 0;
			values[x - 1][y - 3] = 0;
			values[x + 1][y - 3] = 0;
			values[x - 1][y + 3] = 0;
			values[x + 1][y + 3] = 0;
			values[x][y + 1] = 1;
		}

		return values;
	}

	public int[][] spawnStatueTotal(int nbPlayers) {
		int[][] values = spawnExitTotal(nbPlayers);
		spawnStatue(nbPlayers, values);
		values = destroyZoneStatue(listSpawnStatues, values);

		return values;
	}

}
