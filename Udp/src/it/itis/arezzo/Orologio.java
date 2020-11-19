package it.itis.arezzo;

import java.util.Timer;
import java.util.TimerTask;

public class Orologio {
    static Timer timer = new Timer();
    static int seconds = 0;
    final static int CLOCK =1000;
    final static int MAX_SECONDS = 10;

    public static void main(String[] agrs) {
        TimerTask task;
        task =new TimerTask() {
            @Override
            public void run() {
                if (seconds < Orologio.MAX_SECONDS) {
                    System.out.println("Seconds = " + seconds);
                    seconds++;
                } else {
                    timer.cancel();
                    System.out.println("Timer canceled");
                }
            }
        };

        timer.schedule(task, 0, CLOCK);
        System.out.println("Exit main");
}
}

