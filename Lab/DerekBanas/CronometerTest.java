public class CronometerTest {
    public static void main(String[] args) {
        Cronometer cronometer1 = new Cronometer("cronometer1");
        Cronometer cronometer2 = new Cronometer("cronometer2");
        Cronometer cronometer3 = new Cronometer("cronometer3");

        cronometer1.start();
        cronometer2.start();
        cronometer3.start();
    }
}
