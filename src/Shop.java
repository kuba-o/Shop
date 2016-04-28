public class Shop {

    public static void main(String[] argv) {
        DatabaseController databaseController = new DatabaseController();
        SwingController swingController = new SwingController();
        databaseController.createConnection();

        databaseController.countAlbums();
    }
}