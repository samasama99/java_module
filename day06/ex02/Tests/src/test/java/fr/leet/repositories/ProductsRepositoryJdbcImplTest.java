package fr.leet.repositories;

import fr.leet.models.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductsRepositoryJdbcImplTest {
    final List<Product> EXPECTED_FIND_ALL_PRODUCTS = List.of(
            new Product(0, "product 1", 5.5),
            new Product(1, "product 2", 15.5),
            new Product(2, "product 3", 25.5),
            new Product(3, "product 4", 35.5),
            new Product(4, "product 5", 45.5),
            new Product(5, "product 6", 55.5)
    );
    final Product EXPECTED_FIND_BY_ID_PRODUCT =
            new Product(4, "product 5", 45.5);

    final Product EXPECTED_UPDATED_PRODUCT =
            new Product(3, "product 4", 100.5);

    DataSource dataSource;

    @BeforeEach
    void init() {
        System.out.println("[INFO] Before Each init()");
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        assertNotNull(dataSource);
    }

    @Test
    void TestFindAll() {
        System.out.println("[INFO] TestFindAll()");
        ProductsRepositoryJdbcImpl productsRepositoryJdbc = new ProductsRepositoryJdbcImpl(new JdbcTemplate(dataSource));
        assertEquals(productsRepositoryJdbc.findAll(), EXPECTED_FIND_ALL_PRODUCTS);
    }

    @Test
    void TestFindId() {
        System.out.println("[INFO] TestFindId()");
        ProductsRepositoryJdbcImpl productsRepositoryJdbc = new ProductsRepositoryJdbcImpl(new JdbcTemplate(dataSource));
        Optional<Product> optionalProduct = productsRepositoryJdbc.findById(4L);
        optionalProduct.orElseThrow();
        optionalProduct.ifPresent(product -> assertEquals(product, EXPECTED_FIND_BY_ID_PRODUCT));
    }

    @Test
    void TestUpdate() {
        System.out.println("[INFO] TestUpdate()");
        ProductsRepositoryJdbcImpl productsRepositoryJdbc = new ProductsRepositoryJdbcImpl(new JdbcTemplate(dataSource));
        {
            Optional<Product> optionalProduct = productsRepositoryJdbc.findById(3L);
            optionalProduct.orElseThrow();
            optionalProduct.ifPresent(product -> {
                product.setPrice(100.5);
                productsRepositoryJdbc.update(product);
            });
        }
        {

            Optional<Product> optionalProduct = productsRepositoryJdbc.findById(3L);
            optionalProduct.orElseThrow();
            optionalProduct.ifPresent(product -> assertEquals(product, EXPECTED_UPDATED_PRODUCT));
        }
    }

    @Test
    void TestSave() {
        System.out.println("[INFO] TestSave()");
        ProductsRepositoryJdbcImpl productsRepositoryJdbc = new ProductsRepositoryJdbcImpl(new JdbcTemplate(dataSource));
        {
            Optional<Product> optionalProduct = productsRepositoryJdbc.findById(6L);
            assertThrows(NoSuchElementException.class, optionalProduct::orElseThrow);
        }
        {
            Product newProduct = new Product(6, "product 7", 90.55);
            productsRepositoryJdbc.save(newProduct);
            Optional<Product> optionalProduct = productsRepositoryJdbc.findById(6L);
            optionalProduct.orElseThrow();
            optionalProduct.ifPresent(product -> assertEquals(product, newProduct));
        }
    }

    @Test
    void TestDelete() {
        System.out.println("[INFO] TestDelete()");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        ProductsRepositoryJdbcImpl productsRepositoryJdbc = new ProductsRepositoryJdbcImpl(jdbcTemplate);
        {
            Optional<Product> optionalProduct = productsRepositoryJdbc.findById(6L);
            assertThrows(NoSuchElementException.class, optionalProduct::orElseThrow);
        }
        {
            Product newProduct = new Product(6, "product 7", 90.55);
            productsRepositoryJdbc.save(newProduct);
            Optional<Product> optionalProduct = productsRepositoryJdbc.findById(6L);
            optionalProduct.orElseThrow();
            optionalProduct.ifPresent(product -> assertEquals(product, newProduct));
        }
        {
            productsRepositoryJdbc.delete(6L);
            Optional<Product> optionalProduct = productsRepositoryJdbc.findById(6L);
            assertThrows(NoSuchElementException.class, optionalProduct::orElseThrow);
        }
    }

    @AfterEach
    void close() throws Exception {
        System.out.println("[INFO] After Each close()");
        Connection con = dataSource.getConnection();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("DROP TABLE Product");
        con.close();
        Thread.sleep(Duration.ofMillis(100));
    }
}