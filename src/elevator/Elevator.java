package elevator;

import java.awt.print.Printable;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.MessageLogger;
import api.AbstractElevator;

public class Elevator extends AbstractElevator implements Runnable {

	private int passengersRiding = 0;
	private int[] floorsToVisit;
	private boolean isAscending = true;
	private int boundaryFloor;
	private int doorTimeout = 0;

	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold, int doorTimeout) {
		super(numFloors, elevatorId, maxOccupancyThreshold);
		floorsToVisit = new int[numFloors];
		this.doorTimeout = doorTimeout;
	}

	@Override
	public void run() {
		while (true) {
			while (isIdle()) {
				print("Idling", Level.INFO);
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						continue;
					}
				}
			}
			boundaryFloor = isAscending ? numFloors - 1 : 0;
			VisitFloor();
		}

	}

	@Override
	public synchronized void OpenDoors() {
		print("Opening Doors!", Level.INFO);

		notifyAll();
		while (floorsToVisit[currentFloor] != 0) {
			try {
				wait(doorTimeout);
			} catch (InterruptedException e) {
				continue;
			}
		}
	}

	@Override
	public void ClosedDoors() {
		print("Closing doors!", Level.INFO);
	}

	@Override
	public void VisitFloor() {
		while (currentFloor != boundaryFloor) {
			if (!isAscending && currentFloor != 0) {
				currentFloor--;
			} else if (isAscending && currentFloor != numFloors - 1) {
				currentFloor++;
			}
			if (floorsToVisit[currentFloor] != 0) {
				print("Stopped!", Level.INFO);
				OpenDoors();
				ClosedDoors();
			}

		}
		isAscending = !isAscending;

	}

	@Override
	public synchronized boolean Enter() {
		if (maxOccupancyThreshold < 0) {
			if (passengersRiding == maxOccupancyThreshold) {
				checkIfThatIsFullAndBeDisappointed();
				return false;
			}
		}
		passengersRiding++;
		floorsToVisit[currentFloor]--;
		notifyAll();
		return true;
	}

	@Override
	public synchronized void Exit() {
		passengersRiding--;
		if (floorsToVisit[currentFloor] != 0)
			floorsToVisit[currentFloor]--;
		notifyAll();
	}

	public synchronized boolean jokerExit(int floor) {
		if (this.getCurrentFloor() == floor) {
			passengersRiding--;
			if (floorsToVisit[currentFloor] != 0)
				floorsToVisit[currentFloor]--;
			notifyAll();
			return true;
		}
		return false;
	}

	/**
	 * Invoked by passenger when a full elevator arrives Frees the request for
	 * the current floor without adding passengers
	 */
	private synchronized void checkIfThatIsFullAndBeDisappointed() {
		floorsToVisit[currentFloor]--;
	}

	@Override
	public synchronized void RequestFloor(int floor) {
		floorsToVisit[floor]++;
	}

	/**
	 * 
	 * @return true if elevator is going up
	 */
	public boolean isAscending() {
		return isAscending;
	}

	/**
	 * Invoked by building to call an elevator to a passenger
	 * 
	 * @param floor
	 *            the floor a passenger is waiting
	 */
	public synchronized void callToFloor(int floor) {
		if (floor > numFloors)
			return;
		floorsToVisit[floor]++;
		try {
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Called when an elevator is idle. Resets to list of floor and direction
	 * the elevator needs to go
	 * 
	 * @param floor
	 *            the floor a passenger is waiting
	 */
	public synchronized void startElevator(int floor) {
		if (floor > numFloors)
			return;
		floorsToVisit[floor]++;
		isAscending = (currentFloor < floor) ? true : false;
		boundaryFloor = isAscending ? numFloors - 1 : 0;

		// Wake up idle thread

		this.notifyAll();

		try {
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Checks if this elevator has to stop in any floor
	 */
	public boolean isIdle() {
		for (int i : floorsToVisit) {
			if (i > 0)
				return false;
		}
		return true;
	}

	/**
	 * @return current number of passengers in this elevator
	 */
	public int getNumberOfPassengers() {
		return passengersRiding;
	}

	private void print(String message, Level level) {
		String prefix = "E:" + elevatorId + " F:" + currentFloor + "> ";
		System.out.println(prefix + message);
		MessageLogger.myLogger.log(level, prefix + message);
	}

}
