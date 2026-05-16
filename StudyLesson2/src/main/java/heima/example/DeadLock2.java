package heima.example;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.*;

@Slf4j(topic="DeadLock")
public class DeadLock2 {
    public static void main(String[] args) {
        Chopstick22 c1 = new Chopstick22("1");

        Chopstick22 c2 = new Chopstick22("2");

        Chopstick22 c3 = new Chopstick22("3");

        Chopstick22 c4 = new Chopstick22("4");
        Chopstick22 c5 = new Chopstick22("5");
        new Philosopher2("苏格拉底", c1, c2).start();
        new Philosopher2("柏拉图", c2, c3).start();
        new Philosopher2("亚里士多德", c3, c4).start();
        new Philosopher2("赫拉克利特", c4, c5).start();
        new Philosopher2("阿基米德", c5, c1).start();
    }
}
@Slf4j(topic="c.Philosopher2")
class Philosopher2 extends Thread {
    Chopstick22 left;
    Chopstick22 right;

    public Philosopher2(String name,Chopstick22 left, Chopstick22 right) {
        super(name);
        this.left = left;
        this.right = right;

    }

    @Override
    public void run() {
        while (true) {
            // 尝试获得左手筷子
            synchronized (left){
                synchronized (right){
                    try {
                        eat2();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
//                if (left.tryLock()) {
//                    try {
//                        // 尝试获得右手筷子
//                        if (right.tryLock()) {
//                            try {
//                                System.out.println("eat2ing...");
//                                Thread.sleep(1000);
//                            } finally {
//                                right.unlock();
//                            }
//                        }
//                    } finally {
//                        left.unlock();
//                    }
//                }
        }
    }

    private void eat2() throws InterruptedException {
        log.debug("eat2ing");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
class Chopstick22 {
    String name;

    public Chopstick22(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "筷子{" + name + '}';
    }
}


