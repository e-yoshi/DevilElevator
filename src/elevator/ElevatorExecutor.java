package elevator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.EventFactory;

public class ElevatorExecutor {
	private static final String FILENAME = "elevatorEvents.csv";
	private static EventFactory factory = null;
	private static List<Thread> threadList = new ArrayList<Thread>();
	private static List<Passenger> passengerList = new ArrayList<Passenger>();
	private static List<Joker> jokerList = new ArrayList<Joker>();
	
	private static int numFloors;
	private static int numElevators;
	
	
	public ElevatorExecutor(){
		numFloors = factory.getNumFloors();
		numElevators = factory.getNumElevators();
		passengerList = factory.getPassengers();
		jokerList = factory.getJokers();
	}
	
	public static void execute() {
		Building b = new Building(numFloors, numElevators);
		
		for (Passenger p : passengerList){
			p.setBuilding(b);
			Thread t = new Thread(p,"Passenger "+p.getID());
			t.start();
		}
	}
}