public class RunnableDemo  implements Runnable {
    private final String name;

    public RunnableDemo(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i <10 ; i++) {
            System.out.println("Hello " + this.name+"\t"+i);
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new RunnableDemo("Runnable Thread 1"));
        t1.start();
        Thread t2 = new Thread(new RunnableDemo("Runnable Thread 2"));
        t2.start();
    }
}
