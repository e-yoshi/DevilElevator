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
	private ArrayList<Elevator> elevators;
	private int maxOccupancy;

	public Building(int numFloors, int numElevators) {
		super(numFloors, numElevators);
		maxOccupancy = 5;
		elevator = new Elevator(numFloors, 0, maxOccupancy);
		for(int i = 0; i<numElevators; i++) {
		    Elevator elevatorService = new Elevator(numFloors, i, maxOccupancy);
		    Thread t = new Thread(elevatorService, "Elevator "+i);
		    t.start();
		}
	}

	@Override
	public AbstractElevator CallUp(int fromFloor) {
	    for(Elevator elevator : elevators) {
	        synchronized (elevator) {
	            if(elevator.getNumberOfPassengers() == maxOccupancy)
	                continue;
	            
	            if(elevator.isIdle()) { 
                        elevator.notify();
                    }
	            
	            if ((elevator.isAscending() && elevator.getCurrentFloor() <= fromFloor) ||
	                    (!elevator.isAscending() && fromFloor == 0)) {
	                elevator.callToFloor(fromFloor);
	                return elevator;
	            }
	        }
	    }
	    return null;
	}

	@Override
	public AbstractElevator CallDown(int fromFloor) {
	    
	    for(Elevator elevator : elevators) {
                synchronized (elevator) {
                    if(elevator.getNumberOfPassengers() == maxOccupancy)
                        continue;
                    
                    if(elevator.isIdle()) { 
                        elevator.notify();
                    }
                    
                    if ((!elevator.isAscending() && elevator.getCurrentFloor() >= fromFloor) ||
                            (elevator.isAscending() && fromFloor == numFloors-1)) {
                        elevator.callToFloor(fromFloor);
                        return elevator;
                    }
                }
            }
            return null;
	}
}
