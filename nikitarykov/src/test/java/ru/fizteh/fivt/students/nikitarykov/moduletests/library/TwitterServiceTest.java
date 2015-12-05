package ru.fizteh.fivt.students.nikitarykov.moduletests.library;

import com.beust.jcommander.JCommander;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;

/**
 * Created by Nikita Rykov on 05.12.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceTest {
    @Mock
    private Twitter twitter;

    @Mock
    private TwitterStream twitterStream;

    @InjectMocks
    private TwitterService twitterService;

    public static List<Status> statuses;

    @BeforeClass
    public static void loadSampleData() {
        statuses = Twitter4jTestUtils.tweetsFromJson("/search-java-response.json");
    }

    @Before
    public void setTwitterService() throws Exception {
        QueryResult queryResult = mock(QueryResult.class);
        when(queryResult.getTweets()).thenReturn(statuses);
        when(twitter.search(argThat(hasProperty("query", equalTo("java")))))
                .thenReturn(queryResult);
        QueryResult emptyQueryResult = mock(QueryResult.class);
        when(emptyQueryResult.getTweets()).thenReturn(Collections.emptyList());
        when(twitter.search(argThat(hasProperty("query", not(equalTo("java"))))))
                .thenReturn(emptyQueryResult);
    }

    @Test
    public void testSearchStream() throws Exception {
        ArgumentCaptor<StatusListener> statusListener = ArgumentCaptor.forClass(StatusListener.class);
        doNothing().when(twitterStream).addListener((StatusListener) statusListener.capture());
        doAnswer(i -> {
            statuses.forEach(s -> statusListener.getValue().onStatus(s));
            return null;
        }).when(twitterStream).filter(any(FilterQuery.class));
        List<Status> tweets = new ArrayList<>();
        ParameterParser parser = new ParameterParser();
        JCommander jCommander = new JCommander(parser);
        String[] args = {"--query", "java", "-s"};
        jCommander.parse(args);
        twitterService.searchStream(parser, tweets::add);
        List<String> result = new ArrayList<>();
        for (Status status : tweets) {
            result.add(new TweetParser().getTweet(status));
        }
        assertThat(tweets, hasSize(15));
        assertThat(result, hasItems(
                "@lxwalls: ретвитнул @Space_Station: How do astronauts take their coffee?\n#NationalCoffeeDay \nhttp://t.co/fx4lQcu0Xp http://t.co/NbOZoQDags",
                "@Ankit__Tomar: #Hiring Java Lead Developer - Click here for job details : http://t.co/0slLn3YVTW"
        ));
        verify(twitterStream).addListener((StatusListener) any(StatusAdapter.class));
        verify(twitterStream).filter(any(FilterQuery.class));
    }

    @Test
    public void testSearchNonStream() throws Exception {
        ParameterParser parser = new ParameterParser();
        JCommander jCommander = new JCommander(parser);
        String[] args = {"--query", "java", "--place", "nearby", "-l", "15"};
        jCommander.parse(args);
        List<Status> tweets = twitterService.searchNonStream(parser);
        List<String> result = new ArrayList<>();
        for (Status status : tweets) {
            result.add(new TweetParser().getTweet(status));
        }
        assertThat(tweets, hasSize(15));
        assertThat(result, hasItems(
                "@lxwalls: ретвитнул @Space_Station: How do astronauts take their coffee?\n#NationalCoffeeDay \nhttp://t.co/fx4lQcu0Xp http://t.co/NbOZoQDags",
                "@Ankit__Tomar: #Hiring Java Lead Developer - Click here for job details : http://t.co/0slLn3YVTW"
        ));
        verify(twitter).search(argThat(hasProperty("query", equalTo("java"))));
    }

    @Test
    public void testsearchNonStreamEmptyResult() throws Exception {
        ParameterParser parser = new ParameterParser();
        JCommander jCommander = new JCommander(parser);
        String[] args = {"--query", "c#", "--hideRetweets"};
        jCommander.parse(args);
        List<Status> tweets = twitterService.searchNonStream(parser);
        assertThat(tweets, hasSize(0));
        verify(twitter).search(argThat(hasProperty("query", equalTo("c#"))));
    }
}