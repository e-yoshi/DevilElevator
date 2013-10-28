package eventBarrier;

import api.AbstractEventBarrier;

public class EventBarrier extends AbstractEventBarrier implements Runnable{

    @Override
    public void arrive () {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void raise () {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void complete () {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int waiters () {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void run () {
        // TODO Auto-generated method stub
        
    }
    
    public static void main(String args[]) {
        EventBarrier barrier = new EventBarrier();
        Thread thread1 = new Thread(barrier, "Test 1");
    }
    
}
