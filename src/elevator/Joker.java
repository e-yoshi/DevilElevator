package elevator;

import java.util.logging.Level;

import api.AbstractElevator;

public class Joker extends Passenger {

	private boolean waitElevator;
	private int wrongFloor;
	private boolean noRequest;

	public Joker(Building b, int id, int from, int dest, int wrongFloor, boolean waitElevator, boolean noRequest) {
		super(b, id, from, dest);
		this.waitElevator = waitElevator;
		this.wrongFloor = wrongFloor;
		this.noRequest = noRequest;
	}

	@Override
	public void run() {
		AbstractElevator elevator = null;
		print("Calling Elevator!", Level.INFO);
		elevator = requestElevator(super.destinationFloor);
		if (waitElevator)
			waitForElevator(elevator);
		while (!rideElevator(elevator)) {
			elevator = requestElevator(destinationFloor);
			if (waitElevator)
				waitForElevator(elevator);
		}
	}
	
	@Override
	protected boolean rideElevator(AbstractElevator elevator) {
		if (elevator == null) {
			return false;
		}
		synchronized (elevator) {
			if (!elevator.Enter()) {
				print("FULL! E:" + elevator.getId(), Level.INFO);
				return true;
			}
			print("Entered! E:" + elevator.getId(), Level.INFO);
			elevator.RequestFloor(destinationFloor);
			print("Requested F:" + destinationFloor + " E:" + elevator.getId(), Level.INFO);
			// waiting for destination floor
			while (elevator.getCurrentFloor() != destinationFloor) {
				try {
					elevator.wait();
				} catch (InterruptedException e) {
					print("Interrupted in elevator!", Level.WARNING);
					return true;
				}
			}
			
			if(elevator.getCurrentFloor() != wrongFloor) {
				elevator.Exit();
			}
				
			
			notifyAll();
		
			print("Exited! E:" + elevator.getId() + " F:" + elevator.getCurrentFloor(), Level.INFO);
			return true;
		}	
	}
	
	

}