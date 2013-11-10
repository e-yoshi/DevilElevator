package elevator;

import java.util.ArrayList;
import java.util.Collections;
import elevator.Elevator;
import api.AbstractBuilding;
import api.AbstractElevator;

public class Building extends AbstractBuilding {
	private ArrayList<Elevator> elevators;
	private int maxOccupancy;

	public Building(int numFloors, int numElevators) {
		super(numFloors, numElevators);
		maxOccupancy = 15;
		//TODO: maybe change this to an input value from a csv
		elevators = new ArrayList<Elevator>();

		for (int i = 0; i < numElevators; i++) {
			Elevator elevatorService = new Elevator(numFloors, i, maxOccupancy);
			elevators.add(elevatorService);
			Thread t = new Thread(elevatorService, "Elevator " + i);
			t.start();
		}
	}

	// int 1 = up, int -1 = down
	private boolean callCorrectElevator(Elevator elevator, int floorFrom, boolean up) {
		if (elevator.isIdle()) {
			elevator.startElevator(floorFrom);
			return true;
		}
		if (!up) {
			if ((elevator.isAscending() && elevator.getCurrentFloor() <= floorFrom)) {
				elevator.callToFloor(floorFrom);
				return true;
			}
		} else {
			if ((elevator.isAscending() && elevator.getCurrentFloor() >= floorFrom)) {
				elevator.callToFloor(floorFrom);
				return true;
			}
		}
		return false;
	}

	@Override
	public AbstractElevator CallUp(int fromFloor) {
		while (true) {
			shuffleElevators();
			for (Elevator elevator : elevators) {
				synchronized (elevator) {
					if (elevator.getNumberOfPassengers() == maxOccupancy)
						continue;
					if (callCorrectElevator(elevator, fromFloor, false))
						return elevator;
				}
			}
		}
	}

	@Override
	public AbstractElevator CallDown(int fromFloor) {
		while (true) {
			shuffleElevators();
			for (Elevator elevator : elevators) {
				synchronized (elevator) {
					if (elevator.getNumberOfPassengers() == maxOccupancy)
						continue;
					if (callCorrectElevator(elevator, fromFloor, true))
						return elevator;
				}
			}
		}
	}

	/**
	 * Adds randomness to the list being iterated, otherwise the first elevator
	 * will be returned more often.
	 */
	private synchronized void shuffleElevators() {
		Collections.shuffle(elevators);
	}
}
