package requirement1.models.alieninvaders;

import java.io.IOException;

/**
 * Background is a background (I know)
 */
public class Background extends GameModel{
	
	private static int WIDTH = 2800;
	private static int HEIGHT = 2000;

	public Background() throws IOException {
		super("background.png", 2800);
	}

	@Override
	public int getWidth() {
		return Background.WIDTH;
	}
	
	@Override
	public int getHeight() {
		return Background.HEIGHT;
	}

}
