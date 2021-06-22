package SleepingBarbers;

class Barber {

    public void cutHair() {
        System.out.println("Barber: Cutting Hair --- " + Thread.currentThread().getName());
    }
}

