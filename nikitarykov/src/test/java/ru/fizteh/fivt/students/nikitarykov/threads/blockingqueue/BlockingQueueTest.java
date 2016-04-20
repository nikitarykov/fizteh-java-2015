package ru.fizteh.fivt.students.nikitarykov.threads.blockingqueue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Nikita Rykov on 16.12.2015.
 */

public class BlockingQueueTest {
    private BlockingQueue<Integer> queue;
    private Adder adder1;
    private Adder adder2;
    private Getter getter1;
    private Getter getter2;
    private List<Integer> list1;
    private List<Integer> list2;

    private class Adder extends Thread {
        private List<Integer> list;
        private int timeout;

        Adder(List<Integer> list, int timeout) {
            this.list = list;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            try {
                if (timeout <= 0) {
                    queue.offer(list);
                } else {
                    queue.offer(list, timeout);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Getter extends Thread {
        private int n;
        private int timeout;
        private List<Integer> result;

        Getter(int n, int timeout) {
            this.n = n;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            try {
                if (timeout > 0) {
                    result = queue.take(n, timeout);
                } else {
                    result = queue.take(n);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public List<Integer> getResult() {
            return result;
        }
    }

    @Before
    public void setUp() throws Exception {
        queue = new BlockingQueue<>(10);
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        for (int i = 10; i < 15; ++i) {
            list1.add(i);
        }
        for (int i = 0; i < 10; ++i) {
            list2.add(i);
        }
    }

    @Test
    public void test1() throws Exception {
        adder1 = new Adder(list1, 0);
        adder2 = new Adder(list2, 0);
        getter1 = new Getter(5, 0);
        getter2 = new Getter(10, 0);
        adder1.start();
        if (adder1.isAlive()) {
            adder1.join();
        }
        adder2.start();
        getter1.start();
        if (getter1.isAlive()) {
            getter1.join();
        }
        if (adder2.isAlive()) {
            adder2.join();
        }
        getter2.start();
        if (getter2.isAlive()) {
            getter2.join();
        }
        List<Integer> result1 = getter1.getResult();
        List<Integer> result2 = getter2.getResult();
        assertEquals(result1.size(), list1.size());
        for(int i = 0; i < result1.size(); ++i) {
            assertEquals(result1.get(i), list1.get(i));
        }
        assertEquals(result2.size(), list2.size());
        for(int i = 0; i < result2.size(); ++i) {
            assertEquals(result2.get(i), list2.get(i));
        }
    }

    @Test
     public void test2() throws Exception {
        adder1 = new Adder(list1, 1000);
        adder2 = new Adder(list2, 2000);
        getter1 = new Getter(5, 2000);
        getter2 = new Getter(10, 3000);
        getter1.start();
        getter2.start();
        adder1.start();
        adder2.start();
        if (adder1.isAlive()) {
            adder1.join();
        }
        if (getter1.isAlive()) {
            getter1.join();
        }
        if (adder2.isAlive()) {
            adder2.join();
        }
        if (getter2.isAlive()) {
            getter2.join();
        }
        List<Integer> result1 = getter1.getResult();
        List<Integer> result2 = getter2.getResult();
        assertEquals(result1.size(), list1.size());
        for(int i = 0; i < result1.size(); ++i) {
            assertEquals(result1.get(i), list1.get(i));
        }
        assertEquals(result2.size(), list2.size());
        for(int i = 0; i < result2.size(); ++i) {
            assertEquals(result2.get(i), list2.get(i));
        }
    }

    @Test(timeout = 5000)
    public void test3() throws Exception {
        list2.add(10);
        adder1 = new Adder(list2, 2000);
        getter1 = new Getter(5, 2000);
        getter1.start();
        if (getter1.isAlive()) {
            getter1.join();
        }
        adder1.start();
        if (adder1.isAlive()) {
            adder1.join();
        }
    }
}