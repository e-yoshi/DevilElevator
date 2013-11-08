package elevator;

import api.AbstractElevator;

public class Passenger implements Runnable {

	private int id;
	private int destinationFloor;
	private int fromFloor;
	private Building building;

	public Passenger(int id, int from, int dest) {
		this.id = id;
		destinationFloor = dest;
		fromFloor = from;
	}
	
	public Passenger(Building b, int id, int from, int dest) {
	    this(id, from, dest);
            setBuilding(b);
    }

	public void setBuilding(Building b) {
		building = b;
	}

	@Override
	public void run() {
	    AbstractElevator elevator = null;
	    System.out.println("Passenger "+id+" is in floor "+fromFloor+" goes to floor "+destinationFloor);
	    if (fromFloor < destinationFloor) {
	        //In case no match is found
	        System.out.println("Passenger "+id+" calling up");
	        while(elevator == null) {
	            elevator = building.CallUp(fromFloor);
	        }
	        if (elevator == null) {
	            System.out.println("Elevator is null");
	        }
	    } //TODO implement someone that comes and leaves from same floor
	    else if (fromFloor > destinationFloor){
	        while(elevator == null) {
	            elevator = building.CallDown(fromFloor);
	        }
	    }
	    
	    RideElevator(elevator);

	}
	
	private synchronized void RideElevator(AbstractElevator elevator) {

	    //Whats the difference between synchronizing the method or the elevator?
	    while (elevator.getCurrentFloor() != fromFloor) {
	        try {
	            elevator.wait();
	        }
	        catch (InterruptedException e) {
	            // TODO Log error
	            System.out.println("Error waiting for elevator");
	            e.printStackTrace();
	        }
	    }
	    elevator.Enter();
	    System.out.println("Passenger "+id+" entered elevator "+elevator.getId()+" from floor "+fromFloor);
	    //TODO Log passenger entering
	    elevator.RequestFloor(destinationFloor);
	    System.out.println("Passenger "+id+" requested floor "+destinationFloor);
	    //TODO Log floor request
	    while (elevator.getCurrentFloor() != destinationFloor) {
	        try {
	            elevator.wait();
	        } catch (InterruptedException e) {
	            // TODO Write error on log
	            System.out.println("Error waiting in elevator");
	            e.printStackTrace();
	        }
	    }
	    elevator.Exit();
	    System.out.println("Passenger "+id+" exited elevator "+elevator.getId()+" on floor "+elevator.getCurrentFloor());
	    //TODO Log passenger exiting

	}

}
