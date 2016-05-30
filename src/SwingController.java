import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class SwingController {
    public DatabaseController databaseController = new DatabaseController();
    public JFrame mainFrame = new JFrame();
    public JPanel controlButtons = new JPanel();
    public JPanel tablePanel = new JPanel();
    public JTable mainTable;
    public int selectedRow;
    SwingController(){ prepareGui();}

    public void prepareGui(){
        databaseController.createConnection();
        controlButtons.setMaximumSize(new Dimension(800, controlButtons.getHeight()));
        mainFrame = new JFrame("Title");
        mainFrame.setSize(1200, 800);
        BoxLayout boxLayout = new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
        mainFrame.setLayout(boxLayout);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        JButton addNewAlbum= new JButton("Add");
        addNewAlbum.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fillNewAlbums();
            }
        });

        JButton searchRecords = new JButton("Search");
        searchRecords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSearchMenu();
            }
        });

        JButton editRecords = new JButton("Edit");
        editRecords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditMenu();
            }
        });

        JButton deleteRecords= new JButton("Delete");
        deleteRecords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = mainTable.getSelectedRow();
                String artist = mainTable.getModel().getValueAt(selectedRow, 1).toString();
                String title = mainTable.getModel().getValueAt(selectedRow, 0).toString();
                databaseController.removeRow(title, artist);
                displayMainTable(tablePanel);
            }
        });

        JButton singleRecordAdd = new JButton("+");
        singleRecordAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = mainTable.getSelectedRow();
                String artist = mainTable.getModel().getValueAt(selectedRow, 1).toString();
                String title = mainTable.getModel().getValueAt(selectedRow, 0).toString();
                databaseController.updateQuantity(title, artist, 1);
                displayMainTable(tablePanel);
                mainTable.addRowSelectionInterval(selectedRow, selectedRow);
            }
        });

        JButton singleRecordRemove = new JButton("-");
        singleRecordRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = mainTable.getSelectedRow();
                String artist = mainTable.getModel().getValueAt(selectedRow, 1).toString();
                String title = mainTable.getModel().getValueAt(selectedRow, 0).toString();
                databaseController.updateQuantity(title, artist, -1);
//                System.out.println(mainTable.getRowCount());
                System.out.println(selectedRow);
                if (databaseController.getQuantity(title, artist)<1){
                    selectedRow = 0;
                    databaseController.removeRow(title, artist);
                }
                displayMainTable(tablePanel);
                mainTable.addRowSelectionInterval(selectedRow, selectedRow);
            }
        });

        JButton searchRecords2 = new JButton("Search");
        controlButtons.add(addNewAlbum);
        controlButtons.add(searchRecords);
        controlButtons.add(editRecords);
        controlButtons.add(deleteRecords);
        controlButtons.add(singleRecordAdd);
        controlButtons.add(singleRecordRemove);
        controlButtons.setSize(30, 800);
        mainFrame.add(controlButtons);
        mainFrame.setVisible(true);
        displayMainTable(tablePanel);
    }

    public void fillNewAlbums(){
        JFrame newAlbumsFrame = new JFrame("Title");
        newAlbumsFrame.setSize(400, 400);
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
                    displayMainTable(tablePanel);
                }
                newAlbumsFrame.setVisible(false);
                newAlbumsFrame.dispose();
            }
        });

        panel.add(addButton);
        layout.putConstraint(SpringLayout.WEST, addButton, 200, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, addButton, 300, SpringLayout.NORTH, panel);
        newAlbumsFrame.add(panel);
        newAlbumsFrame.setVisible(true);
    }

    public void displayMainTable(JPanel tablePanel){
        try {
            tablePanel.removeAll();
            mainTable = new JTable(buildTableModel(databaseController.searchEverything()));
            mainTable.setDefaultEditor(Object.class, null);
            tablePanel.repaint();
            tablePanel.add(mainTable);
            tablePanel.setVisible(true);
            mainFrame.add(tablePanel);
            mainFrame.setVisible(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<Object> columnNames = new Vector<Object>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        data.add(columnNames);
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }

    public void openSearchMenu(){
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
                try {
                    displaySearchResults(rs);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        searchPanel.add(searchByArtist);
        JButton searchByTitle = new JButton("Title");
        searchByTitle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultSet rs = null;
                rs = searchQuery("title", phraseField.getText());
                searchFrame.setVisible(false);
                searchFrame.dispose();
                try {
                    displaySearchResults(rs);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
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

    public void displaySearchResults (ResultSet rs) throws SQLException {
        JTable table = new JTable(buildTableModel(rs));
        JFrame searchResultFrame = new JFrame("Search results");
        searchResultFrame.setSize(800, 500);
        searchResultFrame.add(table);
        searchResultFrame.setVisible(true);

    }

    public void openEditMenu(){
        JFrame editFrame = new JFrame("Title");
        editFrame.setSize(400, 400);
        SpringLayout layout = new SpringLayout();

        int selectedRow = mainTable.getSelectedRow();
        String old_title = mainTable.getModel().getValueAt(selectedRow, 0).toString();
        String old_artist = mainTable.getModel().getValueAt(selectedRow, 1).toString();

        JPanel panel = new JPanel();
        panel.setLayout(layout);

        JLabel titleLabel = new JLabel("Enter Title: ");
        JTextField titleField = new JTextField("", 15);
        titleField.setText(mainTable.getModel().getValueAt(selectedRow, 0).toString());
        JLabel artistLabel = new JLabel("Enter Artist: ");
        JTextField artistField= new JTextField("", 15);
        artistField.setText(mainTable.getModel().getValueAt(selectedRow, 1).toString());
        JLabel quantityLabel = new JLabel("Enter quantity: ");
        JTextField quantityField= new JTextField("", 15);
        quantityField.setText(mainTable.getModel().getValueAt(selectedRow, 2).toString());
        JLabel priceLabel= new JLabel("Enter price: ");
        JTextField priceField= new JTextField("", 15);
        priceField.setText(mainTable.getModel().getValueAt(selectedRow, 3).toString());

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

        JButton saveButton= new JButton("Save changes");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                databaseController.editRow(old_title, old_artist, artistField.getText(), titleField.getText(),
                        Integer.parseInt(quantityField.getText()), Double.parseDouble(priceField.getText()));
                editFrame.setVisible(false);
                editFrame.dispose();
                displayMainTable(tablePanel);
            }
        });

        JButton cancelButton= new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editFrame.setVisible(false);
                editFrame.dispose();
            }
        });

        panel.add(saveButton);
        panel.add(cancelButton);
        layout.putConstraint(SpringLayout.WEST, saveButton, 150, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, saveButton, 300, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, cancelButton, 300, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, cancelButton, 300, SpringLayout.NORTH, panel);
        editFrame.add(panel);
        editFrame.setVisible(true);
    }
}