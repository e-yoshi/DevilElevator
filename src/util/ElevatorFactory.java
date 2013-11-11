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
	int jokersIndex = 10;
	int numJokers = 1;

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

				if (params[0].startsWith("//")) {
					lineNum++;
					continue;
				} else if (params[0].equals("floors"))
					numFloors = Integer.parseInt(params[1]);
				else if (params[0].equals("random?")) {
					randPassengers = Boolean.parseBoolean(params[1]);
				} else if (params[0].equals("passengers")) {
					numPassengers = Integer.parseInt(params[1]);
					passIndex = lineNum;

				} else if (params[0].equals("elevators")) {
					numElevators = Integer.parseInt(params[1]);
				} else if (params[0].equals("jokers")) {
					numJokers = Integer.parseInt(params[1]);
				} else if (isPassenger(lineNum)) {
					addPassenger(lineNum, Integer.parseInt(params[0]),
							Integer.parseInt(params[1]));
				} else if (isJoker(lineNum)) {
					// public Joker(int id, int from, int dest, int wrongFloor,
					// boolean waitElevator, boolean noRequest)
					addJoker(lineNum, Integer.parseInt(params[0]),
							Integer.parseInt(params[1]),
							Integer.parseInt(params[2]),
							Boolean.parseBoolean(params[3]),
							Boolean.parseBoolean(params[4]));
				}

				lineNum++;
			}

			if (randPassengers) {
				addRandomPassengers();
			}
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
			System.out
					.println("An IO exeption ocurred while reading a line in the file.");

		}

	}

	private void addJoker(int lineNumber, int from, int to, int wrongFloor,
			boolean wait, boolean request) {
		// public Joker(int id, int from, int dest, int wrongFloor, boolean
		// waitElevator, boolean noRequest)
		Joker j = new Joker(lineNumber - jokersIndex, from, to, wrongFloor,
				wait, request);
		jokerList.add(j);
	}

	private boolean isJoker(int lineNumber) {
		return (lineNumber > jokersIndex && lineNumber <= jokersIndex
				+ numJokers);
	}

	private void addRandomPassengers() {
		Random rand = new Random();
		// just defining ground floor as 0. No really difference between a
		// building only having
		// positive floors, just a simple case for random generation
		int minimum = 0;
		for (int i = 0; i < numPassengers; i++) {
			int from = -1;
			int to = -1;
			while (from == to) {
				to = rand.nextInt(numFloors - minimum) + minimum;
				from = rand.nextInt(numFloors - minimum) + minimum;
			}

			Passenger p = new Passenger(i, from, to);
			// MessageLogger.myLogger.log(Level.INFO,("ID: " + i + " from: " +
			// from + " to: " + to));
			passengerList.add(p);
		}
	}

	private void addPassenger(int lineNumber, int from, int to) {
		// id = lineNumber - lineNumber of passengers declaration
		Passenger p = new Passenger(lineNumber - passIndex, to, from);
		passengerList.add(p);

	}

	private boolean isPassenger(int lineNumber) {
		return (!randPassengers && lineNumber > passIndex && lineNumber <= passIndex
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
