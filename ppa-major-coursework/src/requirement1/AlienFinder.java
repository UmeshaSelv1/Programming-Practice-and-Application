package requirement1;

import requirement1.controllers.YearsController;
import requirement1.models.IncidentsHolderModel;
import requirement1.views.MainFrame;
import requirement1.views.mainpanels.APanel;

public class AlienFinder {

    public AlienFinder() {


        // Holds all the alien data, provides caching
        IncidentsHolderModel incidentsModel = new IncidentsHolderModel();

        // Controller for the years
        YearsController topController = new YearsController(incidentsModel);

        // Open frame
        MainFrame frame = new MainFrame(incidentsModel, topController);

        // Add panels and observers
        APanel[] innerPanels = frame.getInnerPanels();
        for (APanel innerPanel : innerPanels) {
            incidentsModel.addObserver(innerPanel);
        }
        incidentsModel.addObserver(frame);
    }

    // main method
	public static void main(String[] args) {

	    AlienFinder finder = new AlienFinder();
	}

}
