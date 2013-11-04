package eventBarrier;

import api.AbstractEventBarrier;

public class EventBarrier extends AbstractEventBarrier implements Runnable {
	private int raiseTime = 0;
	static final String GATEKEEPER = "Gate Keeper";
	private volatile int numFinished = 0;
	private volatile int numCrossing = 0;
	private volatile boolean openedBarrier = false;
	private int total = 0;

	@Override
	public synchronized void arrive() {
		if (openedBarrier) {
			updateCrossing(true);
			return;
		} else {
			notifyAll();
			while (!openedBarrier) {
				updateCrossing(true);
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}

	@Override
	public synchronized void raise() {
		if (openedBarrier && numCrossing == 0) {
			openedBarrier = false;
			System.out.println("Closing Barrier!");
			return;
		}

		while (numCrossing == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// e.printStackTrace();
				continue;
			}
		}
		
		openedBarrier = true;
		System.out.println("Opening Barrier!");
		notifyAll();
		while (numCrossing > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// e.printStackTrace();
				continue;
			}
		}
		System.out.println("Closing Barrier!");
		openedBarrier = false;

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
		while (total > numFinished) {
			raise();
		}
		System.out.println("All commuters crossed the EventBarrier!");
	}

	public synchronized void updateCrossing(boolean increment) {
		if (increment) {
			numCrossing++;
		} else {
			numCrossing--;
		}
	}

	public synchronized void updateFinished(boolean increment) {
		if (increment) {
			numFinished++;
		} else {
			numFinished--;
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
