package requirement1.models.alieninvaders;

/**
 * Objects that can collide with other objects inherit this
 */
public interface ICollidable extends IGameModel {
	public boolean checkCollisionWith(ICollidable otherModel);
}
