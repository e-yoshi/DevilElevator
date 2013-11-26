package elevator;

import java.util.ArrayList;
import java.util.Collections;

import api.AbstractBuilding;
import api.AbstractElevator;

public class Building extends AbstractBuilding {
	private ArrayList<Elevator> elevators;
	private int maxOccupancy;

	public Building(int numFloors, int numElevators, int maxOccupancy) {
		super(numFloors, numElevators);
		elevators = new ArrayList<Elevator>();

		for (int i = 0; i < numElevators; i++) {
			Elevator elevatorService = new Elevator(numFloors, i, maxOccupancy, 5000);
			elevators.add(elevatorService);
			Thread t = new Thread(elevatorService, "Elevator " + i);
			t.start();
		}
		this.maxOccupancy = maxOccupancy;
	}

	@Override
	public AbstractElevator CallUp(int fromFloor) {
		while (true) {
			shuffleElevators();
			for (Elevator elevator : elevators) {
				synchronized (elevator) {
					if (maxOccupancy > 0 && elevator.getNumberOfPassengers() >= maxOccupancy)
						continue;
					if (elevator.isIdle()) {
						elevator.startElevator(fromFloor);
						return elevator;
					}
					if ((elevator.isAscending() && elevator.getCurrentFloor() <= fromFloor)) {
						elevator.callToFloor(fromFloor);
						return elevator;
					}
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
					if (maxOccupancy > 0 && elevator.getNumberOfPassengers() >= maxOccupancy)
						continue;
					if (elevator.isIdle()) {
						elevator.startElevator(fromFloor);
						return elevator;
					}
					if ((!elevator.isAscending() && elevator.getCurrentFloor() >= fromFloor)) {
						elevator.RequestFloor(fromFloor);
						return elevator;
					}
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
