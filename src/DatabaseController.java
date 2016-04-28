import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;

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

    public void countAlbums(){
        try {
            Statement stmt = null;
            stmt = connection.createStatement();
            String query = "select count(*) as c from shop";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                int count = rs.getInt("c");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void addAlbum(String title, String artist, int quantity, double price){
            String query = "insert into shop (title, artist, quantity, price) values ('" +
                    title + "', '" + artist + "', " + quantity + ", " + price + ");";
            int result = 0;
            queryCreatorUpdate(query, result);
    }

    public void sellAlbum(String title, String artist){
        createConnection();
        try {
            String query = "update shop set quantity = quantity -1 where title = '" + title + "' and artist = '" + artist + "';";
            Statement stmt = null;
            stmt = connection.createStatement();
            stmt.executeUpdate(query);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showQuery(String artist, String title){
        createConnection();
        try {
            String query = "select quantity from shop where artist = '" + artist + "' and title = '" + title + "';";
            Statement stmt = null;
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkIfAlbumArtistExists(String artist, String title) {
        createConnection();
        try {
            String query = "select quantity from shop where artist = '" + artist + "' and title = '" + title + "';";
            Statement stmt = null;
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs.first();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateQuantity(String title, String artist, int quantity){
       // update shop set quantity = quantity + 3 where artist = 'zxc' and title = 'zxc';
        String query = "update shop set quantity = quantity +" + quantity + " where artist = '" + artist + "' and title = '" + title + "';";

        createConnection();
        try {
            Statement stmt = null;
            stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queryCreatorUpdate(String query, int result){
        createConnection();
        try {
            Statement stmt = null;
            stmt = connection.createStatement();
            result = stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queryCreator(String query, ResultSet result){
        createConnection();
        try {
            Statement stmt = null;
            stmt = connection.createStatement();
            result= stmt.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet searchQuery(String column, String phrase) throws Exception{
        createConnection();
        String query = "select * from shop where " + column + " = '" + phrase + "';";
        System.out.println(query);
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