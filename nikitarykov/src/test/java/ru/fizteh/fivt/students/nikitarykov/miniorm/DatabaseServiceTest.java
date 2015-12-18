package ru.fizteh.fivt.students.nikitarykov.miniorm;

import org.junit.Test;
import ru.fizteh.fivt.students.nikitarykov.miniorm.annotations.Column;
import ru.fizteh.fivt.students.nikitarykov.miniorm.annotations.PrimaryKey;
import ru.fizteh.fivt.students.nikitarykov.miniorm.annotations.Table;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Nikita Rykov on 18.12.2015.
 */
public class DatabaseServiceTest {
    DatabaseService<RatingOfStudents> database;

    @Test
    public void test() throws Exception {
        database = new DatabaseService<>(RatingOfStudents.class);
        database.dropTable();
        database.createTable();
        assertEquals(database.getTableName(), "rating_of_students");
        assertEquals(database.getFields().size(), 3);
        assertEquals(database.getFields().get(0).getType(), String.class);
        assertEquals(database.getFields().get(1).getType(), Integer.class);
        assertEquals(database.getFields().get(2).getType(), Date.class);
        assertEquals(database.getName(database.getFields().get(0)), "name");
        RatingOfStudents rating = new RatingOfStudents("ivanov",
                20, Date.valueOf("1995-09-23"));
        database.insert(rating);
        RatingOfStudents result = database.queryById("ivanov");
        assertEquals(result.getName(), "ivanov");
        assertEquals(result.getRank(), (Object) 20);
        assertEquals(result.getDateOfBirth(), (Object) Date.valueOf("1995-09-23"));
        database.delete(result);
        database.insert(result);
        result.setRank(45);
        result.setDateOfBirth(Date.valueOf("1996-02-14"));
        database.update(result);
        result = database.queryById("ivanov");
        assertEquals(result.getName(), "ivanov");
        assertEquals(result.getRank(), (Object) 45);
        assertEquals(result.getDateOfBirth(), (Object) Date.valueOf("1996-02-14"));
        rating.setName("petrov");
        database.insert(rating);
        List<RatingOfStudents> list = database.queryForAll();
        assertEquals(list.size(), 2);
        assertEquals(list.get(0).getName(), "ivanov");
        assertEquals(list.get(1).getName(), "petrov");
        database.dropTable();
    }
}