package eventBarrier;

import api.AbstractEventBarrier;

public class Commuter extends Thread{

    private int crossTime = 0;
    private String name = "";
    private AbstractEventBarrier barrier = null;
    
    public Commuter() {
    	super();
    	this.name = super.getName();
		// TODO Auto-generated constructor stub
	}
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("Minstrel "+name+" arrived!"+barrier);
        barrier.arrive();
        System.out.println("Minstrel "+name+" woke up! Crossing barrier for "+ crossTime + "ms!");
        try {
			Thread.sleep(crossTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Minstrel "+name+" finished crossing! Completed!");
        barrier.complete();
    }


    public int getCrossTime () {
        return crossTime;
    }


    public void setCrossTime (int crossTime) {
        this.crossTime = crossTime;
    }


	public AbstractEventBarrier getBarrier() {
		return barrier;
	}


	public void setBarrier(AbstractEventBarrier barrier) {
		this.barrier = barrier;
	}


}
