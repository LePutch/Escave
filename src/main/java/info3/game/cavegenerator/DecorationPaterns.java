package info3.game.cavegenerator;

import java.util.HashMap;
import java.util.Map;

public class DecorationPaterns {

	public static int comparePatterns(int[][] otherPattern, HashMap<Integer[][], Integer> PatternCouche) {
		for (Map.Entry<Integer[][], Integer> entry : PatternCouche.entrySet()) {
			Integer[][] pattern = entry.getKey();
			if (comparePattern(otherPattern, pattern)) {
				return (entry.getValue());
			}
		}
		return otherPattern[1][1];
	}

	public static boolean comparePattern(int[][] blocs, Integer[][] pattern) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int bloc = blocs[i][j];
				int other = pattern[i][j];
				if (!compareIdsBlocks(bloc, other)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean compareIdsBlocks(int bloc, int other) {
		if (other == -2) {
			return true;
		}
		if (other == 1) {
			return bloc < 0 || bloc == 1;
		}
		return (bloc == other);
	}
}