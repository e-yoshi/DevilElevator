package simpleEventBarrier;

import api.AbstractEventBarrier;

public class SimpleEventBarrier extends AbstractEventBarrier {
	private volatile boolean opened = false;
	private volatile int waiters = 0;

	@Override
	public synchronized void arrive() {
		if (opened) {
			return;
		} else {
			notifyAll();
			waiters++;
			while (!opened) {
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
		opened = true;
		System.out.println("Opened!");
		notifyAll();
		while (waiters > 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				continue;
			}
		}
		opened = false;
		System.out.println("Closed!");
	}

	@Override
	public synchronized void complete() {
		waiters--;
		if(waiters == 0)
			notifyAll();
	}

	@Override
	public synchronized int waiters() {
		return waiters;
	}
}
