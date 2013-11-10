import java.util.Random;
import java.util.logging.Level;

import simpleEventBarrier.SimpleEventExecutor;
import elevator.Building;
import elevator.Passenger;
import eventBarrier.EventExecutor;
import util.MessageLogger;

public class Main {

    public static void main(String args[]) {
    	MessageLogger.getInstance();
    	MessageLogger.myLogger.log(Level.INFO, "Begining Program");
        if (args.length == 0) {
            // No options specified; make the default as the part 3 elevator
            // submission
//        	startP2Part3();
        	startP1();
   
            
            //SimpleEventExecutor ee = new SimpleEventExecutor();
        } else if (args.length > 2) {
            // Throw an error--too many args
        } else // known just one arg
            if (args[0].equals("p1")) {
                // call the EventBarrier-- happens in the constructor.
                startP1();
            }
        
		if (args[0].equals("p2part1")) {
			// call the elevator part1
			startP2part1();
		} else if (args[0].equals("p2part2")) {
			startP2Part2();
			// call the elevator part2
		} else if (args[0].equals("p2part3")) {
			startP2Part3();
			// call the elevator part3
		}
         
	}
    
    private static void startP1(){
    	EventExecutor ee = new EventExecutor();
    }
    private static void startP2part1(){
    	
    }
    private static void startP2Part2(){
    	
    }
    private static void startP2Part3(){
    	Random generator = new Random(34569);
        int numFloors = 5;
        int numPassengers = 5;
        int id = 0;
        Building b = new Building(numFloors, 2);
        for (int i = 0; i < numPassengers; i++) {
            int from = -1, to = -1;
            while (from == to) {
                from = generator.nextInt(numFloors);
                to = generator.nextInt(numFloors);
            }
            
            Passenger p = new Passenger(b, id,from,to);
            Thread t = new Thread(p,"Passenger "+id);
            id++;
            t.start();
        }
    }
}
