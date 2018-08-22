package requirement1.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import requirement1.controllers.YearsController;

class TopPanel extends JPanel {

	private static final long serialVersionUID = 6721861832251610862L;
    private JComboBox<String> fromCB;
	private JComboBox<String> toCB;
	private YearsController controller;
	
	TopPanel(YearsController controller) {
        JPanel container = new JPanel();
		this.setLayout(new BorderLayout());

        this.controller = controller;

        List<String> years = new ArrayList<>();
		years.add(0, "--");
		for (Integer year : this.controller.getAllYears()) {
			years.add(String.valueOf(year));
		}
		
		// from cb
		
		fromCB = new JComboBox<>(years.toArray(new String[0]));
		fromCB.setSelectedIndex(0);
		fromCB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				    int yearFrom = Integer.parseInt((String)fromCB.getSelectedItem());
					TopPanel.this.controller.setYearFrom(yearFrom);
				}
				catch(Exception ex) {
					// do nothing selected something like --
				}
			}
		});
		
		container.add(new JLabel("From:"));
		container.add(fromCB);

		// to cb
		
		container.add(new JLabel("To:"));
		toCB = new JComboBox<>(years.toArray(new String[0]));
		toCB.setSelectedIndex(0);
		toCB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
                    int yearTo = Integer.parseInt((String) toCB.getSelectedItem());
                    TopPanel.this.controller.setYearTo(yearTo);
				}
				catch(Exception ex) {
					// do nothing, selected something like --
				}
			}
		});
		container.add(toCB);
		
		// add the container

		this.add(container, BorderLayout.EAST);
		
	}

}
