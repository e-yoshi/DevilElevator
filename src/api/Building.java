package api;

public class Building extends AbstractBuilding implements Runnable {

    public Building (int numFloors, int numElevators) {
        super(numFloors, numElevators);

    }

    @Override
    public void run () {
        // TODO Auto-generated method stub

    }

    @Override
    public AbstractElevator CallUp (int fromFloor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractElevator CallDown (int fromFloor) {
        // TODO Auto-generated method stub
        return null;
    }

}
