import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;

import javax.xml.crypto.Data;
import javax.xml.transform.Result;
import java.sql.*;

public class DatabaseController {
    Enum password = Password.ONE;
    Connection connection = null;
    public void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/informatics_shop","root", password.toString());
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (connection == null) {
            System.out.println("Failed to make connection!");
        }
    }

    public void addAlbum(String title, String artist, int quantity, double price){
            String query = "insert into shop (title, artist, quantity, price) values ('" +
                    title + "', '" + artist + "', " + quantity + ", " + price + ");";
            DatabaseTools.queryUpdateCreator(connection, query);
    }

    public boolean checkIfAlbumArtistExists(String artist, String title) {
        String query = "select QUANTITY from shop where ARTIST = '" + artist + "' and TITLE = '" + title + "';";
        ResultSet rs = DatabaseTools.queryReturnRsCreator(connection, query);
        try {
            return rs.first();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateQuantity(String title, String artist, int quantity){
        String query = "update shop set QUANTITY = QUANTITY +" + quantity + " where ARTIST = '" + artist + "' and TITLE = '" + title + "';";
        DatabaseTools.queryUpdateCreator(connection, query);
    }

    public ResultSet searchQuery(String column, String phrase) throws Exception{
        String query = "select * from shop where " + column + " = '" + phrase + "';";
        return DatabaseTools.queryReturnRsCreator(connection, query);
    }

    public ResultSet searchEverything() throws Exception{
        String query = "select * from shop";
        return DatabaseTools.queryReturnRsCreator(connection, query);
    }

    public int getQuantity(String title, String artist) {
        String query = "select QUANTITY from shop where ARTIST='" + artist + "' and TITLE='" + title +"';";
        int quantity = -1;
        ResultSet rs = DatabaseTools.queryReturnRsCreator(connection, query);
        try {
            while (rs.next()){
                quantity = (int) rs.getObject("QUANTITY");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            Statement stm = null;
//            stm = connection.createStatement();
//            ResultSet rs = stm.executeQuery(query);
//            while (rs.next()){
//                quantity = (int) rs.getObject("QUANTITY");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return quantity;
    }

    public void removeRow(String title, String artist){
        String query = "delete from shop where ARTIST='" + artist + "' AND TITLE = '" + title + "';";
        DatabaseTools.queryUpdateCreator(connection, query);
    }

    public void editRow(String old_title, String old_artist, String artist, String title, int quantity, double price){
        String query = "update shop set TITLE = '" + title + "', ARTIST = '" + artist + "', QUANTITY = " + quantity +
                ", PRICE = " + price + " WHERE ARTIST = '" + old_artist + "' and TITLE ='" + old_title + "';";
        DatabaseTools.queryUpdateCreator(connection, query);
    }
}