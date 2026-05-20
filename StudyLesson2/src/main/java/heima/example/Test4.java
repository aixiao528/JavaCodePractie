package heima.example;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//ReentrantLock
@Slf4j(topic = "c.Test4")
public class Test4 {
    public static void main(String[] args) {
        AwaitSignal as=new AwaitSignal(5);
        Condition a=as.newCondition();
        Condition b=as.newCondition();
        Condition c=as.newCondition();
        //3个休息室
        new Thread(()->{
            try {
                as.print("a",a,b);
            } catch (InterruptedException e) {
                
                e.printStackTrace();
            }
        }).start();
        
        new Thread(()->{
            try {
                as.print("b",b,c);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
        new Thread(()->{
            try {
                as.print("c",c,a);
            } catch (InterruptedException e) {
                
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        as.lock();
        try{
            System.out.println("打印开始");
            a.signal();//唤醒a线程
        }finally{
            as.unlock();
        }
    }
}

class AwaitSignal extends ReentrantLock{
    private int loopNumber;

    public AwaitSignal(int loopNumber){
        this.loopNumber=loopNumber;
    }

    //参数一：打印内容，参数2：进入哪一件休息 参数三：下一个休息室
    public void print(String str,Condition current,Condition next) throws InterruptedException{
        for(int i=0;i<loopNumber;i++){
            lock();
            try{
                current.await();
                System.out.print(str);
                next.signal();
            }finally{
                unlock();
            }
        }
    }
}