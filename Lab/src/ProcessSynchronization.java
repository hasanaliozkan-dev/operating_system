public class ProcessSynchronization {
    static int[] BUFFER = new int[10];
    static int count = 0;
    public static void main(String[] args) throws InterruptedException {

        ProcessSynchronization dene = new ProcessSynchronization();
        dene.notify();
        dene.wait(10);

    }
    public static void Consumer(){

    }
    public static void Producer(){
        while (true){


        while (count == BUFFER.length);

        }
    }
}
