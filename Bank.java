/*
 * Lucy Zhang
 * SWEN-342 Final Project
 * Bank Simulator - Bank (Main class)
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Bank {
    public static volatile boolean bankIsOpen = true;
    public static volatile boolean updates = true;
    private Semaphore mutex;
    private Queue<Customer> queue;
    private Teller[] tellers;

    public Bank() {
        mutex = new Semaphore(3);
        queue = new LinkedList<>();
        tellers = new Teller[3];
    }

    public void acquire() {
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public void release() {
        mutex.release();
    }

    public Queue<Customer> getQueue() {
        return queue;
    }

    public Teller[] getTellers() {
        return tellers;
    }

    public static void main(String[] args) {
        Bank bank = new Bank();
        LinkedList<Customer> customers = new LinkedList<>();
        
        Random random = new Random();
        int maxQueueDepth = 0;

        // Bank opens
        System.out.println("9am - Bank opens!");
        long openTime = System.currentTimeMillis();
        Monitor monitor = new Monitor(bank, openTime);

        // create 3 tellers
        for(int i = 0; i < 3; i++) {
            bank.tellers[i] = new Teller("Teller " + (i+1), bank.queue, bank);
            bank.tellers[i].start();
        }
        monitor.start();

        while(bankIsOpen) {
            if(System.currentTimeMillis() - openTime >= 42000) {
                bankIsOpen = false;
                break;
            }
            // customers start to arrive
            try {
                double wait = random.nextDouble() * 1.5 + 0.5;    // 0.5-2 mins
                long delay = (long)(wait * 100);    // 100 milisec = 1 min
                Thread.sleep(delay);
            } catch(InterruptedException e) {
                System.out.println("Error: " + e);
            }

            // customer lines up in queue
            Customer customer = new Customer();
            bank.queue.add(customer);
            customers.add(customer);

            maxQueueDepth = Math.max(maxQueueDepth, bank.queue.size());
        }

        updates = false;

        for(Teller teller : bank.tellers) {
            try {
                teller.join();
            } catch(InterruptedException e) {
                System.out.println("Error: " + e);
            }
        }

        System.out.println("\n4pm - Bank is closed!");

        // total customers served
        int totalServed = 0;
        // average time tellers wait for customers
        long averageTellerWait = 0;
        // max wait time for tellers
        long maxTellerWait = 0;
        // max transaction time
        long maxTellerTransaction = 0;

        // # of customers served by each teller
        int teller1 = 0;
        int teller2 = 0;
        int teller3 = 0;

        // average time each customer spends in queue
        long averageQueueTime = 0;
        // max customer wait time in queue
        long maxWaitTime = 0;
        // average time each customer spends with teller
        long averageTransactionTime = 0;

        System.out.println("\n----- End of Day Report -----");
        for(int i = 0; i < 3; i++) {
            if(i == 0) {
                teller1 = bank.tellers[i].customerServed();
            } else if(i == 1) {
                teller2 = bank.tellers[i].customerServed();
            } else {
                teller3 = bank.tellers[i].customerServed();
            }
            totalServed += bank.tellers[i].customerServed();
            maxTellerWait = Math.max(maxTellerWait,bank. tellers[i].maxWaitTime());
            maxTellerTransaction = Math.max(maxTellerTransaction, bank.tellers[i].getMaxTransactionTime());
            averageTellerWait += bank.tellers[i].averageIdleTime();

            for(Customer customer : bank.tellers[i].servedCustomers()) {
                averageQueueTime += customer.waitTime();
                maxWaitTime = Math.max(maxWaitTime, customer.waitTime());
                averageTransactionTime += customer.serviceDuration();
            }
        }
        // # of customers served
        System.out.println("Total Customers served: " + totalServed);
        System.out.println("Teller 1: " + teller1);
        System.out.println("Teller 2: " + teller2);
        System.out.println("Teller 3: " + teller3);

        System.out.println("Average time each customer spends in queue: " + (averageQueueTime / customers.size())/100 + " mins");
        System.out.println("Average time each customer spends with the teller: " + (averageTransactionTime / customers.size())/100 + " mins");
        System.out.println("Average time tellers wait for customers: " + (averageTellerWait / 3) + " mins");

        System.out.println("Max customer wait time in queue: "+ maxWaitTime/100 + " mins");
        System.out.println("Max wait time for tellers waiting for customers: " + maxTellerWait/100 + " mins");
        System.out.println("Max transaction time for tellers: " + maxTellerTransaction/100 + " mins");

        // max depth of queue
        System.out.println("Max depth of queue: " + maxQueueDepth);
    }
}
