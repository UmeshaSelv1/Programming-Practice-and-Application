package requirement1.models;

import requirement1.models.StatisticsHolder.EStatistics;

import javax.swing.*;
import java.awt.*;

/**
 * A view model for a single statistic, holds the title and the value of the statistic
 */
public class SingleStatisticViewModel extends JPanel {

    private EStatistics statistics;
    private JLabel valueLabel;

    /**
     * Creates a view-model for a statistic
     * @param statistics for which statistic that model is responsible
     */
    public SingleStatisticViewModel(EStatistics statistics) {
        this.statistics = statistics;

        this.setLayout(new BorderLayout());
        this.add(new JLabel(statistics.getTitle(), SwingConstants.CENTER), BorderLayout.NORTH);
        valueLabel = new JLabel("-", SwingConstants.CENTER);
        this.add(valueLabel, BorderLayout.CENTER);
    }

    /**
     * Sets the value of the current statistic
     */
    public void setValue(Object newVal) {
        this.remove(valueLabel);
        valueLabel = new JLabel(newVal == null ? "-" : newVal.toString(), SwingConstants.CENTER);
        this.add(valueLabel, BorderLayout.CENTER);
    }

    @Override
    public String toString() {
        return "[" + statistics.name() + "]";
    }
}
