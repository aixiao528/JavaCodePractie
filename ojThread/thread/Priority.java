package thread;

import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

class MinPriorityThread extends Thread{
    static AtomicInteger cout=new AtomicInteger(0);
    MinPriorityThread(){
        this.setPriority(Thread.MIN_PRIORITY);
        this.setDaemon(true);
    }
    @Override
    public void run() {
        while(true) {
            //synchronized(cout)
            {
                cout.getAndIncrement();
               // break;
            }
        }

    }
}
class MaxPriorityThread extends Thread{
    static AtomicInteger cout=new AtomicInteger(0);
    MaxPriorityThread(){
        this.setPriority(Thread.MAX_PRIORITY);
        this.setDaemon(true);
    }
    public void run() {
        while(true) {
            //synchronized(cout)
            {
                cout.getAndIncrement();
             //   break;
            }
        }
    }
}
public class Priority {


    public static void main(String [] arg) throws InterruptedException {
//        ThreadPoolExecutor threadPool =
//                new ThreadPoolExecutor(10, 1000, 30,
//                        TimeUnit.SECONDS,
//                        new ArrayBlockingQueue<Runnable>(3),
//                        new ThreadPoolExecutor.DiscardOldestPolicy()); //创建线程池
//
//        Stream.generate(()->{ return new MinPriorityThread();})
//                .limit(1)
//                .forEach(e->{threadPool.execute(e);});
//        Stream.generate(()->{ return new MaxPriorityThread();})
//                .limit(1)
//                .forEach(e->{threadPool.execute(e);});

        Stream.generate(()->{ return new MinPriorityThread();})
                .limit(10)
                .forEach(e->{e.start();});
        Stream.generate(()->{ return new MaxPriorityThread();})
                .limit(10)
                .forEach(e->{e.start();});
        Thread.sleep(100);
       // threadPool.shutdownNow();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("max:"+MaxPriorityThread.cout);
                System.out.println("min:"+MinPriorityThread.cout);
            }
        }));
    }
}

