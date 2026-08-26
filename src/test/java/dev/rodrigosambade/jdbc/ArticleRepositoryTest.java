package dev.rodrigosambade.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArticleRepositoryTest {

    @Test
    void insertsArticleAndReturnsGeneratedIdentifier() throws Exception {
        try (Connection connection = DemoDatabase.open()) {
            ArticleRepository repository = new ArticleRepository();
            int articlesBeforeInsert = repository.all(connection).size();

            Article inserted = repository.insert(
                    connection,
                    "Nuevo",
                    "Demo",
                    new BigDecimal("12.50"));

            assertTrue(inserted.id() > 0);
            assertEquals(articlesBeforeInsert + 1, repository.all(connection).size());
        }
    }
}
