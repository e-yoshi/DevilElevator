package elevator;

import java.util.HashMap;
import java.util.Map;
import api.AbstractElevator;

public class Elevator extends AbstractElevator implements Runnable {

	private int passengersRiding = 0;
	private int[] floorsToVisit;
	private boolean isAscending = true;
	private int visit;
	

	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
		super(numFloors, elevatorId, maxOccupancyThreshold);
		floorsToVisit = new int[numFloors];
	}

	@Override
	public void run() {

	    while (true) {
	        while (isIdle()) {
	            System.out.println("Elevator "+elevatorId+" does not have requests");
	            synchronized (this) {
	                try {
	                    this.wait();
	                }
	                catch (InterruptedException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        }
	        visit = isAscending ?  numFloors - 1 : 0;
	        VisitFloor();
	    }

	}

	@Override
	public void OpenDoors() {
	    System.out.println("Opening doors at floor "+currentFloor+" and index is "+floorsToVisit[currentFloor]);
	    System.out.println("passengers riding is "+passengersRiding);
	    while (floorsToVisit[currentFloor] != 0) {
	        synchronized(this) {
	            notifyAll();
	        }
	    }

	}

	@Override
	public void ClosedDoors() {
	    System.out.println("Closing doors from floor "+currentFloor);
	    //TODO Log this
	}

	@Override
	public void VisitFloor() {
		while (currentFloor != visit) {
			if (!isAscending && currentFloor != 0) {
				currentFloor--;
			} else if (isAscending && currentFloor != numFloors-1) {
				currentFloor++;
			}
			//System.out.println("Elevator is on floor "+currentFloor);
			if (floorsToVisit[currentFloor] != 0) {
			    System.out.println("Elevator "+getId()+" Stopped on floor "+currentFloor);
			  
			    OpenDoors();
			    ClosedDoors();
			}
			
			
		}
		isAscending = !isAscending;

		
	}

	@Override
	public synchronized boolean Enter() {
	    if(passengersRiding == maxOccupancyThreshold) {
	        checkThatItIsFullAndBeDisappointed();
	        return false;
	    }
    
	    passengersRiding++;
	    floorsToVisit[currentFloor]--;
	    return true;
	}

	@Override
	public synchronized void Exit() {
		passengersRiding--;
		// No error check yet in case this is 0 already
		floorsToVisit[currentFloor]--;

	}
	
	/**
	 * Invoked by passenger when a full elevator arrives
	 * Frees the request for the current floor without adding passengers
	 */
	private synchronized void checkThatItIsFullAndBeDisappointed() {
	    floorsToVisit[currentFloor]--;
	}

	@Override
	public synchronized void RequestFloor(int floor) {
	    
		floorsToVisit[floor]++;

	}

	public boolean isAscending() {
		return isAscending;
	}

	public synchronized void callToFloor(int floor) { 
	    floorsToVisit[floor]++;
	    try {
	        this.wait();
	    }
	    catch (InterruptedException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
	
	public synchronized void startElevator(int floor) {
	    floorsToVisit[floor]++;
	    isAscending = (currentFloor < floor) ? true : false;
	    visit = isAscending ?  numFloors - 1 : 0;
	    this.notify();
	    
	    try {
                this.wait();
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
	}
	
	/**
	 * Checks if this elevator has to stop in any floor
	 */
	public boolean isIdle() {
	    for (int i : floorsToVisit) {
	        if (i > 0) return false;
	    }
	    return true;
	}
	
	public int getNumberOfPassengers() {
	    return passengersRiding;
	}

}
