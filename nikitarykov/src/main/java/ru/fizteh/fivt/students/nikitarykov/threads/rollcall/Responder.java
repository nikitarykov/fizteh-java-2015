package ru.fizteh.fivt.students.nikitarykov.threads.rollcall;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Nikita Rykov on 16.12.2015.
 */
public class Responder extends Thread {
    private CyclicBarrier barrier;
    private boolean response;
    private Random rand = new Random();

    public void setResponse(boolean response) {
        this.response = response;
    }

    public boolean getResponse() {
        return response;
    }

    Responder(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        while (!response) {
            if (rand.nextInt() % 10 == 0) {
                System.out.println("No");
            } else {
                System.out.println("Yes");
                response = true;
            }
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
