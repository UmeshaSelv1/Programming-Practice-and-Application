package requirement1.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.TreeMap;

import javax.swing.SwingWorker;

import api.ripley.Incident;
import api.ripley.Ripley;

/**
 * Holds all the incident models. Connects to the DB to get new Incidents but also provides caching for the already extracted years.
 */
public class IncidentsHolderModel extends Observable {
	
	private TreeMap<Integer, List<IncidentModel>> yearsToIncidents;
	private Ripley ripley;
	private Integer yearFrom;
	private Integer yearTo;

    /**
     * Creates a IncidentsHolderModel instance
     */
	public IncidentsHolderModel() {
		this.yearsToIncidents = new TreeMap<Integer, List<IncidentModel>>();
		this.ripley = API.getRipleyInstance();
		yearFrom = null;
		yearTo = null;
	}

    /**
     * Retrieves all IncidentModel objects in the current time range
     * @return all IncidentModel objects in the current time range, null if date range is invalid
     */
	public List<IncidentModel> getIncidentModelsInRange() {

	    if (yearFrom > yearTo) {
	        return null;
        }
		
		List<IncidentModel> allIncidents = new LinkedList<>();
		for (int i = yearFrom; i <= yearTo; i++) {
			Collection<IncidentModel> values = yearsToIncidents.get(i);
			allIncidents.addAll(values);
		}
		
		return allIncidents;
	}

    /**
     * Sets the incidents in the current time range. Uses both caching and API calls to reduce both API calls and size of data received
     */
	private void setIncidentModelsInRange() {
		
		// Only 1 or neither is set
		if (yearFrom == null || yearTo == null) {
			return;
		}
		
		ArrayList<IncidentModel> modelsToReturn = new ArrayList<>();
		
		// yearFrom is higher than yearTo
		if (yearFrom > yearTo) {
			return;
		}
		new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {

			    // get the span of years that is not cached and call the API for them
				Integer lastSpanStart = null;
				int lastSpanCount = 0;

				System.out.println(">> Start grabbing data");

				long oldTime = System.currentTimeMillis();

				for (int i = yearFrom; i <= yearTo; i++) {
					if (yearsToIncidents.containsKey(i)) {
						if (lastSpanStart == null) {
							modelsToReturn.addAll(yearsToIncidents.get(i));
						}
						else {
							// API call is here
							ArrayList<Incident> incidents = grabData(lastSpanStart, lastSpanStart + lastSpanCount + 1);
							lastSpanStart = null;
							lastSpanCount = 0;
							
							// Cache.....
							List<IncidentModel> modelsAdded = addIncidents(incidents);

							modelsToReturn.addAll(modelsAdded);
						}
					}
					else {
						if (lastSpanStart == null) {
							lastSpanStart = i;
						}
						else {
							lastSpanCount++;
						}
					}
					
					// !!! if no incidents are found we need to cache empty list so we don't call the API again
					if (!yearsToIncidents.containsKey(i)) {
						yearsToIncidents.put(i, new ArrayList<>());
					}
				}

				// if nothing is cached yet...
				if (lastSpanStart != null) {
					ArrayList<Incident> incidents = grabData(lastSpanStart, lastSpanStart + lastSpanCount);
					List<IncidentModel> modelsAdded = addIncidents(incidents);
					modelsToReturn.addAll(modelsAdded);
				}

				System.out.println(">> End grabbing data [" + modelsToReturn.size() + "] models found");

				long newTime = System.currentTimeMillis();
				Integer timePassedSeconds = (int) ((newTime - oldTime) / 1000);
				
				System.out.println("> Data grabbed, [" + modelsToReturn.size() + "] objects found, notifying observers... ");

				// Notify observers with new time
				setChanged();
				notifyObservers(timePassedSeconds);

				return null;
			}
			
		}.execute();
	}

	/**
	 * Notifies oservers for changed year and grabs the new data (either from cache, from API or a combination)
	 */
	private void notifyAndSetData() {


        if (yearFrom > yearTo) {
            System.out.println("> Years changed but invalid range, notifying observers with arg Integer[-2]...");
            setChanged();
            notifyObservers(-2);
            return;
        }
		
		System.out.println("> Years changed, notifying observers with arg Integer[-1]...");
		setChanged();
		notifyObservers(-1);
		
		setIncidentModelsInRange();
	}

	/**
	 * Sets the starting year
	 */
	public void setYearFrom(int yearFrom) {
		this.yearFrom = yearFrom;
		notifyAndSetData();
	}

	/**
	 * Sets the end year
	 */
	public void setYearTo(int yearTo) {
		this.yearTo = yearTo;
		notifyAndSetData();
	}
	
	/**
	 * Calls addIncident for all incidents
	 */
	private List<IncidentModel> addIncidents(List<Incident> incidents) {
		List<IncidentModel> models = new ArrayList<>();
		for(Incident incident : incidents) {
			IncidentModel addIncident = addIncident(incident);
			models.add(addIncident);
		}
		return models;
	}

	
	/**
	 * Adds an incident to the caching data structure
	 */
	private IncidentModel addIncident(Incident incident) {
		String dateAndTime = incident.getDateAndTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date date = df.parse(dateAndTime);
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy");
			int year = Integer.parseInt(simpleDF.format(date));
			
			// put array list to year to cache
			if (!yearsToIncidents.containsKey(year)) {
				yearsToIncidents.put(year, new ArrayList<IncidentModel>());
			}
			// add new incident model
			IncidentModel newIncidentModel = new IncidentModel(incident);
			yearsToIncidents.get(year).add(newIncidentModel);
			
			return newIncidentModel;
		} catch (ParseException e) {
			System.out.println("No date time");
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Calls the Ripley API to get data for given years
	 */
	private ArrayList<Incident> grabData(int startYear, int finalYear) {
		System.out.println("API called for " + startYear + "-" + finalYear);
		return ripley.getIncidentsInRange(startYear + "-01-01 00:00:00", finalYear + "-12-31 23:59:59");  
	}

    /**
     * Returns current user-selected year-from
     */
	public Integer getYearFrom() {
		return this.yearFrom;
	}


    /**
     * Returns current user-selected year-to
     */
	public Integer getYearTo() {
		return this.yearTo;
	}

}
