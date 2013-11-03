package api;

public class Elevator extends AbstractElevator implements Runnable {

    private int passengersRiding = 0;
    private int[] floorsToVisit;
    private int currentFloor = 1;
    private boolean isAscending = true;
    
    public Elevator (int numFloors, int elevatorId, int maxOccupancyThreshold) {
        super(numFloors, elevatorId, maxOccupancyThreshold);
        floorsToVisit = new int[numFloors];
    }

    @Override
    public void run () {
        while(true) {
            if (isAscending) {
                VisitFloor(numFloors-1);
            }
            else {
                VisitFloor(0);
            }
        }

    }

    @Override
    public void OpenDoors () {
        // TODO Auto-generated method stub

    }

    @Override
    public void ClosedDoors () {
        // TODO Auto-generated method stub

    }

    @Override
    public void VisitFloor (int floor) {
        while(currentFloor != floor) {
            if (currentFloor > floor) {
                currentFloor--;
            }
            else if (currentFloor < floor) {
                currentFloor++;
            }
            
            if (floorsToVisit[currentFloor] != 0) {
                //TODO Make a stop
            }
        }

    }

    @Override
    public boolean Enter () {
        passengersRiding++;
        return true;
    }

    @Override
    public void Exit () {
        // TODO Auto-generated method stub

    }

    @Override
    public void RequestFloor (int floor) {
        // TODO Auto-generated method stub

    }
    
    public boolean isAscending() {
        return isAscending;
    }
    
    public int getCurrentFloor() {
        return currentFloor;
    }
    
    public void callToFloor(int floor) {
        floorsToVisit[floor]++;
    }

}
