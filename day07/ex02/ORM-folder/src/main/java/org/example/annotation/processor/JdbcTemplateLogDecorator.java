package org.example.annotation.processor;

import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


public class JdbcTemplateLogDecorator extends JdbcTemplate {
    DataSource dataSource;
    Logger logger;

    JdbcTemplateLogDecorator(DataSource dataSource, Logger logger) {
        super(dataSource);
        this.logger = logger;
        this.dataSource = dataSource;
    }

    @Override
    public void execute(final String sql) throws DataAccessException {
        this.logger.info(sql);
        super.execute(sql);
    }

    @Override
    public int update(final String sql) throws DataAccessException {
        this.logger.info(sql);
        super.update(sql);
        return 0;
    }
}
