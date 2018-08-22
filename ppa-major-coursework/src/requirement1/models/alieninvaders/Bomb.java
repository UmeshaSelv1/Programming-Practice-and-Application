package requirement1.models.alieninvaders;

import java.io.IOException;

/**
 * Bomb kills the ship. Ends the game.
 */
public class Bomb extends GameModel implements IDroppable{

	static double CHANCE = 4;
	private static double SPEED = 3;

	private static int WIDTH = 20;
	private static int HEIGHT = 13;

	Bomb(double bombX, double bombY) throws IOException {
		super("shit.png", WIDTH);
		this.setX(bombX);
		this.setY(bombY);
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	public void move() {
		this.setY(this.getY() + this.getSpeed());
	}

	@Override
	public double getSpeed() {
		return SPEED;
	}

	@Override
	public void hit(Spaceship ship) {
		ship.kill();
	}
	
	
}
