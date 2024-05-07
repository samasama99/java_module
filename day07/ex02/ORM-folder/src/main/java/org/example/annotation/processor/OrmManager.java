package org.example.annotation.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class OrmManager {
    JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrmManager.class);

    public OrmManager(DataSource dataSource, Set<Class<?>> classes) {
        this.jdbcTemplate = new JdbcTemplateLogDecorator(dataSource, LOGGER);
        classes.stream().map(OrmManager::generateDroppingSqlQuery).forEach(this.jdbcTemplate::execute);
        classes.stream().map(OrmManager::generateCreationSqlQuery).forEach(this.jdbcTemplate::execute);
    }

    static String generateSelectById(Long id, Class<?> aClass) {
        OrmEntity entity = aClass.getAnnotation(OrmEntity.class);
        return "SELECT * FROM " + entity.table() + " WHERE id = " + id + ";";
    }


    static String generateDroppingSqlQuery(Class<?> aClass) {
        OrmEntity entity = aClass.getAnnotation(OrmEntity.class);
        return "DROP TABLE IF EXISTS " + entity.table();
    }


    public <T> Optional<T> findById(Long id, Class<T> aClass) {
        final String SQL = generateSelectById(id, aClass);
        DynamicRowMapper<T> rowMapper = new DynamicRowMapper<>(aClass);
        T tmp;
        try {
            tmp = this.jdbcTemplate.queryForObject(SQL, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        if (tmp == null)
            return Optional.empty();
        return Optional.of(tmp);
    }

    static String generateCreationSqlQuery(Class<?> aClass) {
        StringBuilder stringBuilder = new StringBuilder();
        OrmEntity entity = aClass.getAnnotation(OrmEntity.class);
        stringBuilder.append("CREATE TABLE IF NOT EXISTS ").append(entity.table()).append(" (\n");
        for (Field field : aClass.getDeclaredFields()) {
            OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
            if (ormColumn != null) {
                String type = mapTypeToSqlType(field.getType());
                if (type.equals("TEXT") && ormColumn.length() >= 0) {
                    stringBuilder.append(ormColumn.name()).append(" varchar(").append(ormColumn.length()).append(")");
                } else {
                    stringBuilder.append(ormColumn.name()).append(" ").append(type);
                }
                stringBuilder.append(",\n");
            } else {
                OrmColumnId ormColumnId = field.getAnnotation(OrmColumnId.class);
                if (ormColumnId != null) {
                    String columnName = "id";
                    if (field.getType() == Long.class) {
                        stringBuilder.append(columnName).append(" BIGSERIAL PRIMARY KEY,\n");
                    } else if (field.getType() == Integer.class || field.getType() == int.class) {
                        stringBuilder.append(columnName).append(" SERIAL PRIMARY KEY,\n");
                    }
                }
            }
        }
        // Remove the trailing comma and newline
        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        stringBuilder.append(");\n");
        return stringBuilder.toString();
    }

    static String generateInsertionSqlQuery(Class<?> aClass, Object entity) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();
        OrmEntity annotation = aClass.getAnnotation(OrmEntity.class);
        stringBuilder.append("INSERT INTO ").append(annotation.table().toLowerCase()).append(" (");

        List<String> columnNames = new ArrayList<>();
        List<String> columnValues = new ArrayList<>();

        for (Field field : aClass.getDeclaredFields()) {
            OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
            if (ormColumn != null) {
                columnNames.add(ormColumn.name());
                field.setAccessible(true);
                if (field.getType() == String.class) {
                    String value = "'" + field.get(entity).toString() + "'";
                    columnValues.add(value);
                } else {

                    columnValues.add(field.get(entity).toString());
                }
            }
        }

        stringBuilder.append(String.join(", ", columnNames));
        stringBuilder.append(") ");
        stringBuilder.append("VALUES (");
        stringBuilder.append(String.join(",", columnValues));
        stringBuilder.append(");");

        return stringBuilder.toString();
    }

//    UPDATE "User" SET -- Added SET before column updates
//    firstName = ?,
//    lastName = ?,
//    password = ?,
//    age = ?,
//    money = ? -- Assuming money is a column in the table
//    WHERE id = ?;

    record Pair<T, U>(T left, U right) {
    }

    String generateUpdateSqlQuery(Class<?> aClass, Object entity) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();
        OrmEntity annotation = aClass.getAnnotation(OrmEntity.class);
        stringBuilder.append("UPDATE ").append(annotation.table()).append(" SET\n");


        List<Pair<String, String>> columnNamesAndValues = new ArrayList<>();

        String id = null;
        for (Field field : aClass.getDeclaredFields()) {
            OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
            if (ormColumn != null) {
                field.setAccessible(true);
                if (field.getType() == String.class) {
                    String value = null;
                    try {
                        value = "'" + field.get(entity).toString() + "'";
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    columnNamesAndValues.add(new Pair<>(ormColumn.name(), value));
                } else {
                    columnNamesAndValues.add(new Pair<>(ormColumn.name(), field.get(entity).toString()));
                }
            } else {
                field.setAccessible(true);
                id = field.get(entity).toString();
            }
        }


        stringBuilder.append(columnNamesAndValues.stream().map(n -> n.left + " = " + n.right).collect(Collectors.joining(",\n"))).append("\n");
        stringBuilder.append("WHERE id = ").append(id).append(";");
        System.out.println("KILL ME " + stringBuilder);
        return stringBuilder.toString();
    }

    private static String mapTypeToSqlType(Class<?> type) {
        if (type == String.class) {
            return "TEXT";
        } else if (type == Double.class) {
            return "DOUBLE PRECISION";
        } else if (type == Integer.class) {
            return "INT";
        } else if (type == Boolean.class) {
            return "BOOLEAN";
        } else if (type == Long.class) {
            return "BIGINT";
        } else {
            throw new UnsupportedType("this type is not supported: " + type.getName());
        }
    }

    public void save(Object entity) {
        try {
            String SQL_INSERT = generateInsertionSqlQuery(entity.getClass(), entity);
            System.out.println("THE RETURN IS: " + this.jdbcTemplate.update(SQL_INSERT));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Object entity) {
        try {
            String SQL_INSERT = generateUpdateSqlQuery(entity.getClass(), entity);
            System.out.println("THE RETURN IS: " + this.jdbcTemplate.update(SQL_INSERT));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    static class DynamicRowMapper<T> implements RowMapper<T> {
        private final Class<T> mappedClass;

        public DynamicRowMapper(Class<T> mappedClass) {
            this.mappedClass = mappedClass;
        }


        @Override
        public T mapRow(ResultSet rs, int rowNum) throws SQLException {
            System.out.println("MAPPING !!!!");
            try {
                System.out.println("CONSTRUCTOR ??");
                T instance = mappedClass.getDeclaredConstructor().newInstance();
                System.out.println("CONSTRUCTOR HUH ??");
                for (Field field : mappedClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(OrmColumnId.class)) {
                        System.out.println("IS THIS ID ??");
                        Object value = rs.getObject("id");
                        System.out.println("PROBABLY");
                        System.out.println(value);
                        field.setAccessible(true);
                        field.set(instance, value);
                        System.out.println("ID WORKING");
                    }
                    if (field.isAnnotationPresent(OrmColumn.class)) {
                        System.out.println("IS THIS COLUMN ??");
                        OrmColumn ormColumnAnnotation = field.getAnnotation(OrmColumn.class);
                        String columnName = ormColumnAnnotation.name();
                        System.out.println("PROBABLY");
                        Object value = rs.getObject(columnName);
                        System.out.println(value);
                        field.setAccessible(true);
                        field.set(instance, value);
                        System.out.println("COLUMN WORKING");
                    }
                }
                return instance;
            } catch (Exception e) {
                throw new SQLException("Failed to map row", e);
            }
        }
    }

}
