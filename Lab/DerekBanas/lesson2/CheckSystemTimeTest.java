package lesson2;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeUnit.*;
public class CheckSystemTimeTest {

    public static void main(String[] args) {
        addThreadsPool();
    }

    private static void addThreadsPool() {
        ScheduledThreadPoolExecutor eventPool = new ScheduledThreadPoolExecutor(5);
        eventPool.scheduleAtFixedRate(new CheckSystemTime(),0,2, TimeUnit.SECONDS);
        eventPool.scheduleAtFixedRate(new PerformSystemCheck("Mail"),5,5, TimeUnit.SECONDS);
        eventPool.scheduleAtFixedRate(new PerformSystemCheck("Clendar"),10,10, TimeUnit.SECONDS);

        System.out.println("Number of Threads : " + Thread.activeCount());
        Thread[] listOfThreads = new Thread[Thread.activeCount()];
        Thread.enumerate(listOfThreads);
        for (Thread t :listOfThreads) {
            System.out.println(t.getName() + t.getId() + t.getPriority());
        }

    }
}
