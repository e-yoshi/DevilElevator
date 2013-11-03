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
        if (elevator.)
        return null;
    }

    @Override
    public AbstractElevator CallDown (int fromFloor) {
        // TODO Auto-generated method stub
        return null;
    }

}
