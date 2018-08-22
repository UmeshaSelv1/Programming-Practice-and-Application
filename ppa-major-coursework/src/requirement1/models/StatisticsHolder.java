package requirement1.models;

import java.io.*;
import java.util.*;

/**
 * A model that holds all of the statistics. It's singleton, so it's acquired by the static StatisticsHolder.getInstance() method
 */
public class StatisticsHolder implements Serializable {

    private static final long serialVersionUID = -2161288960936309965L;
    private HashMap<EStatistics, SingleStatisticViewModel> allStatistics;
    private LinkedList<SingleStatisticViewModel> availableStatistics;
    private LinkedList<SingleStatisticViewModel> currentStatistics;
    private static StatisticsHolder instance;

    /**
     * Singleton, returns instance of this holder, holds all statistics values
     */
    public static StatisticsHolder getInstance() {
        if (instance == null) {
            instance = new StatisticsHolder();
        }

        return instance;
    }

    /**
     * Private constructor, singleton
     */
    private StatisticsHolder() {
        StatisticsHolder dummyHolder = null;
        try {
            FileInputStream fileIn = new FileInputStream("statistics.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            dummyHolder = (StatisticsHolder) in.readObject();
            in.close();
            fileIn.close();

            this.availableStatistics = dummyHolder.availableStatistics;
            this.currentStatistics = dummyHolder.currentStatistics;
            this.allStatistics = dummyHolder.allStatistics;
            return;
        }catch(IOException i) {
            System.out.println("No deserialization file found");
        }catch(ClassNotFoundException c) {
            System.out.println("StatisticsHolder class not found");
            c.printStackTrace();
        }
        catch(Exception f) {
            // do nothing
            f.printStackTrace();
        }

        availableStatistics = new LinkedList<>();
        currentStatistics = new LinkedList<>();
        allStatistics = new HashMap<>();
        for (EStatistics value : EStatistics.values()) {
            SingleStatisticViewModel model = new SingleStatisticViewModel(value);
            allStatistics.put(value, model);
            availableStatistics.add(model);
        }
    }

    /**
     * Updates a specified statistic by provided value.
     * @param statistics the statistic to be updated
     * @param newValue new value
     */
    public void updateStatisticsFor(EStatistics statistics, Object newValue) {
        allStatistics.get(statistics).setValue(newValue);
    }

    /**
     * Returns the next statistic from the available ones.
     * @param toBecomeAvailable the statistic that disappears. Could be null, in which case nothing disappears.
     * @return a SingleStatisticViewModel object, the next statistic
     */
    public SingleStatisticViewModel getNextAvailableInsteadOf(SingleStatisticViewModel toBecomeAvailable) {
        SingleStatisticViewModel polled = availableStatistics.pollLast();
        currentStatistics.add(polled);
        if (toBecomeAvailable != null && currentStatistics.contains(toBecomeAvailable)) {
            availableStatistics.addFirst(toBecomeAvailable);
            currentStatistics.remove(toBecomeAvailable);
        }
        serialize();
        return polled;
    }

    /**
     * Returns the previous statistic from the available ones.
     * @param toBecomeAvailable the statistic that disappears. Could be null, in which case nothing disappears.
     * @return a SingleStatisticViewModel object, the previous statistic
     */
    public SingleStatisticViewModel getPreviousAvailableInsteadOf(SingleStatisticViewModel toBecomeAvailable) {
        SingleStatisticViewModel polled = availableStatistics.pollFirst();
        currentStatistics.add(polled);
        if (toBecomeAvailable != null && currentStatistics.contains(toBecomeAvailable)) {
            availableStatistics.addLast(toBecomeAvailable);
            currentStatistics.remove(toBecomeAvailable);
        }
        serialize();
        return polled;
    }

    public LinkedList<SingleStatisticViewModel> getSerialized() {
        if (currentStatistics != null && currentStatistics.size() > 0) {
            return currentStatistics;
        }
        return null;
    }

    private void serialize() {
        try {
            FileOutputStream fileOut = new FileOutputStream("statistics.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.printf("Statistics serialized. Path [statistics.ser]");
        }catch(IOException i) {
            i.printStackTrace();
            System.out.println();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("availableStatistics: ").append(availableStatistics).append("\n");
        sb.append("currentStatistics: ").append(currentStatistics).append("\n\n");
        return sb.toString();
    }

    /**
     * This enum contains all the statistics that we currently have.
     * If another statistic is to be added - all should be done is to put a new value here, with its corresponding title.
     */
    public enum EStatistics {
        HOAXES("Number of hoaxes"),
        NO_STATE("Incidents without state"),
        LIKELIEST_STATE("Likeliest state for next alien"),
        YOUTUBE("Number of sightings in youtube"),
        NO_SHAPE("Number of sightings with no shape"),
        BEFORE_2010("Number of sightings before 2010"),
        SUMMER_TO_ALL("In summer in relation to all"),
        DURATION_MORE_THAN_HOUR("With duration more than an hour");

        private String title;

        EStatistics(String title) {
            this.title = title;
        }

        public String getTitle() {
            return this.title;
        }

    }
}
