package BusPassSystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import java.io.File;

public class Demo {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/buspass";
    private static final String USER = "root";
    private static final String PASS = "2002600688@Kt";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new AdminLogin());
    }

    // Admin Login Window
    static class AdminLogin {
        private JFrame frame;
        private JTextField adminNameField;
        private JPasswordField passwordField;

        private static final Map<String, String> ADMIN_CREDENTIALS = new HashMap<>();
        static {
            ADMIN_CREDENTIALS.put("Nandhu", "Nandhu@123");
            ADMIN_CREDENTIALS.put("Girish", "1234");
            ADMIN_CREDENTIALS.put("Mutthu", "mutthu@123");
            ADMIN_CREDENTIALS.put("Abeed", "1234");
            ADMIN_CREDENTIALS.put("Ajay", "ajay@123");
        }

        public AdminLogin() {
            frame = new JFrame("Admin Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.decode("#E8ECEF"));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;

            JLabel lblAdmin = new JLabel("Admin Name:");
            lblAdmin.setForeground(Color.decode("#212529"));
            lblAdmin.setFont(new Font("Arial", Font.PLAIN, 14));
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(lblAdmin, gbc);

            adminNameField = new JTextField(15);
            adminNameField.setBackground(Color.WHITE);
            adminNameField.setBorder(BorderFactory.createLineBorder(Color.decode("#6C757D")));
            gbc.gridx = 1;
            panel.add(adminNameField, gbc);

            JLabel lblPassword = new JLabel("Password:");
            lblPassword.setForeground(Color.decode("#212529"));
            lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(lblPassword, gbc);

            passwordField = new JPasswordField(15);
            passwordField.setBackground(Color.WHITE);
            passwordField.setBorder(BorderFactory.createLineBorder(Color.decode("#6C757D")));
            gbc.gridx = 1;
            panel.add(passwordField, gbc);

            JButton btnLogin = new JButton("Login");
            btnLogin.setBackground(Color.decode("#0D6EFD"));
            btnLogin.setForeground(Color.WHITE);
            btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
            btnLogin.setFocusPainted(false);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(btnLogin, gbc);

            frame.add(panel);
            frame.setVisible(true);

            btnLogin.addActionListener(e -> {
                String enteredName = adminNameField.getText();
                String enteredPass = new String(passwordField.getPassword());

                if (ADMIN_CREDENTIALS.containsKey(enteredName) &&
                        ADMIN_CREDENTIALS.get(enteredName).equals(enteredPass)) {
                    frame.dispose();
                    new AdminPanel();
                } else {
                    JOptionPane.showMessageDialog(frame, "Incorrect Username or Password");
                }
            });
        }
    }

    // Admin Dashboard Window
    static class AdminPanel {
        private JFrame frame;

        public AdminPanel() {
            frame = new JFrame("Admin Dashboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(450, 350);
            frame.setLocationRelativeTo(null);

            frame.setLayout(new BorderLayout());

            JLabel title = new JLabel("Bus Pass Management System", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 20));
            title.setForeground(Color.decode("#0056b3"));
            frame.add(title, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.decode("#E8ECEF"));
            buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JButton btnAdd = new JButton("Add New Bus Pass");
            btnAdd.setBackground(Color.decode("#0D6EFD"));
            btnAdd.setForeground(Color.WHITE);
            btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
            btnAdd.setFocusPainted(false);

            JButton btnView = new JButton("View All Passes");
            btnView.setBackground(Color.decode("#0D6EFD"));
            btnView.setForeground(Color.WHITE);
            btnView.setFont(new Font("Arial", Font.BOLD, 14));
            btnView.setFocusPainted(false);

            JButton btnExit = new JButton("Exit");
            btnExit.setBackground(Color.decode("#6C757D"));
            btnExit.setForeground(Color.WHITE);
            btnExit.setFont(new Font("Arial", Font.BOLD, 14));
            btnExit.setFocusPainted(false);

            buttonPanel.add(btnAdd);
            buttonPanel.add(btnView);
            buttonPanel.add(btnExit);

            frame.add(buttonPanel, BorderLayout.CENTER);

            btnAdd.addActionListener(e -> new AddBusPass());
            btnView.addActionListener(e -> new ViewPasses());
            btnExit.addActionListener(e -> frame.dispose());

            frame.setVisible(true);
        }
    }

    // Add Bus Pass Form Window
    static class AddBusPass {
        private JFrame frame;
        private JTextField nameField, dobField, ageField, collegeField, distanceField, startPointField, viaRouteField, destinationField;
        private JComboBox<String> passTypeBox, passengerTypeBox;

        private static final Map<String, Integer> PASS_PRICES = new HashMap<>();
        static {
            PASS_PRICES.put("Day Pass - ₹30", 30);
            PASS_PRICES.put("Monthly Pass - ₹400", 400);
            PASS_PRICES.put("Quarter-Year Pass - ₹1000", 1000);
        }

        private static final String[] PASSENGER_TYPES = {"Child", "Adult", "Student"};

        public AddBusPass() {
            frame = new JFrame("Add Bus Pass");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(550, 700);
            frame.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBackground(Color.decode("#E8ECEF"));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Personal Details Section
            JPanel personalPanel = createFormPanel("Personal Details", new String[]{"Student Name:", "Date of Birth (YYYY-MM-DD):", "Age:"});
            nameField = (JTextField) personalPanel.getComponent(1);
            dobField = (JTextField) personalPanel.getComponent(3);
            ageField = (JTextField) personalPanel.getComponent(5);
            mainPanel.add(personalPanel);

            // College Details Section
            JPanel collegePanel = createFormPanel("College Details", new String[]{"College Name:", "Distance (in km):"});
            collegeField = (JTextField) collegePanel.getComponent(1);
            distanceField = (JTextField) collegePanel.getComponent(3);
            mainPanel.add(collegePanel);

            // Route Details Section
            JPanel routePanel = createFormPanel("Route Details", new String[]{"Starting Point:", "Via Route:", "Destination:"});
            startPointField = (JTextField) routePanel.getComponent(1);
            viaRouteField = (JTextField) routePanel.getComponent(3);
            destinationField = (JTextField) routePanel.getComponent(5);
            mainPanel.add(routePanel);

            // Pass Type Section
            JPanel passTypePanel = createComboBoxPanel("Pass Type", "Pass Type:");
            passTypeBox = new JComboBox<>(PASS_PRICES.keySet().toArray(new String[0]));
            passTypeBox.setBackground(Color.WHITE);
            passTypeBox.setBorder(BorderFactory.createLineBorder(Color.decode("#6C757D")));
            passTypePanel.add(passTypeBox);
            mainPanel.add(passTypePanel);

            // Passenger Type Section
            JPanel passengerTypePanel = createComboBoxPanel("Passenger Type", "Passenger Type:");
            passengerTypeBox = new JComboBox<>(PASSENGER_TYPES);
            passengerTypeBox.setBackground(Color.WHITE);
            passengerTypeBox.setBorder(BorderFactory.createLineBorder(Color.decode("#6C757D")));
            passengerTypePanel.add(passengerTypeBox);
            mainPanel.add(passengerTypePanel);

            // Purchase Button
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.decode("#E8ECEF"));
            JButton purchaseButton = new JButton("Purchase Pass");
            purchaseButton.setBackground(Color.decode("#28A745"));
            purchaseButton.setForeground(Color.WHITE);
            purchaseButton.setFont(new Font("Arial", Font.BOLD, 14));
            purchaseButton.setFocusPainted(false);
            buttonPanel.add(purchaseButton);

            frame.add(mainPanel, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            purchaseButton.addActionListener(e -> {
                try {
                    String name = nameField.getText();
                    String dob = dobField.getText().trim();
                    int age = Integer.parseInt(ageField.getText().trim());
                    String college = collegeField.getText();
                    double distance = Double.parseDouble(distanceField.getText().trim());
                    String startPoint = startPointField.getText();
                    String viaRoute = viaRouteField.getText();
                    String destination = destinationField.getText();
                    String passTypeFull = (String) passTypeBox.getSelectedItem();
                    int price = PASS_PRICES.get(passTypeFull);
                    String passType = passTypeFull.split(" - ")[0];
                    String passengerType = (String) passengerTypeBox.getSelectedItem();

                    if (name.isEmpty() || dob.isEmpty() || college.isEmpty() || startPoint.isEmpty()
                            || viaRoute.isEmpty() || destination.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                        return;
                    }

                    if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        JOptionPane.showMessageDialog(frame, "DOB must be in format YYYY-MM-DD.");
                        return;
                    }

                    // Show payment page
                    new PaymentPage(price, () -> {
                        try {
                            int passId = insertIntoDatabase(name, dob, age, college, distance, startPoint, viaRoute, destination, passType, price, passengerType);
                            JOptionPane.showMessageDialog(frame, "Pass Purchased Successfully!");
                            frame.dispose();
                            new TemporaryPassWindow(passId, name, passType, price, startPoint, destination, passengerType);
                            new ViewPasses();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Unexpected Error: " + ex.getMessage());
                        }
                    });
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Age and Distance must be numbers.");
                }
            });

            frame.setVisible(true);
        }

        private JPanel createFormPanel(String title, String[] labels) {
            JPanel panel = new JPanel(new GridLayout(labels.length, 2, 10, 10));
            panel.setBorder(BorderFactory.createTitledBorder(title));
            panel.setBackground(Color.decode("#E8ECEF"));

            for (String label : labels) {
                JLabel jLabel = new JLabel(label);
                jLabel.setForeground(Color.decode("#212529"));
                jLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                panel.add(jLabel);

                JTextField textField = new JTextField();
                textField.setBackground(Color.WHITE);
                textField.setBorder(BorderFactory.createLineBorder(Color.decode("#6C757D")));
                panel.add(textField);
            }

            return panel;
        }

        private JPanel createComboBoxPanel(String title, String label) {
            JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
            panel.setBorder(BorderFactory.createTitledBorder(title));
            panel.setBackground(Color.decode("#E8ECEF"));

            JLabel jLabel = new JLabel(label);
            jLabel.setForeground(Color.decode("#212529"));
            jLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            panel.add(jLabel);

            // Placeholder component to maintain layout alignment
            panel.add(new JLabel(""));
            return panel;
        }

        private int insertIntoDatabase(String name, String dob, int age, String college, double distance,
                                       String startPoint, String viaRoute, String destination, String passType, int price, String passengerType) throws SQLException {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO passes (name, dob, age, college, distance, start_point, via_route, destination, pass_type, price, passenger_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, dob);
            stmt.setInt(3, age);
            stmt.setString(4, college);
            stmt.setDouble(5, distance);
            stmt.setString(6, startPoint);
            stmt.setString(7, viaRoute);
            stmt.setString(8, destination);
            stmt.setString(9, passType);
            stmt.setInt(10, price);
            stmt.setString(11, passengerType);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int passId = 0;
            if (rs.next()) {
                passId = rs.getInt(1);
            }

            rs.close();
            stmt.close();
            conn.close();
            return passId;
        }

        // Dummy Payment Page
        private class PaymentPage {
            private JFrame paymentFrame;

            public PaymentPage(int amount, Runnable onPaymentSuccess) {
                paymentFrame = new JFrame("Payment");
                paymentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                paymentFrame.setSize(400, 400);
                paymentFrame.setLocationRelativeTo(null);

                JPanel mainPanel = new JPanel(new BorderLayout());
                mainPanel.setBackground(Color.decode("#E8ECEF"));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                // Payment Details
                JPanel detailsPanel = new JPanel(new GridLayout(6, 1, 10, 10));
                detailsPanel.setBackground(Color.decode("#E8ECEF"));
                detailsPanel.setBorder(BorderFactory.createTitledBorder("Payment Details"));

                detailsPanel.add(createLabel("Amount to Pay: ₹" + amount));
                detailsPanel.add(createLabel("Payment Options:"));

                // Payment Options
                JRadioButton cardOption = new JRadioButton("Credit/Debit Card");
                JRadioButton upiOption = new JRadioButton("UPI");
                JRadioButton netBankingOption = new JRadioButton("Net Banking");

                ButtonGroup paymentGroup = new ButtonGroup();
                paymentGroup.add(cardOption);
                paymentGroup.add(upiOption);
                paymentGroup.add(netBankingOption);

                detailsPanel.add(cardOption);
                detailsPanel.add(upiOption);
                detailsPanel.add(netBankingOption);

                mainPanel.add(detailsPanel, BorderLayout.CENTER);

                // Pay Button
                JPanel buttonPanel = new JPanel();
                buttonPanel.setBackground(Color.decode("#E8ECEF"));
                JButton payButton = new JButton("Pay Now");
                payButton.setBackground(Color.decode("#28A745"));
                payButton.setForeground(Color.WHITE);
                payButton.setFont(new Font("Arial", Font.BOLD, 14));
                payButton.setFocusPainted(false);

                payButton.addActionListener(e -> {
                    if (!cardOption.isSelected() && !upiOption.isSelected() && !netBankingOption.isSelected()) {
                        JOptionPane.showMessageDialog(paymentFrame, "Please select a payment option.");
                        return;
                    }

                    JOptionPane.showMessageDialog(paymentFrame, "Payment Successful!");
                    paymentFrame.dispose();
                    onPaymentSuccess.run();
                });

                JButton cancelButton = new JButton("Cancel");
                cancelButton.setBackground(Color.decode("#6C757D"));
                cancelButton.setForeground(Color.WHITE);
                cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
                cancelButton.setFocusPainted(false);
                cancelButton.addActionListener(e -> paymentFrame.dispose());

                buttonPanel.add(payButton);
                buttonPanel.add(cancelButton);

                mainPanel.add(buttonPanel, BorderLayout.SOUTH);

                paymentFrame.add(mainPanel);
                paymentFrame.setVisible(true);
            }

            private JLabel createLabel(String text) {
                JLabel label = new JLabel(text);
                label.setFont(new Font("Arial", Font.PLAIN, 14));
                label.setForeground(Color.decode("#212529"));
                return label;
            }
        }
    }

    // View Passes Window
    static class ViewPasses {
        private JFrame frame;

        public ViewPasses() {
            frame = new JFrame("All Bus Passes");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(900, 400);
            frame.setLocationRelativeTo(null);

            String[] columns = {"ID", "Name", "DOB", "Age", "College", "Distance", "Start Point", "Via Route", "Destination", "Pass Type", "Price", "Passenger Type"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable table = new JTable(model);
            table.setFont(new Font("Arial", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.setGridColor(Color.decode("#6C757D"));
            table.setBackground(Color.WHITE);

            try {
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                String sql = "SELECT * FROM passes";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("dob"),
                        rs.getInt("age"),
                        rs.getString("college"),
                        rs.getDouble("distance"),
                        rs.getString("start_point"),
                        rs.getString("via_route"),
                        rs.getString("destination"),
                        rs.getString("pass_type"),
                        rs.getInt("price"),
                        rs.getString("passenger_type")
                    };
                    model.addRow(row);
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#6C757D")));

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(Color.decode("#E8ECEF"));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.decode("#E8ECEF"));
            JButton closeButton = new JButton("Close");
            closeButton.setBackground(Color.decode("#6C757D"));
            closeButton.setForeground(Color.WHITE);
            closeButton.setFont(new Font("Arial", Font.BOLD, 14));
            closeButton.setFocusPainted(false);
            closeButton.addActionListener(e -> frame.dispose());
            buttonPanel.add(closeButton);

            frame.add(mainPanel, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        }
    }

    // Temporary Pass Window
    static class TemporaryPassWindow {
        private JFrame frame;
        private int passId;
        private String name;
        private String passType;
        private int price;
        private String startPoint;
        private String destination;
        private String passengerType;

        public TemporaryPassWindow(int passId, String name, String passType, int price, String startPoint, String destination, String passengerType) {
            this.passId = passId;
            this.name = name;
            this.passType = passType;
            this.price = price;
            this.startPoint = startPoint;
            this.destination = destination;
            this.passengerType = passengerType;

            frame = new JFrame("Temporary Bus Pass");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 350);
            frame.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(Color.decode("#E8ECEF"));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Pass Card
            JPanel cardPanel = new JPanel();
            cardPanel.setBackground(Color.WHITE);
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#0D6EFD"), 2));
            cardPanel.setLayout(new GridLayout(8, 1, 5, 5));
            cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel titleLabel = new JLabel("BMTC Bus Pass", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setForeground(Color.decode("#0056b3"));
            cardPanel.add(titleLabel);

            cardPanel.add(createPassDetailLabel("Pass ID: " + passId));
            cardPanel.add(createPassDetailLabel("Name: " + name));
            cardPanel.add(createPassDetailLabel("Passenger Type: " + passengerType));
            cardPanel.add(createPassDetailLabel("Pass Type: " + passType));
            cardPanel.add(createPassDetailLabel("Price: ₹" + price));
            cardPanel.add(createPassDetailLabel("From: " + startPoint));
            cardPanel.add(createPassDetailLabel("To: " + destination));

            mainPanel.add(cardPanel, BorderLayout.CENTER);

            // Buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.decode("#E8ECEF"));

            JButton downloadButton = new JButton("Download PDF");
            downloadButton.setBackground(Color.decode("#0D6EFD"));
            downloadButton.setForeground(Color.WHITE);
            downloadButton.setFont(new Font("Arial", Font.BOLD, 14));
            downloadButton.setFocusPainted(false);
            downloadButton.addActionListener(e -> downloadPassAsPDF());

            JButton closeButton = new JButton("Close");
            closeButton.setBackground(Color.decode("#6C757D"));
            closeButton.setForeground(Color.WHITE);
            closeButton.setFont(new Font("Arial", Font.BOLD, 14));
            closeButton.setFocusPainted(false);
            closeButton.addActionListener(e -> frame.dispose());

            buttonPanel.add(downloadButton);
            buttonPanel.add(closeButton);

            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            frame.add(mainPanel);
            frame.setVisible(true);
        }

        private JLabel createPassDetailLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            label.setForeground(Color.decode("#212529"));
            return label;
        }

        private void downloadPassAsPDF() {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File("BMTC_BusPass_" + passId + ".pdf"));
                int result = fileChooser.showSaveDialog(frame);
                
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    
                    // Ensure the file doesn't already exist
                    if (file.exists()) {
                        int confirm = JOptionPane.showConfirmDialog(frame, "File already exists. Overwrite?", "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
                        if (confirm != JOptionPane.YES_OPTION) {
                            return; // User chose not to overwrite
                        }
                    }

                    PdfWriter writer = new PdfWriter(file);
                    PdfDocument pdf = new PdfDocument(writer);
                    Document document = new Document(pdf);

                    // Header
                    Paragraph header = new Paragraph()
                        .add(new Text("BMTC - Bengaluru Metropolitan Transport Corporation\n").setBold().setFontSize(16))
                        .add(new Text("Karnataka Sarige").setFontSize(12));
                    document.add(header);

                    document.add(new Paragraph("\n"));

                    // Pass Details
                    document.add(new Paragraph("Bus Pass").setBold().setFontSize(14));
                    document.add(new Paragraph("Pass ID: " + passId));
                    document.add(new Paragraph("Name: " + name));
                    document.add(new Paragraph("Passenger Type: " + passengerType));
                    document.add(new Paragraph("Pass Type: " + passType));
                    document.add(new Paragraph("Price: ₹" + price));
                    document.add(new Paragraph("From: " + startPoint));
                    document.add(new Paragraph("To: " + destination));

                    document.add(new Paragraph("\n"));

                    // Signature
                    document.add(new Paragraph("Student's Signature: ____________________").setFontSize(12));

                    document.close();
                    JOptionPane.showMessageDialog(frame, "PDF downloaded successfully to " + file.getAbsolutePath());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error downloading PDF: " + ex.getMessage());
            }
        }
    }
}
