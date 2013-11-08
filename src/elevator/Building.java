package src.elevator;



import elevator.Elevator;
import api.AbstractBuilding;
import api.AbstractElevator;

public class Building extends AbstractBuilding {
	private Elevator elevator;

	public Building(int numFloors, int numElevators) {
		super(numFloors, numElevators);
		elevator = new Elevator(numFloors, 0, 0);
	}

	@Override
	public AbstractElevator CallUp(int fromFloor) {
	    synchronized (elevator) {
	        elevator.callToFloor(fromFloor);
	        return elevator;
	    }

	    // This if statement will be useful for multiple elevators
	    // Since we only have one, we return the only one
	    /*
	     * if (elevator.isAscending() && elevator.getCurrentFloor() <=
	     * fromFloor) {
	     * 
	     * } return null;
	     */
	}

	@Override
	public AbstractElevator CallDown(int fromFloor) {
	    synchronized(elevator) {	
	        elevator.callToFloor(fromFloor);
	        return elevator;
	    }

	    // This if statement will be useful for multiple elevators
	    // Since we only have one, we return the only one
	    /*
	     * if (!elevator.isAscending() && elevator.getCurrentFloor() >=
	     * fromFloor) {
	     * 
	     * } return null;
	     */
	}

}
