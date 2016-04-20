package ru.fizteh.fivt.students.nikitarykov.collectionquery.aggregates;

import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Created by Nikita Rykov on 07.12.2015.
 */
public class Avg<T, R extends Comparable<R>> implements AggregateFunction<T, Double> {
    private Function<T, R> function;

    public Avg(Function<T, R> function) {
        this.function = function;
    }

    @Override
    public Double apply(List<T> rows) {
        ToDoubleFunction<T> toDoubleFunction = (t) -> (Double) function.apply(t);
        return rows.stream().mapToDouble(toDoubleFunction).average().getAsDouble();
    }
}
