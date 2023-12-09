package info3.game.torus;

public class IntTorus extends Torus<Integer> {
	int[] array;

	public IntTorus(int w, int h) {
		super(w, h);
		this.array = new int[w * h];
	}

	public IntTorus(int[][] array) {
		super(array.length, array[0].length);
		this.array = new int[this.width * this.height];
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				this.array[y * this.height + x] = array[x][y];
			}
		}
	}

	public int[][] toArray() {
		int[][] arr = new int[this.width][this.height];
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				arr[x][y] = (int) this.array[y * this.height + x];
			}
		}
		return arr;
	}

	@Override
	public Integer getNoWrap(int x, int y) {
		return this.array[y * this.height + x];
	}

	@Override
	protected void setNoWrap(int x, int y, Integer value) {
		this.array[y * this.height + x] = value;
	}
}