package elevator;

import java.util.ArrayList;
import java.util.List;
import util.ElevatorFactory;

public class ElevatorExecutor {
	private static final String FILENAME = "elevatorEvents.csv";
	private static ElevatorFactory factory = null;
	private static List<Passenger> passengerList = new ArrayList<Passenger>();
	private static List<Joker> jokerList = new ArrayList<Joker>();

	private static int numFloors;
	private static int numElevators;

	public ElevatorExecutor(int partNumber) {
		factory = new ElevatorFactory(FILENAME);
		numFloors = factory.getNumFloors();
		numElevators = factory.getNumElevators();
		passengerList = factory.getPassengers();
		jokerList = factory.getJokers();

		if (partNumber == 1) {
			numElevators = 1;
			// TODO: need some sort of statement saying elevators will be
			// unbounded
		} else if (partNumber == 2) {
			numElevators = 1;
			// TODO: need some sort of statement saying elevators will be
			// bounded
		}
		execute();
	}

	private static void execute() {
		Building b = new Building(numFloors, numElevators);

		for (Passenger p : passengerList) {
			p.setBuilding(b);
			Thread t = new Thread(p, "Passenger " + p.getID());
			t.start();
		}
	}

}