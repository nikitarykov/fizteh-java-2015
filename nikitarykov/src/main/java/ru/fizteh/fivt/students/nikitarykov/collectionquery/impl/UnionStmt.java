package ru.fizteh.fivt.students.nikitarykov.collectionquery.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by kormushin on 09.10.15.
 */
public class UnionStmt<T, R> {
    private List<R> existingRows;
    private List<T> rows;
    private boolean isJoin;

    UnionStmt(Iterable<R> existingRows, Iterable<T> rows, boolean isJoin) {
        this.existingRows = new ArrayList<>();
        existingRows.forEach(this.existingRows::add);
        if (rows != null) {
            this.rows = new ArrayList<>();
            rows.forEach(this.rows::add);
        }
        this.isJoin = isJoin;
    }

    public <T> UnionStmt<T, R> from(Iterable<T> list) {
        return new UnionStmt<>(existingRows, list, isJoin);
    }

    public <T> UnionStmt<T, R> from(SelectStmt<?, T> stmt) throws InvocationTargetException,
            NoSuchMethodException, IllegalAccessException, InstantiationException {
        return from(stmt.execute());
    }

    @SafeVarargs
    public final SelectStmt<T, R> select(Class<R> clazz, Function<T, ?>... functions) {
        return new SelectStmt<>(rows, clazz, false, isJoin, existingRows, functions);
    }

    @SafeVarargs
    public final SelectStmt<T, R> selectDistinct(Class<R> clazz, Function<T, ?>... functions) {
        return new SelectStmt<>(rows, clazz, true, isJoin, existingRows, functions);
    }
}
