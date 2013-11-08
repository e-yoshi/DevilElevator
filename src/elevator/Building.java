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

	public Building(int numFloors, int numElevators) {
		super(numFloors, numElevators);
		elevator = new Elevator(numFloors, 0, 0);
		Thread t = new Thread(elevator, "Elevator");
		peopleMap = new HashMap<Integer, ArrayList<Passenger>>();
		t.start();
	}

	@Override
	public AbstractElevator CallUp(int fromFloor) {
	    
	    synchronized (elevator) {
	        if(elevator.isIdle()) {
	            
                    //elevator.notify();
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
	            
	            //elevator.notify();
	        }
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
	
	public void arriveAtFloor(int floor) {
	    if(peopleMap.get(floor).isEmpty()) return;
	    for (Passenger p : peopleMap.get(floor)) {
	        synchronized (this) {
	        p.notify();
	        }
	    }
	}

}
