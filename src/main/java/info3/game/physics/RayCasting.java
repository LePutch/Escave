package info3.game.physics;

import java.util.ArrayList;

import info3.game.Model;
import info3.game.Vec2;
import info3.game.entities.Block;

public class RayCasting {

	public static Block quadCast(Vec2 mousePos, Vec2 playerPos, int range) {
		Vec2[] playPos = new Vec2[4];
		playPos[0] = playerPos;
		playPos[1] = new Vec2(playerPos.getX() - 1, playerPos.getY());
		playPos[2] = new Vec2(playerPos.getX() - 1, playerPos.getY() - 4);
		playPos[3] = new Vec2(playerPos.getX(), playerPos.getY() - 4);

		Block retBlock = null;
		Block temp;
		for (int i = 0; i < 4; i++) {
			temp = singleCast(mousePos, playPos[i], range);
			if (temp != null) {
				retBlock = temp;
			}
		}
		return retBlock;

	}

	/*
	 * Retourne 1er block touché par le RayCasting
	 */
	public static Block singleCast(Vec2 mousePos, Vec2 playerPos, int range) {
		Block closest = null;
		float minLength = Float.POSITIVE_INFINITY;

		Ray ray = new Ray(playerPos, playerPos.sub(mousePos).normalized());

		Vec2 coord = playerPos.divide(Block.SIZE).round();
		Block[][] mapZone = Model.getMapZone((int) coord.getX() - range, (int) coord.getY() - range, range * 2,
				range * 2);
		for (int i = 1; i < range * 2 - 1; i++) {
			for (int j = 1; j < range * 2 - 1; j++) {
				Block bl = mapZone[i][j];
				if (bl == null) {
					continue;
				}
				Vec2 blPos = bl.getPosition();

				ArrayList<Line> lines = new ArrayList<Line>();
				if (mapZone[i - 1][j] == null)
					lines.add(new Line(blPos.getX(), blPos.getY(), blPos.getX(), blPos.getY() + Block.SIZE));
				if (mapZone[i][j - 1] == null)
					lines.add(new Line(blPos.getX(), blPos.getY(), blPos.getX() + Block.SIZE, blPos.getY()));
				if (mapZone[i][j + 1] == null)
					lines.add(new Line(blPos.getX(), blPos.getY() + Block.SIZE, blPos.getX() + Block.SIZE,
							blPos.getY() + Block.SIZE));
				if (mapZone[i + 1][j] == null)
					lines.add(new Line(blPos.getX() + Block.SIZE, blPos.getY(), blPos.getX() + Block.SIZE,
							blPos.getY() + Block.SIZE));

				for (Line line : lines) {
					Vec2 intersec = ray.intersect(line);
					if (intersec != null) {
						float len = intersec.sub(playerPos).length();
						if (len < minLength) {
							minLength = len;
							closest = bl;
						}
					}
				}
			}
		}
		return closest;
	}

	public ArrayList<Ray> multiCast(float angleBetween) {
		ArrayList<Ray> rays = new ArrayList<Ray>();
		return rays;
	}

}
