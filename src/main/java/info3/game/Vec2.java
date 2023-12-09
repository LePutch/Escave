package info3.game;

import java.io.Serializable;

import info3.game.automata.Direction;
import info3.game.entities.Block;

/**
 * Un vecteur à deux éléments.
 * 
 * Peut servir à représenter une position, une vitesse, une taille, ou autre.
 * 
 * La position d'une entité correspond à son coin en haut à gauche. L'axe X va
 * vers la droite et l'axe Y vers le bas.
 * 
 * # Le double système de coordonnées
 * 
 * Pour les positions, on a un double système :
 * 
 * - des coordonnées globales qui indiquent où se trouve une entité dans
 * l'ensemble du monde
 * 
 * - des coordonnées à l'écran qui indiquent où elle se trouve par rapport à la
 * caméra
 * 
 * Des fonctions sont fournies dans cette classe pour faire la conversion entre
 * les deux.
 */
public class Vec2 extends Object implements Serializable {

	private static final long serialVersionUID = -5705242718418470298L;
	float x;
	float y;

	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2(Vec2 copy) {
		this.x = copy.x;
		this.y = copy.y;
	}

	public Vec2(float a) {
		this.x = a;
		this.y = a;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float distance(Vec2 other) {
		double x = Math.pow(this.x - other.x, 2.0);
		double y = Math.pow(this.y - other.y, 2.0);
		return (float) Math.sqrt(x + y);
	}

	/**
	 * Prend une position dans l'espace global et la renvoie dans l'espace de
	 * l'écran
	 * 
	 * @param cameraPosition La position de la caméra
	 * @return La position dans l'espace de l'écran
	 */
	public Vec2 globalToScreen(Vec2 cameraPosition) {
		return this.minus(cameraPosition);
	}

	/**
	 * Calcule la différence entre deux vecteurs
	 * 
	 * @param other L'autre vecteur
	 * @return La différence de ce vecteur et de l'autre
	 */
	public Vec2 minus(Vec2 other) {
		return new Vec2(this.x - other.x, this.y - other.y);
	}

	/**
	 * Calcule le produit scalaire entre deux vecteurs
	 * 
	 * @param other autre vecteur
	 * @return Le produit scalaire de ce vecteur et de l'autre
	 */
	public float dot(Vec2 other) {
		return this.x * other.x + this.y * other.y;
	}

	/**
	 * On fait le floor de deux vecteurs
	 * 
	 * @return a new vector with each element floor()ed.
	 */
	public Vec2 floor() {
		return new Vec2((float) Math.floor(this.x), (float) Math.floor(this.y));
	}

	public Vec2 round() {
		return new Vec2((float) Math.round(this.x), (float) Math.round(this.y));
	}

	/**
	 * Add 2 vectors
	 * 
	 * @return a new vector with sum of each vectors.
	 */
	public Vec2 add(Vec2 other) {
		return new Vec2(other.x + this.x, other.y + this.y);
	}

	public Vec2 add(int a) {
		return new Vec2(this.x + a, a + this.y);
	}

	public Vec2 sub(Vec2 other) {
		return new Vec2(other.x - this.x, other.y - this.y);
	}

	public Vec2 multiply(float a) {
		return new Vec2(this.x * a, this.y * a);
	}

	public Vec2 multiply(int a) {
		return new Vec2(this.x * a, this.y * a);
	}

	public Vec2 divide(float a) {
		return new Vec2(this.x / a, this.y / a);
	}

	public Vec2 pow(int a) {
		return new Vec2((float) Math.pow(this.x, a), (float) Math.pow(this.y, a));
	}

	public float product() {
		return this.x * this.y;
	}

	public Vec2 abs() {
		return new Vec2(Math.abs(this.x), Math.abs(this.y));
	}

	public float length() {
		return (float) Math.sqrt(this.x * this.x + this.y * this.y);
	}

	public Vec2 normalized() {
		return new Vec2(this.x / this.length(), this.y / this.length());
	}

	public static Vec2 nullVector() {
		return new Vec2(0.0f, 0.0f);
	}

	public float Angle() {
		return (float) Math.atan(y / x);
	}

	public void nullX() {
		this.x = 0;
	}

	public void nullY() {
		this.y = 0;
	}

	public Vec2 screenToGlobal(Vec2 pos) {
		return pos.add(this);
	}

	public void print() {
		System.out.println(this.getX() + " " + this.getY());
	}

	public Direction orientation(Vec2 mousePos) {
		float distX = mousePos.getX() - this.getX();
		float distY = mousePos.getY() - this.getY();

		if (Math.abs(distX) > Math.abs(distY)) {
			if (distX > 0) {
				return Direction.EST;
			} else {
				return Direction.WEST;
			}
		} else if (distY > 0) {
			return Direction.SOUTH;
		} else {
			return Direction.NORTH;
		}
	}

	public Vec2 sub(int i) {
		return new Vec2(this.x - i, this.y - i);
	}

	public Vec2 wrapCoords() {
		float max = Block.SIZE * Model.getMap().width;

		float x = this.x % max;
		if (x <= 0) {
			x = max + x;
		}

		float y = this.y % max;
		if (y <= 0) {
			y = max + y;
		}

		return new Vec2(x, y);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Vec2 && ((Vec2) other).x == this.x && ((Vec2) other).y == this.y;
	}
}
