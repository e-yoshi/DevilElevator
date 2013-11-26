package simpleEventBarrier;

import java.util.ArrayList;
import java.util.List;

import util.EventFactory;

public class SimpleEventExecutor {

	private static final String FILENAME = "simpleBarrierEvent.csv";
	private static SimpleEventBarrier barrier = null;
	private static EventFactory factory = null;
	private static List<Thread> threadList = new ArrayList<Thread>();

	public SimpleEventExecutor() {
		barrier = new SimpleEventBarrier();
		factory = new EventFactory(FILENAME, barrier);
		threadList = factory.getThreadList();
		execute();
	}

	private static void execute() {
		for (Thread c : threadList) {
			if (!c.isAlive())
				c.start();
		}
		SimpleProducer keeper = new SimpleProducer();
		keeper.setBarrier(barrier);
		Thread keeperThread = new Thread(keeper);
		keeperThread.start();
	}

}
