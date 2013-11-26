package elevator;

import java.util.logging.Level;

import util.MessageLogger;

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

	@Override
	public void setBuilding(Building b) {
		super.setBuilding(b);
	}

	@Override
	public void run() {
		Elevator elevator = null;
		print("Calling Elevator!", Level.INFO);
		elevator = (Elevator) requestElevator(super.destinationFloor);
		if (waitElevator)
			waitForElevator(elevator);
		if (!rideJokerElevator(elevator)) {
			elevator = (Elevator) requestElevator(super.destinationFloor);
			if (waitElevator)
				waitForElevator(elevator);
			while (!super.rideElevator(elevator)) {
				elevator = (Elevator) requestElevator(destinationFloor);
				waitForElevator(elevator);
			}
		}
	}

	protected boolean rideJokerElevator(Elevator elevator) {
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
			while (elevator.getCurrentFloor() != wrongFloor) {
				try {
					elevator.wait(1000); // joker gets tired of being a joker eventually...
				} catch (InterruptedException e) {
					print("Interrupted in elevator!", Level.WARNING);
					return false;
				}
				if (elevator.isIdle() && elevator.getNumberOfPassengers() == 1)
					break;
			}

			if (elevator.jokerExit(wrongFloor)) {
				print("Joker Exited! E:" + elevator.getId() + " F:" + elevator.getCurrentFloor(), Level.INFO);
			} else {
				print("Joker Got Tired ZzZz", Level.INFO);
				return false;
			}
			return true;
		}
	}

	@Override
	protected void print(String message, Level level) {
		String prefix = "# J:" + id + " F:" + fromFloor + "->" + destinationFloor + "> ";
		System.out.println(prefix + message);
		MessageLogger.myLogger.log(level, prefix + message);
	}
}
