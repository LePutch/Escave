package info3.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import info3.game.cavegenerator.BlockIDs;
import info3.game.cavegenerator.DecorationGenerator;
import info3.game.torus.IntTorus;

public class GenerationTest {
	@Test
	public void testBlocSeul() {
		int[][] map = makeMap9(0, 0, 0, 0, 1, 0, 0, 0, 0);
		IntTorus res = genMap(map);
		assertEquals(res.get(1, 1), 17);
	}

	@Test
	public void testComplexe() {
		int[][] map = makeMap25(0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0);
		IntTorus res = genMap(map);
		assertEquals(res.get(1, 1), 22);
		assertEquals(res.get(2, 1), 2);
		assertEquals(res.get(3, 1), 3);
		assertEquals(res.get(3, 2), 4);
		assertEquals(res.get(3, 3), 5);
		assertEquals(res.get(2, 3), 6);
		assertEquals(res.get(1, 3), 7);
		assertEquals(res.get(1, 2), 8);
		assertEquals(res.get(2, 2), 23);

		int[][] map2 = makeMap25(0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0);
		IntTorus res2 = DecorationGenerator.decorate(map2);
		assertEquals(res2.get(1, 1), 17);
		assertEquals(res2.get(3, 1), 20);
		assertEquals(res2.get(3, 2), 21);
		assertEquals(res2.get(1, 3), 18);
		assertEquals(res2.get(2, 3), 19);
	}

	private IntTorus genMap(int[][] map) {
		IntTorus step1 = DecorationGenerator.decorateMap(map, BlockIDs.PatternCouche1ToIDs);
		IntTorus step2 = DecorationGenerator.decorateMap(step1.toArray(), BlockIDs.PatternCouche2ToIDs);
		IntTorus step3 = DecorationGenerator.decorateMap(step2.toArray(), BlockIDs.PatternCouche3ToIDs);
		return step3;
	}

	private int[][] makeMap9(int i, int j, int k, int l, int m, int n, int o, int p, int q) {
		int[] l1 = { i, j, k };
		int[] l2 = { l, m, n };
		int[] l3 = { o, p, q };
		int[][] result = { l1, l2, l3 };
		return result;
	}

	private int[][] makeMap25(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j, int k, int l, int m,
			int n, int o, int p, int q, int r, int s, int t, int u, int v, int w, int x, int y) {
		int[] l1 = { a, b, c, d, e };
		int[] l2 = { f, g, h, i, j };
		int[] l3 = { k, l, m, n, o };
		int[] l4 = { p, q, r, s, t };
		int[] l5 = { u, v, w, x, y };
		int[][] result = { l1, l2, l3, l4, l5 };
		return result;
	}
}
