package ru.fizteh.fivt.students.nikitarykov.collectionquery.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Created by kormushin on 06.10.15.
 */
public class FromStmt<T> {
    private List<T> rows;
    private boolean isJoin;

    FromStmt(Iterable<T> rows, boolean isJoin) {
        this.rows = new ArrayList<>();
        rows.forEach(this.rows::add);
        this.isJoin = isJoin;
    }

    public List<T> getRows() {
        return rows;
    }

    public static <T> FromStmt<T> from(Iterable<T> iterable) {
        return new FromStmt<>(iterable, false);
    }

    public static <T> FromStmt<T> from(SelectStmt<?, T> stmt) throws InvocationTargetException,
            NoSuchMethodException, IllegalAccessException, InstantiationException {
        return from(stmt.execute());
    }

    @SafeVarargs
    public final <R> SelectStmt<T, R> select(Class<R> clazz, Function<T, ?>... functions) {
        return new SelectStmt<>(rows, clazz, false, isJoin, null, functions);
    }

    public final <F, S> SelectStmt<T, Tuple<F, S>> select(Function<T, F>  first, Function<T, S> second) {
        Function<T, ?>[] functions = new Function[2];
        functions[0] = first;
        functions[1] = second;
        return new SelectStmt<>(rows, null, false, isJoin, null, functions);
    }

    public final <F, S> SelectStmt<T, Tuple<F, S>> selectDistinct(Function<T, F>  first, Function<T, S> second) {
        Function<T, ?>[] functions = new Function[2];
        functions[0] = first;
        functions[1] = second;
        return new SelectStmt<>(rows, null, true, isJoin, null, functions);
    }

    @SafeVarargs
    public final <R> SelectStmt<T, R> selectDistinct(Class<R> clazz, Function<T, ?>... functions) {
        return new SelectStmt<>(rows, clazz, true, isJoin, null, functions);
    }

    public <J> JoinClause<T, J> join(Iterable<J> iterable) {
        List<J> joinRows = new ArrayList<>();
        iterable.forEach(joinRows::add);
        return new JoinClause<>(rows, joinRows);
    }

    public <J> JoinClause<T, J> join(SelectStmt<?, J> stmt) throws InvocationTargetException,
            NoSuchMethodException, IllegalAccessException, InstantiationException {
        return join(stmt.execute());
    }

    public class JoinClause<T, J> {
        private List<T> rows;
        private List<J> joinRows;

        JoinClause(List<T> rows, List<J> joinRows) {
            this.rows = rows;
            this.joinRows = joinRows;
        }

        public List<T> getRows() {
            return rows;
        }

        public List<J> getJoinRows() {
            return joinRows;
        }

        public FromStmt<Tuple<T, J>> on(BiPredicate<T, J> condition) {
            List<Tuple<T, J>> result = new ArrayList<>();
            for (T row : rows) {
                for (J joinRow : joinRows) {
                    if (condition.test(row, joinRow)) {
                        result.add(new Tuple<>(row, joinRow));
                    }
                }
            }
            return new FromStmt<>(result, true);
        }

        public <K extends Comparable<K>> FromStmt<Tuple<T, J>> on(
                Function<T, K> leftKey,
                Function<J, K> rightKey) {
            List<Tuple<T, J>> result = new ArrayList<>();
            for (T row : rows) {
                for (J joinRow : joinRows) {
                    if (leftKey.apply(row).compareTo(rightKey.apply(joinRow)) == 0) {
                        result.add(new Tuple<>(row, joinRow));
                    }
                }
            }
            return new FromStmt<>(result, true);
        }
    }
}
