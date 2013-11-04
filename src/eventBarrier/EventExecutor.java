package eventBarrier;

import java.util.ArrayList;
import java.util.List;
import api.AbstractEventBarrier;

public class EventExecutor {

    private static final String filename = "testWave.cvs";
    private static AbstractEventBarrier barrier = new EventBarrier();
    private static EventFactory factory = null;
    private static List<Thread> threadList = new ArrayList<Thread>();
    private static List<Integer> waveList = new ArrayList<Integer>();
    private static List<Integer> waveInterval = new ArrayList<Integer>();
    private static GateKeeper gateKeeper = new GateKeeper();
    private static long raiseTime = 0;

    public EventExecutor(){
        factory = new EventFactory(filename);
        threadList = factory.getThreadList();
        waveList = factory.getWaveList();  
        raiseTime = factory.getRaiseTime();
        waveInterval = factory.getWaveInterval();
        execute();
    }

    private static void execute() {
        Thread gateKeeperThread = new Thread(gateKeeper);
        gateKeeperThread.start();
        int accumulator = 0;
        int arrayPos = 0;
        for(int i: waveList){
            
            for(int j=0; j<i; j++){
                threadList.get(j+accumulator).start();
            }
            
            try {
                Thread.sleep(waveInterval.get(arrayPos));
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            accumulator += i;
            arrayPos ++;
        }
    }
}
