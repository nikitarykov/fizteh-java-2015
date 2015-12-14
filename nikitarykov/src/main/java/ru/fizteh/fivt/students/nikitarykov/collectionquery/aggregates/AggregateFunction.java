package ru.fizteh.fivt.students.nikitarykov.collectionquery.aggregates;

import java.util.List;
import java.util.function.Function;

public interface AggregateFunction<T, R> extends Function<T, R> {

    R apply(List<T> t);

    default R apply(T t) {
        return null;
    }
}
