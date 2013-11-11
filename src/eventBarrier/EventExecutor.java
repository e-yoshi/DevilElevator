package eventBarrier;

import java.util.ArrayList;
import java.util.List;

import util.EventFactory;

public class EventExecutor {

	private static final String FILENAME = "barrierEvents.csv";
	private static EventBarrier barrier = null;
	private static EventFactory factory = null;
	private static List<Thread> threadList = new ArrayList<Thread>();
	private static List<Integer> waveList = new ArrayList<Integer>();
	private static List<Integer> waveInterval = new ArrayList<Integer>();

	public EventExecutor() {
		barrier = new EventBarrier();
		factory = new EventFactory(FILENAME, barrier);
		threadList = factory.getThreadList();
		waveList = factory.getWaveList();
		waveInterval = factory.getWaveInterval();
		barrier.setRaiseTime(factory.getRaiseTime());
		barrier.setTotal(threadList.size());
		execute();
	}

	private static void execute() {
		Gatekeeper keeper = new Gatekeeper();
		keeper.setBarrier(barrier);
		Thread keeperThread = new Thread(keeper);
		keeperThread.start();
		int accumulator = 0;
		int arrayPos = 0;
		for (int waveSize : waveList) {
			for (int j = 0; j < waveSize; j++) {
				Thread thread = threadList.get(j + accumulator);
				if (!thread.isAlive())
					thread.start();
			}
			try {
				Thread.sleep(waveInterval.get(arrayPos));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			accumulator += waveSize;
			arrayPos++;
		}
	}
}