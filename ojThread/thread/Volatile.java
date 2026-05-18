package thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Volatile extends Thread{
   volatile static int count=0;
   Lock l;
   Volatile(Lock l){
	   this.l=l;
   }
  public void run() {
//	  while(state) {
//		  
//	  }
//	  System.out.println("stop");
	  for(int i=0;i<1000;i++) {
		  l.lock();
		  count++;
		  l.unlock();
	  }
  }
 
  public static void main(String[] arg) {
//	  Volatile v=new Volatile();
//	  v.start();
	  Long time=System.currentTimeMillis();
	  ReentrantLock lock=new ReentrantLock();
	  for(int i=0;i<10;i++) {
		  new Volatile(lock).start();
	  }
	  Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("runtime:"+
			(System.currentTimeMillis()-time)
			+"count:"+Volatile.count);
			}
		});
	 
  }
}
