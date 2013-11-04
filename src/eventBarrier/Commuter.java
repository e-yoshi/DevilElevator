package eventBarrier;

import api.AbstractEventBarrier;

public class Commuter implements Runnable{

    private int crossTime = 0;
    private String name = "";
    private AbstractEventBarrier barrier = null;
    
    @Override
    public void run() {
        System.out.println("Commuter "+name+" arrived!");
        barrier.arrive();
        System.out.println("Commuter "+name+" woke up! Crossing barrier for "+ crossTime + "ms!");
        try {
			Thread.sleep(crossTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Commuter "+name+" finished crossing! Completed!");
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
