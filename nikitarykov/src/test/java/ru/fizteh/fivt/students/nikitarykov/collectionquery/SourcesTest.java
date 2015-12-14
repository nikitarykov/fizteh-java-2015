package ru.fizteh.fivt.students.nikitarykov.collectionquery;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.CollectionQuery.Student.student;

/**
 * Created by Nikita Rykov on 13.12.2015.
 */
public class SourcesTest {

    @Test
    public void testList() throws Exception {
        List<CollectionQuery.Student> list = new ArrayList<>();
        list.add(new CollectionQuery.Student("homyakov", LocalDate.parse("1995-12-11"), "392"));
        list.add(new CollectionQuery.Student("bobrov", LocalDate.parse("1994-12-12"), "396"));
        list.add(new CollectionQuery.Student("surkov", LocalDate.parse("1995-06-28"), "399"));
        list.add(new CollectionQuery.Student("karasev", LocalDate.parse("1996-02-14"), "396"));

        List<CollectionQuery.Student> result = Sources.list(
                student("homyakov", LocalDate.parse("1995-12-11"), "392"),
                student("bobrov", LocalDate.parse("1994-12-12"), "396"),
                student("surkov", LocalDate.parse("1995-06-28"), "399"),
                student("karasev", LocalDate.parse("1996-02-14"), "396"));
        assertEquals(list.size(), result.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i).toString(), result.get(i).toString());
        }
    }
}