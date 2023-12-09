package info3.game;

import java.util.ArrayList;

import info3.game.entities.Block;
import info3.game.entities.Mushroom;
import info3.game.entities.Player;
import info3.game.physics.RigidBody;

/*
 * 2 3 22 
 */

public class MobSpawner {
	private static int maxEntity;
	public static float spawnProba;
	private static ArrayList<Player> players;

	public final static int MIN_SPAWN_DISTANCE = 12;
	public final static int MAX_SPAWN_DISTANCE = 16;

	public static void init(int maxEntityPerPlayer, float probaSpawnPerPlayer) {
		maxEntity = maxEntityPerPlayer * Model.getPlayers().size();
		spawnProba = probaSpawnPerPlayer * Model.getPlayers().size();
	}

	public static void tick() {
		players = new ArrayList<>(Model.getPlayers());
		int minD = MIN_SPAWN_DISTANCE;
		int maxD = MAX_SPAWN_DISTANCE;
		if (minD >= maxD) {
			minD = MAX_SPAWN_DISTANCE;
			maxD = MIN_SPAWN_DISTANCE;
		}

		int mobCount = 0;
		for (RigidBody rb : Model.getEntities()) {
			if (rb instanceof Mushroom) {
				mobCount++;
			}
		}

		if (mobCount >= maxEntity) {
			return;
		}

		for (Player player : players) {
			Block[][] spawnable = getSpawnableBlocks(player.getPosition().add(new Vec2(0, -Block.SIZE)), minD, maxD);
			randomSpawn(spawnable);
		}
	}

	private static void randomSpawn(Block[][] blocks) {
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[0].length; j++) {
				Block bl = blocks[i][j];
				if (bl == null)
					continue;
				if (isInPlayersRange(bl))
					blocks[i][j] = null;
				else {
					int rand = (int) Math.floor((float) Math.random() + spawnProba);
					if (rand != 0) {
						spawn(blocks[i][j]);
						return;
					}
				}
			}
		}
	}

	private static void spawn(Block block) {
		Mushroom mob = new Mushroom(Model.controller, block.getPosition(), 1);
		Model.spawn(mob);
	}

	private static Block[][] getSpawnableBlocks(Vec2 center, int rangeMin, int rangeMax) {
		Vec2 coords = new Vec2(center).divide(Block.SIZE).round();
		Block[][] mapZone = Model.getMapZone((int) coords.getX() - rangeMax, (int) coords.getY() - rangeMax,
				2 * rangeMax, 2 * rangeMax);
		Block[][] blocks = mapZone.clone();

		for (int i = rangeMin; i < mapZone.length - rangeMin; i++) {
			for (int j = rangeMin; j < mapZone[0].length - rangeMin; j++) {
				blocks[i][j] = null;
			}
		}

		for (int i = 0; i < mapZone.length; i++) {
			for (int j = 0; j < mapZone[0].length; j++) {
				if (mapZone[i][j] == null)
					continue;
				if (j < 2) {
					blocks[i][j] = null;
					continue;
				}
				if (j >= mapZone.length - 2) {
					blocks[i][j] = null;
					continue;
				}
				if (mapZone[i][j].id == 0)
					blocks[i][j] = null;
				if (!(mapZone[i][j + 1] == null && mapZone[i][j + 2] == null)) {
					blocks[i][j] = null;
				}
			}
		}
		return blocks;
	}

	private static boolean isInPlayersRange(Block block) {
		if (block == null)
			return true;
		for (Player player : MobSpawner.players) {
			if (block.getPosition().sub(player.getPosition()).length() <= MIN_SPAWN_DISTANCE * Block.SIZE) {
				return true;
			}
		}
		return false;
	}
}
