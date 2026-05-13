package heima.example1;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

@Slf4j(topic="c.Test2")
public class Test2 {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(2);
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                queue.put(new Message(id,"值"+id));
            }, "生产者" + i).start();
        }

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    Message message = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"消费者").start();
    }
}

@Slf4j(topic="c.Message")
//消息队列类，Java间线程之间通信
class MessageQueue {
    //双向队列很适合存储消息
    private LinkedList<Message> list = new LinkedList<>();//消息的队列集合
    private int capacity;//队列容量
    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    //获取消息
    public Message take() {
        //检查队列是否为空
        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空，消费者线程等待");
                    //sout是打印快捷键
                    // sout(Thread.currentThread().getName() + ":队列为空，消费者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //从队列的头部获取消息返回
            Message message = list.removeFirst();
            log.debug("已消费消息{}",message);
            //sout(Thread.currentThread().getName() + "：已消费消息--" + message);
            list.notifyAll();
            return message;
        }
    }

    //存入消息
    public void put(Message message) {
        synchronized (list) {
            //检查队列是否满
            while (list.size() == capacity) {
                try {
                    log.debug("队列已满，生产者线程等待");
                    //sout(Thread.currentThread().getName()+":队列为已满，生产者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //将消息加入队列尾部
            list.addLast(message);
            log.debug("已生产消息{}",message);
            //sout(Thread.currentThread().getName() + ":已生产消息--" + message);
            list.notifyAll();
        }
    }
}


//主要是为了包装id
//final 为了不被extends继承
final class Message {
    private int id;
    private Object value;
    //get set


    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}