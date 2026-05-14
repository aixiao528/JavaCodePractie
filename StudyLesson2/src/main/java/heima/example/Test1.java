package heima.example;

import lombok.extern.slf4j.Slf4j;
import java.util.*;

@Slf4j(topic="c.Test1")
public class Test1 {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        Thread.sleep(1000);
        for (Integer id : Mailboxes.getIds()) {
            new Postman(id, id + "号快递到了").start();
        }
    }

    //====================居民类=============================
    @Slf4j(topic = "c.People")
    static class People extends Thread {
        @Override
        public void run() {
            GuardedObject guardedObject = Mailboxes.createGuardedObject();
            log.debug("开始收信id:{}", guardedObject.getId());
            Object mail = guardedObject.get(5000);
            log.debug("收到信id:{}，内容:{}", guardedObject.getId(), mail);
        }
    }

    //=================邮递员类==============================
    @Slf4j(topic = "c.PSostman")
    static class Postman extends Thread {
        private int id;
        private String mail;

        public Postman(int id, String mail) {
            this.id = id;
            this.mail = mail;
        }

        @Override
        public void run() {
            GuardedObject guardedObject = Mailboxes.getGuardedObject(id);
            log.debug("开始送信id:{}，内容:{}", guardedObject.getId(), mail);
            guardedObject.complete(mail);
        }
    }

    //==================邮箱类===============================
    //==========通用的，有一定通用性=============
    static class Mailboxes {
        private static Map<Integer, GuardedObject> boxes = new Hashtable<>();
        private static int id = 1;

        private static synchronized int generateId() {
            return id++;
        }

        public static GuardedObject getGuardedObject(int id) {
            return boxes.remove(id);
        }

        public static GuardedObject createGuardedObject() {
            GuardedObject go = new GuardedObject(generateId());
            boxes.put(go.getId(), go);
            return go;
        }

        public static Set<Integer> getIds() {
            return boxes.keySet();
        }
    }

    //==================================================
    static class GuardedObject {
        private int id;
        private Object response;

        public GuardedObject(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public Object get(long timeout) {
            synchronized (this) {
                long begin = System.currentTimeMillis();
                long passedTime = 0;
                while (response == null) {
                    long waitTime = timeout - passedTime;
                    if (waitTime <= 0) {
                        break;
                    }
                    try {
                        this.wait(waitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    passedTime = System.currentTimeMillis() - begin;
                }
                return response;
            }
        }

        public void complete(Object response) {
            synchronized (this) {
                this.response = response;
                this.notifyAll();
            }
        }
    }
}
