package elevator;

import java.util.ArrayList;
import elevator.Elevator;
import api.AbstractBuilding;
import api.AbstractElevator;

public class Building extends AbstractBuilding {
	//private Elevator elevator;
	private ArrayList<Elevator> elevators;
	private int maxOccupancy;
	public int complete = 0;

	public Building(int numFloors, int numElevators) {
		super(numFloors, numElevators);
		maxOccupancy = 5;
		elevators = new ArrayList<Elevator>();

		//elevator = new Elevator(numFloors, 0, maxOccupancy);
		for(int i = 0; i<numElevators; i++) {
		    Elevator elevatorService = new Elevator(numFloors, i, maxOccupancy);
		    elevators.add(elevatorService);
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
	            //TODO make it look good
	            if(elevator.isIdle()) { 
                        elevator.startElevator(fromFloor);
                        return elevator;
                    }
	            if ((elevator.isAscending() && elevator.getCurrentFloor() <= fromFloor)) {     
	                
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
                  //TODO make it look good
                    if(elevator.isIdle()) { 
                        elevator.startElevator(fromFloor);
                        return elevator;
                    }
                    if ((!elevator.isAscending() && elevator.getCurrentFloor() >= fromFloor)) {
                        
                        elevator.callToFloor(fromFloor);
                        
                        return elevator;
                    }
                }
            }
            return null;
	}
}
