public class Cronometer implements Runnable{

    private Thread t;
    private String threadName;

    public Cronometer(String threadName) {
        this.threadName = threadName;
        System.out.println("It has been creating : "+threadName);
    }

    @Override
    public void run() {
        System.out.println("It has been working : " + threadName);
        try {
            for (int i = 1; i <=10 ; i++) {
                System.out.println(threadName + " : " + i);
                Thread.sleep(1000);

            }
        }catch (InterruptedException e){
            System.out.println("Interrupted : " + threadName);
        }

        System.out.println("It is finish : "+threadName);

    }
    public void start(){
        System.out.println("Thread is creating.");
        if(t == null){
            t = new Thread(this,threadName);
            t.start();
        }
    }

    public static void main(String[] args) {

    }

}
