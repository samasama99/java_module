package fr.leet.repositories;

import fr.leet.models.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductsRepositoryJdbcImpl implements ProductsRepository {
    JdbcTemplate jdbcTemplate;
    RowMapper<Product> rowMapper;

    ProductsRepositoryJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new ProductRowMapper();
    }

    @Override
    public List<Product> findAll() {
        final String SQL = "SELECT identifier, name, price FROM Product";
        return this.jdbcTemplate.query(SQL, rowMapper);
    }

    @Override
    public Optional<Product> findById(Long id) {
        final String SQL = "SELECT identifier, name, price FROM Product WHERE identifier = ?";
        Product product;
        try {
            product = this.jdbcTemplate.queryForObject(SQL, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        if (product == null)
            return Optional.empty();
        return Optional.of(product);
    }

    @Override
    public void update(Product product) {
        final String SQL = "UPDATE Product SET name = ?, price = ? WHERE identifier = ?";
        this.jdbcTemplate.update(SQL, product.getName(), product.getPrice(), product.getIdentifier());
    }

    @Override
    public void save(Product product) {
        final String SQL = "INSERT INTO Product (name, price) VALUES (?, ?)";
        this.jdbcTemplate.update(SQL, product.getName(), product.getPrice());
    }

    @Override
    public void delete(Long id) {
        final String SQL = "DELETE FROM Product WHERE identifier = ?";
        this.jdbcTemplate.update(SQL, id);
    }

    static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setIdentifier(rs.getLong("identifier"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            return product;
        }
    }
}
