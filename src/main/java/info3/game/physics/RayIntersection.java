package info3.game.physics;

import info3.game.entities.Block;

public class RayIntersection {
	public float t;
	public float u;
	public Block intersectedBlock;
	public Line line;
	public Ray ray;

	public RayIntersection(Block bl, Line line, Ray ray, float t, float u) {
		this.intersectedBlock = bl;
		this.line = line;
		this.ray = ray;
		this.t = t;
		this.u = u;
	}
}
