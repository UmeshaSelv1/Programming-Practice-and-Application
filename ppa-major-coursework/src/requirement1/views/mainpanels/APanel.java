package requirement1.views.mainpanels;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import requirement1.models.IncidentsHolderModel;

public abstract class APanel extends JPanel implements Observer, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4353719858788939130L;
	
	protected Integer yearFrom;
	protected Integer yearTo;
	
	@Override
	public void update(Observable o, Object arg) {
		IncidentsHolderModel holder = (IncidentsHolderModel) o;
		this.yearFrom = holder.getYearFrom();
		this.yearTo = holder.getYearTo();

        System.out.println();
        System.out.println("* " + getClass().getSimpleName() + ": Update executing...");
	}


}
