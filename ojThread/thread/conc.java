package thread;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
class ConcStudent{
	int score=10;
	public  void getScore(int threadid) throws InterruptedException  {
		System.out.println(threadid+" "+this.score);
		Thread.sleep(1000);
		System.out.println(threadid+" 2 "+this.score);
	}
}
class GetCon extends Thread{
	static int threadcount=0;
	ReentrantReadWriteLock lock;
	ConcStudent stu;
	
	int threadid=threadcount++;
	GetCon(ReentrantReadWriteLock l,ConcStudent stu){
		this.stu=stu;
		this.lock=l;
		this.setDaemon(true);
		
	}
	public void run() {
		//System.out.println(threadid+"");
		WriteLock read1=lock.writeLock();
		read1.lock();
		try {
			System.out.println("holdcount "+threadid+" 1 "+lock.getReadHoldCount());
			stu.getScore(threadid);
			ReadLock read2=lock.readLock();
			read2.lock();
			System.out.println("holdcount "+threadid+" 2 "+lock.getReadHoldCount());
			read2.unlock();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//read1.unlock();
	}
}
public class conc {

	public static void main(String [] arg) throws InterruptedException {
		ReentrantReadWriteLock l=new ReentrantReadWriteLock();
		
		ConcStudent s=new ConcStudent();
		GetCon g1=new GetCon(l,s);
		GetCon g2=new GetCon(l,s);
		GetCon g3=new GetCon(l,s);
		g1.start();
		g2.start();
		g3.start();
		Thread.sleep(3000);
	}
	
}
