package requirement1.controllers;

import api.ripley.Incident;
import requirement1.models.IncidentModel;
import requirement1.models.IncidentsHolderModel;
import requirement1.models.StatisticsHolder;
import requirement1.models.EState;

import java.text.DecimalFormat;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Controller for the statistics, view uses this to update the models and view models
 */
public class StatisticsUpdater {

    private static Month[] summerMonths = {Month.JUNE, Month.JULY, Month.AUGUST};
    private static StatisticsUpdater instance;

    private StatisticsUpdater(){};

    public static StatisticsUpdater getInstance() {
        if (instance == null) {
            instance = new StatisticsUpdater();
        }

        return instance;
    }

    /**
     * Updates all statistics' models
     * @param holder IncidentsHolderModel instance
     */
    public void updateAllModels(IncidentsHolderModel holder) {
        List<IncidentModel> incidents = holder.getIncidentModelsInRange();

        // don't update anything if we don't have appropriate data
        if (incidents == null) {
            return;
        }

        // compulsory
        updateNumberOfHoaxes(incidents);
        updateIncidentsWithoutState(incidents);
        updateLikeliestState(incidents);
        updateNumberOfUfoSightingsYoutube(incidents, holder.getYearFrom(), holder.getYearTo());

        // optional
        updateNotHavingShapeCount(incidents);
        updateYearBefore2010Count(incidents);
        updateHappenedInSummerInRelationToAll(incidents);
        updateCountOfLongerThanOneHour(incidents);
    }

    /**
     * Updates number of hoaxes
     */
    private void updateNumberOfHoaxes(List<IncidentModel> incidents) {
        int numberOfHoaxes = 0;
        for ( IncidentModel incident : incidents )
        {
            if (incident.isHoax()) {
                numberOfHoaxes++;
            }
        }

        StatisticsHolder.getInstance().updateStatisticsFor(StatisticsHolder.EStatistics.HOAXES, numberOfHoaxes);
    }

    /**
     * Updates number of incidents that do not have state
     */
    private void updateIncidentsWithoutState(List<IncidentModel> incidents) {
        int incidentsWithoutState = 0;
        for ( IncidentModel incident : incidents )
        {
            if (incident.getEState() == null) {
                incidentsWithoutState++;
            }
        }

        StatisticsHolder.getInstance().updateStatisticsFor(StatisticsHolder.EStatistics.NO_STATE, incidentsWithoutState);
    }

    /**
     * Updates likeliest state to have new alien
     */
    private void updateLikeliestState(List<IncidentModel> incidents) {

        // map states to how many times they are have occurred
        HashMap<EState, Integer> stateToOccurrenceCount = new HashMap<>();
        for (IncidentModel incident : incidents) {
            if (!stateToOccurrenceCount.containsKey(incident.getEState())) {
                stateToOccurrenceCount.put(incident.getEState(), 1);
            } else {
                Integer oldVal = stateToOccurrenceCount.get(incident.getEState());
                stateToOccurrenceCount.put(incident.getEState(), oldVal + 1);
            }
        }

        // get the highest occurrence
        System.out.println("likeliest state start");
        EState likeliestState = incidents.size() == 0 ? null : Collections.max(stateToOccurrenceCount.entrySet(), (e1, e2) -> e1.getValue() - e2.getValue()).getKey();

        System.out.println("likeliest state start");
        StatisticsHolder.getInstance().updateStatisticsFor(StatisticsHolder.EStatistics.LIKELIEST_STATE, likeliestState);
    }

    /**
     * Updates number of sightings on youtube for the current time period
     */
    private void updateNumberOfUfoSightingsYoutube(List<IncidentModel> incidents, Integer yearFrom, Integer yearTo) {
        Integer numberOfUfoSigthingsYoutube = RestRequestSender.getNumberOfUFOSigthingsYoutube(yearFrom, yearTo);
        StatisticsHolder.getInstance().updateStatisticsFor(StatisticsHolder.EStatistics.YOUTUBE, numberOfUfoSigthingsYoutube);
    }

    /**
     * Updates number of sightings that do not have a shape defined
     */
    private void updateNotHavingShapeCount(List<IncidentModel> incidents) {

        // check if the shape is one of those
        String[] elementsToCheck = new String[] {
                "not specified", "not_specified",
                "unknown", "none", "null",
                "other", "else"
        };

        // update value
        int notHavingShapeCount = 0;

        for ( Incident incident : incidents )
        {
            String lowerCaseShape = incident.getShape().toLowerCase();
            boolean shapeNotSpecified = Arrays.stream(elementsToCheck).parallel().anyMatch(lowerCaseShape::contains);
            if (shapeNotSpecified) {
                notHavingShapeCount++;
            }
        }

        StatisticsHolder.getInstance().updateStatisticsFor(StatisticsHolder.EStatistics.NO_SHAPE, notHavingShapeCount);
    }

    /**
     * Updates number incidents happened before 2010
     */
    private void updateYearBefore2010Count(List<IncidentModel> incidents) {
        int before2010Count = 0;
        for ( IncidentModel incident : incidents )
        {
            if ( incident.getLocalDate().getYear() < 2010 )
            {
                before2010Count++;
            }
        }

        StatisticsHolder.getInstance().updateStatisticsFor(StatisticsHolder.EStatistics.BEFORE_2010, before2010Count);
    }

    /**
     * Updates statistics for all incidents happened in the summer in relation to all incidents, info in percentage
     */
    private void updateHappenedInSummerInRelationToAll(List<IncidentModel> incidents) {
        int inSummer = 0;

        for ( IncidentModel incident : incidents )
        {
            boolean isInSummer = Arrays.stream(summerMonths).parallel().anyMatch(incident.getLocalDate().getMonth()::equals);
            if (isInSummer) {
                inSummer++;
            }
        }

        Double happenedInSummerInRelationToAll = incidents.size() != 0 ? ((double)inSummer) / ((double)incidents.size()) : 0;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        StatisticsHolder.getInstance().updateStatisticsFor(StatisticsHolder.EStatistics.SUMMER_TO_ALL, decimalFormat.format(happenedInSummerInRelationToAll) + "%");
    }

    /**
     * Updates number of incidents which duration is equal or more than 1 hour
     */
    private void updateCountOfLongerThanOneHour(List<IncidentModel> incidents) {
        int durationLongerThanHourCount = 0;
        for ( IncidentModel incident : incidents ) {
            if (incident.getDurationMinutes() >= 60) {
                durationLongerThanHourCount++;
            }
        }

        StatisticsHolder.getInstance().updateStatisticsFor(StatisticsHolder.EStatistics.DURATION_MORE_THAN_HOUR, durationLongerThanHourCount);
    }
}
