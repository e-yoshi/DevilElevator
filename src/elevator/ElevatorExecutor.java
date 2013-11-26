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
	private static int maxOccupancy = 15;

	private static Building building = null;

	public ElevatorExecutor(int partNumber) {
		factory = new ElevatorFactory(FILENAME);
		numFloors = factory.getNumFloors();
		numElevators = factory.getNumElevators();
		passengerList = factory.getPassengers();
		jokerList = factory.getJokers();

		if (partNumber == 1) {
			numElevators = 1;
			maxOccupancy = -1;
			// TODO: need some sort of statement saying elevators will be
			// unbounded
		} else if (partNumber == 2) {
			numElevators = 1;
			// This can be set to whatever value is desired for part 2
			maxOccupancy = 12;
		}

		building = new Building(numFloors, numElevators, maxOccupancy);

		execute();
	}

	private static void execute() {

		for (Passenger p : passengerList) {
			p.setBuilding(building);
			Thread t = new Thread(p, "Passenger " + p.getID());
			t.start();
		}

		for (Joker j : jokerList) {
			j.setBuilding(building);
			Thread t = new Thread(j, "Joker " + j.getID());
			t.start();
		}
	}

}