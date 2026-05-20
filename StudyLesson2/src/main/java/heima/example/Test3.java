package heima.example;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

//wait notify
@Slf4j(topic = "c.Test3")
public class Test3 {
    public static void main(String[] args) {
        WaitNotify wn=new WaitNotify(1,5);

            new Thread(()->{
                wn.print("a",1,2);
            }).start();

            new Thread(()->{
                wn.print("b",2,3);
            }).start();

            new Thread(()->{
                wn.print("c",3,1);
            }).start();

    }
 
}
class WaitNotify{

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    //打印
    public void print(String str,int waitFlag,int nextFlag){
        for(int i=0;i<loopNumber;i++){
            //这个循环就是a,b,c每个线程都会循环loopNumber次
            synchronized (this){
                while(flag!=waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.print(str);
                flag=nextFlag;
                this.notifyAll();
            }
        }
    }
    //等待标记
    private int flag;//1->2->3

    //循环次数
    private int loopNumber;
}