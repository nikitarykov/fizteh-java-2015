package ru.fizteh.fivt.students.nikitarykov.threads.blockingqueue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Nikita Rykov on 16.12.2015.
 */
public class BlockingQueue<T> {
    private Queue<T> queue;
    private int capacity;
    private ReentrantLock lock;
    private Condition notFull;
    private Condition notEmpty;

    BlockingQueue(int capacity) {
        queue = new LinkedList<>();
        this.capacity = capacity;
        lock = new ReentrantLock();
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
    }

    void offer(List<? extends T> list) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() + list.size() > capacity) {
                notFull.await();
            }
            queue.addAll(list);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    List<T> take(int n) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() < n) {
                notEmpty.await();
            }
            List<T> result = new ArrayList<>();
            for (int i = 0; i < n; ++i) {
                result.add(queue.poll());
            }
            notFull.signalAll();
            return result;
        } finally {
            lock.unlock();
        }
    }

    void offer(List<? extends T> list, long timeout) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() + list.size() > capacity) {
                if (timeout <= 0) {
                    return;
                }
                long startTime = System.currentTimeMillis();
                notFull.await(timeout, TimeUnit.MILLISECONDS);
                long endTime = System.currentTimeMillis();
                timeout -= endTime - startTime;
            }
            queue.addAll(list);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }
    List<T> take(int n, long timeout) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() < n) {
                if (timeout <= 0) {
                    return null;
                }
                long startTime = System.currentTimeMillis();
                notEmpty.await(timeout, TimeUnit.MILLISECONDS);
                long endTime = System.currentTimeMillis();
                timeout -= endTime - startTime;
            }
            List<T> result = new ArrayList<>();
            for (int i = 0; i < n; ++i) {
                result.add(queue.poll());
            }
            notFull.signalAll();
            return result;
        } finally {
            lock.unlock();
        }
    }
}
