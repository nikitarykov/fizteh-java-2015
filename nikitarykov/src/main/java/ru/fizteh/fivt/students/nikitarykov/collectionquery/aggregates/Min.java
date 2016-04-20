package ru.fizteh.fivt.students.nikitarykov.collectionquery.aggregates;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Nikita Rykov on 07.12.2015.
 */
public class Min<T, R extends Comparable<R>> implements AggregateFunction<T, R> {
    private Function<T, R> function;

    public Min(Function<T, R> function) {
        this.function = function;
    }

    @Override
    public R apply(List<T> rows) {
        return rows.stream().map(function).max(Comparator.<R>reverseOrder()).orElse(null);
    }
}
