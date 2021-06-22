package lesson2;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;
public class PerformSystemCheck implements  Runnable{
    private String checkWhat;
    ReentrantLock lock = new ReentrantLock();
    public PerformSystemCheck(String checkWhat) {
        this.checkWhat = checkWhat;
    }

    @Override
    //If we write synchronized in front of the methods that means -
    // if a procces running this method other procces can't acces this method.
    //But this is dramaticcly slow down the java.
    public void run() {
        lock.lock();
        System.out.println("Checking " + this.checkWhat);
        lock.unlock();
    }
}
