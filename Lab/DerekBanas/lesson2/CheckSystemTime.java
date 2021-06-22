package lesson2;

import java.text.DateFormat;
import java.util.*;

public class CheckSystemTime implements Runnable{

    public void run(){
        Date rightNow;
        Locale currentLocale;
        DateFormat timeFormatter;
        String timeOutput;

        for (int i = 1; i <=20 ; i++) {
            rightNow = new Date();
            currentLocale = new Locale("tr");
            timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT,currentLocale);


            timeOutput = timeFormatter.format(rightNow);


            System.out.println(timeOutput);

            System.out.println();

            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){

            }
        }

    }

}
