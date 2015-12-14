package ru.fizteh.fivt.students.nikitarykov.collectionquery.impl;

import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.students.nikitarykov.collectionquery.CollectionQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.Aggregates.avg;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.Aggregates.count;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.OrderByConditions.asc;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.OrderByConditions.desc;

/**
 * Created by Nikita Rykov on 14.12.2015.
 */
public class SelectStmtTest {
    List<CollectionQuery.Student> list;
    SelectStmt<CollectionQuery.Student, CollectionQuery.Student> select;
    SelectStmt<CollectionQuery.Student, CollectionQuery.Statistics> groupSelect;

    @Before
    public void setUp() throws Exception {
        list = new ArrayList<>();
        list.add(new CollectionQuery.Student("homyakov", LocalDate.parse("1995-12-11"), "392"));
        list.add(new CollectionQuery.Student("bobrov", LocalDate.parse("1994-12-12"), "396"));
        list.add(new CollectionQuery.Student("surkov", LocalDate.parse("1995-06-28"), "399"));
        list.add(new CollectionQuery.Student("karasev", LocalDate.parse("1996-02-14"), "396"));
        select = FromStmt.from(list).select(CollectionQuery.Student.class,
                CollectionQuery.Student::getName,
                CollectionQuery.Student::getDateOfBirth,
                CollectionQuery.Student::getGroup);
        groupSelect = FromStmt.from(list).select(CollectionQuery.Statistics.class,
                CollectionQuery.Student::getGroup,
                count(CollectionQuery.Student::getGroup),
                avg(CollectionQuery.Student::age));
    }

    @Test
    public void testWhere() throws Exception {
        List<CollectionQuery.Student> result = new ArrayList<>();
        select.where(s -> s.getGroup().equals("392"))
                .execute().forEach(result::add);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), list.get(0));
    }

    @Test
    public void testExecute() throws Exception {
        List<CollectionQuery.Student> result = new ArrayList<>();
        select.execute().forEach(result::add);
        assertEquals(result.size(), list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), result.get(i));
        }
    }

    @Test
    public void testGroupBy() throws Exception {
        List<CollectionQuery.Statistics> result = new ArrayList<>();
        groupSelect.groupBy(CollectionQuery.Student::getGroup).execute()
                .forEach(result::add);
        assertEquals(result.size(), 3);
        assertEquals(result.get(0),
                new CollectionQuery.Statistics("399", 1L, 20.0));
        assertEquals(result.get(1),
                new CollectionQuery.Statistics("392", 1L, 20.0));
        assertEquals(result.get(2),
                new CollectionQuery.Statistics("396", 2L, 20.0));
    }

    @Test
    public void testOrderBy() throws Exception {
        List<CollectionQuery.Statistics> result = new ArrayList<>();
        groupSelect.groupBy(CollectionQuery.Student::getGroup)
                .orderBy(asc(CollectionQuery.Statistics::getGroup),
                        desc(CollectionQuery.Statistics::getCount)).execute()
                .forEach(result::add);
        assertEquals(result.size(), 3);
        assertEquals(result.get(0),
                new CollectionQuery.Statistics("392", 1L, 20.0));
        assertEquals(result.get(1),
                new CollectionQuery.Statistics("396", 2L, 20.0));
        assertEquals(result.get(2),
                new CollectionQuery.Statistics("399", 1L, 20.0));
    }

    @Test
    public void testHaving() throws Exception {
        List<CollectionQuery.Statistics> result = new ArrayList<>();
        groupSelect.groupBy(CollectionQuery.Student::getGroup)
                .having(s -> s.getCount() > 1)
                .orderBy(asc(CollectionQuery.Statistics::getGroup),
                        desc(CollectionQuery.Statistics::getCount)).execute()
                .forEach(result::add);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0),
                new CollectionQuery.Statistics("396", 2L, 20.0));
    }

    @Test
    public void testLimit() throws Exception {
        List<CollectionQuery.Student> result = new ArrayList<>();
        select.limit(1).execute().forEach(result::add);
        assertEquals(result.size(), 1);
        assertEquals(list.get(0), result.get(0));
    }

    @Test
    public void testUnion() throws Exception {
        List<CollectionQuery.Student> unionList = new ArrayList<>();
        unionList.add(new CollectionQuery.Student("ivanov",
                LocalDate.parse("1985-08-06"), "494"));
        List<CollectionQuery.Statistics> result = new ArrayList<>();
        groupSelect
                .groupBy(CollectionQuery.Student::getGroup)
                .having(s -> s.getCount() > 1)
                .union()
                .from(unionList)
                .select(CollectionQuery.Statistics.class, s -> "all",
                        count(s -> 1), avg(CollectionQuery.Student::age))
                .execute().forEach(result::add);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0), new CollectionQuery.Statistics("396", 2L, 20.0));
        assertEquals(result.get(1), new CollectionQuery.Statistics("all", 1L, 30.0));
    }
}