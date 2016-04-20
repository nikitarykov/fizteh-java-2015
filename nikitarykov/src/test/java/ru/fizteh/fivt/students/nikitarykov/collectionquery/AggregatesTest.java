package ru.fizteh.fivt.students.nikitarykov.collectionquery;

import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.students.nikitarykov.collectionquery.aggregates.AggregateFunction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Nikita Rykov on 13.12.2015.
 */
public class AggregatesTest {
    List<CollectionQuery.Student> list;
    List<CollectionQuery.Student> emptyList;


    @Before
    public void setUp() throws Exception {
        list = new ArrayList<>();
        emptyList = new ArrayList<>();
        list.add(new CollectionQuery.Student("homyakov", LocalDate.parse("1995-12-11"), "392"));
        list.add(new CollectionQuery.Student("bobrov", LocalDate.parse("1994-12-12"), "396"));
        list.add(new CollectionQuery.Student("surkov", LocalDate.parse("1995-06-28"), "399"));
        list.add(new CollectionQuery.Student("karasev", LocalDate.parse("1996-02-14"), "396"));
    }

    @Test
    public void testMax() throws Exception {
        assertEquals(((AggregateFunction)Aggregates.max(CollectionQuery.Student::getName))
                .apply(list), "surkov");
        assertEquals(((AggregateFunction)Aggregates.max(CollectionQuery.Student::getDateOfBirth))
                .apply(list), LocalDate.parse("1996-02-14"));
        assertEquals(((AggregateFunction)Aggregates.max(CollectionQuery.Student::getGroup))
                .apply(list), "399");
        assertEquals((Double)((AggregateFunction)Aggregates.max(CollectionQuery.Student::age))
                .apply(list), 21.0, 0.00001);
        assertEquals(((AggregateFunction)Aggregates.max(CollectionQuery.Student::getName))
                .apply(emptyList), null);
    }

    @Test
    public void testMin() throws Exception {
        assertEquals(((AggregateFunction)Aggregates.min(CollectionQuery.Student::getName))
                .apply(list), "bobrov");
        assertEquals(((AggregateFunction)Aggregates.min(CollectionQuery.Student::getDateOfBirth))
                .apply(list), LocalDate.parse("1994-12-12"));
        assertEquals(((AggregateFunction)Aggregates.min(CollectionQuery.Student::getGroup))
                .apply(list), "392");
        assertEquals((Double)((AggregateFunction)Aggregates.min(CollectionQuery.Student::age))
                .apply(list), 19.0, 0.00001);
        assertEquals(((AggregateFunction)Aggregates.min(CollectionQuery.Student::getName))
                .apply(emptyList), null);
    }

    @Test
    public void testCount() throws Exception {
        assertEquals(((AggregateFunction)Aggregates.count(CollectionQuery.Student::getName))
                .apply(list), 4L);
        assertEquals(((AggregateFunction)Aggregates.count(CollectionQuery.Student::getGroup))
                .apply(list), 4L);
        assertEquals(((AggregateFunction)Aggregates.count(CollectionQuery.Student::age))
                .apply(list), 4L);
        assertEquals(((AggregateFunction)Aggregates.count(CollectionQuery.Student::age))
                .apply(emptyList), 0L);
    }

    @Test
    public void testAvg() throws Exception {
        assertEquals((Double)((AggregateFunction)Aggregates.avg(CollectionQuery.Student::age))
                .apply(list), 20.0, 0.00001);
        assertEquals((Double)((AggregateFunction)Aggregates.avg(CollectionQuery.Student::age))
                .apply(list.get(0)), null);
    }
}