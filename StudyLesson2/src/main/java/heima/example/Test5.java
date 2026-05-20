package heima.example;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.locks.LockSupport;


//park unpark
@Slf4j(topic = "c.Test5")
public class Test5 {
    static Thread t1;
    static Thread t2;
    static Thread t3;

    public static void main(String[] args) {
        packUnpack pu=new packUnpack(5);
        log.info("打印开始");
        t1 = new Thread(() -> pu.print("a", t2), "t1");
        t2 = new Thread(() -> pu.print("b", t3), "t2");
        t3 = new Thread(() -> pu.print("c", t1), "t3");

        t1.start();
        t2.start();
        t3.start();

        //主线程先把t1唤醒
        LockSupport.unpark(t1);
    }
}

class packUnpack {
    public void print(String str,Thread next){
        for(int i=0;i<loopNumber;i++){
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(next);
        }
    }
    private int loopNumber;

    public packUnpack(int loopNumber){
        this.loopNumber=loopNumber;
    }
}
