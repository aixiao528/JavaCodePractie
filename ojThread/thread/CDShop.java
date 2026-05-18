package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.LocalTime;
import java.util.Collections;
import java.io.PrintWriter;
import java.io.IOException;



class CD {
    String name;
}

//可售cd列表
class SaleCD extends CD {
    static int id = 1;

    SaleCD() {
        name = "cd" + (id++);
        count = 10;
    }

    int count;
}

//可租cd列表
class RentCD extends CD {
    int ID;
    boolean status=false;//true booked

    RentCD(int ID) {
        this.ID = ID;
        this.name = "rent" + ID;
    }
}




//进货线程
class InThread extends Thread {
    CDShop cdshop;

    InThread(CDShop cdshop) {
        this.cdshop=cdshop;
    }

    public void run() {
        synchronized (cdshop.salelist) {
            while(cdshop.running){
                for (SaleCD cd : cdshop.salelist) {
                    if(cd.count<10){
                        cd.count=10;
                        cdshop.addRecord("in: curr cd " + cd.name + " 已补满");
                    }   
                }
                cdshop.salelist.notifyAll();
                try {
                    cdshop.salelist.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            cdshop.salelist.notifyAll();
        }
    }
}

//销售线程
class SaleThread extends Thread {
    CDShop cdshop;
    static Random r = new Random();

    SaleThread(CDShop cdshop) {
        this.cdshop=cdshop;
    }

    public void run() {
        //加锁或者同步
        outter:
        while (cdshop.running) {
            int sleepTime=r.nextInt(200);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            SaleCD cd = cdshop.salelist.get(r.nextInt(cdshop.salelist.size()));
            int count = r.nextInt(5)+1;//购买数量为5以内的随机数1~5
            synchronized (cdshop.salelist) {
                
                //通知进货线程
                if (cd.count < count) {
                    //当前这种cd还剩count张
                    cdshop.addRecord("sale: curr cd " + cd.name + " 数量不足 ");
                    cdshop.salelist.notifyAll();
                }
                boolean ifWait=false;
                while (cdshop.running && cd.count < count) {
                    if (r.nextBoolean()) {
                        cdshop.addRecord("sale: 放弃购买 " + cd.name);
                        continue outter;
                    } else {
                        ifWait=true;
                        try {
                            cdshop.addRecord("sale: 等候" + cd.name + " 进货 ");
                            //System.out.println("sale: 等候" + cd.name + "进货 ");
                            //等待进货线程进货完成
                            cdshop.salelist.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!cdshop.running) {
                    break outter;
                }
                cd.count -= count;
                if(!ifWait)
                     //没经过等待就卖了   
                    cdshop.addRecord("sale: curr cd " + cd.name + " 销售" + count + "  剩余 " + cd.count);
                else
                    //经过等待后才卖了
                    cdshop.addRecord("sale: curr cd " + cd.name + " 等候后销售" + count + "  剩余 " + cd.count);
            }
        }

    }
}

// 租借线程：可以有两个或两个以上，租借CD店的可租借CD，启动时间为200ms以内的随机数。租借序号为1-10随机序号的CD，如果该CD已经出租则随机选择等候或者放弃。如果可以借到CD则随机等候200~300ms然后归还。
class RentThread extends Thread {
    CDShop cdshop;
    static Random r=new Random();

    RentThread(CDShop cdshop) {
        this.cdshop=cdshop;
    }

    public void run(){
        outter:
        while(cdshop.running){
            int sleepTime=r.nextInt(200);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            RentCD cd = cdshop.rentlist.get(r.nextInt(cdshop.rentlist.size()));
            boolean ifWait = false;
            synchronized (cdshop.rentlist) {
                if(cd.status){
                    cdshop.addRecord("rent: curr cd " + cd.ID + " 已被借走了 ");
                    cdshop.rentlist.notifyAll();
                }
                while(cdshop.running && cd.status){
                    if(r.nextBoolean()){
                        ifWait = true;
                        try {
                            cdshop.addRecord("rent: 等候" + cd.ID + "归还");
                            cdshop.rentlist.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        cdshop.addRecord("rent: 放弃租借 " + cd.ID);
                        continue outter;
                    }
                }
                if (!cdshop.running) {
                    break;
                }
                cd.status = true;
                if(!ifWait){
                    //没经过等待就借借了
                    cdshop.addRecord("rent: curr cd " + cd.ID + " 直接借了 ");
                    //System.out.println("rent: curr cd" + cd.ID + " 直接借了 ");
                }else{
                    //经过等待后才借借
                    cdshop.addRecord("rent: curr cd " + cd.ID + " 等候后借借了 ");
                    //System.out.println("rent: curr cd" + cd.ID + " 等候后借借了 ");
                }

            int waitTime=r.nextInt(101)+200;
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (cdshop.rentlist) {
                cd.status=false;
                cdshop.addRecord("rent: curr cd " + cd.ID + " 已归还");
                cdshop.rentlist.notifyAll();
            }
        }
    }
}
} 
//一个线程管理器，统一start和统一join
class Control {
    CDShop cdshop;
    InThread in;
    SaleThread sales1;
    SaleThread sales2;
    RentThread rents1;
    RentThread rents2;

    Control(CDShop cdshop) {
        this.cdshop = cdshop;
    }

    public void startWorkers() {
        in = new InThread(cdshop);
        sales1 = new SaleThread(cdshop);
        sales2 = new SaleThread(cdshop);
        rents1 = new RentThread(cdshop);
        rents2 = new RentThread(cdshop);

        in.start();
        sales1.start();
        sales2.start();
        rents1.start();
        rents2.start();
    }

    public void joinWorkers() throws InterruptedException {
        in.join();
        sales1.join();
        sales2.join();
        rents1.join();
        rents2.join();
    }
}



public class CDShop {
    List<SaleCD> salelist = new ArrayList<>();
    List<RentCD> rentlist=new ArrayList<>();
    volatile boolean running=true;

    //日志
    List<String> records = Collections.synchronizedList(new ArrayList<>());

    //记录方法
    void addRecord(String action){
        String time=LocalTime.now().toString();
        records.add(time + " " +action);
    }

    static void runOnce(int round,PrintWriter writer) throws InterruptedException{
        SaleCD.id = 1;
        CDShop cdshop = new CDShop();
        cdshop.salelist =
        Stream.generate(() -> new SaleCD())
                .limit(10)
                .collect(Collectors.toList());
        cdshop.rentlist =
        Stream.iterate(1, i -> i + 1)
                .limit(10)
                .map(RentCD::new)
                .collect(Collectors.toList());

        Control control = new Control(cdshop);
        control.startWorkers();
        Thread.sleep(120000);
        cdshop.running=false;
        synchronized (cdshop.salelist) {
            cdshop.salelist.notifyAll();
        }
        synchronized (cdshop.rentlist) {
            cdshop.rentlist.notifyAll();
        }
        control.joinWorkers();
        
        writer.println("========第"+round+"次运行========");
        synchronized (cdshop.records) {
            for(String record:cdshop.records){
                writer.println(record);
            }
        }
        writer.println();
        writer.flush();
    }
    
    // 主方法
    public static void main(String[] args) throws Exception {
        try (PrintWriter writer = new PrintWriter("record.txt")) {
            runOnce(1,writer);
            runOnce(2,writer);
        }
    }

}
