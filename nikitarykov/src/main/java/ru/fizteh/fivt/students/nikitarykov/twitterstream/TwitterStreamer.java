package ru.fizteh.fivt.students.nikitarykov.twitterstream;

import com.beust.jcommander.JCommander;
import twitter4j.*;
import static ru.fizteh.fivt.students.nikitarykov.twitterstream.Constants.SLEEP_TIME;
import static ru.fizteh.fivt.students.nikitarykov.twitterstream.Constants.MAX_COUNT_OF_TRIES;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita Rykov on 28.11.2015.
 */
public class TwitterStreamer {

    private static String wordForRetweet(int count) {
        if (count % 100 > 10 && count % 100 < 20) {
            return "ретвитов";
        }
        switch (count % 10) {
            case 1 : return "ретвит";
            case 2: case 3: case 4 : return "ретвита";
            default: return "ретвитов";
        }
    }

    public static void printHelp(JCommander jCommander) {
        jCommander.setProgramName("TwitterParser");
        jCommander.usage();
    }

    public static void printTweet(Status status) {
        System.out.print("@" + status.getUser().getScreenName() + ": ");
        String text = status.getText();
        if (status.isRetweet()) {
            int index = text.indexOf("@");
            text = text.substring(index, text.length());
            index = text.indexOf(" ");
            String retweetedName = text.substring(0, index - 1);
            text = text.substring(index + 1, text.length());
            System.out.print("ретвитнул " + retweetedName + ": ");
        }
        System.out.print(text);
        if (!status.isRetweet() && status.getRetweetCount() > 0) {
            System.out.print(" (" + status.getRetweetCount() + " " + wordForRetweet(status.getRetweetCount()) + ")");
        }
        System.out.println();
    }

    public static void printStream(ParameterParser parser) throws TwitterException {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        String[] queries = parser.getQuery().toArray(new String[parser.getQuery().size()]);
        Location searchBounds;
        if (parser.getPlace() != null && !parser.getPlace().isEmpty()) {
            searchBounds = new GeoNavigator().search(parser.getPlace());
        } else {
            searchBounds = null;
        }
        StatusListener listener = new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (parser.hideRetweets() && status.isRetweet()) {
                    return;
                }
                if (parser.getQuery().size() > 0 && parser.getPlace() != null && !parser.getPlace().isEmpty()) {
                    if (status.getGeoLocation() == null) {
                        return;
                    }
                    double latitude = status.getGeoLocation().getLatitude();
                    double longitude = status.getGeoLocation().getLongitude();
                    if (!searchBounds.isInBounds(latitude, longitude)) {
                        return;
                    }
                }
                printTweet(status);
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException exception) {
                    System.err.println("Interrupted by other thread.");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        };
        twitterStream.addListener(listener);
        FilterQuery query = null;
        if (queries.length > 0) {
            query = new FilterQuery().track(queries);
        } else if (searchBounds != null) {
            double[][] boundBox = {{searchBounds.getSouthwestLat(), searchBounds.getSouthwestLng()},
                {searchBounds.getNortheastLat(), searchBounds.getNortheastLng()}};
            query = new FilterQuery().locations(boundBox);
        }
        if (query == null) {
            twitterStream.sample();
        } else {
            twitterStream.filter(query);
        }
    }

    public static void printNonStream(ParameterParser parser) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        String queryString = "";
        if (!parser.getQuery().isEmpty()) {
            queryString = String.join(" ", parser.getQuery());
        }
        Query query = new Query();
        if (!queryString.isEmpty()) {
            query.setQuery(queryString);
        }
        Location searchBounds;
        if (parser.getPlace() != null && !parser.getPlace().isEmpty()) {
            searchBounds = new GeoNavigator().search(parser.getPlace());
            query.setGeoCode(new GeoLocation(searchBounds.getLatitude(), searchBounds.getLongitude()),
                    searchBounds.getRadius(), Query.Unit.km);
        } else {
            searchBounds = null;
        }
        query.setCount(parser.getLimit());
        QueryResult result = null;
        int tweetCount = 0;
        List<Status> tweetList = new ArrayList<>();
        try {
            while (tweetCount < parser.getLimit()) {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    if (parser.hideRetweets() && tweet.isRetweet()) {
                        continue;
                    }
                    tweetCount++;
                    tweetList.add(tweet);
                    if (tweetCount == parser.getLimit()) {
                        break;
                    }
                }
                if (!result.hasNext()) {
                    break;
                }
                query = result.nextQuery();
            }
        } catch (TwitterException exception) {
            System.err.println("Problems with search.");
            throw new TwitterException("Error in connection");
        }
        if (tweetList.isEmpty()) {
            System.out.println("No tweets were found.");
        } else {
            DataParser dataParser = new DataParser();
            for (Status status : tweetList) {
                dataParser.printTime(status.getCreatedAt());
                printTweet(status);
            }
        }
    }

    public static void main(String[] args) {
        ParameterParser parser = new ParameterParser();
        JCommander jCommander = new JCommander(parser);
        jCommander.parse(args);
        int triesCounter = 0;
        while (triesCounter < MAX_COUNT_OF_TRIES) {
            try {
                if (parser.isHelp()) {
                    printHelp(jCommander);
                } else if (parser.isStream()) {
                    printStream(parser);
                } else {
                    printNonStream(parser);
                }
                break;
            } catch (TwitterException exception) {
                triesCounter++;
            }
        }
    }
}
