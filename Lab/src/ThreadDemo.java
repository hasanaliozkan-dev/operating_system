public class ThreadDemo extends Thread{
    private final String name;

    @Override
    public void run() {
        for (int i = 0; i <10 ; i++) {
            System.out.println("Hello " + this.name +"\t"+i);
        }

    }public ThreadDemo(String name){
        this.name = name; }

    public static void main(String[] args) {
        ThreadDemo thread1 = new ThreadDemo("Thread 1 ");
        thread1.start();

        ThreadDemo thread2 = new ThreadDemo("Thread 2 ");
        thread2.start();
    }
}
