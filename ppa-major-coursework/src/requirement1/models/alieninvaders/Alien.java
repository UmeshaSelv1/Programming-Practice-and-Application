package requirement1.models.alieninvaders;

import java.io.IOException;

import requirement1.models.EState;

/**
 * Alien is the bad guy, kill aliens to win
 */
public class Alien extends GameModel {

	public static int WIDTH = 40;
	public static int HEIGHT = 60;
    static int PADDING_X = 30;
	static int PADDING_Y = 20;
	static double SPEED = 1.5;
	public static int MAX_HP = 10;

	private int yearOfOrigin;
	private double hp;
	private boolean isDead;

	public Alien(int x, int y) throws IOException {
		super("alien.png", WIDTH);
		this.setX(x);
		this.setY(y);
		
		this.isDead = false;
		this.yearOfOrigin = 1994;
		this.setHp(MAX_HP);
	}
	
	private void die() {
		this.isDead = true;
	}

	public int getYearOfOrigin() {
		return yearOfOrigin;
	}

	public double getHp() {
		return hp;
	}

	public void setHp(double hp) {
		this.hp = hp;
		if (hp <= 0) {
			this.die();
		}
	}

	public boolean isDead() {
		return isDead;
	}

	@Override
	public int getWidth() {
		return Alien.WIDTH;
	}

	@Override
	public int getHeight() {
		return Alien.HEIGHT;
	}

}
