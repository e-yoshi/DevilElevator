package elevator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import elevator.Elevator;
import elevator.Passenger;
import api.AbstractBuilding;
import api.AbstractElevator;

public class Building extends AbstractBuilding {
	private Elevator elevator;
	private Map<Integer, ArrayList<Passenger>> peopleMap;
	private int maxOccupancy;

	public Building(int numFloors, int numElevators) {
		super(numFloors, numElevators);
		maxOccupancy = 5;
		elevator = new Elevator(numFloors, 0, maxOccupancy);
		Thread t = new Thread(elevator, "Elevator");
		t.start();
	}

	@Override
	public AbstractElevator CallUp(int fromFloor) {
	    
	    synchronized (elevator) {
	        if(elevator.isIdle()) {
	            
                    elevator.notify();
                }
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
	        if(elevator.isIdle()) {         
	            elevator.notify();
	        }
	        if(elevator.getNumberOfPassengers() == maxOccupancy)
	            return null;
	        
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
