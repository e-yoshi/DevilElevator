package eventBarrier;

import java.util.ArrayList;
import java.util.List;

public class EventExecutor {

    private static final String filename = "events.csv";
    private static EventBarrier barrier = null;
    private static EventFactory factory = null;
    private static List<Thread> threadList = new ArrayList<Thread>();
    private static List<Integer> waveList = new ArrayList<Integer>();
    private static List<Integer> waveInterval = new ArrayList<Integer>();

    public EventExecutor(){
    	barrier = new EventBarrier();
        factory = new EventFactory(filename, barrier);
        threadList = factory.getThreadList();
        waveList = factory.getWaveList();  
        waveInterval = factory.getWaveInterval();
        barrier.setRaiseTime(factory.getRaiseTime());
        barrier.setTotal(threadList.size());
        execute();
    }

    private static void execute() {
    	Thread barrierThread = new Thread(barrier);
    	barrierThread.start();
        int accumulator = 0;
        int arrayPos = 0;
        for(int waveSize: waveList){
            for(int j=0; j<waveSize; j++){
            	Thread thread = threadList.get(j+accumulator);
            	if(!thread.isAlive())
            		thread.start();
            }
            try {
                Thread.sleep(waveInterval.get(arrayPos));
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            accumulator += waveSize;
            arrayPos ++;
        }
    }
}
