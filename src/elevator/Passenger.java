package elevator;

import java.util.logging.Level;

import api.AbstractElevator;
import util.MessageLogger;


public class Passenger implements Runnable {

    private int id;
    private int destinationFloor;
    private int fromFloor;
    private Building building;

    public Passenger (int id, int from, int dest) {
        this.id = id;
        destinationFloor = dest;
        fromFloor = from;
    }

    public Passenger (Building b, int id, int from, int dest) {
        this(id, from, dest);
        setBuilding(b);
    }

    public void setBuilding (Building b) {
        building = b;
    }

    @Override
    public void run () {
        AbstractElevator elevator = null;
        System.out.println("Passenger " + id + " is in floor " + fromFloor + " goes to floor " +
                           destinationFloor);

        while(true) {
            if (fromFloor < destinationFloor) {
                // In case no match is found
                elevator = building.CallUp(fromFloor);    
            } // TODO implement someone that comes and leaves from same floor
            else if (fromFloor > destinationFloor) {
                elevator = building.CallDown(fromFloor);
            }

            if (RideElevator(elevator)) 
                break;
            
        }

    }

    private boolean RideElevator (AbstractElevator elevator) {
        if(elevator == null) {
            return false;
        }
        synchronized (elevator) {
            while (elevator.getCurrentFloor() != fromFloor) {
                try {
                     elevator.wait();   
                }
                catch (InterruptedException e) {
                    // TODO Log error
                    System.out.println("Error waiting for elevator");
                    MessageLogger.myLogger.log(Level.WARNING,"Error waiting for elevator at floor: " + fromFloor);
                    e.printStackTrace();
                }
            }

            if(!elevator.Enter()) {
                System.out.println("Passenger " + id + " faces a full elevator and is disappointed");
                return false;   
            }
            
            System.out.println("Passenger " + id + " entered elevator " + elevator.getId() +
                               " from floor " + elevator.getCurrentFloor());
            // TODO Log passenger entering
            MessageLogger.myLogger.log(Level.INFO, "Passenger " + id + " entered elevator " + elevator.getId() +
                    " from floor " + elevator.getCurrentFloor());
            elevator.RequestFloor(destinationFloor);
            System.out.println("Passenger " + id + " requested floor " + destinationFloor);
            
            
            // TODO Log floor request
            while (elevator.getCurrentFloor() != destinationFloor) {
                try {
                    elevator.wait();
                }
                catch (InterruptedException e) {
                    // TODO Write error on log
                	MessageLogger.myLogger.log(Level.WARNING, "Error waiting in Elevator at: " + elevator.getCurrentFloor());
                    System.out.println("Error waiting in elevator");
                    e.printStackTrace();
                }
            }
            elevator.Exit();
            System.out.println("Passenger " + id + " exited elevator " + elevator.getId() +
                           " on floor " + elevator.getCurrentFloor());
            
            // TODO Log passenger exiting
            MessageLogger.myLogger.log(Level.INFO, "Passenger " + id + " exited elevator " + elevator.getId() +
                           " on floor " + elevator.getCurrentFloor());
        }
       return true;

    }

}
