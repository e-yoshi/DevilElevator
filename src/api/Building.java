package api;

public class Building extends AbstractBuilding implements Runnable {
    private Elevator elevator;
    
    
    public Building (int numFloors, int numElevators) {
        super(numFloors, numElevators);
        elevator = new Elevator(numFloors, 0, 0);
    }

    @Override
    public void run () {
        // TODO Auto-generated method stub

    }

    @Override
    public AbstractElevator CallUp (int fromFloor) {
        
        elevator.callToFloor(fromFloor);
        return elevator;
        
        //This if statement will be useful for multiple elevators
        //Since we only have one, we return the only one
        /*
        if (elevator.isAscending() && elevator.getCurrentFloor() <= fromFloor) {
            
        }
        return null;
        */
    }

    @Override
    public AbstractElevator CallDown (int fromFloor) {
        elevator.callToFloor(fromFloor);
        return elevator;
        
        //This if statement will be useful for multiple elevators
        //Since we only have one, we return the only one
        /*
        if (!elevator.isAscending() && elevator.getCurrentFloor() >= fromFloor) {
            
        }
        return null;
        */
    }

}
