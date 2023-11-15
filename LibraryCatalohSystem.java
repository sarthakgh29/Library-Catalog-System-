import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LibraryCatalogSystem {
    private static final String DB_URL = "jdbc:mysql://localhost/library";
    private static final String DB_USER = "uruser";
    private static final String DB_PASSWORD = "urpass";

    private JFrame frame;
    private JTextField titleField, authorField, yearField, isbnField;
    private JButton addButton, viewButton;

    public LibraryCatalogSystem() {
        frame = new JFrame("Library Catalog System");
        titleField = new JTextField(20);
        authorField = new JTextField(20);
        yearField = new JTextField(4);
        isbnField = new JTextField(13); // Add ISBN field
        addButton = new JButton("Add Book");
        viewButton = new JButton("View Catalog");

        frame.setLayout(new FlowLayout());

        frame.add(new JLabel("Title:"));
        frame.add(titleField);

        frame.add(new JLabel("Author:"));
        frame.add(authorField);

        frame.add(new JLabel("Year:"));
        frame.add(yearField);

        frame.add(new JLabel("ISBN:"));
        frame.add(isbnField);

        frame.add(addButton);
        frame.add(viewButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBookToCatalog();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewCatalog();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void addBookToCatalog() {
        String title = titleField.getText();
        String author = authorField.getText();
        int year = Integer.parseInt(yearField.getText());
        String isbn = isbnField.getText(); // Get ISBN

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String insertQuery = "INSERT INTO books (title, author, publication_year, isbn) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, year);
            preparedStatement.setString(4, isbn); // Set ISBN
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Book added to the catalog!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void viewCatalog() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String selectQuery = "SELECT title, author, publication_year, isbn FROM books";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            StringBuilder catalog = new StringBuilder("Library Catalog:\n");

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int year = resultSet.getInt("publication_year");
                String isbn = resultSet.getString("isbn");
                catalog.append(title).append(" by ").append(author).append(" (").append(year).append(") - ISBN: ").append(isbn).append("\n");
            }

            JOptionPane.showMessageDialog(frame, catalog.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryCatalogSystem());
    }
}
