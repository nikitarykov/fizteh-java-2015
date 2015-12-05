package ru.fizteh.fivt.students.nikitarykov.moduletests.library;import org.junit.*;import twitter4j.*;import java.io.ByteArrayOutputStream;import java.io.PrintStream;import java.util.List;import static org.junit.Assert.*;/** * Created by Nikita Rykov on 04.12.2015. */public class TweetParserTest {    private static List<Status> statuses;    @BeforeClass    public static void loadSampleData() {        statuses = Twitter4jTestUtils.tweetsFromJson("/search-java-response.json");    }    @Test    public void testPrintTweet1() throws Exception {        TweetParser printer = new TweetParser();        String result = printer.getTweet(statuses.get(5));        assertEquals("@_dead_beef_: ретвитнул @matthew_d_green: " +            "As bad as F5 crypto is, it's not as bad as Java crypto -- " +            "which appears to have been created on a dare.", result);    }    @Ignore    public void testPrintTweet2() throws Exception {        TweetParser printer = new TweetParser();        String result = printer.getTweet(statuses.get(1));        assertEquals("@Ankit__Tomar: #Hiring Java Lead Developer" +                " - Click here for job details : http://t.co/0slLn3YVTW", result);    }    @Test    public void testPrintTweet3() throws Exception {        TweetParser printer = new TweetParser();        String result = printer.getTweet(statuses.get(8));        assertEquals("@ZazenAcademy: We are enrolling for our preparation" +            " courses for the Oracle Java Certification nationwide " +            "#Ireland #techie http://t.co/WZfLORunxv (34 ретвита)", result);    }}