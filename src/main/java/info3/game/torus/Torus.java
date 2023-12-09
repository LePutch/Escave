package info3.game.torus;

/**
 * Une classe permettant d'accéder à une matrice d'entier comme si c'était un
 * tore.
 * 
 * Si on demande un indice hors de la matrice, on reboucle de l'autre côté.
 * 
 * Cette classe sert à manipuler la carte de notre monde qui est torique, chaque
 * entier correspondant à un type de bloc différent.
 */
public abstract class Torus<T> {
	public int width;
	public int height;

	public Torus(int w, int h) {
		this.width = w;
		this.height = h;
	}

	public T get(int x, int y) {
		return this.getNoWrap(this.wrap(x, this.width), this.wrap(y, this.height));
	}

	protected abstract T getNoWrap(int x, int y);

	public void set(int x, int y, T value) {
		this.setNoWrap(this.wrap(x, this.width), this.wrap(y, this.height), value);
	}

	protected abstract void setNoWrap(int x, int y, T value);

	private int wrap(int x, int max) {
		x = x % max;
		if (x < 0) {
			return max + x;
		} else {
			return x;
		}
	}
}
