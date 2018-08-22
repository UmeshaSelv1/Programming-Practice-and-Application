package requirement1.views.mainpanels;

import java.awt.GridLayout;
import java.util.*;

import requirement1.controllers.StatisticsUpdater;
import requirement1.models.*;

import requirement1.models.SingleStatisticViewModel;
import requirement1.views.StatisticFrameViewModel;

/**
 * Panel for statistics
 */
public class PanelThree extends APanel {

	private static final long serialVersionUID = 3721353038267102790L;

    private StatisticFrameViewModel[] statsViews;
    private static int ROWS = 2;
    private static int COLS = 2;

	public PanelThree()
	{
		setLayout(new GridLayout(ROWS, COLS));

        statsViews = new StatisticFrameViewModel[ROWS * COLS];

		// Try to get serialized object
        LinkedList<SingleStatisticViewModel> stats = StatisticsHolder.getInstance().getSerialized();

        if (stats != null) {
            int counter = 0;
            for (SingleStatisticViewModel frameView : stats) {
                statsViews[counter] = new StatisticFrameViewModel(frameView);
                this.add(statsViews[counter]);
                counter++;
            }
        }
        // No serialized objects - populate view with available ones
        else {
            StatisticsHolder holder = StatisticsHolder.getInstance();
            for (int i = 0; i < statsViews.length; i++) {
                SingleStatisticViewModel singleStatistics = holder.getNextAvailableInsteadOf(null);
                if (singleStatistics != null) {
                    statsViews[i] = new StatisticFrameViewModel(singleStatistics);
                    this.add(statsViews[i]);
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        super.update(o, arg);

        if (arg != null && (arg instanceof Integer) && ((Integer)arg) < 0) {
            return;
        }

        IncidentsHolderModel holder = (IncidentsHolderModel) o;
        StatisticsUpdater.getInstance().updateAllModels(holder);
    }

}

