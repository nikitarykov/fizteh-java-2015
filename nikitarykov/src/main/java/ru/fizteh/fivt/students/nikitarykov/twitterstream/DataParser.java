package ru.fizteh.fivt.students.nikitarykov.moduletests.library;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.time.*;

/**
 * Created by Nikita Rykov on 30.11.2015.
 */
public class DataParser {

    public String getTime(Date date) {
        LocalDateTime tweetTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime currentTime = LocalDateTime.now();
        WordMatcher matcher = new WordMatcher();
        if (tweetTime.isAfter(currentTime.minusMinutes(2))) {
            return "только что ";
        } else if (tweetTime.isAfter(currentTime.minusHours(1))) {
            int minutes = (int) ChronoUnit.MINUTES.between(tweetTime, currentTime);
            return minutes + " " + matcher.wordForMinutes(minutes) + " назад ";
        } else if (currentTime.toLocalDate().isEqual(tweetTime.toLocalDate())) {
            int hours = (int) ChronoUnit.HOURS.between(tweetTime, currentTime);
            return hours + " " + matcher.wordForHours(hours) + " назад ";
        } else if (tweetTime.toLocalDate().isEqual(currentTime.minusDays(1).toLocalDate())) {
            return "вчера ";
        } else {
            int days = (int) ChronoUnit.DAYS.between(tweetTime, currentTime);
            return days + " " + matcher.wordForDays(days) + " назад ";
        }
    }
}
