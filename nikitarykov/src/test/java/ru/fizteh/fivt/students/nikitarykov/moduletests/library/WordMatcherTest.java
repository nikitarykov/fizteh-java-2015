package ru.fizteh.fivt.students.nikitarykov.moduletests.library;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static ru.fizteh.fivt.students.nikitarykov.moduletests.library.Constants.RETWEET;
import static ru.fizteh.fivt.students.nikitarykov.moduletests.library.Constants.MINUTES;
import static ru.fizteh.fivt.students.nikitarykov.moduletests.library.Constants.HOURS;
import static ru.fizteh.fivt.students.nikitarykov.moduletests.library.Constants.DAYS;

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
    public void testMatch() throws Exception {
        assertNotNull(matcher);
        assertEquals("ретвит", matcher.match(1, RETWEET));
        assertEquals("минута", matcher.match(51, MINUTES));
        assertEquals("час", matcher.match(21, HOURS));
        assertEquals("день", matcher.match(101, DAYS));
        assertEquals("ретвита", matcher.match(22, RETWEET));
        assertEquals("минуты", matcher.match(43, MINUTES));
        assertEquals("часа", matcher.match(4, HOURS));
        assertEquals("дня", matcher.match(552, DAYS));
        assertEquals("ретвитов", matcher.match(55, RETWEET));
        assertEquals("минут", matcher.match(6, MINUTES));
        assertEquals("часов", matcher.match(7, HOURS));
        assertEquals("дней", matcher.match(38, DAYS));
        assertEquals("ретвитов", matcher.match(109, RETWEET));
        assertEquals("минут", matcher.match(40, MINUTES));
        assertEquals("ретвитов", matcher.match(11, RETWEET));
        assertEquals("минут", matcher.match(12, MINUTES));
        assertEquals("часов", matcher.match(13, HOURS));
        assertEquals("дней", matcher.match(114, DAYS));
        assertEquals("ретвитов", matcher.match(115, RETWEET));
        assertEquals("минут", matcher.match(16, MINUTES));
        assertEquals("часов", matcher.match(17, HOURS));
        assertEquals("дней", matcher.match(1318, DAYS));
        assertEquals("ретвитов", matcher.match(519, RETWEET));
    }
}