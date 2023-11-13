package fr.leet.chat.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.leet.chat.datasource.DataSource;
import fr.leet.chat.repositories.UserRepositoryJdbcImpl;

import java.sql.SQLException;

public class Program {
    public static void main(String[] args)  {
        DataSource dataSource = new DataSource();
        UserRepositoryJdbcImpl userRepositoryJdbc = new UserRepositoryJdbcImpl(dataSource);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            userRepositoryJdbc.findAll(0, 3).forEach((el) -> {
                System.out.println(gson.toJson(el));
            });
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        dataSource.close();
    }
}
