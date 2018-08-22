package requirement1.views.mainpanels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;

import javax.swing.JLabel;
import javax.swing.JPanel;

import requirement1.models.API;
import requirement1.models.IncidentsHolderModel;

public class PanelOne extends APanel {

	private static final long serialVersionUID = -8113659767352959760L;
	private String mainString;
	private String dateRange;
	private String version;
	private String timePassed;
	private JLabel mainLabel;
	private EAppState currentState;
    private Integer numberOfObjects;

    /**
	 * Creates new Welcome panel
	 */
	public PanelOne() {
		
		// take care of boring stuff such as sizes and positions
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		mainLabel = new JLabel();
		mainLabel.setSize(new Dimension(300, 400));
		mainLabel.setPreferredSize(new Dimension(300, 400));
		mainLabel.setMinimumSize(new Dimension(300, 400));
		
		mainLabel.setHorizontalTextPosition(JLabel.CENTER);
		
		panel.add(mainLabel, BorderLayout.CENTER);
		
		// version
		setVersion(API.getRipleyInstance().getVersion());
		timePassed = null;
		
		this.add(panel);
		
	}


	/**
	 * Creates a string for the time that passed to grab the data and visualises it in Welcome Panel
	 */
	private void updateTimePassed(Integer timePassedSeconds) {
		
		if (timePassedSeconds == null) {
			this.timePassed = null;
			updateMainString();
			return;
		}
		
		String finalString = "";
		if (timePassedSeconds > 60) {
			String minutes = (timePassedSeconds / 60) == 1 ? "minute " : "minutes ";
			finalString += (timePassedSeconds / 60) + " " + minutes;
		}
		if (timePassedSeconds % 60 != 0) {
			String seconds = (timePassedSeconds % 10) == 1 ? "second " : "seconds ";
			finalString += (timePassedSeconds % 60) + " " + seconds;
		}
		else {
			finalString = String.valueOf(timePassedSeconds) + " seconds";
		}

		this.timePassed = finalString;
		updateMainString();
	}
	
	private void updateMainString() {
		mainString = "<html><center>";
		appendLineToMainString("Welcome to Ripley API v" + version);
		appendLineToMainString("Please select from the dates above in order to");
		appendLineToMainString("begin analysing UFO sighting data.");
		appendLineToMainString("");
		
		if (dateRange != null) {
            appendLineToMainString("");
			appendLineToMainString("Date range selected: " + dateRange);
			appendLineToMainString("");
		}

		if (currentState == EAppState.INVALID_RANGE) {
            appendLineToMainString("Invalid time range...");

        }
        else if (currentState == EAppState.CURRENTLY_GRABBING) {
            appendLineToMainString("Grabbing data...");
        }
        else if (timePassed != null && currentState == EAppState.GRABBED_DATA) {
			appendLineToMainString("Data grabbed in " + timePassed);
			if (numberOfObjects != null) {
                appendLineToMainString(numberOfObjects + " incidents extracted for the selected time range.");
            }
			appendLineToMainString("");
			appendLineToMainString("<b>Please now interact with this data using the buttons to the left and the right.</b>");
		}
		
		mainString += "</center></html>";

		mainLabel.setText(mainString);
	}

	private void appendLineToMainString(String something) {
		mainString += something + "<br>";
	}
	
	/**
	 * Sets the date range that is currently selected
	 */
	private void setDateRange() {
		if (this.yearFrom == null || this.yearTo == null) {
			dateRange = null;
			updateMainString();
			return;
		}
		dateRange = this.yearFrom + " - " + this.yearTo;
		updateMainString();
	}

	/**
	 * Sets the version of Ripley API
	 */
	private void setVersion(double version) {
		this.version = String.valueOf(version);
		updateMainString();
	}

    @Override
    public void update(Observable o, Object arg) {
        super.update(o, arg);

        setNumberOfObjects(null);
        setDateRange();

        // Data grabbed
        if (arg != null && arg instanceof Integer && ((Integer) arg) >= 0) {
            this.currentState = EAppState.GRABBED_DATA;

            IncidentsHolderModel holder = (IncidentsHolderModel) o;
            setNumberOfObjects(holder.getIncidentModelsInRange().size());

            updateTimePassed((int) arg);
        }
        // Data is being grabbed at the moment
        else if (arg != null && arg instanceof Integer && ((Integer) arg) == -1) {
            this.currentState = EAppState.CURRENTLY_GRABBING;
            updateTimePassed(null);
        }
        // Invalid range
        else if (arg != null && arg instanceof Integer && ((Integer) arg) == -2) {
            this.currentState = EAppState.INVALID_RANGE;
            updateMainString();
        }
    }

    private void setNumberOfObjects(Integer numberOfObjects) {
        this.numberOfObjects = numberOfObjects;
    }

    private enum EAppState {
	    GRABBED_DATA,
        CURRENTLY_GRABBING,
        INVALID_RANGE
    }
}
