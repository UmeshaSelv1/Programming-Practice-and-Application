package requirement1.models.alieninvaders;

import java.io.IOException;

/**
 * Bullet is shot from the spaceship
 */
public class Bullet extends GameModel{
	
	public static int SPEED = 15;
	public static int SIZE_X = 4;
	public static int SIZE_Y = 8;

	protected Bullet(double x) throws IOException {
		super();
		this.setX(x);
		this.setY(GameState.HEIGHT - GameState.INSETS - 40);
	}
	
	public double updateBulletPosition() {
		double newPosition = this.getY() - Bullet.SPEED;
		this.setY(newPosition);
		
		return newPosition;
	}

	@Override
	public int getWidth() {
		return SIZE_X;
	}

	@Override
	public int getHeight() {
		return SIZE_Y;
	}

}
