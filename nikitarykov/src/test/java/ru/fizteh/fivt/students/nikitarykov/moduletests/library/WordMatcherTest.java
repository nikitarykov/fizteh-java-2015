package ru.fizteh.fivt.students.nikitarykov.moduletests.library;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Nikita Rykov on 04.12.2015.
 */
public class WordMatcherTest {
    private static WordMatcher matcher;

    @BeforeClass
    public static void setWordMatcher() throws Exception {
        matcher = new WordMatcher();
    }

    @Test
    public void testWordForRetweet() throws Exception {
        assertNotNull(matcher);
        assertEquals("ретвит", matcher.wordForRetweet(1));
        assertEquals("ретвита", matcher.wordForRetweet(22));
        assertEquals("ретвита", matcher.wordForRetweet(133));
        assertEquals("ретвита", matcher.wordForRetweet(4));
        assertEquals("ретвитов", matcher.wordForRetweet(55));
        assertEquals("ретвитов", matcher.wordForRetweet(666));
        assertEquals("ретвитов", matcher.wordForRetweet(77));
        assertEquals("ретвитов", matcher.wordForRetweet(8));
        assertEquals("ретвитов", matcher.wordForRetweet(99));
        assertEquals("ретвитов", matcher.wordForRetweet(100));
        assertEquals("ретвитов", matcher.wordForRetweet(11));
        assertEquals("ретвитов", matcher.wordForRetweet(112));
        assertEquals("ретвитов", matcher.wordForRetweet(13));
        assertEquals("ретвитов", matcher.wordForRetweet(214));
        assertEquals("ретвитов", matcher.wordForRetweet(15));
        assertEquals("ретвитов", matcher.wordForRetweet(1316));
        assertEquals("ретвитов", matcher.wordForRetweet(17));
        assertEquals("ретвитов", matcher.wordForRetweet(518));
        assertEquals("ретвитов", matcher.wordForRetweet(19));

    }

    @Test
    public void testWordForMinutes() throws Exception {
        assertNotNull(matcher);
        assertEquals("минута", matcher.wordForMinutes(1));
        assertEquals("минуты", matcher.wordForMinutes(22));
        assertEquals("минуты", matcher.wordForMinutes(33));
        assertEquals("минуты", matcher.wordForMinutes(4));
        assertEquals("минут", matcher.wordForMinutes(55));
        assertEquals("минут", matcher.wordForMinutes(26));
        assertEquals("минут", matcher.wordForMinutes(37));
        assertEquals("минут", matcher.wordForMinutes(8));
        assertEquals("минут", matcher.wordForMinutes(49));
        assertEquals("минут", matcher.wordForMinutes(50));
        assertEquals("минут", matcher.wordForMinutes(11));
        assertEquals("минут", matcher.wordForMinutes(12));
        assertEquals("минут", matcher.wordForMinutes(13));
        assertEquals("минут", matcher.wordForMinutes(14));
        assertEquals("минут", matcher.wordForMinutes(15));
        assertEquals("минут", matcher.wordForMinutes(16));
        assertEquals("минут", matcher.wordForMinutes(17));
        assertEquals("минут", matcher.wordForMinutes(18));
        assertEquals("минут", matcher.wordForMinutes(19));
    }

    @Test
    public void testWordForHours() throws Exception {
        assertNotNull(matcher);
        assertEquals("час", matcher.wordForHours(21));
        assertEquals("часа", matcher.wordForHours(22));
        assertEquals("часа", matcher.wordForHours(23));
        assertEquals("часа", matcher.wordForHours(4));
        assertEquals("часов", matcher.wordForHours(5));
        assertEquals("часов", matcher.wordForHours(6));
        assertEquals("часов", matcher.wordForHours(7));
        assertEquals("часов", matcher.wordForHours(8));
        assertEquals("часов", matcher.wordForHours(9));
        assertEquals("часов", matcher.wordForHours(10));
        assertEquals("часов", matcher.wordForHours(11));
        assertEquals("часов", matcher.wordForHours(12));
        assertEquals("часов", matcher.wordForHours(13));
        assertEquals("часов", matcher.wordForHours(14));
        assertEquals("часов", matcher.wordForHours(15));
        assertEquals("часов", matcher.wordForHours(16));
        assertEquals("часов", matcher.wordForHours(17));
        assertEquals("часов", matcher.wordForHours(18));
        assertEquals("часов", matcher.wordForHours(19));
    }

    @Test
    public void testWordForDays() throws Exception {
        assertNotNull(matcher);
        assertEquals("день", matcher.wordForDays(21));
        assertEquals("дня", matcher.wordForDays(2));
        assertEquals("дня", matcher.wordForDays(123));
        assertEquals("дня", matcher.wordForDays(104));
        assertEquals("дней", matcher.wordForDays(55));
        assertEquals("дней", matcher.wordForDays(6));
        assertEquals("дней", matcher.wordForDays(117));
        assertEquals("дней", matcher.wordForDays(68));
        assertEquals("дней", matcher.wordForDays(9));
        assertEquals("дней", matcher.wordForDays(100));
        assertEquals("дней", matcher.wordForDays(11));
        assertEquals("дней", matcher.wordForDays(12));
        assertEquals("дней", matcher.wordForDays(313));
        assertEquals("дней", matcher.wordForDays(414));
        assertEquals("дней", matcher.wordForDays(15));
        assertEquals("дней", matcher.wordForDays(816));
        assertEquals("дней", matcher.wordForDays(17));
        assertEquals("дней", matcher.wordForDays(618));
        assertEquals("дней", matcher.wordForDays(19));
    }
}