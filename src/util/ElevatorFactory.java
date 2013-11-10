package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadFactory;

import elevator.Joker;
import elevator.Passenger;

public class ElevatorFactory implements ThreadFactory {

	private static final String FOLDER = "/testFiles/";
	private static final String SPLIT_AT = ",";

	private List<Passenger> passengerList = new ArrayList<Passenger>();
	private List<Thread> threadList = new ArrayList<Thread>();
	private List<Joker> jokerList = new ArrayList<Joker>();

	// default values
	private int numFloors = 10;

	int numPassengers = 10;
	int numElevators = 5;
	boolean randPassengers = false;
	int passIndex = 3;

	public ElevatorFactory(String filename) {
		readFile(filename);
	}

	private void readFile(String fileName) {
		// URL url = this.getClass().getResourceAsStream(FOLDER+fileName);
		// File file = new File(url.getPath());
		Reader reader = new InputStreamReader(this.getClass()
				.getResourceAsStream(FOLDER + fileName));

		BufferedReader br = null;
		String line = "";

		br = new BufferedReader(reader);

		try {
			int lineNum = 0;
			while ((line = br.readLine()) != null) {
				String[] params = line.split(SPLIT_AT);

				if (params[0].startsWith("//"))
					continue;
				else if (params[0].equals("floors"))
					numFloors = Integer.parseInt(params[1]);
				else if (params[0].equals("random?")) {
					randPassengers = Boolean.parseBoolean(params[1]);
				} else if (params[0].equals("passengers")) {
					numPassengers = Integer.parseInt(params[1]);

				} else if (params[0].equals("elevators")) {
					numElevators = Integer.parseInt(params[1]);
				} else if (isPassenger(lineNum)) {
					addPassenger(lineNum, Integer.parseInt(params[0]),
							Integer.parseInt(params[1]));
				}
				lineNum++;
			}

			if (randPassengers) {
				addRandomPassengers();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out
					.println("An IO exeption ocurred while reading a line in the file.");

		}

	}

	private void addRandomPassengers() {
		Random rand = new Random();
		// just defining ground floor as 0. No realy difference between a
		// building only having
		// positive floors, just a simple case for random generation
		int minimum = 0;
		int to = rand.nextInt((numFloors - minimum) + 1) + minimum;
		int from = rand.nextInt((numFloors - minimum) + 1) + minimum;
		for (int i = 0; i < numPassengers; i++) {
			Passenger p = new Passenger(i, from, to);
			passengerList.add(p);
		}
	}

	private void addPassenger(int lineNumber, int to, int from) {
		// id = lineNumber - lineNumber of passengers declaration
		// TODO: does some sort of concurrency control need to be implemented.
		// Probably not because only 1 thead is running at this point and it
		// will stay that way
		// until all of these have defined state
		Passenger p = new Passenger(lineNumber - passIndex, to, from);
		passengerList.add(p);

	}

	private boolean isPassenger(int lineNumber) {
		// TODO: check to make sure this is not off by 1 index
		return (!randPassengers && lineNumber > passIndex && lineNumber <= lineNumber
				+ numPassengers);
	}

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r);
	}

	public List<Thread> getThreadList() {
		return threadList;
	}

	public int getNumElevators() {
		return numElevators;
	}

	public int getNumFloors() {
		return numFloors;
	}

	public List<Passenger> getPassengers() {
		return passengerList;
	}

	public List<Joker> getJokers() {
		return jokerList;
	}
}
