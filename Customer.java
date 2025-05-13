/*
* Lucy Zhang
* SWEN-342 Final Project
* Bank Simulator - Customer class
*/

import java.util.Random;

public class Customer {
    private long arrivalTime;   // arrival in queue time
    private long startServiceTime;  // when they get to the teller
    private long serviceDuration;    // how long their transaction takes
    private Random random = new Random();
    
    public Customer() {
        arrivalTime = System.currentTimeMillis();
        startServiceTime = 0;
        // each transaction takes 30 secs to 8 mins
        // equivalent to 50 milis to 800 milis
        // 100 milis = 1 min
        serviceDuration = 50 + random.nextInt(751);
    }

    /*
     * Stimulates a customer completing their transaction with a teller.
     */
    public void startTransaction() {
        startServiceTime = System.currentTimeMillis();

        try {
            Thread.sleep(serviceDuration);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e);
        }
    }

    /*
     * Returns the time spent waiting in queue.
     */
    public long waitTime() {
        return startServiceTime - arrivalTime;
    }

    /*
     * Returns the time spent with teller.
     */
    public long serviceDuration() {
        return serviceDuration;
    }
}
