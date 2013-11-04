package eventBarrier;

public class Minstrel implements Runnable{

    private int crossTime = 0;
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("Minstrel started!");

    }


    public int getCrossTime () {
        return crossTime;
    }


    public void setCrossTime (int crossTime) {
        this.crossTime = crossTime;
    }
    
}
