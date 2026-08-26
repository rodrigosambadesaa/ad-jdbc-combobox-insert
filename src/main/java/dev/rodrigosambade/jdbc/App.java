package dev.rodrigosambade.jdbc;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public final class App {

    private App() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::show);
    }

    private static void show() {
        try {
            Connection connection = DemoDatabase.open();
            ArticleRepository repository = new ArticleRepository();
            JFrame frame = createFrame(connection, repository);
            frame.setVisible(true);
        } catch (SQLException exception) {
            showError(null, exception);
        }
    }

    private static JFrame createFrame(
            Connection connection,
            ArticleRepository repository) throws SQLException {
        JFrame frame = new JFrame("Alta de artículos");
        JComboBox<Article> articles = new JComboBox<>();
        JTextField model = new JTextField();
        JTextField price = new JTextField();
        JTextArea description = new JTextArea(4, 30);
        JButton add = new JButton("Añadir");

        reloadArticles(articles, repository, connection);
        add.addActionListener(event -> insertArticle(
                frame,
                articles,
                model,
                description,
                price,
                repository,
                connection));

        frame.add(createForm(model, price, description, add, articles));
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(closeConnectionWhenWindowCloses(connection));
        return frame;
    }

    private static JPanel createForm(
            JTextField model,
            JTextField price,
            JTextArea description,
            JButton add,
            JComboBox<Article> articles) {
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        form.add(new JLabel("Modelo"));
        form.add(model);
        form.add(new JLabel("Precio"));
        form.add(price);
        form.add(new JLabel("Descripción"));
        form.add(new JScrollPane(description));
        form.add(add);
        form.add(articles);
        return form;
    }

    private static void insertArticle(
            JFrame frame,
            JComboBox<Article> articles,
            JTextField model,
            JTextArea description,
            JTextField price,
            ArticleRepository repository,
            Connection connection) {
        try {
            repository.insert(
                    connection,
                    model.getText(),
                    description.getText(),
                    new BigDecimal(price.getText().trim()));

            clearForm(model, description, price);
            reloadArticles(articles, repository, connection);
        } catch (RuntimeException | SQLException exception) {
            showError(frame, exception);
        }
    }

    private static void reloadArticles(
            JComboBox<Article> articles,
            ArticleRepository repository,
            Connection connection) throws SQLException {
        Article[] values = repository.all(connection).toArray(Article[]::new);
        articles.setModel(new DefaultComboBoxModel<>(values));
    }

    private static void clearForm(
            JTextField model,
            JTextArea description,
            JTextField price) {
        model.setText("");
        description.setText("");
        price.setText("");
    }

    private static WindowAdapter closeConnectionWhenWindowCloses(Connection connection) {
        return new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                try {
                    connection.close();
                } catch (SQLException ignored) {
                    // The window is already closing; there is no useful recovery action.
                }
            }
        };
    }

    private static void showError(JFrame frame, Exception exception) {
        JOptionPane.showMessageDialog(
                frame,
                exception.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
