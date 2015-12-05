package ru.fizteh.fivt.students.nikitarykov.moduletests.library;

import java.util.Date;
import java.time.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Nikita Rykov on 04.12.2015.
 */
public class DataParserTest {

    @Test
    public void testPrintTimeJustNow() throws Exception {
        LocalDateTime justNow = LocalDateTime.now().minusMinutes(1).minusSeconds(30);
        Date date = Date.from(justNow.atZone(ZoneId.systemDefault()).toInstant());
        DataParser parser = new DataParser();
        String result = parser.getTime(date);
        assertEquals("только что ", result);
    }

    @Test
    public void testPrintTimeLessThanHour() throws Exception {
        LocalDateTime lessThanHour = LocalDateTime.now().minusMinutes(59);
        Date date = Date.from(lessThanHour.atZone(ZoneId.systemDefault()).toInstant());
        DataParser parser = new DataParser();
        String result = parser.getTime(date);
        assertEquals("59 минут назад ", result);
    }

    @Test
    public void testPrintTimeToday() throws Exception {
        LocalDateTime today = LocalDateTime.now().minusHours(1);
        Date date = Date.from(today.atZone(ZoneId.systemDefault()).toInstant());
        DataParser parser = new DataParser();
        String result = parser.getTime(date);
        if (today.toLocalDate().isEqual(LocalDateTime.now().toLocalDate())) {
            assertEquals("1 час назад ", result);
        } else {
            assertEquals("вчера ", result);
        }
    }

    @Test
        public void testPrintTimeYesturday() throws Exception {
        LocalDateTime yesturday = LocalDateTime.now().minusDays(1);
        Date date = Date.from(yesturday.atZone(ZoneId.systemDefault()).toInstant());
        DataParser parser = new DataParser();
        String result = parser.getTime(date);
        assertEquals("вчера ", result);
    }

    @Test
    public void testPrintTimeLongAgo() throws Exception {
        LocalDateTime longAgo = LocalDateTime.now().minusDays(36);
        Date date = Date.from(longAgo.atZone(ZoneId.systemDefault()).toInstant());
        DataParser parser = new DataParser();
        String result = parser.getTime(date);
        assertEquals("36 дней назад ", result);
    }

}