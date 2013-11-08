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
	    while (floorsToVisit[currentFloor] != 0) {
	        synchronized(this) {
	        notifyAll();
	        }
	    }

	}

	@Override
	public void ClosedDoors() {
		// TODO Auto-generated method stub

	}

	@Override
	public void VisitFloor(int floor) {
		while (currentFloor != floor) {
			if (!isAscending && currentFloor != 0) {
				currentFloor--;
			} else if (isAscending && currentFloor != numFloors-1) {
				currentFloor++;
			}
			//System.out.println("Elevator is on floor "+currentFloor);
			if (floorsToVisit[currentFloor] != 0) {
			    System.out.println("Elevator "+getId()+" Stopped on floor "+currentFloor);
				OpenDoors();
				ClosedDoors();
			}
			
		}
		isAscending = !isAscending;

		
	}

	@Override
	public synchronized boolean Enter() {
		passengersRiding++;
		floorsToVisit[currentFloor]--;
		return true;
	}

	@Override
	public synchronized void Exit() {
		passengersRiding--;
		// No error check yet in case this is 0 already
		floorsToVisit[currentFloor]--;

	}

	@Override
	public synchronized void RequestFloor(int floor) {
		floorsToVisit[floor]++;

	}

	public boolean isAscending() {
		return isAscending;
	}

	public synchronized void callToFloor(int floor) { 
	    floorsToVisit[floor]++;
	}

}
