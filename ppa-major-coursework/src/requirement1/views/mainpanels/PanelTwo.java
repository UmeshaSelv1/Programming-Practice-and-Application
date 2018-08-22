
package requirement1.views.mainpanels;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.*;

import requirement1.models.IncidentModel;
import requirement1.models.IncidentsHolderModel;
import requirement1.models.EState;


public class PanelTwo extends APanel {

	private static final long serialVersionUID = 1L;

	private BufferedImage mapPanel;
	private HashMap<EState, List<IncidentModel>> states;
	private BufferedImage image;

	public PanelTwo(){

		// alien image
		InputStream alienIS = PanelTwo.class.getResourceAsStream("/images/alien.png");
		try {
			image = ImageIO.read(alienIS);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("NO ALIEN IMAGE FOUND");
		}

		// map image
		InputStream mapIS = PanelTwo.class.getResourceAsStream("/images/map.gif");
		try{
			mapPanel = ImageIO.read(mapIS);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("NO MAP IMAGE FOUND");

		}

		setLayout(null);
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draws background
		g.drawImage(mapPanel, 17, 0, this);

		// Draws all aliens
		if (states == null) {
			return;
		}

		for (Entry<EState, List<IncidentModel>> stateEntry : states.entrySet()) {
			Integer numberOfIncidents = stateEntry.getValue().size();

			int size = getSizeFromIncidentsCount(numberOfIncidents);

			g.drawImage(image, stateEntry.getKey().getX() - size/2, stateEntry.getKey().getY() - size/2, size, size, this);
		}


	}

	private int getSizeFromIncidentsCount(int numberOfIncidents) {
		int size;
		if (numberOfIncidents == 0) {
			size = 0;
		}else if (numberOfIncidents < 50){
			size = 17;
		}else if (numberOfIncidents < 100){
			size = 26;
		}else if (numberOfIncidents < 250){
			size = 35;
		}else if (numberOfIncidents < 500){
			size = 44;
		}else if (numberOfIncidents < 1000){
			size = 52;
		}else if (numberOfIncidents < 2500){
			size = 60;
		}else if (numberOfIncidents < 5000){
			size = 65;
		}else{
			size = 70;
		} 

		return size;
	}


	private void buildStatesToIncidents(List<IncidentModel> allIncidentsInRange) {
		// data
		states = new HashMap<EState, List<IncidentModel>>();
		for (EState stateEntry : EState.values()) {
			List<IncidentModel> incidentsForState = new LinkedList<>();
			for (IncidentModel model : allIncidentsInRange) {
				if (stateEntry == model.getEState()) {
					incidentsForState.add(model);
				}
			}

			states.put(stateEntry, incidentsForState);
		}
	}




	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);

		if(arg!= null && arg instanceof Integer && ((Integer)arg) == -1) {

			return;
		}

		IncidentsHolderModel holder = (IncidentsHolderModel) o;
		List<IncidentModel> allIncidents = holder.getIncidentModelsInRange();
		if (allIncidents == null) {
			return;
		}
		buildStatesToIncidents(allIncidents);
		createAlienButtons(allIncidents);

		revalidate();
		repaint();
	}


	private void createAlienButtons(List<IncidentModel> allIncidents) {

		for (Entry<EState, List<IncidentModel>> stateEntry : states.entrySet()) {
			EState state = stateEntry.getKey();
			int size = getSizeFromIncidentsCount(stateEntry.getValue().size());

			JLabel alienButtonAsLabel = new JLabel("");
			alienButtonAsLabel.setBounds(state.getX() - size/2, state.getY() - size/2, size, size);
			alienButtonAsLabel.repaint();

			alienButtonAsLabel.addMouseListener(new MouseListener() {
				
				private List<IncidentModel> getSortedByShape(List<IncidentModel> value) {
					List<IncidentModel> modelsSortedByShape = new ArrayList<>(value);
                    modelsSortedByShape.sort(new Comparator<IncidentModel>(){

						@Override
						public int compare(IncidentModel left, IncidentModel right) {
							return left.getShape().compareTo(right.getShape());
						}
					});
					
					return modelsSortedByShape;
				}

				private List<IncidentModel> getSortedByPosted(List<IncidentModel> value) {
					List<IncidentModel> modelsSortedByPosted = new ArrayList<>(value);
                    modelsSortedByPosted.sort(new Comparator<IncidentModel>(){

						@Override
						public int compare(IncidentModel left, IncidentModel right) {
							return left.getPosted().compareTo(right.getPosted());
						}
					});
					
					return modelsSortedByPosted;
				}
				
				private List<IncidentModel> getSortedByDuration(List<IncidentModel> value) {
					List<IncidentModel> modelsSortedByDuration = new ArrayList<>(value);
                    modelsSortedByDuration.sort(new Comparator<IncidentModel>(){

						@Override
						public int compare(IncidentModel left, IncidentModel right) {
							return left.getDurationMinutes().compareTo(right.getDurationMinutes());
						}
					});
					
					
					return modelsSortedByDuration;
				}

				@Override
				public void mouseClicked(MouseEvent e) {

                    JDialog dialog = new JDialog();
                    dialog.setTitle(state.getStateName() + " - " + stateEntry.getValue().size() + " sightings");
                    dialog.setSize(830, 200);
                    JComboBox<SortingListModel> comboBox = new JComboBox<>();

                    comboBox.addItem(new SortingListModel(null));

                    comboBox.addItem(new SortingListModel(ESortings.Date));
                    comboBox.addItem(new SortingListModel(ESortings.City));
                    comboBox.addItem(new SortingListModel(ESortings.Shape));
                    comboBox.addItem(new SortingListModel(ESortings.Duration));
                    comboBox.addItem(new SortingListModel(ESortings.Posted));

                    DefaultListModel<IncidentModel> dlm = new DefaultListModel<>();

                    // default sorting
                    for (IncidentModel element : stateEntry.getValue()) {
                        dlm.addElement(element);
                    }

                    // action listener for sorting
                    comboBox.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {

                            dlm.removeAllElements();

                            ESortings sorting = (ESortings) ((SortingListModel)comboBox.getSelectedItem()).getSorting();
                            List<IncidentModel> currentSort = null;
                            switch(sorting) {
                                // Duration
                                case Duration :
                                    System.out.println("Sort by duration");
                                    currentSort = new ArrayList<>(getSortedByDuration(stateEntry.getValue()));
                                    break;
                                // Date
                                case Date :
                                    System.out.println("Sort by date");
                                    currentSort = new ArrayList<>(getSortedByDate(stateEntry.getValue()));
                                    break;
                                case City :
                                    System.out.println("Sort by city");
                                    currentSort = new ArrayList<>(getSortedByCity(stateEntry.getValue()));
                                    break;
                                case Shape :
                                    System.out.println("Sort by shape");
                                    currentSort = new ArrayList<>(getSortedByShape(stateEntry.getValue()));
                                    break;
                                case Posted :
                                    System.out.println("Sort by posted");
                                    currentSort = new ArrayList<>(getSortedByPosted(stateEntry.getValue()));
                                    break;

                                default:
                                    System.out.println("Sort default");
                                    currentSort = stateEntry.getValue();
                                    break;
                            }

                            // Add everything to the DLM of the list
                            for (IncidentModel incident : currentSort) {
                                dlm.addElement(incident);
                            }
                        }
                    });

                    JList<IncidentModel> incidentsForStateList = new JList<>(dlm);

                    JScrollPane scroll = new JScrollPane(incidentsForStateList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                    scroll.setViewportView(incidentsForStateList);
                    scroll.setAutoscrolls(true);

                    dialog.add(scroll);

                    dialog.add(comboBox, BorderLayout.NORTH);

                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);

                    incidentsForStateList.addMouseListener(new SummaryMouseListener(incidentsForStateList));

                }

                /**
                 * This method returns a sorted copy of the list by duration
                 */
                private List<IncidentModel> getSortedByDate(List<IncidentModel> value) {
                    List<IncidentModel> modelsSortedByDate = new ArrayList<>(value);
                    modelsSortedByDate.sort(new Comparator<IncidentModel>(){

                        @Override
                        public int compare(IncidentModel left, IncidentModel right) {
                            return left.getLocalDate().compareTo(right.getLocalDate());
                        }
                    });

                    return modelsSortedByDate;
                }

                /**
                 * This method returns a sorted copy of the list by duration
                 */
                private List<IncidentModel> getSortedByCity(List<IncidentModel> value) {
                    List<IncidentModel> modelsSortedByCity = new ArrayList<>(value);
                    modelsSortedByCity.sort(new Comparator<IncidentModel>(){

                        @Override
                        public int compare(IncidentModel left, IncidentModel right) {
                            return left.getCity().toLowerCase().compareTo(right.getCity().toLowerCase());
                        }
                    });

                    return modelsSortedByCity;
                }

                // not needed methods
                @Override
                public void mouseExited(MouseEvent e) { }

                @Override
                public void mouseEntered(MouseEvent e) { }

                @Override
                public void mouseReleased(MouseEvent e) { }

                @Override
                public void mousePressed(MouseEvent e) { }
			});

			add(alienButtonAsLabel);

		}
	}


	private class SummaryMouseListener implements MouseListener {

		private JList<IncidentModel> incidentsForStateList;

		SummaryMouseListener(JList<IncidentModel> models) {
			incidentsForStateList = models;
		}

		@Override
		public void mouseClicked(MouseEvent e) { }

		@Override
		public void mousePressed(MouseEvent e) {

			int index = incidentsForStateList.locationToIndex(e.getPoint());
			if (incidentsForStateList.getModel().getSize() < index) {
				return;
			}

			IncidentModel currentIncident = incidentsForStateList.getModel().getElementAt(index);

			JDialog incidentSummaryDialog = new JDialog();
			incidentSummaryDialog.setTitle("Message");
			Dimension dimension = new Dimension(500, 230);
			incidentSummaryDialog.setSize(dimension);
			incidentSummaryDialog.setMinimumSize(dimension);
			incidentSummaryDialog.setMaximumSize(dimension);
			incidentSummaryDialog.setPreferredSize(dimension);

			JTextArea summary = new JTextArea(currentIncident.getSummary());
			summary.setSize(new Dimension(50, 50));
			summary.setBackground(null);
			
			summary.setLineWrap( true );
			summary.setWrapStyleWord( true );

			summary.setSize(90, 40);
		
			summary.setSize( summary.getPreferredSize() );

			summary.setEditable(false);

			JButton okButton = new JButton("OK");
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new BorderLayout());

			buttonPanel.add(okButton, BorderLayout.EAST);

			InputStream isAlienG = PanelTwo.class.getResourceAsStream("/images/aleinG.png");
			ImageIcon alienIcon = null;
			try {
				alienIcon = new ImageIcon(ImageIO.read(isAlienG));
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			JLabel alienLabel = new JLabel(alienIcon);

			JPanel alienPanel = new JPanel();
			alienPanel.setLayout(new BorderLayout());

			alienPanel.add(alienLabel, BorderLayout.NORTH);


			incidentSummaryDialog.add(summary, BorderLayout.CENTER);
			incidentSummaryDialog.add(buttonPanel, BorderLayout.SOUTH);
			incidentSummaryDialog.add(alienPanel, BorderLayout.WEST);
			incidentSummaryDialog.setLocationRelativeTo(null);

			incidentSummaryDialog.setVisible(true);

			okButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) { }

				@Override
				public void mousePressed(MouseEvent e) { }

				@Override
				public void mouseExited(MouseEvent e) { }

				@Override
				public void mouseEntered(MouseEvent e) { }

				@Override
				public void mouseClicked(MouseEvent e) {
					incidentSummaryDialog.setVisible(false);
				}
			});
		}

		@Override
		public void mouseReleased(MouseEvent e) { }

		@Override
		public void mouseEntered(MouseEvent e) { }

		@Override
		public void mouseExited(MouseEvent e) { }
	}
	
	private enum ESortings {
		Date,
		City,
		Shape,
		Duration,
		Posted,
	}
	
	public class SortingListModel {

		private ESortings sorting;
		private String display;
		
		SortingListModel(ESortings sorting) {
			this.sorting = sorting;
			this.display = sorting == null ? "--" : sorting.name(); 
		}
		
		ESortings getSorting() {
			return sorting;
		}
		
		@Override
		public String toString() {
			return display;
		}
	}
}

