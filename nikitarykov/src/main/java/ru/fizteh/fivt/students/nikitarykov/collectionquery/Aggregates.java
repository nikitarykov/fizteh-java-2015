package ru.fizteh.fivt.students.nikitarykov.collectionquery;

import ru.fizteh.fivt.students.nikitarykov.collectionquery.aggregates.Avg;
import ru.fizteh.fivt.students.nikitarykov.collectionquery.aggregates.Count;
import ru.fizteh.fivt.students.nikitarykov.collectionquery.aggregates.Max;
import ru.fizteh.fivt.students.nikitarykov.collectionquery.aggregates.Min;

import java.util.function.Function;

/**
 * Aggregate functions.
 *
 * @author akormushin
 */
public class Aggregates {

    public static <C, T extends Comparable<T>> Function<C, T> max(Function<C, T> expression) {
        return new Max<>(expression);
    }

    public static <C, T extends Comparable<T>> Function<C, T> min(Function<C, T> expression) {
        return new Min<>(expression);
    }

    public static <C, T extends Comparable<T>> Function<C, Long> count(Function<C, T> expression) {
        return new Count<>(expression);
    }

    public static <C, T extends Comparable<T>> Function<C, Double> avg(Function<C, T> expression) {
        return new Avg<>(expression);
    }

}
