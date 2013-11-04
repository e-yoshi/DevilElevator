package eventBarrier;

import api.AbstractEventBarrier;

public class Minstrel implements Runnable{

	private int id = 0;
    private int crossTime = 0;
    private AbstractEventBarrier barrier = null;
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("Minstrel "+id+" started!");
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
