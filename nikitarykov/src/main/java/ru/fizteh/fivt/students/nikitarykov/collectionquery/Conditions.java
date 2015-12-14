package ru.fizteh.fivt.students.nikitarykov.collectionquery;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Where clause conditions.
 *
 * @author akormushin
 */
public class Conditions<T> {

    /**
     * Matches string result of expression against regexp pattern.
     *
     * @param expression expression result to match
     * @param regexp     pattern to match to
     * @param <T>        source object type
     * @return
     */
    public static <T> Predicate<T> rlike(Function<T, String> expression, String regexp) {
        return (t -> expression.apply(t).matches(regexp));
    }

    private static String changeDelimiter(String pattern, String delimiter) {
        boolean isEnd = false;
        if (pattern.endsWith("[" + delimiter + "]")) {
            isEnd = true;
        }
        String newDelimiter;
        if (delimiter.equals("?")) {
            delimiter = "\\?";
            newDelimiter = ".";
        } else {
            newDelimiter = ".*";
        }
        String[] words = pattern.split("\\[" + delimiter + "\\]");
        for (int i = 0; i < words.length; ++i) {
            if (delimiter.equals("\\?")) {
                words[i] = words[i].replace("?", "\\?");
            }
            words[i] = words[i].replace(delimiter, newDelimiter);
        }
        if (isEnd) {
            return String.join(delimiter, words) + delimiter;
        } else {
            return String.join(delimiter, words);
        }
    }

    /**
     * Matches string result of expression against SQL like pattern.
     *
     * @param expression expression result to match
     * @param pattern    pattern to match to
     * @param <T>        source object type
     * @return
     */
    public static <T> Predicate<T> like(Function<T, String> expression, String pattern) {
        pattern = pattern.replace(".", "\\.");
        pattern = changeDelimiter(pattern, "%");
        pattern = changeDelimiter(pattern, "?");
        return rlike(expression, pattern);
    }

}
