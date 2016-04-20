package ru.fizteh.fivt.students.nikitarykov.threads.counting;

/**
 * Created by Nikita Rykov on 16.12.2015.
 */
public class Turn {
    private int numOfThreads;
    private volatile int value;

    Turn(int numOfThreads) {
        this.numOfThreads = numOfThreads;
        value = 1;
    }

    public int getValue() {
        return value;
    }

    public void nextValue() {
        value = value % numOfThreads + 1;
    }

}
