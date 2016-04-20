package ru.fizteh.fivt.students.nikitarykov.collectionquery.impl;

import ru.fizteh.fivt.students.nikitarykov.collectionquery.aggregates.AggregateFunction;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SelectStmt<T, R> {
    private List<T> rows;
    private List<R> existingRows;
    private List<R> resultRows;
    private Function<T, ?>[] functions;
    private Function<T, ?>[] groupByFunctions;
    private Predicate<T> wherePredicate;
    private Class returnClazz;
    private boolean isDistinct;
    private boolean isJoin;
    private OrderByComparator<R> comparator;
    private Predicate<R> havingPredicate;
    private Integer limit;

    public List<T> getRows() {
        return rows;
    }

    public List<R> getExistingRows() {
        return existingRows;
    }

    public Function<T, ?>[] getFunctions() {
        return functions;
    }

    public Function<T, ?>[] getGroupByFunctions() {
        return groupByFunctions;
    }

    public Predicate<T> getWherePredicate() {
        return wherePredicate;
    }

    public Class getReturnClazz() {
        return returnClazz;
    }

    public boolean isDistinct() {
        return isDistinct;
    }

    public boolean isJoin() {
        return isJoin;
    }

    public OrderByComparator<R> getComparator() {
        return comparator;
    }

    public Predicate<R> getHavingPredicate() {
        return havingPredicate;
    }

    public int getLimit() {
        return limit;
    }


    @SafeVarargs
    SelectStmt(List<T> rows, Class<R> returnClazz, boolean isDistinct,
               boolean isJoin, List<R> existingRows, Function<T, ?>... functions) {
        this.rows = rows;
        this.returnClazz = returnClazz;
        this.isDistinct = isDistinct;
        this.isJoin = isJoin;
        this.functions = functions;
        if (existingRows != null) {
            this.existingRows = existingRows;
        }
        resultRows = new ArrayList<>();
    }

    public SelectStmt<T, R> where(Predicate<T> predicate) {
        wherePredicate = predicate;
        return this;
    }

    public Iterable<R> execute() throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        Object[] parameterList = new Object[functions.length];
        Class[] parameterListTypes = new Class[functions.length];
        if (wherePredicate != null) {
            rows = rows.stream().filter(wherePredicate).collect(Collectors.toList());
        }
        if (groupByFunctions != null) {
            Map<Integer, List<T>> groupsMap = new HashMap<>();
            Object[] key = new Object[groupByFunctions.length];
            for (T row : rows) {
                for (int i = 0; i < groupByFunctions.length; ++i) {
                    key[i] = groupByFunctions[i].apply(row);
                }
                if (groupsMap.containsKey(Arrays.hashCode(key))) {
                    List<T> currentValue = groupsMap.remove(Arrays.hashCode(key));
                    currentValue.add(row);
                    groupsMap.put(Arrays.hashCode(key), currentValue);
                } else {
                    List<T> currentValue = new ArrayList<>();
                    currentValue.add(row);
                    groupsMap.put(Arrays.hashCode(key), currentValue);
                }
            }
            List<List<T>> groups = new ArrayList(groupsMap.values());
            for (List<T> group : groups) {
                for (int i = 0; i < functions.length; ++i) {
                    if (functions[i] instanceof AggregateFunction) {
                        parameterList[i] = ((AggregateFunction) functions[i]).apply(group);
                    } else {
                        parameterList[i] = functions[i].apply(group.get(0));
                    }
                    parameterListTypes[i] = parameterList[i].getClass();
                }
                R resultItem = (R) returnClazz.getConstructor(parameterListTypes).newInstance(parameterList);
                resultRows.add(resultItem);
            }
            if (havingPredicate != null) {
                resultRows = resultRows.stream().filter(havingPredicate).collect(Collectors.toList());
            }
        } else {
            for (T row : rows) {
                for (int i = 0; i < functions.length; ++i) {
                    if (functions[i] instanceof AggregateFunction) {
                        parameterList[i] = ((AggregateFunction) functions[i]).apply(rows);
                    } else {
                        parameterList[i] = functions[i].apply(row);
                    }
                    parameterListTypes[i] = parameterList[i].getClass();
                }
                R resultItem;
                if (isJoin) {
                    resultItem = (R) new Tuple<>(parameterList[0], parameterList[1]);
                } else {
                    resultItem = (R) returnClazz.getConstructor(parameterListTypes).newInstance(parameterList);
                }
                resultRows.add(resultItem);
            }
        }
        if (comparator != null) {
            resultRows.sort(comparator);
        }
        if (isDistinct) {
            resultRows = resultRows.stream().distinct().collect(Collectors.toList());
        }
        if (limit != null) {
            if (limit < resultRows.size()) {
                resultRows = resultRows.subList(0, limit);
            }
        }
        if (existingRows != null && !existingRows.isEmpty()) {
            existingRows.addAll(resultRows);
            return existingRows;
        }
        return resultRows;
    }

    @SafeVarargs
    public final SelectStmt<T, R> groupBy(Function<T, ?>... expressions) {
        groupByFunctions = expressions;
        return this;
    }

    @SafeVarargs
    public final SelectStmt<T, R> orderBy(Comparator<R>... comparators) {
        comparator = new OrderByComparator<>(comparators);
        return this;
    }

    public SelectStmt<T, R> having(Predicate<R> condition) {
        havingPredicate = condition;
        return this;
    }

    public SelectStmt<T, R> limit(int amount) {
        limit = amount;
        return this;
        }

    public UnionStmt<T, R> union() throws InstantiationException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        return new UnionStmt<>(execute(), null, isJoin);
    }

    private class OrderByComparator<R> implements Comparator<R> {
        private Comparator<R>[] comparators;

        OrderByComparator(Comparator<R>... comparators) {
            this.comparators = comparators;
        }

        @Override
        public int compare(R first, R second) {
            for (Comparator<R> comparator : comparators) {
                if (comparator.compare(first, second) != 0) {
                    return comparator.compare(first, second);
                }
            }
            return 0;
        }
    }
}
