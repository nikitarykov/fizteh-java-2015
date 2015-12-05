package ru.fizteh.fivt.students.nikitarykov.twitterstream;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.time.*;

/**
 * Created by Nikita Rykov on 30.11.2015.
 */
public class DataParser {

    public void printTime(Date date) {
        LocalDateTime tweetTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime currentTime = LocalDateTime.now();
        WordMatcher matcher = new WordMatcher();
        if (tweetTime.isAfter(currentTime.minusMinutes(2))) {
            System.out.print("только что ");
        } else if (tweetTime.isAfter(currentTime.minusHours(1))) {
            int minutes = (int) ChronoUnit.MINUTES.between(tweetTime, currentTime);
            System.out.print(minutes + " " + matcher.wordForMinutes(minutes) + " назад ");
        } else if (currentTime.toLocalDate().isEqual(tweetTime.toLocalDate())) {
            int hours = (int) ChronoUnit.HOURS.between(tweetTime, currentTime);
            System.out.print(hours + " " + matcher.wordForHours(hours) + " назад ");
        } else if (tweetTime.toLocalDate().isEqual(currentTime.minusDays(1).toLocalDate())) {
            System.out.print("вчера ");
        } else {
            int days = (int) ChronoUnit.DAYS.between(tweetTime, currentTime);
            System.out.print(days + " " + matcher.wordForDays(days) + " назад ");
        }
    }
}
