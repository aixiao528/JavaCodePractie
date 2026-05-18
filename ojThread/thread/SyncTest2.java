package thread;

import java.util.stream.Stream;
class syncobject{
	int value=0;
	synchronized public void setvalue(int i) {
		value=i;
	}
	 public void inc()  {
		value++;
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
// class SyncTest3 extends Thread {
//
//	 static volatile syncobject state=SyncTest2.state; 
//	public void run() {
//		for(int i=0;i<10000;i++) {
//			//synchronized(state) {
//			state.inc();
//			state.notifyAll();
//			//}
//		}
//	}
//}
public class SyncTest2 extends Thread {

	 static volatile syncobject state=new syncobject(); 
	public void run() {
		for(int i=0;i<100;i++) {
			synchronized(state) {
				synchronized(this) {
					state.inc();
					state.setvalue(i);
				}
			}
		}
	}
	
	public static void main(String[] arg) throws InterruptedException {
		SyncTest2 thread=new SyncTest2();
		Long starttime=System.currentTimeMillis();
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//			@Override
//			public void run() {
//				System.out.println("JVM runtime"+(System.currentTimeMillis()-starttime));
//			}
//		});
		Stream.generate(()->{return new SyncTest2();})
		.limit(100)
		.forEach(e->{e.start();});
//		Stream.generate(()->{return new SyncTest3();})
//		.limit(10)
//		.forEach(e->{e.start();});
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				Long endtime=System.currentTimeMillis();
				System.out.println(endtime-starttime);
				System.out.println("SyncTest2.state"+SyncTest2.state.value);
			}
		}));

	}
}
