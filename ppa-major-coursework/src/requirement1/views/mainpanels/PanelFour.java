package requirement1.views.mainpanels;

import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 * Panel four. Contains the AlienInvaders game. No update method as it does not do anything with the data...
 */
public class PanelFour extends APanel {

    private static final long serialVersionUID = 2712734883038599016L;

	private AlienGamePanel gamePanel;

    /**
     * Constructor for panel four
     */
	public PanelFour() {

		gamePanel = new AlienGamePanel();

        Box box = new Box(BoxLayout.Y_AXIS);

        box.add(Box.createVerticalGlue());
        box.add(gamePanel);     
        box.add(Box.createVerticalGlue());
        
		this.add(box);
	}
}
