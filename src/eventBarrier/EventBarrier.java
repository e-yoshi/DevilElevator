package eventBarrier;

import api.AbstractEventBarrier;

public class EventBarrier extends AbstractEventBarrier implements Runnable{
    static final String GATEKEEPER = "Gate Keeper";
    private int numPassengers = 0;
    private boolean bridgeIsLowered = false;
    
    @Override
    public void arrive () {
        while (!bridgeIsLowered) {
            Thread.currentThread()
        }
        
    }

    @Override
    public void raise () {
        this.notifyAll();
        
    }

    @Override
    public void complete () {
        
        
    }

    @Override
    public int waiters () {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void run () {
        if(Thread.currentThread().getName().equals(GATEKEEPER)) {
            raise();
        }
        else {
            arrive();

        }
    }
    
    public static void main(String args[]) {
        EventBarrier barrier = new EventBarrier();
        Thread passenger1 = new Thread(barrier, "Passenger 1");
        Thread gateKeeper = new Thread(barrier, GATEKEEPER);
        
    }
    
}
