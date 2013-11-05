package simpleEventBarrier;

public class SimpleProducer implements Runnable {
	private SimpleEventBarrier barrier = null;

	@Override
	public void run() {
		barrier.raise();
		if (barrier.waiters() == 0) {
			System.out.println("All commuters crossed the EventBarrier!");
		} else {
			System.out.println("EventBarrier failed! :(");
		}
	}

	public void setBarrier(SimpleEventBarrier barrier) {
		this.barrier = barrier;
	}

}
