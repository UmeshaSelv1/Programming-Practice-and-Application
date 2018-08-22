package requirement1.models.alieninvaders;

import java.io.IOException;

/**
 * Doge increases damage by POWER amount if it's collected
 */
public class Doge extends GameModel implements IDroppable{

	public static int SIZE_X = 40;
	public static int SIZE_Y = 40;
	public static double SPEED = 2;
	public static int CHANCE = 20;
	public static double POWER = 0.25;

	public Doge(double newX, double newY) throws IOException {
		super("doge.png");
		this.setX(newX);
		this.setY(newY);
	}

	@Override
	public int getWidth() {
		return SIZE_X;
	}

	@Override
	public int getHeight() {
		return SIZE_Y;
	}

	@Override
	public void move() {
		this.setY(this.getY() + this.getSpeed());
	}

	@Override
	public double getSpeed() {
		return Doge.SPEED;
	}

	@Override
	public void hit(Spaceship ship) {
		ship.increasePower();
	}
}
