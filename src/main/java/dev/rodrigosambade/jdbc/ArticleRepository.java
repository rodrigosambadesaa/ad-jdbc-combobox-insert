package dev.rodrigosambade.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class ArticleRepository {

    private static final String SELECT_ALL = """
            SELECT codigo, modelo, descripcion, precio
              FROM articulos
             ORDER BY codigo
            """;

    private static final String INSERT_ARTICLE = """
            INSERT INTO articulos (
                modelo, descripcion, precio, descuento, cod_marca, cod_familia
            ) VALUES (?, ?, ?, 0, 1, 1)
            """;

    public List<Article> all(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            List<Article> articles = new ArrayList<>();
            while (resultSet.next()) {
                articles.add(map(resultSet));
            }
            return List.copyOf(articles);
        }
    }

    public Article insert(
            Connection connection,
            String model,
            String description,
            BigDecimal price) throws SQLException {
        String normalizedModel = validateModel(model);
        validatePrice(price);

        try (PreparedStatement statement = connection.prepareStatement(
                INSERT_ARTICLE,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, normalizedModel);
            statement.setString(2, description);
            statement.setBigDecimal(3, price);
            statement.executeUpdate();

            return readInsertedArticle(statement, normalizedModel, description, price);
        }
    }

    private static Article readInsertedArticle(
            PreparedStatement statement,
            String model,
            String description,
            BigDecimal price) throws SQLException {
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (!generatedKeys.next()) {
                throw new SQLException("No se obtuvo la clave generada");
            }
            return new Article(
                    generatedKeys.getInt(1),
                    model,
                    description,
                    price);
        }
    }

    private static Article map(ResultSet resultSet) throws SQLException {
        return new Article(
                resultSet.getInt("codigo"),
                resultSet.getString("modelo"),
                resultSet.getString("descripcion"),
                resultSet.getBigDecimal("precio"));
    }

    private static String validateModel(String model) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Modelo obligatorio");
        }
        return model.trim();
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("Precio inválido");
        }
    }
}
