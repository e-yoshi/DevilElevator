import eventBarrier.EventExecutor;

public class Main {

	public static void main(String args[]) {
		if (args.length == 0) {
			// No options specified; make the default as the part 3 elevator
			// submission
			EventExecutor ee = new EventExecutor();
		} else if (args.length > 2) {
			// Throw an error--too many args
		} else // known just one arg
		if (args[0].equals("p1")) {
			// call the EventBarrier-- happens in the constructor.
			EventExecutor ee = new EventExecutor();
		}
		if (args[0].equals("p2part1")) {
			// call the elevator part1
		} else if (args[0].equals("p2part2")) {
			// call the elevator part2
		} else if (args[0].equals("p2part3")) {
			// call the elevator part3
		}

	}
}
