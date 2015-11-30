package ru.fizteh.fivt.students.nikitarykov.twitterstream;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.time.*;

/**
 * Created by Nikita Rykov on 30.11.2015.
 */
public class DataParser {

    private String wordForMinutes(int minutes) {
        if (minutes % 100 > 10 && minutes % 100 < 20) {
            return "минут";
        }
        switch (minutes % 10) {
            case 1 : return "минута";
            case 2: case 3: case 4 : return "минуты";
            default: return "минут";
        }
    }

    private String wordForHours(int hours) {
        if (hours % 100 > 10 && hours % 100 < 20) {
            return "часов";
        }
        switch (hours % 10) {
            case 1 : return "час";
            case 2: case 3: case 4 : return "часа";
            default: return "часов";
        }
    }

    private String wordForDays(int days) {
        if (days % 100 > 10 && days % 100 < 20) {
            return "дней";
        }
        switch (days % 10) {
            case 1 : return "день";
            case 2: case 3: case 4 : return "дня";
            default: return "дней";
        }
    }

    public void printTime(Date date) {
        LocalDateTime tweetTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime currentTime = LocalDateTime.now();
        if (tweetTime.isAfter(currentTime.minusMinutes(2))) {
            System.out.print("только что ");
        } else if (tweetTime.isAfter(currentTime.minusHours(1))) {
            int minutes = (int) ChronoUnit.MINUTES.between(tweetTime, currentTime);
            System.out.print(minutes + " " + wordForMinutes(minutes) + " назад ");
        } else if (currentTime.toLocalDate().isEqual(tweetTime.toLocalDate())) {
            int hours = (int) ChronoUnit.HOURS.between(tweetTime, currentTime);
            System.out.print(hours + " " + wordForHours(hours) + " назад ");
        } else if (tweetTime.toLocalDate().isEqual(currentTime.minusDays(1).toLocalDate())) {
            System.out.print("вчера ");
        } else {
            int days = (int) ChronoUnit.DAYS.between(tweetTime, currentTime);
            System.out.print(days + " " + wordForDays(days) + " назад ");
        }
    }
}
