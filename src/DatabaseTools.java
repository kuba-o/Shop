import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseTools {
    public static void queryUpdateCreator(Connection connection, String query){
        try {
            Statement stmt = null;
            stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static <E> E queryReturnCreator(Connection connection, String query, E type){
////        String query = "select * from shop where " + column + " = '" + phrase + "';";
//        E result = null;
//        ResultSet rs = null;
//        try {
//            Statement stmt = null;
//            stmt = connection.createStatement();
//            rs = stmt.executeQuery(query);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return rs;
//
//    }

    public static ResultSet queryReturnRsCreator(Connection connection, String query){
        ResultSet rs = null;
        try {
            Statement stmt = null;
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
}
