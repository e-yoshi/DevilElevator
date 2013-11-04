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
    private static int raiseTime = 0;

    public EventExecutor(){
    	barrier = new EventBarrier();
    	System.out.println(barrier);
        factory = new EventFactory(filename, barrier);
        threadList = factory.getThreadList();
        waveList = factory.getWaveList();  
        raiseTime = factory.getRaiseTime();
        waveInterval = factory.getWaveInterval();
        barrier.setRaiseTime(raiseTime);
        execute();
    }

    private static void execute() {
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
            
            accumulator += waveSize-1;
            arrayPos ++;
        }
    }
}
