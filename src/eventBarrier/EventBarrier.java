package eventBarrier;

import api.AbstractEventBarrier;

public class EventBarrier extends AbstractEventBarrier implements Runnable {
	private int raiseTime = 0;
	static final String GATEKEEPER = "Gate Keeper";
	private volatile int numFinished = 0;
	private volatile int numCrossing = 0;
	private volatile boolean bridgeIsLowered = false;
	private int total = 0;

	

	@Override
	public synchronized void arrive() {
		if (bridgeIsLowered) {
			updateCrossing(true);
			return;
		} else {
			while (!bridgeIsLowered) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			updateCrossing(true);
		}
	}

	@Override
	public synchronized void raise() {
		if (bridgeIsLowered) {
			return;
		}

		bridgeIsLowered = true;
		notifyAll();
		while (numCrossing > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
		}
		//bridgeIsLowered = false;
	}

	@Override
	public synchronized void complete() {
		updateCrossing(false);
		updateFinished(true);
		if (numCrossing == 0) {
			notifyAll();
		}
	}

	@Override
	public synchronized int waiters() {
		return numCrossing;
	}

	@Override
	public void run() {
		while(total > numFinished) {
			raise();
		}
	}
	
	public synchronized void updateCrossing(boolean increment) {
		if (increment)	{
			numCrossing ++;
		} else {
			numCrossing --;
		}
	}
	
	public synchronized void updateFinished(boolean increment) {
		if (increment)	{
			numFinished ++;
		} else {
			numFinished --;
		}
	}
	
	public int getRaiseTime() {
		return raiseTime;
	}

	public void setRaiseTime(int raiseTime) {
		this.raiseTime = raiseTime;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}


}
