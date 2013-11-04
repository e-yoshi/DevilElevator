package eventBarrier;

import api.AbstractEventBarrier;

public class EventBarrier extends AbstractEventBarrier implements Runnable {
	static final String GATEKEEPER = "Gate Keeper";
	private int numPassengers = 0;
	private boolean bridgeIsLowered = false;

	@Override
	public synchronized void arrive() {
		numPassengers++;

		if (bridgeIsLowered) {
			return;
		}

		while (!bridgeIsLowered) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
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

		while (numPassengers > 0) {
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
		numPassengers--;
		if (numPassengers == 0) {
			notifyAll();
		}

	}

	@Override
	public synchronized int waiters() {
		return numPassengers;
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

	public static void main(String args[]) {
		EventBarrier barrier = new EventBarrier();
		Thread passenger1 = new Thread(barrier, "Passenger 1");
		Thread gateKeeper = new Thread(barrier, GATEKEEPER);

		passenger1.start();

	}

}
