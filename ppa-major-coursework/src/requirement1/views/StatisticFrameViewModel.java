package requirement1.views;

import requirement1.models.SingleStatisticViewModel;
import requirement1.models.StatisticsHolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A view-model for a single statistic view. Holds the buttons for back and forward and the statistic itself.
 */
public class StatisticFrameViewModel extends JPanel{
    private JButton previousBtn;
    private JButton nextBtn;
    private SingleStatisticViewModel currentModel;

    public StatisticFrameViewModel(SingleStatisticViewModel initialModel) {
        this.currentModel = initialModel;

        previousBtn = new JButton("<");
        previousBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(currentModel);
                currentModel = StatisticsHolder.getInstance().getPreviousAvailableInsteadOf(currentModel);
                add(currentModel);
                revalidate();
                repaint();
            }
        });

        nextBtn = new JButton(">");
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(currentModel);
                currentModel = StatisticsHolder.getInstance().getNextAvailableInsteadOf(currentModel);
                add(currentModel);
                revalidate();
                repaint();
            }
        });

        initUI();
    }

    /**
     * Initializer for the UI
     */
    private void initUI() {
        this.setLayout(new BorderLayout());
        this.add(previousBtn, BorderLayout.WEST);
        this.add(nextBtn, BorderLayout.EAST);
        this.add(currentModel);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(2, 2, 2, 2 )));

    }
}
