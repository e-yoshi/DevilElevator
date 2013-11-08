import java.util.Random;
import simpleEventBarrier.SimpleEventExecutor;
import elevator.Building;
import elevator.Passenger;
import eventBarrier.EventExecutor;

public class Main {

    public static void main(String args[]) {
        if (args.length == 0) {
            // No options specified; make the default as the part 3 elevator
            // submission
            Random generator = new Random(34569);
            int numFloors = 100;
            int id = 0;
            Building b = new Building(numFloors, 1);
            for (int i = 0; i < 5; i++) {
                int from = -1, to = -1;
                while (from == to) {
                    from = generator.nextInt(100);
                    to = generator.nextInt(100);
                }
                
                Passenger p = new Passenger(b, id,from,to);
                Thread t = new Thread(p,"Passenger "+id);
                id++;
                t.start();
            }
            
            //SimpleEventExecutor ee = new SimpleEventExecutor();
        } else if (args.length > 2) {
            // Throw an error--too many args
        } else // known just one arg
            if (args[0].equals("p1")) {
                // call the EventBarrier-- happens in the constructor.
                EventExecutor ee = new EventExecutor();
            }
        /*
		if (args[0].equals("p2part1")) {
			// call the elevator part1
		} else if (args[0].equals("p2part2")) {
			// call the elevator part2
		} else if (args[0].equals("p2part3")) {
			// call the elevator part3
		}
         */
	}
}
