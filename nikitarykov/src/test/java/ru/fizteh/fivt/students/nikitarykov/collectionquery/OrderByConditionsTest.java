package ru.fizteh.fivt.students.nikitarykov.collectionquery;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Nikita Rykov on 13.12.2015.
 */
public class OrderByConditionsTest {

    @Test
    public void testAsc() throws Exception {
        assertTrue(OrderByConditions.asc(s -> s.toString())
                .compare("abcd", "abce") < 0);
        assertTrue(OrderByConditions.asc(s -> s.toString())
                .compare("abcd", "abcd") == 0);
        assertTrue(OrderByConditions.asc(s -> s.toString())
                .compare("abce", "abcd") > 0);
    }

    @Test
    public void testDesc() throws Exception {
        assertTrue(OrderByConditions.desc(s -> s.toString())
                .compare("abcd", "abce") > 0);
        assertTrue(OrderByConditions.desc(s -> s.toString())
                .compare("abcd", "abcd") == 0);
        assertTrue(OrderByConditions.desc(s -> s.toString())
                .compare("abce", "abcd") < 0);
    }
}