package thread;
class syncStudent{
	Integer score=10;
	public  void getScore(int threadid) throws InterruptedException {
		System.out.println(threadid+" "+this.score);
		//this.wait(1000);
		System.out.println(threadid+" 2 "+this.score);
	}
	public  void printScore(int threadid,int value) throws InterruptedException {
		synchronized(this) {
		synchronized(score)
		{
		this.score=value;
		System.out.println(threadid+""+this.score);
		}
		}
	}
}
class Get extends Thread{
	static int threadcount=0;
	syncStudent stu;
	int threadid=threadcount++;
	Get(syncStudent s){
		this.stu=s;
		this.setDaemon(true);
		
	}
	public void run() {
		//System.out.println(threadid+"");
		try {
			
			while(true) {
				Object syncStudent = null;
				synchronized(syncStudent) {
			stu.printScore(threadid,threadcount);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
public class Sync {

	public static void main(String [] arg) throws InterruptedException {
		syncStudent s=new syncStudent();
		Get g1=new Get(s);
		Get g2=new Get(s);
		Get g3=new Get(s);
		g1.start();
		g2.start();
		g3.start();
		Thread.sleep(1000);
	}
	
}
