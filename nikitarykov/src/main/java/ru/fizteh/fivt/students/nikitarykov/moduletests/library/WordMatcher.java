package ru.fizteh.fivt.students.nikitarykov.moduletests.library;

/**
 * Created by Nikita Rykov on 04.12.2015.
 */
public class WordMatcher {
    private static String[][] words = {{"ретвит", "ретвита", "ретвитов"},
            {"минута", "минуты", "минут"},
            {"час", "часа", "часов"},
            {"день", "дня", "дней"}};

    public String match(int count, int word) {
        if (count % 100 > 10 && count % 100 < 20) {
            return words[word][2];
        }
        switch (count % 10) {
            case 1 : return words[word][0];
            case 2: case 3: case 4 : return words[word][1];
            default: return words[word][2];
        }
    }
}
