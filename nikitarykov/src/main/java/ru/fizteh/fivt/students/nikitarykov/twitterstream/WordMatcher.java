package ru.fizteh.fivt.students.nikitarykov.twitterstream;

/**
 * Created by Nikita Rykov on 04.12.2015.
 */
public class WordMatcher {
    public String wordForRetweet(int count) {
        if (count % 100 > 10 && count % 100 < 20) {
            return "ретвитов";
        }
        switch (count % 10) {
            case 1 : return "ретвит";
            case 2: case 3: case 4 : return "ретвита";
            default: return "ретвитов";
        }
    }

    public String wordForMinutes(int minutes) {
        if (minutes % 100 > 10 && minutes % 100 < 20) {
            return "минут";
        }
        switch (minutes % 10) {
            case 1 : return "минута";
            case 2: case 3: case 4 : return "минуты";
            default: return "минут";
        }
    }

    public String wordForHours(int hours) {
        if (hours % 100 > 10 && hours % 100 < 20) {
            return "часов";
        }
        switch (hours % 10) {
            case 1 : return "час";
            case 2: case 3: case 4 : return "часа";
            default: return "часов";
        }
    }

    public String wordForDays(int days) {
        if (days % 100 > 10 && days % 100 < 20) {
            return "дней";
        }
        switch (days % 10) {
            case 1 : return "день";
            case 2: case 3: case 4 : return "дня";
            default: return "дней";
        }
    }
}
