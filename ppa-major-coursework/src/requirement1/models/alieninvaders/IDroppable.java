package requirement1.models.alieninvaders;

/**
 * Objects thats can be dropped inherit this
 */
public interface IDroppable extends ICollidable {

	public void move();
	public double getSpeed();
	public void hit(Spaceship gameState);
	
	public enum EDroppableType {
        BOMB,
		DOGE
	}
}
