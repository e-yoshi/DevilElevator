package elevator;

import java.util.logging.Level;
import util.MessageLogger;
import api.AbstractElevator;

public class Elevator extends AbstractElevator implements Runnable {

	private int passengersRiding = 0;
	private int[] floorsToVisit;
	private volatile boolean isAscending = true;
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
				synchronized (this) {
					try {
						print("Idle____________", Level.INFO);
						this.wait();
					} catch (InterruptedException e) {
						return;
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
		notifyAll(); // Notify that arrived at floor.
		while (floorsToVisit[currentFloor] != 0) {
			notifyAll(); // Notify that door opened
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
		if (floor > numFloors)
			return;
		floorsToVisit[floor]++;
		this.notifyAll();
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
		RequestFloor(floor);
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
		print("Started!", Level.INFO);
		this.notifyAll(); // Wake up elevator from idle
	}

	/**
	 * Checks if this elevator has to stop in any floor
	 */
	public synchronized boolean isIdle() {
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
