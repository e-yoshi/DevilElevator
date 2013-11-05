package eventBarrier;

public class Gatekeeper implements Runnable{
	private EventBarrier barrier = null;

	
	@Override
	public void run() {
		while (!barrier.isFinished()) {
			barrier.raise();
		}
		System.out.println("All commuters crossed the EventBarrier!");
	}

	public void setBarrier(EventBarrier barrier) {
		this.barrier = barrier;
	}

}
