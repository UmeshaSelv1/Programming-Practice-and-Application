package requirement1.controllers;

import requirement1.models.API;
import requirement1.models.IncidentsHolderModel;
import requirement1.models.alieninvaders.GameState;
import requirement1.models.alieninvaders.Spaceship;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller for the top panel, provides functions for interaction with the model
 */
public class YearsController {
    private IncidentsHolderModel model;

    /**
     * Creates an instance of the controller with a provided model
     */
    public YearsController(IncidentsHolderModel model) {
        this.model = model;
    }

    /**
     * Sets the year from in the model
     */
    public void setYearFrom(Integer year) {
        this.model.setYearFrom(year);

    }

    /**
     * Sets the year to in the model
     */
    public void setYearTo(Integer year) {
        this.model.setYearTo(year);
    }

    /**
     * Returns an array of all available years
     */
    public Integer[] getAllYears() {
        List<Integer> list = new LinkedList<>();
        int startYear = API.getRipleyInstance().getStartYear();
        int latestYear = API.getRipleyInstance().getLatestYear();
        for (int i = startYear; i <= latestYear; i++) {
            list.add(i);
        }

        return list.toArray(new Integer[0]);
    }

    public static class SpaceshipKeyAdapter extends KeyAdapter {

        private GameState gameState;

        public SpaceshipKeyAdapter(GameState gameState) {
            this.gameState = gameState;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                gameState.getSpaceship().setVelocityX(-Spaceship.SPEED);
            }
            if (key == KeyEvent.VK_RIGHT) {
                gameState.getSpaceship().setVelocityX(Spaceship.SPEED);
            }
            if (key == KeyEvent.VK_SPACE) {
                gameState.getSpaceship().startShooting();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            int velocityX = gameState.getSpaceship().getVelocityX();

            if (key == KeyEvent.VK_LEFT && velocityX < 0) {
                gameState.getSpaceship().setVelocityX(0);
            }
            if (key == KeyEvent.VK_RIGHT && velocityX > 0) {
                gameState.getSpaceship().setVelocityX(0);
            }
            if (key == KeyEvent.VK_SPACE) {
                gameState.getSpaceship().stopShooting();
            }
        }
    }
}
