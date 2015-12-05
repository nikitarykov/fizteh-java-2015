package ru.fizteh.fivt.students.nikitarykov.moduletests.library;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.*;
/**
 * Created by Nikita Rykov on 04.12.2015.
 */
public class ParameterParserTest {
    private ParameterParser parser;
    private JCommander jCommander;

    @Before
    public void setJCommander() {
        parser = new ParameterParser();
        jCommander = new JCommander(parser);
    }

    @Test
    public void test1() throws Exception {
        String[] args = {"--place", "London", "--query", "java", "twitter", "--stream", "--limit", "33"};
        jCommander.parse(args);
        assertEquals("London", parser.getPlace());
        assertTrue(parser.isStream());
        assertEquals(Arrays.asList("java", "twitter"), parser.getQuery());
        assertEquals(33, parser.getLimit());
        assertFalse(parser.hideRetweets());
        assertFalse(parser.isHelp());
    }

    @Test
         public void test2() throws Exception {
        String[] args1 = { "--help"};
        jCommander.parse(args1);
        assertNull(parser.getPlace());
        assertFalse(parser.isStream());
        assertEquals(Arrays.asList(), parser.getQuery());
        assertEquals(1000, parser.getLimit());
        assertFalse(parser.hideRetweets());
        assertTrue(parser.isHelp());
    }

    @Test
    public void test3() throws Exception {
        String[] args1 = {"-h"};
        jCommander.parse(args1);
        assertNull(parser.getPlace());
        assertFalse(parser.isStream());
        assertEquals(Arrays.asList(), parser.getQuery());
        assertEquals(1000, parser.getLimit());
        assertFalse(parser.hideRetweets());
        assertTrue(parser.isHelp());
    }

    @Test
    public void test4() throws Exception {
        String[] args = { "-l", "5000", "-s", "-q", "java", "-p", "London", "--hideRetweets"};
        jCommander.parse(args);
        assertEquals("London", parser.getPlace());
        assertTrue(parser.isStream());
        assertEquals(Arrays.asList("java"), parser.getQuery());
        assertEquals(5000, parser.getLimit());
        assertTrue(parser.hideRetweets());
        assertFalse(parser.isHelp());
    }

    @Test(expected = ParameterException.class)
    public void test5() throws Exception {
        String[] args = { "-place", "London"};
        jCommander.parse(args);
    }

    @Test(expected = ParameterException.class)
    public void test6() throws Exception {
        String[] args = { "--l", "33"};
        jCommander.parse(args);
    }

    @Test(expected = ParameterException.class)
    public void test7() throws Exception {
        String[] args = { "-p", "-s", "London"};
        jCommander.parse(args);
    }

    @Test(expected = ParameterException.class)
    public void test8() throws Exception {
        String[] args = { "--query"};
        jCommander.parse(args);
    }
}