package thread;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Stream;

public class MultiThread {
    public static void main(String[] arg){
        Double d=0.0;
        int threadcount=1;
        int maxloop=1000000000;
        List<FutureTask> results= Collections.synchronizedList(new ArrayList<>());
        Long starttime=System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                Long endtime=System.currentTimeMillis();
                System.out.println("execute time:"+(endtime-starttime));
            }
        }));
        Stream.generate(()->{ return new CalulateThread(maxloop/threadcount);})
        .limit(threadcount)
        .forEach(e->{FutureTask<Double> futureTask=new FutureTask<Double>(e);
            Thread t=new Thread(futureTask);
            t.start();
                results.add(futureTask);

        });
       Double pai= results.stream().mapToDouble(
                e->{
                    try {
                        return (double)e.get();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();

                    } catch (ExecutionException ex) {
                        ex.printStackTrace();
                    }
                    return 0;
                }
        ).sum();
       System.out.println(pai*4);
    }

    static class CalulateThread implements Callable<Double>{
        static int nextbegin=0;
        int begin;
        int end;
        CalulateThread(int gap){
            this.begin=nextbegin;
            this.end=this.begin+gap;
            nextbegin=end;
        }
        //π = 4 * (1 - 1/3 + 1/5 - 1/7 + 1/9 - ……)
        @Override
        public Double call() throws Exception {
            double d=0;
            for(int i=begin;i<end;i++){
                if(i%2==0){
                    d+=1.0/(2*i+1);
                }
                else
                    d-=1.0/(2*i+1);
            }
            return d;
        }
    }
}
