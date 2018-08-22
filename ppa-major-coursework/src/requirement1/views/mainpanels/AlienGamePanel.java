package requirement1.views.mainpanels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.*;

import requirement1.controllers.YearsController;
import requirement1.models.alieninvaders.Alien;
import requirement1.models.alieninvaders.Background;
import requirement1.models.alieninvaders.Bullet;
import requirement1.models.alieninvaders.Doge;
import requirement1.models.alieninvaders.GameModel;
import requirement1.models.alieninvaders.GameState;
import requirement1.models.alieninvaders.IDroppable;
import requirement1.models.alieninvaders.Bomb;
import requirement1.models.alieninvaders.Spaceship;

public class AlienGamePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4146340436416450067L;

	private static int UPDATE_RATE = 1000/60;
	private static Color GREEN_COLOR = new Color(126, 247, 27, 150);
	private static int CIRCLE_SIZE = 18;
	private static int HP_BAR_HEIGHT = 6;
    private static RenderingHints rh = new RenderingHints(
								             RenderingHints.KEY_TEXT_ANTIALIASING,
								             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
	private GameState gameState;
	private Timer gameLoopTimer;
	private ActionListener timerListener;
    Background bg;


    /**
     * Alien Game panel
     */
    public AlienGamePanel() {
        try {
            bg = new Background();
        }
        catch (IOException ex) {
            System.out.println("No background found");
        }

		Dimension dimension = new Dimension(GameState.WIDTH, GameState.HEIGHT);
		this.setMinimumSize(dimension);
		this.setMaximumSize(dimension);
		this.setPreferredSize(dimension);
		this.setSize(dimension);
        this.setLayout(new GridBagLayout());

		// intro panel
        JPanel introPanel = createBasicDialog("Start!",
                "Rules are - Kill the aliens!",
                "Collect doges to increase damage, don't get hit by bombs :)",
                "Sorry, no time to develop further x(");
        openGameDialog(introPanel);

        // set focusable so key events work, add key adapter too
		this.setFocusable(true);
		this.setRequestFocusEnabled(true);
		
		// take care of gameLoopTimer
        timerListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			    if (gameState == null) {
			        return;
                }

				EGameState newState = gameState.updateState();
				if (newState == EGameState.LOST) {
                    gameLoopTimer.stop();
					openLoseDialog();
				}
				else if (newState == EGameState.WON) {
                    gameLoopTimer.stop();
					openWinDialog();
					gameState = null;
				}
				
				draw();
			}
		};

		gameLoopTimer = new Timer(UPDATE_RATE, timerListener);
	}

	private void startGame() {

        gameState = new GameState();
        gameLoopTimer.start();
        gameState.setEGameState(EGameState.RUNNING);

        KeyAdapter keyListener = new YearsController.SpaceshipKeyAdapter(gameState);
        addKeyListener(keyListener);
    }

    /**
     * Opens dialog on the game itself
     */
	private void openGameDialog(JPanel panel) {

        GridBagConstraints gbc = new GridBagConstraints();
        this.removeAll();
        this.add(panel, gbc);
        this.revalidate();
        this.repaint();

    }

    private JPanel createBasicDialog(String startButtonName, String... labels) {
        JPanel introPanel = new JPanel();

        introPanel.setLayout(new BorderLayout());
        introPanel.setBackground(new Color(255, 255, 255, 200));

        JPanel container = new JPanel();
//        container.setLayout();
        introPanel.add(container);
        for (String label : labels) {
            container.add(new JLabel(label, SwingConstants.CENTER));
        }

        Dimension dim = new Dimension(500, 300);
        introPanel.setSize(dim);
        introPanel.setMinimumSize(dim);
        introPanel.setMaximumSize(dim);
        introPanel.setPreferredSize(dim);

        if (startButtonName != null){
            JButton startButton = new JButton(startButtonName);
            startButton.setBackground(Color.DARK_GRAY);
            startButton.setOpaque(true);
            startButton.setBorderPainted(false);
            startButton.setForeground(Color.WHITE);
            ActionListener startButtonAL = new StarButtonAL();
            startButton.addActionListener(startButtonAL);
            introPanel.add(startButton, BorderLayout.SOUTH);
        }

        return introPanel;
    }

    private void openWinDialog() {
        JPanel winDialog = createBasicDialog("Try to do it faster!", "YOU WON!!!!");
        openGameDialog(winDialog);
	}

	private void openLoseDialog() {
        JPanel introPanel = createBasicDialog("Try again, noob!",
                "You are so bad at this game",
                "It's very easy to win...",
                "Prove you don't suck and try again!");
        openGameDialog(introPanel);
	}
	
	private void draw() {
		this.grabFocus();
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHints(rh);

	    if (gameState == null) {
            draw(g, bg);
	        return;
        }
		
		// draw background
		Background background = gameState.getBackground();
		draw(g, background);
		
		// draw spaceship
		Spaceship spaceship = gameState.getSpaceship();
		draw(g, spaceship);
		
		// draw aliens
		Alien[][] aliens = gameState.getAliens();
		for (Alien[] alienRow : aliens) {
			for (Alien alien : alienRow) {
				// don't draw dead aliens
				if (alien.isDead()) {
					continue;
				}
				// draw image
				draw(g, alien);
				
				// state circle
				g.setColor(new Color(255,255,255,50));
				int circleX = (int)alien.getX() - CIRCLE_SIZE;
				int circleY = (int)alien.getY() + Alien.HEIGHT - CIRCLE_SIZE;
				g.fillOval(circleX, circleY, CIRCLE_SIZE, CIRCLE_SIZE);

				// state name
				g.setColor(GREEN_COLOR);
				g.setFont(g.getFont().deriveFont(10f));
				g.drawString(String.valueOf(new DecimalFormat("#0").format(alien.getHp())), circleX + 3, circleY + CIRCLE_SIZE - 5);
				
				// hp background
				g.setColor(Color.WHITE);
				g.fillRoundRect((int) alien.getX(), (int) (alien.getY() + Alien.HEIGHT - HP_BAR_HEIGHT), Alien.WIDTH, HP_BAR_HEIGHT, 5, 5);

				// actual hp
				g.setColor(Color.red);
				double ratio = alien.getHp() / (double)Alien.MAX_HP;
				g.fillRoundRect(
						(int) (alien.getX() + 1), 
						(int) (alien.getY() + Alien.HEIGHT - HP_BAR_HEIGHT + 1), 
						(int) (ratio * (double)Alien.WIDTH) - 2, 
						HP_BAR_HEIGHT - 2, 
						5, 5);
				
			}
		}
		
		// draw bullets
		for (Bullet bullet : spaceship.getBullets()) {
			g.setColor(Color.WHITE);
			g.fillRoundRect((int)bullet.getX(), (int)bullet.getY(), Bullet.SIZE_X, Bullet.SIZE_Y, 4, 4);
		}
		
		// draw bombs
		for (IDroppable bomb : gameState.getBombs()) {
			draw(g, (Bomb) bomb);
		}
		
		// draw bombs
		for (IDroppable doge : gameState.getDoges()) {
			draw(g, (Doge) doge);
		}

		// draw info

        g.setColor(Color.WHITE);
		g.drawString("Damage: " + gameState.getSpaceship().getDamagePerHit(), GameState.WIDTH - 100, GameState.HEIGHT - 40);

		
	}
	
	/**
	 * Draws a GameModel with images
	 */
	private void draw(Graphics g, GameModel model) {
		g.drawImage(model.getScaledImage(), (int)model.getX(), (int)model.getY(), this);
	}
	
	public enum EGameState {
		RUNNING,
        NOT_RUNNING,
		PAUSED,
		WON,
		LOST
	}

    private class StarButtonAL implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            startGame();
            removeAll();
            revalidate();
            repaint();
        }
    }
}
