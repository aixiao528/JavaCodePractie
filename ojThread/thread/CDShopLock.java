package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SaleCDLock extends SaleCD {
	Lock l = new ReentrantLock();
	Condition signal = l.newCondition();
	int count;
}

class InCDThreadLock extends Thread {
	SaleCDLock cd;

	InCDThreadLock(SaleCDLock cd) {
		this.cd = cd;
	}

	public void run() {
		while (true) {
			cd.l.lock();
			cd.count = 10;
			
			System.out.println("in: curr cd" + cd.name + " " + cd.count);
			cd.signal.signalAll();
			
			
			try {
				cd.signal.await(1, TimeUnit.SECONDS);;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cd.l.unlock();
		}

	}
}

class InThreadLock extends Thread {
	List<SaleCDLock> salelist;

	InThreadLock(List<SaleCDLock> cd) {
		this.salelist = cd;
		this.setPriority(MAX_PRIORITY);
	}

	public void run() {
		for (SaleCDLock cd : salelist) {
			new InCDThreadLock(cd).start();
		}
	}
}

class SaleThreadLock extends Thread {
	List<SaleCDLock> salelist;
	static Random r = new Random();

	SaleThreadLock(List<SaleCDLock> cd) {

		this.salelist = cd;
	}

	public void run() {
		// 加锁或者同步
		outter: while (true) {
			SaleCDLock cd = salelist.get(r.nextInt(salelist.size()));
			int count = r.nextInt(6);
			cd.l.lock();
			if (cd.count < count) {
				System.out.println("sale: curr cd" + cd.name + "数量不足 ");
				cd.signal.signalAll();
			}
			while (cd.count < count) {
				if (r.nextBoolean()) {
					continue outter;
				}
				else {
					try {
						cd.signal.await(1, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			cd.count -= count;
			cd.l.unlock();
			System.out.println("sale: curr cd" + cd.name + " 销售" + count + "剩余 " + cd.count);
		
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		

	}
}

class ControlLock extends Thread {
	CDShopLock cdshop;

	ControlLock(CDShopLock cdshop) {
		this.setDaemon(true);
		this.cdshop = cdshop;
	}

	public void run() {
		InThreadLock in = new InThreadLock(cdshop.salelist);
		in.start();
		SaleThreadLock sales = new SaleThreadLock(cdshop.salelist);
		sales.start();
		sales = new SaleThreadLock(cdshop.salelist);
		sales.start();
	}
}

public class CDShopLock {
	List<SaleCDLock> salelist = new ArrayList();
	List<RentCD> rentlist;

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		CDShopLock cdshop = new CDShopLock();
		cdshop.salelist=Stream.generate(()->{
			return new SaleCDLock();
		})
		.limit(10)
		.collect(Collectors.toList());
		
		ControlLock control = new ControlLock(cdshop);
		control.start();
		Thread.sleep(1000);

	}

}
