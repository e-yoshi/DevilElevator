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
	
	public Joker(int id, int from, int dest, int wrongFloor, boolean waitElevator, boolean noRequest) {
		super(id, from, dest);
		this.waitElevator = waitElevator;
		this.wrongFloor = wrongFloor;
		this.noRequest = noRequest;
	}
	
	public void setBuilding(Building b) {
		super.setBuilding(b);
	}

	@Override
	public void run() {
		AbstractElevator elevator = null;
		print("Calling Elevator!", Level.INFO);
		elevator = requestElevator(super.destinationFloor);
		if (waitElevator)
			waitForElevator(elevator);
		while (!rideElevator(elevator)) {
			elevator = this.requestElevator(destinationFloor);
			if (waitElevator)
				waitForElevator(elevator);
		}
	}

	
	protected boolean rideElevator(Elevator elevator) {
		if (elevator == null) {
			return false;
		}
		synchronized (elevator) {
			if (!elevator.Enter()) {
				print("FULL! E:" + elevator.getId(), Level.INFO);
				return true;
			}
			print("Entered! E:" + elevator.getId(), Level.INFO);
			if (!noRequest) {
				elevator.RequestFloor(destinationFloor);
			}
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

			if (elevator.jokerExit(wrongFloor)) {
				print("Joker Exited! E:" + elevator.getId() + " F:" + elevator.getCurrentFloor(), Level.INFO);
			} else {
				return false;
			}
			return true;
		}
	}
}
