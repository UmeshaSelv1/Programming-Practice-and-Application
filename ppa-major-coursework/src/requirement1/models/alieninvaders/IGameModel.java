package requirement1.models.alieninvaders;

/**
 * Practically every object in the game
 */
public interface IGameModel {

	public int getWidth();
	public int getHeight();

	public double getX();
	public void setX(double x);

	public double getY();
	public void setY(double y);
}
