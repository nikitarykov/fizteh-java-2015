package ru.fizteh.fivt.students.nikitarykov.miniorm;

import org.h2.jdbcx.JdbcConnectionPool;
import ru.fizteh.fivt.students.nikitarykov.miniorm.annotations.Column;
import ru.fizteh.fivt.students.nikitarykov.miniorm.annotations.PrimaryKey;
import ru.fizteh.fivt.students.nikitarykov.miniorm.annotations.Table;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.fizteh.fivt.students.nikitarykov.miniorm.TypeMatcher.match;

/**
 * Created by Nikita Rykov on 18.12.2015.
 */
public class DatabaseService<T> {
    private Class<T> clazz;
    private String tableName;
    private List<Field> fields;
    private int primaryKeyIndex = -1;
    private JdbcConnectionPool pool;

    DatabaseService(Class<T> clazz) throws SQLException, ClassNotFoundException {
        this.clazz = clazz;
        Class.forName("org.h2.Driver");
        pool = JdbcConnectionPool.create(
                "jdbc:h2:./test", "test", "test");
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Class has no @Table annotation");
        }
        tableName = clazz.getAnnotation(Table.class).name();
        fields = new ArrayList<>();
        if (tableName.equals("")) {
            tableName = clazz.getSimpleName()
                    .replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
        }
        Set<String> columnNames = new HashSet<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                String columnName = getName(field);
                field.setAccessible(true);
                fields.add(field);
                if (columnNames.contains(columnName)) {
                    throw new IllegalArgumentException("Duplicate column names");
                } else {
                    columnNames.add(columnName);
                }
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    if (primaryKeyIndex == -1) {
                        primaryKeyIndex = fields.size() - 1;
                    } else {
                        throw new IllegalArgumentException("Multiple primary keys");
                    }
                }
            }
        }
    }

    public String getTableName() {
        return tableName;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public List<Field> getFields() {
        return fields;
    }

    public String getName(Field field) {
        String name = field.getAnnotation(Column.class).name();
        if (name.equals("")) {
            name = field.getName()
                    .replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
        }
        return name;
    }

    public <K> T queryById(K key) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE "
                + getName(fields.get(primaryKeyIndex)) + " = ?";
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, key.toString());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            T result = clazz.newInstance();
            for (int i = 0; i < fields.size(); ++i) {
                fields.get(i).set(result, resultSet.getObject(i + 1));
            }
            statement.close();
            return result;
         } catch (IllegalAccessException | InstantiationException exception) {
            throw new IllegalArgumentException("Error in object creation");
        } finally {
            connection.close();
        }
    }

    public List<T> queryForAll() throws SQLException {
        List<T> result = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;
        Connection connection = pool.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                T element = clazz.newInstance();
                for (int i = 0; i < fields.size(); ++i) {
                    fields.get(i).set(element, resultSet.getObject(i + 1));
                }
                result.add(element);
            }
            statement.close();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("wrong class");
        } finally {
            connection.close();
        }
        return result;
    }


    void insert(T record) throws SQLException, IllegalAccessException {
        String query = "INSERT INTO " + tableName + " (";
        for (int i = 0; i < fields.size(); ++i) {
            query += getName(fields.get(i));
            if (i < fields.size() - 1) {
                query += ", ";
            } else {
                query += ") ";
            }
        }
        query += "VALUES (";
        for (int i = 0; i < fields.size(); ++i) {
            if (i < fields.size() - 1) {
                query += "?, ";
            } else {
                query += "?)";
            }

        }
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < fields.size(); ++i) {
                statement.setObject(i + 1, fields.get(i).get(record));
            }
            statement.execute();
            statement.close();
        } finally {
            connection.close();
        }
    }

    void update(T record) throws SQLException, IllegalAccessException {
        if (primaryKeyIndex == -1) {
            throw new IllegalArgumentException("No primary key found");
        }
        String query = "UPDATE " + tableName + " SET ";
        for (int i = 0; i < fields.size(); ++i) {
            query += getName(fields.get(i)) + " = ?";
            if (i < fields.size() - 1) {
                query += ", ";
            }
        }
        query += " WHERE " + getName(fields.get(primaryKeyIndex)) + " = ?";
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < fields.size(); ++i) {
                statement.setObject(i + 1, fields.get(i).get(record));
            }
            statement.setObject(fields.size() + 1,
                    fields.get(primaryKeyIndex).get(record));
            statement.execute();
            statement.close();
        } finally {
            connection.close();
        }
    }

    void delete(T record) throws SQLException, IllegalAccessException {
        if (primaryKeyIndex == -1) {
            throw new IllegalArgumentException("No primary key found");
        }
        String query = "DELETE " + tableName + " WHERE "
                + getName(fields.get(primaryKeyIndex)) + " = ?";
        Connection connection = pool.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, fields.get(primaryKeyIndex).get(record));
            statement.execute();
            statement.close();
        } finally {
            connection.close();
        }
    }

    void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS "
                + tableName + "(";
        for (int i = 0; i < fields.size(); ++i) {
            query += getName(fields.get(i))
                    + " " + match(fields.get(i).getType());
            if (i == primaryKeyIndex) {
                query += " PRIMARY KEY";
            }
            if (i < fields.size() - 1) {
                query += ", ";
            } else {
                query += ")";
            }
        }
        Connection connection = pool.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
        } finally {
            connection.close();
        }
    }

    void dropTable() throws SQLException {
        String query = "DROP TABLE IF EXISTS " + tableName;
        Connection connection = pool.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
        } finally {
            connection.close();
        }
    }
}
