;import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SwingController {
    public DatabaseController databaseController = new DatabaseController();
    private JFrame mainFrame;
    private JPanel controlPanel;
    private String[] colnames = {"Artist", "Album", "Price", "Quantity"};
    public SwingController(){
        prepareGui();
    }

    public void prepareGui(){
        mainFrame = new JFrame("Title");
        mainFrame.setSize(1200, 800);
        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JButton addNewAlbum= new JButton("Add");
        addNewAlbum.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fillNewAlbums();
            }
        });
        controlPanel.add(addNewAlbum);

        JButton searchRecords = new JButton("Search");
        searchRecords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSearchMenu();
            }
        });
        controlPanel.add(searchRecords);

        mainFrame.add(controlPanel);

        mainFrame.setVisible(true);
        mainFrame.setLayout(new GridLayout(3, 1));
    }

    public void fillNewAlbums(){
        mainFrame = new JFrame("Title");
        mainFrame.setSize(400, 400);
        SpringLayout layout = new SpringLayout();

        JPanel panel = new JPanel();
        panel.setLayout(layout);

        JLabel titleLabel = new JLabel("Enter Title: ");
        JTextField titleField = new JTextField("", 15);
        JLabel artistLabel = new JLabel("Enter Artist: ");
        JTextField artistField= new JTextField("", 15);
        JLabel quantityLabel = new JLabel("Enter quantity: ");
        JTextField quantityField= new JTextField("", 15);
        JLabel priceLabel= new JLabel("Enter price: ");
        JTextField priceField= new JTextField("", 15);

        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(artistLabel);
        panel.add(artistField);
        panel.add(quantityLabel);
        panel.add(quantityField);
        panel.add(priceLabel);
        panel.add(priceField);

        layout.putConstraint(SpringLayout.NORTH, titleLabel, 5, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, titleLabel, 5, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.WEST, titleField, 5, SpringLayout.EAST, titleLabel);
        layout.putConstraint(SpringLayout.NORTH, titleField, 5, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.NORTH, artistLabel, 15, SpringLayout.SOUTH, titleLabel);
        layout.putConstraint(SpringLayout.WEST, artistLabel,5, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.WEST, artistField, 5, SpringLayout.EAST, artistLabel);
        layout.putConstraint(SpringLayout.NORTH, artistField, 10, SpringLayout.SOUTH, titleField);
        layout.putConstraint(SpringLayout.WEST, quantityLabel, 5, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, quantityLabel, 10, SpringLayout.SOUTH, artistLabel);
        layout.putConstraint(SpringLayout.WEST, quantityField, 5, SpringLayout.EAST, quantityLabel);
        layout.putConstraint(SpringLayout.NORTH, quantityField, 10, SpringLayout.SOUTH, artistField);
        layout.putConstraint(SpringLayout.WEST, priceLabel, 5, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, priceLabel, 10, SpringLayout.SOUTH, quantityLabel);
        layout.putConstraint(SpringLayout.WEST, priceField, 5, SpringLayout.EAST, priceLabel);
        layout.putConstraint(SpringLayout.NORTH, priceField, 10, SpringLayout.SOUTH, quantityField);

        JButton addButton= new JButton("Add the album");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (databaseController.checkIfAlbumArtistExists(artistField.getText(), titleField.getText())) {
                    databaseController.updateQuantity(titleField.getText(), artistField.getText(),Integer.parseInt(quantityField.getText()));
                } else {
                    databaseController.addAlbum(titleField.getText(), artistField.getText(),
                            Integer.parseInt(quantityField.getText()), Double.parseDouble(priceField.getText()));
                }
                mainFrame.setVisible(false);
                mainFrame.dispose();
            }
        });

        panel.add(addButton);
        layout.putConstraint(SpringLayout.WEST, addButton, 200, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, addButton, 300, SpringLayout.NORTH, panel);
        mainFrame.add(panel);
        mainFrame.setVisible(true);
    }

    public void openSearchMenu(){
        ResultSet resSet = null;
        JFrame searchFrame = new JFrame("Search");
        searchFrame.setSize(200, 200);
        JPanel searchPanel = new JPanel();

        JLabel phraseLabel = new JLabel("Type in the phrase:");
        JTextField phraseField = new JTextField("", 15);

        searchPanel.add(phraseLabel);
        searchPanel.add(phraseField);

        JButton searchByArtist = new JButton("Artist");
        searchByArtist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultSet rs = null;
                rs = searchQuery("artist", phraseField.getText());
                searchFrame.setVisible(false);
                searchFrame.dispose();
                displaySearchResults(rs);
            }
        });
        searchPanel.add(searchByArtist);

        JButton searchByTitle = new JButton("Title");
        searchByTitle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFrame.setVisible(false);
                searchFrame.dispose();
            }
        });
        searchPanel.add(searchByTitle);
        searchFrame.add(searchPanel);
        searchFrame.setVisible(true);
    }

    public ResultSet searchQuery(String column, String phrase){
        try {
            return databaseController.searchQuery(column, phrase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public void displaySearchResults(ResultSet rs){
//        JFrame searchResultFrame = new JFrame("Search results");
//        searchResultFrame.setSize(800, 500);
//        JPanel resultPanel = new JPanel(new GridLayout(0,4));
//        TextArea textArea = new TextArea();
//        TextArea textArea2 = new TextArea();
//
//        JScrollPane resultArea = new JScrollPane(resultPanel);
//        try {
//            while (rs.next()){
////                ####
//                FlowLayout fl = new FlowLayout();
//
//                String album = rs.getString("title");
//                String artist= rs.getString("artist");
//                int quantity = rs.getInt("quantity");
//                double price = rs.getDouble("price");
//                textArea.append(album);
//                textArea.append("\n");
//                textArea.append(artist);
//                textArea.append("\n");
//                textArea2.append(Integer.toString(quantity));
//                textArea2.append("\n");
//                textArea2.append(Double.toString(price));
//                textArea2.append("\n");
//                resultPanel.add(textArea);
//                resultPanel.add(textArea2);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        searchResultFrame.add(resultPanel);
//        searchResultFrame.setVisible(true);
//    }

    public void displaySearchResults(ResultSet rs){
        ArrayList<Record> recordsList = new ArrayList<>();
        System.out.println("DUPA1");
        try {
            while (rs.next()) {
                recordsList.add(new Record(rs.getString("title"), rs.getString("artist"), rs.getDouble("price"), rs.getInt("quantity")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
