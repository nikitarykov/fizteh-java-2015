package ru.fizteh.fivt.students.nikitarykov.collectionquery.impl;

import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.students.nikitarykov.collectionquery.CollectionQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.impl.FromStmt.from;

/**
 * Created by Nikita Rykov on 13.12.2015.
 */
public class FromStmtTest {
    List<CollectionQuery.Student> list;

    @Before
    public void setUp() throws Exception {
        list = new ArrayList<>();
        list.add(new CollectionQuery.Student("homyakov", LocalDate.parse("1995-12-11"), "392"));
        list.add(new CollectionQuery.Student("bobrov", LocalDate.parse("1994-12-12"), "396"));
        list.add(new CollectionQuery.Student("surkov", LocalDate.parse("1995-06-28"), "399"));
        list.add(new CollectionQuery.Student("karasev", LocalDate.parse("1996-02-14"), "396"));
    }

    @Test
    public void testFrom() throws Exception {
        FromStmt<CollectionQuery.Student> fromStmt = from(list);
        assertEquals(fromStmt.getRows().size(), list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(fromStmt.getRows().get(i), list.get(i));
        }
    }

    @Test
    public void testFrom1() throws Exception {
        FromStmt<CollectionQuery.Student> fromStmt = from(list);
        SelectStmt<CollectionQuery.Student, String> selectStmt = fromStmt
                .select(String.class, CollectionQuery.Student::getName);
        FromStmt<String> fromStmt1 = from(selectStmt);
        assertEquals(fromStmt1.getRows().size(), list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(fromStmt.getRows().get(i).getName(),
                    list.get(i).getName());
        }
    }

    @Test
    public void testSelect() throws Exception {
        SelectStmt<CollectionQuery.Student, CollectionQuery.Student> select = from(list)
                .select(CollectionQuery.Student.class, CollectionQuery.Student::getName,
                        CollectionQuery.Student::getDateOfBirth,
                        CollectionQuery.Student::getGroup);
        assertEquals(select.getReturnClazz(), CollectionQuery.Student.class);
        assertNull(select.getExistingRows());
        assertFalse(select.isDistinct());
        assertFalse(select.isJoin());
        assertEquals(select.getFunctions().length, 3);
        for (CollectionQuery.Student element : list) {
            assertEquals(element.getName(),
                    select.getFunctions()[0].apply(element));
            assertEquals(element.getDateOfBirth(),
                    select.getFunctions()[1].apply(element));
            assertEquals(element.getGroup(),
                    select.getFunctions()[2].apply(element));
        }
        assertEquals(select.getRows().size(), list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), select.getRows().get(i));
        }
    }

    @Test
    public void testSelect1() throws Exception {
        SelectStmt<CollectionQuery.Student, Tuple<String, String>> select = from(list)
                .select(CollectionQuery.Student::getName,
                        CollectionQuery.Student::getGroup);
        assertNull(select.getReturnClazz());
        assertNull(select.getExistingRows());
        assertFalse(select.isDistinct());
        assertFalse(select.isJoin());
        assertEquals(select.getFunctions().length, 2);
        for (CollectionQuery.Student element : list) {
            assertEquals(element.getName(),
                    select.getFunctions()[0].apply(element));
            assertEquals(element.getGroup(),
                    select.getFunctions()[1].apply(element));
        }
        assertEquals(select.getRows().size(), list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), select.getRows().get(i));
        }
    }

    @Test
    public void testSelectDistinct() throws Exception {
        SelectStmt<CollectionQuery.Student, CollectionQuery.Student> select = from(list)
                .selectDistinct(CollectionQuery.Student.class, CollectionQuery.Student::getName,
                        CollectionQuery.Student::getDateOfBirth,
                        CollectionQuery.Student::getGroup);
        assertEquals(select.getReturnClazz(), CollectionQuery.Student.class);
        assertNull(select.getExistingRows());
        assertTrue(select.isDistinct());
        assertFalse(select.isJoin());
        assertEquals(select.getFunctions().length, 3);
        for (CollectionQuery.Student element : list) {
            assertEquals(element.getName(),
                    select.getFunctions()[0].apply(element));
            assertEquals(element.getDateOfBirth(),
                    select.getFunctions()[1].apply(element));
            assertEquals(element.getGroup(),
                    select.getFunctions()[2].apply(element));
        }
        assertEquals(select.getRows().size(), list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), select.getRows().get(i));
        }
    }

    @Test
    public void testSelectDistinct1() throws Exception {
        SelectStmt<CollectionQuery.Student, Tuple<String, String>> select = from(list)
                .selectDistinct(CollectionQuery.Student::getName,
                        CollectionQuery.Student::getGroup);
        assertNull(select.getReturnClazz());
        assertNull(select.getExistingRows());
        assertTrue(select.isDistinct());
        assertFalse(select.isJoin());
        assertEquals(select.getFunctions().length, 2);
        for (CollectionQuery.Student element : list) {
            assertEquals(element.getName(),
                    select.getFunctions()[0].apply(element));
            assertEquals(element.getGroup(),
                    select.getFunctions()[1].apply(element));
        }
        assertEquals(select.getRows().size(), list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), select.getRows().get(i));
        }
    }

    @Test
    public void testJoin() throws Exception {
        List<CollectionQuery.Group> groupList = new ArrayList<>();
        groupList.add(new CollectionQuery.Group("494", "mr.sidorov"));
        List<CollectionQuery.Student> rows = from(list).join(groupList).getRows();
        List<CollectionQuery.Group> joinRows = from(list).join(groupList).getJoinRows();
        assertEquals(rows.size(), list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), rows.get(i));
        }
        assertEquals(joinRows.size(), groupList.size());
        for (int i = 0; i < groupList.size(); i++) {
            assertEquals(groupList.get(i), joinRows.get(i));
        }
    }

    @Test
    public void testJoin1() throws Exception {
        List<CollectionQuery.Group> groupList = new ArrayList<>();
        groupList.add(new CollectionQuery.Group("494", "mr.sidorov"));
        SelectStmt<CollectionQuery.Group, CollectionQuery.Group> select = from(groupList)
                .select(CollectionQuery.Group.class, CollectionQuery.Group::getGroup,
                        CollectionQuery.Group::getMentor);
        List<CollectionQuery.Group> joinRows = from(list).join(select).getJoinRows();
        List<CollectionQuery.Student> rows = from(list).join(select).getRows();
        assertEquals(rows.size(), list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), rows.get(i));
        }
        assertEquals(joinRows.size(), groupList.size());
        for (int i = 0; i < groupList.size(); i++) {
            assertEquals(groupList.get(i), joinRows.get(i));
        }
    }

    @Test
    public void testOn() throws Exception {
        List<CollectionQuery.Group> groupList = new ArrayList<>();
        groupList.add(new CollectionQuery.Group("399", "mr.sidorov"));
        FromStmt<Tuple<CollectionQuery.Student, CollectionQuery.Group>> fromStmt =
                from(list).join(groupList).on((s,t) -> s.getGroup() == t.getGroup());
        assertEquals(fromStmt.getRows().size(), 1);
        assertEquals(fromStmt.getRows().get(0).getFirst(), list.get(2));
        assertEquals(fromStmt.getRows().get(0).getSecond(), groupList.get(0));

    }

    @Test
    public void testOn1() throws Exception {
        List<CollectionQuery.Group> groupList = new ArrayList<>();
        groupList.add(new CollectionQuery.Group("399", "mr.sidorov"));
        FromStmt<Tuple<CollectionQuery.Student, CollectionQuery.Group>> fromStmt =
                from(list).join(groupList).on(CollectionQuery.Student::getGroup,
                        CollectionQuery.Group::getGroup);
        assertEquals(fromStmt.getRows().size(), 1);
        assertEquals(fromStmt.getRows().get(0).getFirst(), list.get(2));
        assertEquals(fromStmt.getRows().get(0).getSecond(), groupList.get(0));


    }
}