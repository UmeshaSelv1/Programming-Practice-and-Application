
package requirement1.views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import requirement1.controllers.YearsController;
import requirement1.models.API;
import requirement1.models.IncidentsHolderModel;
import requirement1.views.mainpanels.APanel;
import requirement1.views.mainpanels.PanelFour;
import requirement1.views.mainpanels.PanelOne;
import requirement1.views.mainpanels.PanelThree;
import requirement1.views.mainpanels.PanelTwo;

public class MainFrame extends JFrame implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2447042670828204898L;
	private TopPanel topPanel;
	private BottomPanel bottomPanel;
	private APanel[] innerPanels;
	private JPanel innerPanelContainer;
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	
	private int currentPage = 0;

	private ActionListener controller;

	public MainFrame(IncidentsHolderModel model, YearsController topController) {
        showAcknowledgementString();

		System.out.println(API.getRipleyInstance().getLastUpdated());
		System.out.println();

		this.setTitle("NUFORC Statistics");
        this.setSize(WIDTH, HEIGHT);
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setLayout(new BorderLayout());
		
		// create the top panel
		topPanel = new TopPanel(topController);
		this.add(topPanel, BorderLayout.NORTH);
		
		// add inner panels
		innerPanels = new APanel[] {
				new PanelOne(),
				new PanelTwo(),
				new PanelThree(),
				new PanelFour()
		};

		innerPanelContainer = new JPanel();
		innerPanelContainer.setLayout(new CardLayout());

        for (int i = 0; i < innerPanels.length; i++) {
            APanel innerPanel = innerPanels[i];
            innerPanelContainer.add(innerPanel, "panel" + String.valueOf(i));
        }
		this.add(innerPanelContainer);

		// create bottom panel
		bottomPanel = new BottomPanel();
		this.add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setAcknowledgementString();

		// take care of frame visibility
		this.setVisible(true);
	}

    private void showAcknowledgementString() {
        String acknowledgementString = API.getRipleyInstance().getAcknowledgementString();
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println(acknowledgementString);
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("\n");
    }

    @Override
    public void update(Observable o, Object arg) {
        // if we are just starting to grab the data - go to first page
        if (arg != null && arg instanceof Integer && ((Integer)arg) >= 0) {
            bottomPanel.enableButtons();
            return;
        }
        currentPage = 0;
        CardLayout cardLayout = (CardLayout)(innerPanelContainer.getLayout());
        cardLayout.show(innerPanelContainer, "panel0");
        bottomPanel.disableButtons();
    }

    public APanel[] getInnerPanels() {
	    return this.innerPanels;
    }
	
	private class BottomPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6576466188173756801L;
		private JButton forward;
		private JButton back;

		BottomPanel() {
			this.setLayout(new BorderLayout());
			
			forward = new JButton(">");
			forward.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (currentPage < innerPanels.length - 1) {
						currentPage++;
						CardLayout cardLayout = (CardLayout)(innerPanelContainer.getLayout());
						cardLayout.next(innerPanelContainer);
						System.out.println("Go forward to " + currentPage);
					}
				}
			});
			forward.setEnabled(false);
			
			back = new JButton("<");
			back.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (currentPage > 0) {
						currentPage--;
						CardLayout cardLayout = (CardLayout)(innerPanelContainer.getLayout());
						cardLayout.previous(innerPanelContainer);
						System.out.println("Go back to " + currentPage);
					}
				}
			});
			back.setEnabled(false);

			this.add(forward, BorderLayout.EAST);
			this.add(back, BorderLayout.WEST);
		}

        public void disableButtons() {
            forward.setEnabled(false);
            back.setEnabled(false);
        }

        public void enableButtons() {
            forward.setEnabled(true);
            back.setEnabled(true);
        }

        public void setAcknowledgementString() {
            String lastReported = API.getRipleyInstance().getLastUpdated();
            JLabel label = new JLabel(lastReported, SwingConstants.CENTER);
            add(label, BorderLayout.CENTER);
        }
    }
}
