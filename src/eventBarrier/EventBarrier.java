package eventBarrier;

import api.AbstractEventBarrier;

public class EventBarrier extends AbstractEventBarrier {
	private int raiseTime = 0;
	private volatile int numFinished = 0;
	private volatile int numCrossing = 0;
	private volatile boolean openedBarrier = false;
	private volatile boolean finished = false;
	private int total = 0;

	@Override
	public synchronized void arrive() {
		if (openedBarrier) {
			updateCrossing(true);
			return;
		} else {
			updateCrossing(true);
			notifyAll();
			while (!openedBarrier) {
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
		if (openedBarrier && numCrossing == 0) {
			operateBarrier(false);
			System.out.println("Closing Barrier!");
			return;
		}

		if (total == numFinished)
			return;

		while (numCrossing == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
		}

		operateBarrier(true);
		System.out.println("Opening Barrier!");
		notifyAll();
		while (numCrossing > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
		}
		System.out.println("Closing Barrier!");
		operateBarrier(false);
	}

	@Override
	public synchronized void complete() {
		updateCrossing(false);
		updateFinished(true);
		if (numCrossing == 0) {
			notifyAll();
		}
		finished = (numFinished == total);
	}

	@Override
	public synchronized int waiters() {
		return numCrossing;
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

	private void operateBarrier(boolean open) {
		try {
			Thread.sleep(raiseTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		openedBarrier = open;

	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
