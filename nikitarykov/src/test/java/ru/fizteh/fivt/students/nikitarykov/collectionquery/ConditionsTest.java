package ru.fizteh.fivt.students.nikitarykov.collectionquery;

import org.junit.Test;

import static org.junit.Assert.*;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.Conditions.rlike;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.Conditions.like;

/**
 * Created by Nikita Rykov on 13.12.2015.
 */
public class ConditionsTest {

    @Test
    public void testRlike() throws Exception {
        assertEquals(rlike(s -> s.toString(), ".*kov").test("surkov"), true);
        assertEquals(rlike(s -> s.toString(), ".*kov").test("bobrov"), false);
        assertEquals(rlike(s -> s.toString(), ".").test("a"), true);
        assertEquals(rlike(s -> s.toString(), ".").test("aa"), false);
        assertEquals(rlike(s -> s.toString(), "[a-c]e+").test("be"), true);
        assertEquals(rlike(s -> s.toString(), "[a-c]e+").test("bee"), true);
        assertEquals(rlike(s -> s.toString(), "[a-c]e+").test("ee"), false);
        assertEquals(rlike(s -> s.toString(), "\\s").test(" "), true);
        assertEquals(rlike(s -> s.toString(), "\\s").test("a"), false);
    }

    @Test
    public void testLike() throws Exception {
        assertEquals(like(s -> s.toString(), "%kov").test("surkov"), true);
        assertEquals(like(s -> s.toString(), "%kov").test("bobrov"), false);
        assertEquals(like(s -> s.toString(), "[%]").test("%"), true);
        assertEquals(like(s -> s.toString(), "[?]").test("?"), true);
        assertEquals(like(s -> s.toString(), "[?]a[?]").test("?a?"), true);
        assertEquals(like(s -> s.toString(), "?").test("b"), true);
        assertEquals(like(s -> s.toString(), "?").test("bb"), false);
        assertEquals(like(s -> s.toString(), "?a?a?a").test("banana"), true);
        assertEquals(like(s -> s.toString(), "?a?a?a").test("korova"), false);
        assertEquals(like(s -> s.toString(), "[a-c]e").test("be"), true);
        assertEquals(like(s -> s.toString(), "[a-c]e").test("ee"), false);
    }
}