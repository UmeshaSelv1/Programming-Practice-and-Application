package requirement1.models.alieninvaders;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * Every single object is a game model
 */
public abstract class GameModel implements ICollidable {

	private static int DEFAULT_WIDTH = 20;
	
	private Image image;
	protected double x;
	protected double y;

	protected GameModel() throws IOException {
		this(null, DEFAULT_WIDTH);
	}
	
	protected GameModel(String fileName) throws IOException {
		this(fileName, DEFAULT_WIDTH);
	}
	
	protected GameModel(String fileName, int width) throws IOException {
		// Some GameModels might not have image (e.g. Bullets)
		if (fileName != null) {
			String prefix = "/images/alieninvaders/";
			InputStream isAlien = GameModel.class.getResourceAsStream(prefix + fileName);
			BufferedImage originalImage = ImageIO.read(isAlien);
			int newHeight = originalImage.getHeight() / (originalImage.getWidth() / width);
			image = originalImage.getScaledInstance(width, newHeight, Image.SCALE_SMOOTH);
		}
	}
	
	public Image getScaledImage() {
		return image;
	}
	
	/**
	 * Get X position
	 */
	public double getX() {
		return x;
	}

	/**
	 * Set X position
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Get Y position
	 */
	public double getY() {
		return y;
	}

	/**
	 * Set Y position
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName()
				+ ": width [" + this.getWidth() + "], height [" + this.getHeight() + "], "
				+ "X left [" + this.getX() + "], X right [" + (this.getX() + this.getWidth()) +"], "
				+ "Y top [" + this.getY() + "], Y bottom [" + (this.getY() + this.getHeight() + "].");
	}

	/**
	 * Return if otherModel collides with this object, returns true if collides
	 * x_____x
	 * |     |
	 * | o___|_o -> collides (best documentation ever)
	 * | |   | |
	 * x-----x |
	 *   |     |
	 *   o-----o
	 */
	@Override
	public boolean checkCollisionWith(ICollidable otherModel) {
		
		// No collision if something is null
		if (otherModel == null) {
			return false;
		}
		
		// If dead alien - no collision
		if ((this instanceof Alien && ((Alien)this).isDead())
			||(otherModel instanceof Alien && ((Alien)otherModel).isDead())) {
			return false;
		}

		double thisLeftX = this.getX();
		double thisRightX = this.getX() + this.getWidth();
		double thisTopY = this.getY();
		double thisBottomY = this.getY() + this.getHeight();
		
		double otherLeftX = otherModel.getX();
		double otherRightX = otherModel.getX() + otherModel.getWidth();
		double otherTopY = otherModel.getY();
		double otherBottomY = otherModel.getY() + otherModel.getHeight();

		boolean xCollides = 
				(thisLeftX < otherLeftX && thisRightX > otherLeftX) ||
				(thisRightX > otherRightX && thisLeftX < otherRightX) ||
                (thisLeftX > otherLeftX && thisRightX < otherRightX) ||
                (thisLeftX < otherLeftX && thisRightX > otherRightX);

		boolean yCollides = 
				(thisTopY < otherTopY && thisBottomY > otherTopY) ||
				(thisBottomY > otherBottomY && thisTopY < otherBottomY) ||
                (thisTopY > otherTopY && thisBottomY < otherBottomY) ||
                (thisTopY < otherTopY && thisBottomY > otherBottomY);
		
		if (xCollides && yCollides) {

			// Collision found
			return true;
		}
		
		// No collision
		return false;
	}
	
}
