/*
* Lucy Zhang
* SWEN-342 Final Project
* Bank Simulator - Monitor
*/

/*
 * This class is meant to help print updates every 5 secs.
 */

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Monitor extends Thread {
    private Bank bank;
    private long openTime;

    public Monitor(Bank bank, long openTime) { 
        this.bank = bank;
        this.openTime = openTime;
    }

    @Override
    public void run() {
        while(Bank.updates) {
            // wait 6 secs
            try{
                Thread.sleep(6000);
    
            } catch(InterruptedException e) {
                System.out.println("Error: " + e);
            }

            // print updates
            long elapsed = System.currentTimeMillis() - openTime;
            long stimulatedTime = elapsed / 100;
            LocalTime time = LocalTime.of(9, 0).plusMinutes(stimulatedTime);
            DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mm a");
            System.out.println("\n[MONITOR] Current Time: " + time.format(format));
            System.out.println(bank.getQueue().size() + " customers in queue");
            
            // print status of each teller
            Teller[] tellers = bank.getTellers();
            for (int i = 0; i < tellers.length; i++) {
                if(tellers[i].getStatus() == true) {
                    System.out.println("Teller " + (i + 1) + ": BUSY - " + tellers[i].customerServed() + " customers served");
                } else {
                    System.out.println("Teller " + (i + 1) + ": IDLE - " + tellers[i].customerServed() + " customers served");
                }
            }
        }
    }
}
