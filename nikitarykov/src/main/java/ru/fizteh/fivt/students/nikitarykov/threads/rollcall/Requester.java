package ru.fizteh.fivt.students.nikitarykov.threads.rollcall;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by Nikita Rykov on 16.12.2015.
 */
public class Requester {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Number of threads is not found");
            return;
        }
        int numOfThreads = Integer.valueOf(args[0]);
        Responder[] threads = new Responder[numOfThreads];
        CyclicBarrier barrier = new CyclicBarrier(numOfThreads,
                new Runnable() {
                    public void run() {
                        boolean result = true;
                        for (Responder thread : threads) {
                            result &= thread.getResponse();
                        }
                        if (!result) {
                            for (Responder thread : threads) {
                                 thread.setResponse(false);
                            }
                            System.out.println("Are you ready?");
                        }
                    }
                });
        for (int i = 0; i < numOfThreads; ++i) {
            threads[i] = new Responder(barrier);
        }
        System.out.println("Are you ready?");
        for (Thread thread : threads) {
            thread.start();
        }
    }
}
