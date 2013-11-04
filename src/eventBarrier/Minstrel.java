package eventBarrier;

import api.AbstractEventBarrier;

public class Minstrel implements Runnable{

	private int id = 0;
    private int crossTime = 0;
    private AbstractEventBarrier barrier = null;
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("Minstrel "+id+" arrived!");
        barrier.arrive();
        System.out.println("Minstrel "+id+" woke up! Crossing barrier for "+ crossTime + "ms!");
        try {
			Thread.sleep(crossTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Minstrel "+id+" finished crossing! Completed!");
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


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

}
