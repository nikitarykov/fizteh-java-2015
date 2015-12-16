package ru.fizteh.fivt.students.nikitarykov.threads.counting;

/**
 * Created by Nikita Rykov on 16.12.2015.
 */
public class CountingThread extends Thread {
    private Turn turn;
    private int number;

    CountingThread(Turn turn, int number) {
        this.turn = turn;
        this.number = number;
    }

    @Override
    public void run() {
        while (true) {
            if (turn.getValue() == number) {
                synchronized (this) {
                    System.out.println("Thread-" + number);
                    turn.nextValue();
                }
            } else {
                Thread.yield();
            }
        }
    }
}
