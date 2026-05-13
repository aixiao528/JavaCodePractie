import lombok.extern.slf4j.Slf4j;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@Slf4j(topic="c.Test1")
public class Test1{
public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 3; i++) {
        new People().start();
    }
    Thread.sleep(1000);
    for (Integer id : Mailboxes.getIds()) {
        new Postman(id, id + "号快递到了").start();
    }
}
}

//====================居民类=============================
@Slf4j(topic = "c.People")
static class People extends Thread{
    @Override
    public void run() {
        // 收信
        GuardedObject guardedObject = Mailboxes.createGuardedObject();//这样就不用自己产生id
        log.debug("开始收信id:{}", guardedObject.getId());
        Object mail = guardedObject.get(5000);
        log.debug("收到信id:{}，内容:{}", guardedObject.getId(),mail);
    }
}


//=================邮递员类==============================
@Slf4j(topic="c.Postman")
static class Postman extends Thread{
    private int id; //邮件id，对应信箱id
    private String mail;//结果 内容
    //构造方法
    @Override
    public void run() {
        GuardedObject guardedObject = Mailboxes.getGuardedObject(id);
        log.debug("开始送信i d:{}，内容:{}", guardedObject.getId(),mail);
        guardedObject.complete(mail);
    }
}

//==================邮箱类===============================
static class  Mailboxes {
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();
    private static int id = 1;

    //产生唯一的id
    private static synchronized int generateId() {
        return id++;
    }
    //这里sychronized是为了保证多个线程都在要结果时id的唯一性

    public static GuardedObject getGuardedObject(int id) {
        return boxes.remove(id);//因为返回结果之后信箱里就不用存着了
    }

    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(generateId());
        boxes.put(go.getId(), go);
        return go;
    }

    //获得所有的id
    public static Set<Integer> getIds() {
        return boxes.keySet();//key集合
    }
}

//==================================================
static class GuardedObject {
    //标识，Guarded Object
    private int id;//添加get set方法
}