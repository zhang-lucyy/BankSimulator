/*
 * Lucy Zhang
 * SWEN-342 Final Project
 * Bank Simulator - Bank Teller class
 */

import java.util.LinkedList;
import java.util.Queue;

public class Teller extends Thread {
    private int customersServed;    // the # of customers served by this teller
    private long totalIdleTime;     // adds up the total idle time to calculate average
    private Queue<Customer> queue;  // customer queue
    private Bank bank;
    private LinkedList<Customer> servedCustomers;
    private long maxTransactionTime;    // max transaction time for tellers
    private long maxWaitTime;   // max wait time for tellers waiting for customers
    private boolean busy;   // indicates if the teller is currently available

    public Teller(String name, Queue<Customer> queue, Bank bank) {
        super(name);
        this.customersServed = 0;
        this.totalIdleTime = 0;
        this.queue = queue;
        this.bank = bank;
        this.servedCustomers = new LinkedList<>();
        this.maxTransactionTime = 0;
        this.busy = false;
    }

    @Override
    public void run() {
        while(Bank.bankIsOpen) {
            // waits for a customer to arrive
            long start = System.currentTimeMillis();
            if(queue.isEmpty()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e){
                    System.out.println("Error: " + e);
                }
                long end = System.currentTimeMillis();
                long idle = end-start;
                totalIdleTime += idle;
                maxWaitTime = Math.max(maxWaitTime, idle);
                continue;
            }

            bank.acquire();
            busy = true;

            // perform customer transaction
            Customer customer = queue.poll();

            if(customer == null) {
                bank.release();
                continue;
            }

            long startService = System.currentTimeMillis();
            customer.startTransaction();
            long endService = System.currentTimeMillis();
            
            long transactionTime = endService - startService;

            bank.release();
            customersServed += 1;
            servedCustomers.add(customer);
            busy = false;

            // keep track of max transaction and wait time
            maxTransactionTime = Math.max(maxTransactionTime, transactionTime);
        }
    }

    /*
     * Returns the list of served customers.
     */
    public LinkedList<Customer> servedCustomers() {
        return servedCustomers;
    }

    /*
     * Returns the longest transaction time the teller spent 
     * with a customer.
     */
    public long getMaxTransactionTime() {
        return maxTransactionTime;
    }

    /*
     * Returns the total customers this teller served during the day.
     */
    public int customerServed() {
        return customersServed;
    }

    /*
     * Returns the average time the teller waits for a customer.
     */
    public long averageIdleTime() {
        return (totalIdleTime / customersServed) / 100;
    }

    /*
     * Returns the maximum wait time for teller waiting for customers.
     */
    public long maxWaitTime() {
        return maxWaitTime;
    }

    /*
     * Indicates whether or not the teller is currently servicing a customer.
     */
    public boolean getStatus() {
        return busy;
    }
}