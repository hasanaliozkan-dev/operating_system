package lesson1;

public class GetTimeTest {

    public static void main(String[] args) {
        Thread getTime = new GetTime20();
        Runnable getTheMail1 = new GetTheMail(10);
        Runnable getTheMail2 = new GetTheMail(20);

        getTime.start();
        new Thread(getTheMail1).start();
        new Thread(getTheMail2).start();
    }
}
