package eventBarrier;

import api.AbstractEventBarrier;

public class EventBarrier extends AbstractEventBarrier implements Runnable {
	private int raiseTime = 0;
	static final String GATEKEEPER = "Gate Keeper";
	private volatile int numWaiting = 0;
	private volatile int numCrossing = 0;
	private volatile boolean bridgeIsLowered = false;
	

	@Override
	public synchronized void arrive() {
		if (bridgeIsLowered) {
			numCrossing ++;
			return;
		} else {
			while (!bridgeIsLowered) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public synchronized void raise() {
		if (bridgeIsLowered) {
			return;
		}

		bridgeIsLowered = true;
		this.notifyAll();

		while (numCrossing > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		bridgeIsLowered = false;

	}

	@Override
	public synchronized void complete() {
		numCrossing--;
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
		if (Thread.currentThread().getName().equals(GATEKEEPER)) {
			raise();
		} else {
			arrive();
			crossBridge();
			complete();
		}
	}

	public void crossBridge() {

	}

	public int getRaiseTime() {
		return raiseTime;
	}

	public void setRaiseTime(int raiseTime) {
		this.raiseTime = raiseTime;
	}

}
