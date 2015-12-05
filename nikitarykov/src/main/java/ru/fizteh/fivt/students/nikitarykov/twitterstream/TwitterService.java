package ru.fizteh.fivt.students.nikitarykov.twitterstream;

import twitter4j.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static ru.fizteh.fivt.students.nikitarykov.twitterstream.Constants.SLEEP_TIME;
import static ru.fizteh.fivt.students.nikitarykov.twitterstream.Constants.URL_FOR_GOOGLE_MAPS;
import static ru.fizteh.fivt.students.nikitarykov.twitterstream.Constants.URL_FOR_IP_NAVIGATION;

public class TwitterService {
    private final Twitter twitter;
    private final TwitterStream twitterStream;

    public TwitterService(Twitter twitter, TwitterStream twitterStream) {
        this.twitter = twitter;
        this.twitterStream = twitterStream;
    }

    public List<Status> searchNonStream(ParameterParser parser)  throws IOException, TwitterException  {
        String queryString = "";
        if (!parser.getQuery().isEmpty()) {
            queryString = String.join(" ", parser.getQuery());
        }
        Query query = new Query();
        if (!queryString.isEmpty()) {
            query.setQuery(queryString);
        }
        Location searchBounds = null;
        GeoNavigator navigator = new GeoNavigator();
        String address = parser.getPlace();
        if (address != null && !address.isEmpty()) {
            if (address == "nearby") {
                address = navigator.searchByIP(new URL(URL_FOR_IP_NAVIGATION));
            }
            String url = URL_FOR_GOOGLE_MAPS + '?' + "address="
                    + URLEncoder.encode(address, "utf-8") + "&sensor=false";
            searchBounds = navigator.searchByAddress(new URL(url));
            if (searchBounds != null) {
                query.setGeoCode(new GeoLocation(searchBounds.getLatitude(), searchBounds.getLongitude()),
                        searchBounds.getRadius(), Query.Unit.km);
            }
        }
        query.setCount(parser.getLimit());
        QueryResult result = null;
        int tweetCount = 0;
        List<Status> statuses = new ArrayList<>();
        while (tweetCount < parser.getLimit()) {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                if (parser.hideRetweets() && tweet.isRetweet()) {
                    continue;
                }
                tweetCount++;
                statuses.add(tweet);
                if (tweetCount == parser.getLimit()) {
                    break;
                }
            }
            if (!result.hasNext()) {
                break;
            }
            query = result.nextQuery();
        }
        return statuses;
    }

    public void searchStream(ParameterParser parser,  Consumer<Status> listener) throws IOException {
        String[] queries = parser.getQuery().toArray(new String[parser.getQuery().size()]);
        Location searchBounds = null;
        GeoNavigator navigator = new GeoNavigator();
        String address = parser.getPlace();
        if (address != null && !address.isEmpty()) {
            if (address == "nearby") {
                address = navigator.searchByIP(new URL(URL_FOR_IP_NAVIGATION));
            }
            String url = URL_FOR_GOOGLE_MAPS + '?' + "address="
                    + URLEncoder.encode(address, "utf-8") + "&sensor=false";
            searchBounds = navigator.searchByAddress(new URL(url));
        }
        final Location bounds = searchBounds;
        TweetParser tweetParser = new TweetParser();
        StatusListener statusListener = new StatusAdapter() {
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
                    if (bounds == null || !bounds.isInBounds(latitude, longitude)) {
                        return;
                    }
                }
                listener.accept(status);
            }
        };
        twitterStream.addListener(statusListener);
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

    public void printTweets(ParameterParser parser) throws IOException, TwitterException {
        List<Status> tweetList = new ArrayList<>();
        DataParser dataParser = new DataParser();
        TweetParser tweetParser = new TweetParser();
        if (parser.isStream()) {
            searchStream(parser, new Consumer<Status>() {
                @Override
                public void accept(Status status) {
                    System.out.println(tweetParser.getTweet(status));
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        } else {
            tweetList = searchNonStream(parser);
            if (tweetList.isEmpty()) {
                System.out.println("No tweets were found.");
            } else {
                for (Status status : tweetList) {
                    System.out.print(dataParser.getTime(status.getCreatedAt()));
                    System.out.println(tweetParser.getTweet(status));
                }
            }
        }
    }
}
