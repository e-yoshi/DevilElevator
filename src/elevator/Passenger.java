package elevator;

import java.util.logging.Level;

import api.AbstractElevator;
import util.MessageLogger;

public class Passenger implements Runnable {
	protected int id;
	protected int destinationFloor;
	protected int fromFloor;
	protected Building building;

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
	
	public int getID(){
		return id;
	}

	@Override
	public void run() {
		AbstractElevator elevator = null;
		print("Calling Elevator!", Level.INFO);
		elevator = requestElevator(destinationFloor);
		waitForElevator(elevator);
		while(!rideElevator(elevator)) {
			elevator = requestElevator(destinationFloor);
			waitForElevator(elevator);
		}
	}

	protected AbstractElevator requestElevator(int destination) {
		if (fromFloor < destination)
			return building.CallUp(fromFloor);
		else
			return building.CallDown(fromFloor);
	}

	protected void waitForElevator(AbstractElevator elevator) {
		if (elevator == null) {
			return;
		}
		synchronized (elevator) {
			while (elevator.getCurrentFloor() != fromFloor) {
				try {
					elevator.wait(50);
					elevator.notifyAll();
				} catch (InterruptedException e) {
					print("Interrupted when waiting for elevator!", Level.WARNING);
					return;
				}
			}
		}
	}

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
				elevator.notifyAll();
				try {
					elevator.wait();
				} catch (InterruptedException e) {
					print("Interrupted in elevator!", Level.WARNING);
					return true;
				}
			}
			elevator.Exit();
			print("Exited! E:" + elevator.getId() + " F:" + elevator.getCurrentFloor(), Level.INFO);
			return true;
		}	
	}

	protected void print(String message, Level level) {
		String prefix = "# P:" + id + " F:" + fromFloor + "->" + destinationFloor + "> ";
		System.out.println(prefix + message);
		MessageLogger.myLogger.log(level, prefix + message);
	}

}
