# BankSimulator
Stimulates the workflow of a bank with threads.

Bank tellers are represented as a thread while customers will line up in a queue.
Customers arrive every 0.5 - 2 minutes and each customer requires between 30 secs to 8 minutes for their transaction with the bank teller.

Monitor Class - Prints updates every 6 seconds which is equivalent to ~1 hour of the simulated time.
The update prints:
1. The current simulated time.
2. The number of customers waiting.
3. The status of each teller (busy, idle) and the number of customers they each have served.

At the end of the day, a report is printed with:
1. The total number of customers served during the day.
2. The number of customers served by Teller 1, by Teller 2, and by Teller 3.
3. The average time each customer spends waiting in the queue.
4. The average time each customer spends with the teller.
5. The average time tellers wait for customers.
6. The maximum customer wait time in the queue.
7. The maximum wait time for tellers waiting for customers.
8. The maximum transaction time for the tellers.
9. The maximum depth of the customer queue.
