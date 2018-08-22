package requirement1.models.alieninvaders;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import requirement1.models.alieninvaders.IDroppable.EDroppableType;
import requirement1.views.mainpanels.AlienGamePanel.EGameState;

/**
 * Practically the game model itself
 */
public class GameState {
	
	public static int WIDTH = 800;
	public static int HEIGHT = 500;
	public static int INSETS = 10;
	public static int ROWS = 3;
	public static int COLS = 8;
	
	private Background background;
	private Spaceship spaceship;
	private Alien[][] aliensPresent;
	private HashMap<Integer, Boolean> rowToMovingRight;
	
	private EGameState currentGameState;
	private List<IDroppable> shits;
	private List<IDroppable> doges;

    public GameState() {
		try {
			this.background = new Background();
			this.spaceship = new Spaceship();
			this.aliensPresent = new Alien[ROWS][COLS];
			this.rowToMovingRight = new HashMap<>();
			this.shits = new LinkedList<>();
			this.doges = new LinkedList<>();
			// all aliens moving right in the beginning
			for (int i = 0; i < ROWS; i++) {
				this.rowToMovingRight.put(i, true);
			}
			populateAliens();
			
			currentGameState = EGameState.NOT_RUNNING;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Something is wrong with objects creation");
		}
	}
	
	/**
	 * Populates the aliens
	 */
	private void populateAliens() throws IOException {
		int offset = 0;
		for (int y = 0; y < aliensPresent.length; y++) {
			for (int x = 0; x < aliensPresent[y].length; x++) {
				int initX = x * Alien.WIDTH + x * Alien.PADDING_X + offset++ * 5;
				int initY = y * Alien.HEIGHT + y * Alien.PADDING_Y + GameState.INSETS;
				Alien alien = new Alien(initX, initY);
				this.aliensPresent[y][x] = alien;
			}
		}
	}
	
	/**
	 * Aliens move to right and left until they hit the screen
	 */
	private void moveAliens() {
		for (int rowNum = 0; rowNum < aliensPresent.length; rowNum++) {
			// Moving right
			if (this.rowToMovingRight.get(rowNum)) {
				double mostRightAlienX = this.aliensPresent[rowNum][this.aliensPresent[rowNum].length-1].getX() + Alien.WIDTH + GameState.INSETS;
				if (mostRightAlienX < GameState.WIDTH - GameState.INSETS * 2) {
					moveAlienRow(rowNum, true);
				}
				else {
					this.rowToMovingRight.put(rowNum, false);
				}
			}
			// Moving left
			else {
				double mostRightAlienX = this.aliensPresent[rowNum][0].getX();
				if (mostRightAlienX > GameState.INSETS) {
					moveAlienRow(rowNum, false);
				}
				else {
					this.rowToMovingRight.put(rowNum, true);
				}
			}
		}
	}
	
	private void moveAlienRow(int rowNum, boolean directionIsRight) {
		double moveAmount = directionIsRight ? Alien.SPEED : -Alien.SPEED;
		for (Alien alien : this.aliensPresent[rowNum]) {
			alien.setX(alien.getX() + moveAmount);
		}
	}

	/**
	 * True if game is won
	 */
	private void checkEGameState() {
		
		// Check lose
		if (this.spaceship.isDead()) {
			this.currentGameState = EGameState.LOST;
			return;
		}
		
		// Check win
		boolean isWin = true;
		
		for (int y = 0; y < aliensPresent.length; y++) {
			for (int x = 0; x < aliensPresent[y].length; x++) {
				if (!aliensPresent[y][x].isDead()) {
					isWin = false;
				}
			}
		}
		
		if (isWin) {
			this.currentGameState = EGameState.WON;
		}
	}

	/**
	 * Returns true if all aliens are dead
	 */
	private void checkBulletCollisions() {
		for (int y = 0; y < aliensPresent.length; y++) {
			for (int x = 0; x < aliensPresent[y].length; x++) {
				List<Bullet> bulletsToRemove = new LinkedList<>();
				for (Bullet bullet : this.spaceship.getBullets()) {
					Alien alien = aliensPresent[y][x];
					
					// check collision between bullet and alien
					if (alien.checkCollisionWith(bullet)) {
						alien.setHp(alien.getHp() - spaceship.getDamagePerHit());
						bulletsToRemove.add(bullet);
						if (alien.isDead()) {
							createNewDoge(alien);
						}
					}
					
				}
				
				spaceship.destroyBullets(bulletsToRemove);
			}
		}
	}

	private void checkSpaceshipCollisions(Collection<IDroppable> droppables) {
		List<IDroppable> droppablesToRemove = new LinkedList<>();
		for (IDroppable droppable : droppables) {
			if (spaceship.checkCollisionWith(droppable)) {
				System.out.println("spaceship collision found for: " + droppable + " object.");
				droppable.hit(spaceship);
				droppablesToRemove.add(droppable);
			}
		}
		droppables.removeAll(droppablesToRemove);
	}


	/**
	 * Updates the state of the whole game, this should happen on every tick of the game loop
	 */
	public EGameState updateState() {
		this.spaceship.setX(this.spaceship.getX() + this.spaceship.getVelocityX());
		this.moveAliens();
		this.spaceship.moveBullets();
		
		this.moveDroppables(this.shits);
		this.moveDroppables(this.doges);
		
		this.checkBulletCollisions();
		
		this.checkSpaceshipCollisions(this.shits);
		this.checkSpaceshipCollisions(this.doges);
		
		this.createNewBombs();
		
		this.checkEGameState();
		
		return this.currentGameState;
	}

	private void moveDroppables(List<IDroppable> droppables) {
		for (IDroppable droppable : droppables) {
			droppable.move();
		}
	}

	private void createNewDoge(Alien alien) {
		double chance = Doge.CHANCE; // % chance to drop doge
		double random = new Random().nextDouble();
		if (random <= chance/100) {
			dropSingleDroppable(alien, EDroppableType.DOGE);
		}
	}

	private void createNewBombs() {
		double chance = Bomb.CHANCE; // % chance to drop shit
		double random = new Random().nextDouble();
		if (random <= chance/100) {
			Alien alien = getRandomAlien();
			dropSingleDroppable(alien, EDroppableType.BOMB);
		}
	}

	/**
	 * Drops a single droppable
	 */
	private void dropSingleDroppable(Alien alien, EDroppableType type) {
		try {
			double newX = alien.getX() + Alien.WIDTH / 2;
			double newY = alien.getY() + Alien.HEIGHT;
			IDroppable droppable = null;
			if (type == EDroppableType.BOMB) {
				droppable = new Bomb(newX, newY);
				this.shits.add(droppable);
			}
			if (type == EDroppableType.DOGE) {
				droppable = new Doge(newX, newY);
				this.doges.add(droppable);
			}
			
		} catch (IOException e) {
			// this will never happen...
			e.printStackTrace();
		}
	}

	/**
	 * Returns random non-dead alien
	 */
	private Alien getRandomAlien() {
		int y = new Random().nextInt(aliensPresent.length);
		int x = new Random().nextInt(aliensPresent[y].length);
		
		Alien alien = aliensPresent[y][x];
		
		while (alien.isDead()) {
			y = new Random().nextInt(aliensPresent.length);
			x = new Random().nextInt(aliensPresent[y].length);
			alien = aliensPresent[y][x];
		}
		
		return alien;
	}

	public Spaceship getSpaceship() {
		return this.spaceship;
	}
	
	public Alien[][] getAliens() {
		return this.aliensPresent;
	}

	public Background getBackground() {
		return this.background;
	}

	public EGameState getCurrentGameState() {
		return currentGameState;
	}

	public List<IDroppable> getBombs() {
		return this.shits;
	}

	public List<IDroppable> getDoges() {
		return this.doges;
	}

    public void setEGameState(EGameState EGameState) {
        this.currentGameState = EGameState;
    }
}
