package ru.fizteh.fivt.students.nikitarykov.threads.counting;

/**
 * Created by Nikita Rykov on 16.12.2015.
 */
public class Counter {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Number of threads is not found");
            return;
        }
        int numOfThreads = Integer.valueOf(args[0]);
        Turn turn = new Turn(numOfThreads);
        CountingThread[] threads = new CountingThread[numOfThreads];
        for (int i = 0; i < numOfThreads; ++i) {
            threads[i] = new CountingThread(turn, i + 1);
        }
        for (CountingThread thread : threads) {
            thread.start();
        }
    }

}
