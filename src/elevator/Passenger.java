package elevator;

import java.util.logging.Level;

import api.AbstractElevator;
import util.MessageLogger;

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

	public Passenger(Building b, int id, int from, int dest) {
		this(id, from, dest);
		setBuilding(b);
	}

	public void setBuilding(Building b) {
		building = b;
	}

	@Override
	public void run() {
		AbstractElevator elevator = null;
		print("Calling Elevator!", Level.INFO);
		if (fromFloor < destinationFloor) {
			elevator = building.CallUp(fromFloor);
		} // TODO implement someone that comes and leaves from same floor
		else if (fromFloor > destinationFloor) {
			elevator = building.CallDown(fromFloor);
		}
		rideElevator(elevator);
	}

	private void rideElevator(AbstractElevator elevator) {
		if (elevator == null) {
			return;
		}
		synchronized (elevator) {
			// waiting for the assigned elevator to come
			while (elevator.getCurrentFloor() != fromFloor) {
				try {
					elevator.wait();
				} catch (InterruptedException e) {
					print("Interrupted!", Level.INFO);
					return;
				}
			}

			if (!elevator.Enter()) {
				print("FULL! E:" + elevator.getId(), Level.INFO);
				return;
			}
			print("Entered! E:" + elevator.getId(), Level.INFO);
			elevator.RequestFloor(destinationFloor);
			print("Requested F:"+ destinationFloor +" E:" + elevator.getId(), Level.INFO);

			while (elevator.getCurrentFloor() != destinationFloor) {
				try {
					elevator.wait();
				} catch (InterruptedException e) {
					MessageLogger.myLogger.log(Level.WARNING,
							"Error waiting in Elevator at: " + elevator.getCurrentFloor());
					System.out.println("Error waiting in elevator");
					continue;
				}
			}
			elevator.Exit();
			System.out.println("Passenger " + id + " exited elevator " + elevator.getId() + " on floor "
					+ elevator.getCurrentFloor());

			// TODO Log passenger exiting
			MessageLogger.myLogger.log(Level.INFO, "Passenger " + id + " exited elevator " + elevator.getId()
					+ " on floor " + elevator.getCurrentFloor());
		}
		return;

	}

	private void print(String message, Level level) {
		String prefix = "# P:" + id + " F:" + fromFloor + "->" + destinationFloor + "> " + message;
		System.out.println(prefix);
		MessageLogger.myLogger.log(level, prefix);
	}

}
