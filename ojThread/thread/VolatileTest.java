package thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VolatileTest extends Thread{
    static boolean  state=true;
	//volatile  static boolean  state=true;
	VolatileTest(){
		this.setDaemon(true);
	}
  public void run() {
	  while(state) {

	  }
	  System.out.println("stop");

  }
 
  public static void main(String[] arg) throws InterruptedException {
	  Long time=System.currentTimeMillis();

	  for(int i=0;i<10;i++) {
		  new VolatileTest().start();
	  }
	  Thread.sleep(1000);
	  VolatileTest.state=false;


	  Runtime.getRuntime().addShutdownHook(new Thread() {
		  public void run() {
			  System.out.println("runtime:"+
					  (System.currentTimeMillis()-time)
					  );
		  }
	  });
  }
}
