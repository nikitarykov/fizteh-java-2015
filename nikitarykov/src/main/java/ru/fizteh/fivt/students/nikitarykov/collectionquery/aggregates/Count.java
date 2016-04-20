package ru.fizteh.fivt.students.nikitarykov.collectionquery.aggregates;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Nikita Rykov on 07.12.2015.
 */
public class Count<T, R extends Comparable<R>> implements AggregateFunction<T, Long> {
    private Function<T, R> function;

    public Count(Function<T, R> function) {
        this.function = function;
    }

    @Override
    public Long apply(List<T> rows) {
        return rows.stream().map(function).filter(t -> t != null).count();
    }
}

