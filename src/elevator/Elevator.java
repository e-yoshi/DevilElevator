package elevator;

import api.AbstractElevator;

public class Elevator extends AbstractElevator implements Runnable {

	private int passengersRiding = 0;
	private int[] floorsToVisit;
	private boolean isAscending = true;

	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
		super(numFloors, elevatorId, maxOccupancyThreshold);
		floorsToVisit = new int[numFloors];
	}

	@Override
	public void run() {
		while (true) {
			if (isAscending) {
				VisitFloor(numFloors - 1);
			} else {
				VisitFloor(0);
			}
		}

	}

	@Override
	public void OpenDoors() {
		// Synchronize?
		while (floorsToVisit[currentFloor] != 0) {
			notifyAll();
		}

	}

	@Override
	public void ClosedDoors() {
		// TODO Auto-generated method stub

	}

	@Override
	public void VisitFloor(int floor) {
		while (currentFloor != floor) {
			if (currentFloor > floor) {
				currentFloor--;
			} else if (currentFloor < floor) {
				currentFloor++;
			}

			if (floorsToVisit[currentFloor] != 0) {
				OpenDoors();
				ClosedDoors();
			}
		}

		isAscending = !isAscending;

	}

	@Override
	public boolean Enter() {
		passengersRiding++;
		return true;
	}

	@Override
	public void Exit() {
		passengersRiding--;
		// No error check yet in case this is 0 already
		floorsToVisit[currentFloor]--;

	}

	@Override
	public void RequestFloor(int floor) {
		floorsToVisit[floor]++;

	}

	public boolean isAscending() {
		return isAscending;
	}

	public void callToFloor(int floor) {
		floorsToVisit[floor]++;
	}

}
