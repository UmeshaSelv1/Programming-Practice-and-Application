package requirement1.models.alieninvaders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Timer;

/**
 * Spaceship is the object that user moves
 */
public class Spaceship extends GameModel{

	private static int WIDTH = 20;
	private static int HEIGHT = 40;
	private static double BULLET_RATE = 0.5;
	public static int SPEED = 5;
	
	private double damagePerHit = 1;
	private int velocityX;
	private List<Bullet> bulletsShot;
	private Timer shootingTimer;
	private boolean shotOnce = false;
	private boolean isDead = false;

	public Spaceship() throws IOException {
		super("spaceship.png", Spaceship.WIDTH);

		this.setY(GameState.HEIGHT - this.getScaledImage().getHeight(null) - GameState.INSETS);
		this.x = GameState.INSETS;
		
		this.velocityX = 0;
		this.bulletsShot = new ArrayList<>();
		
		shootingTimer = new Timer((int) (BULLET_RATE * 1000), new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				shoot();
			}
		});
	}

	/**
	 * Starts shooting, shoots once and then creates a timer, shooting once every BULLET_RATE seconds (0.5)
	 */
	public void startShooting() {
		if (!shotOnce) {
			shoot();
		}
		shootingTimer.start();
	}
	
	/**
	 * Stops the timer, shoots once if spaceship hasn't been shooting yet. This fixes the tap shoot.
	 */
	public void stopShooting() {
		shootingTimer.stop();
		if (!shotOnce) {
			shoot();
		}
		shotOnce = false;
	}
	
	/**
	 * Creates a new bullet and fires it
	 */
	public void shoot() {
		try {
			// that's the center of the ship
			double bulletInitialX = this.getX() + Spaceship.WIDTH / 2;
			Bullet bullet = new Bullet(bulletInitialX);
			shotOnce = true;
			
			this.bulletsShot.add(bullet);
		} catch (IOException e) {
			// this will never fail, but still...
			e.printStackTrace();
		}
	}
	
	public int getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(int velocityX) {
		this.velocityX = velocityX;
	}
	
	/**
	 * Moves all the bullets of the ship to the appropriate positions (going up)
	 */
	public void moveBullets() {
		List<Bullet> bulletsToRemove = new LinkedList<>();
		for (Bullet bullet : bulletsShot) {
			double newBulletPos = bullet.updateBulletPosition();
			if (newBulletPos <= 0) {
				bulletsToRemove.add(bullet);
			}
		}
		
		destroyBullets(bulletsToRemove);
	}

	/**
	 * Destroys the Bullet objects
	 */
	public void destroyBullets(Collection<Bullet> bulletsToRemove) {
		this.bulletsShot.removeAll(bulletsToRemove);
	}

	/**
	 * Kills the ship
	 */
	public void kill() {
		System.out.println("SHIP IS KILLED");
		this.isDead = true;
	}
	
	/**
	 * True if the ship is killed, a.k.a dead
	 */
	public boolean isDead() {
		return this.isDead;
	}

	public void increasePower() {
		this.damagePerHit += Doge.POWER;
	}

	public List<Bullet> getBullets() {
		return this.bulletsShot;
	}

	public double getDamagePerHit() {
		return this.damagePerHit;
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
	public void setX(double x) {
		int leftBorder = GameState.INSETS;
		int rightBorder = GameState.WIDTH - GameState.INSETS * 2;
		if (x >= leftBorder && x <= rightBorder) {
			super.setX(x);
		}
		else if (x >= leftBorder) {
			super.setX(rightBorder);
		}
		else {
			super.setX(leftBorder);
		}
	}

	
}
