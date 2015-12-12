package ru.fizteh.fivt.students.nikitarykov.twitterstream;

import com.beust.jcommander.JCommander;
import ru.fizteh.fivt.students.nikitarykov.moduletests.library.ParameterParser;
import ru.fizteh.fivt.students.nikitarykov.moduletests.library.TwitterService;
import twitter4j.*;
import java.io.IOException;

import static ru.fizteh.fivt.students.nikitarykov.moduletests.library.Constants.MAX_COUNT_OF_TRIES;

/**
 * Created by Nikita Rykov on 28.11.2015.
 */
public class TwitterStreamer {

    public static void printHelp(JCommander jCommander) {
        jCommander.setProgramName("TwitterParser");
        jCommander.usage();
    }

    public static void main(String[] args) {
        ParameterParser parser = new ParameterParser();
        JCommander jCommander = new JCommander(parser);
        jCommander.parse(args);
        Twitter twitter = new TwitterFactory().getInstance();
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        TwitterService twitterService = new TwitterService(twitter, twitterStream);
        int triesCounter = 0;
        while (triesCounter < MAX_COUNT_OF_TRIES) {
            try {
                if (parser.isHelp()) {
                    printHelp(jCommander);
                } else {
                    twitterService.printTweets(parser);
                }
                break;
            } catch (IOException | TwitterException exception) {
                triesCounter++;
            }
        }
    }
}
