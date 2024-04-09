import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class DBTest {

    private static Connection connection;
    private static int lastAddedId;

    @BeforeAll
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:testdb", "user", "pass");
    }

    @Test
    public void addingPosition() throws SQLException {
        doSelectAllQuery();
        lastAddedId = insertNewFoodQuery("Watermelon", "FRUIT", true);
        doSelectAllQuery();
    }

    @Test
    public void deletingPosition() throws SQLException {
        doSelectAllQuery();
        deleteFromFoodQuery(lastAddedId);
        doSelectAllQuery();
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        connection.close();
    }

    public static void doSelectAllQuery() throws SQLException {
        String selectAllQuery = "SELECT * FROM FOOD";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectAllQuery);
        System.out.println("Execute select query\n");
        resultSet.last();
        int id = resultSet.getInt("FOOD_ID");
        String name = resultSet.getString("FOOD_NAME");
        String type = resultSet.getString("FOOD_TYPE");
        boolean exotic = resultSet.getBoolean("FOOD_EXOTIC");
        System.out.printf("Last row in the table: " + "%d, %s, %s, %b" + "\n", id, name, type, exotic);
    }

    public static int insertNewFoodQuery(String foodName, String foodType, boolean exotic) throws SQLException {
        String insert = "insert into FOOD(FOOD_NAME, FOOD_TYPE, FOOD_EXOTIC) values(?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert);
        preparedStatement.setString(1, foodName);
        preparedStatement.setString(2, foodType);
        preparedStatement.setBoolean(3, exotic);
        preparedStatement.executeUpdate();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM FOOD");
        resultSet.last();
        int id = resultSet.getInt("FOOD_ID");
        System.out.printf("Executing add food query: \"%s\" added with id %d  \n", foodName, id);
        return id;
    }

    public static void deleteFromFoodQuery(int id) throws SQLException{
        String delete = "delete from FOOD where FOOD_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(delete);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        System.out.printf("Deleting food with id %d \n", id);
    }


}
