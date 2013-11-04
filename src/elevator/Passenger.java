package elevator;

import api.AbstractElevator;

public class Passenger implements Runnable {

	private int id;
	private int destinationFloor;
	private int fromFloor;
	private Building building;

	public Passenger(int id, int from, int dest) {
		this.id = id;
		destinationFloor = dest;
		fromFloor = from;
	}

	public void setBuilding(Building b) {
		building = b;
	}

	@Override
	public void run() {
		AbstractElevator elevator;
		if (fromFloor > destinationFloor) {
			elevator = building.CallDown(fromFloor);
		}
		// Maybe check if dest and from are the same
		else {
			elevator = building.CallUp(fromFloor);
		}
		// Synchronize?
		elevator.Enter();
		elevator.RequestFloor(destinationFloor);
		while (elevator.getCurrentFloor() != destinationFloor) {
			try {
				elevator.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		elevator.Exit();

	}

}
