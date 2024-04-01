import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TranslationApp {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/translation_table_demonstration";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Test12";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("1. Add Translation");
                System.out.println("2. Retrieve Translation");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addTranslation(connection, scanner);
                        break;
                    case 2:
                        retrieveTranslation(connection, scanner);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addTranslation(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Language Code: ");
        String languageCode = scanner.nextLine();
        System.out.print("Enter Translation: ");
        String translation = scanner.nextLine();

        String sql = "INSERT INTO employee_translations (emp_id, language_code, name) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, empId);
            statement.setString(2, languageCode);
            statement.setString(3, translation);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Translation added successfully.");
            } else {
                System.out.println("Failed to add translation.");
            }
        }
    }

    private static void retrieveTranslation(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter Employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Language Code: ");
        String languageCode = scanner.nextLine();

        String sql = "SELECT name FROM employee_translations WHERE emp_id = ? AND language_code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, empId);
            statement.setString(2, languageCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String translation = resultSet.getString("name");
                    System.out.println("Translation: " + translation);
                } else {
                    System.out.println("Translation not found for the specified employee and language.");
                }
            }
        }
    }
}
