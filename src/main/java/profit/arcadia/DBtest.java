//package profit.arcadia;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//public class DBtest {
//    // 데이터베이스 연결 정보
//    // 데이터베이스 연결 정보
//    private static String url = "jdbc:mariadb://localhost:3307/bootex";
//    private static final String user = "root";
//    private static final String password = "5451";
//
//    // 데이터 삽입할 SQL 쿼리
//    private static final String insertQuery = "INSERT INTO users (username, email) VALUES ('kim', '@naver.com')";
//
//    // SQL SELECT 쿼리
//    private static final String selectQuery = "SELECT * FROM users";
//
//    public static void insertAndSelectData() {
//        try (Connection connection = DriverManager.getConnection(url, user, password);
//            Statement statement = connection.createStatement()) {
//            // 데이터 삽입 실행
//            int rowsAffected = statement.executeUpdate(insertQuery);
//            System.out.println("Rows affected: " + rowsAffected);
//
//            // SQL SELECT 실행
//            ResultSet resultSet = statement.executeQuery(selectQuery);
//
//            // 결과 출력
//            while (resultSet.next()) {
//                String column1Value = resultSet.getString("username");
//                String column2Value = resultSet.getString("email");
//                System.out.println("Column 1: " + column1Value + ", Column 2: " + column2Value);
//            }
//        } catch (SQLException e) {
//            System.err.println("Error: " + e.getMessage());
//        }
//    }
//}
