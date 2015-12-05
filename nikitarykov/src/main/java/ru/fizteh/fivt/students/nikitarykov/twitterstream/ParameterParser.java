package ru.fizteh.fivt.students.nikitarykov.twitterstream;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

import static ru.fizteh.fivt.students.nikitarykov.twitterstream.Constants.DEFAULT_LIMIT;
/**
 * Created by Nikita Rykov on 29.11.2015.
 */

public class ParameterParser {
    @Parameter(names = { "-q", "--query" }, variableArity = true,
        description = "Ключевые слова для поиска.")
    private List<String> queries = new ArrayList<>();

    @Parameter(names = { "-p", "--place" },
        description = "Искать только по заданному региону или по ip, если значение равно nearby.")
    private String place = null;

    @Parameter(names = { "-s", "--stream" },
        description = "Выводить результаты поиска равномерно и непрерывно с задержкой в 1 секунду.")
    private boolean stream = false;

    @Parameter(names = "--hideRetweets",
            description = "Не выводить ретвиты.")
    private boolean hideRetweets = false;

    @Parameter(names = { "-l", "--limit" },
            description = "Выводить только заданное число твитов. Не применимо для --stream режима.")
    private int limit = DEFAULT_LIMIT;

    @Parameter(names = { "-h", "--help" },
            description = "Справка.")
    private boolean help = false;

    public List<String> getQuery() {
        return queries;
    }

    public String getPlace() {
        return place;
    }

    public boolean isStream() {
        return stream;
    }

    public boolean hideRetweets() {
        return hideRetweets;
    }

    public int getLimit() {
        return limit;
    }

    public boolean isHelp() {
        return help;
    }
}
