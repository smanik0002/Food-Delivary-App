import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class FoodDeliveryApp extends JFrame {

    private JComboBox<String> drinksMenu;
    private JComboBox<String> burgerMenu;
    private JComboBox<String> biryaniMenu;
    private JComboBox<String> snacksMenu;
    private JComboBox<String> pizzaMenu;

    private JTextField customerNameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextArea orderSummary;
    private JButton orderButton;
    private JButton clearButton;
    private JLabel totalLabel;
    private double totalPrice;

    // File path for order storage
    private static final String ORDER_FILE_PATH = "C:\\Users\\User\\OneDrive\\Desktop\\food odering apps\\orders.txt";

    public FoodDeliveryApp() {
        setTitle("Food Delivery App");
        setSize(650, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        drinksMenu = new JComboBox<>(new String[]{
                "Select a Drinks & Beverages item",
                "Milk Shake - ৳150 BDT", "Lassi - ৳50 BDT", "Lemon Juice - ৳50 BDT",
                "Apple Juice - ৳50 BDT", "Orange Juice - ৳50 BDT", "Hot Coffee - ৳50 BDT",
                "Iced Tea - ৳50 BDT", "Cold Chocolate Coffee - ৳100 BDT", "Faluda - ৳100 BDT", "Mojo - ৳20 BDT"
        });

        burgerMenu = new JComboBox<>(new String[]{
                "Select a Burger item",
                "Beef Burger - ৳150 BDT", "Chicken Burger - ৳100 BDT", "Cheese Burger - ৳180 BDT", "Veggie Burger - ৳80 BDT"
        });

        biryaniMenu = new JComboBox<>(new String[]{
                "Select a Biryani & Khichuri item",
                "Chicken Biryani - ৳120 BDT", "Beef Biryani - ৳150 BDT",
                "Vegetable Khichuri - ৳80 BDT", "Egg Khichuri - ৳50 BDT", "Khichuri (1:4) - ৳550 BDT"
        });

        snacksMenu = new JComboBox<>(new String[]{
                "Select a Snacks item",
                "French Fries - ৳50 BDT", "Chicken Nuggets - ৳100 BDT", "Samosa - ৳80 BDT", "Spring Roll - ৳50 BDT"
        });

        pizzaMenu = new JComboBox<>(new String[]{
                "Select a Pizza item",
                "Margherita Pizza - ৳250 BDT", "Pepperoni Pizza - ৳350 BDT",
                "Chicken BBQ Pizza - ৳450 BDT", "Veggie Pizza - ৳200 BDT"
        });

        customerNameField = new JTextField(12);
        addressField = new JTextField(15);
        phoneField = new JTextField(10);
        orderSummary = new JTextArea(12, 50);
        orderSummary.setEditable(false);

        orderButton = new JButton("Place Order");
        clearButton = new JButton("Clear Order");
        totalLabel = new JLabel("Total: ৳0.00 BDT");

        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 5, 5));
        inputPanel.add(new JLabel("Customer Name:"));
        inputPanel.add(customerNameField);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("Phone Number:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Drinks & Beverages:"));
        inputPanel.add(drinksMenu);
        inputPanel.add(new JLabel("Burger:"));
        inputPanel.add(burgerMenu);
        inputPanel.add(new JLabel("Biryani & Khichuri:"));
        inputPanel.add(biryaniMenu);
        inputPanel.add(new JLabel("Snacks:"));
        inputPanel.add(snacksMenu);
        inputPanel.add(new JLabel("Pizza:"));
        inputPanel.add(pizzaMenu);
        inputPanel.add(orderButton);
        inputPanel.add(clearButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(orderSummary), BorderLayout.CENTER);
        add(totalLabel, BorderLayout.SOUTH);

        orderButton.addActionListener(e -> {
            String customerName = customerNameField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();

            if (customerName.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(FoodDeliveryApp.this,
                        "Please fill out all customer information.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder orderDetails = new StringBuilder();
            boolean hasSelection = false;

            orderDetails.append("Customer: ").append(customerName).append("\n");
            orderDetails.append("Address: ").append(address).append("\n");
            orderDetails.append("Phone: ").append(phone).append("\n");

            totalPrice = 0.0;

            hasSelection |= addSelectedItem(drinksMenu, "Drink", orderDetails);
            hasSelection |= addSelectedItem(burgerMenu, "Burger", orderDetails);
            hasSelection |= addSelectedItem(biryaniMenu, "Biryani/Khichuri", orderDetails);
            hasSelection |= addSelectedItem(snacksMenu, "Snack", orderDetails);
            hasSelection |= addSelectedItem(pizzaMenu, "Pizza", orderDetails);

            if (!hasSelection) {
                JOptionPane.showMessageDialog(FoodDeliveryApp.this,
                        "Please select at least one food item to order.",
                        "Selection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            orderDetails.append(String.format("Total: ৳%.2f BDT\n", totalPrice));
            orderDetails.append("Thank you for your order!\n");
            orderDetails.append("--------------------------------------------------\n\n");

            orderSummary.append(orderDetails.toString());
            totalLabel.setText(String.format("Total: ৳%.2f BDT", totalPrice));

            // Write to user-specified file
            try (FileWriter writer = new FileWriter(ORDER_FILE_PATH, true)) {
                writer.write(orderDetails.toString());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(FoodDeliveryApp.this,
                        "Failed to write order to file at:\n" + ORDER_FILE_PATH,
                        "File Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearButton.addActionListener(e -> {
            customerNameField.setText("");
            addressField.setText("");
            phoneField.setText("");
            orderSummary.setText("");
            totalPrice = 0.0;
            totalLabel.setText("Total: ৳0.00 BDT");
            drinksMenu.setSelectedIndex(0);
            burgerMenu.setSelectedIndex(0);
            biryaniMenu.setSelectedIndex(0);
            snacksMenu.setSelectedIndex(0);
            pizzaMenu.setSelectedIndex(0);
        });
    }

    private boolean addSelectedItem(JComboBox<String> menu, String category, StringBuilder orderDetails) {
        int selectedIndex = menu.getSelectedIndex();
        if (selectedIndex > 0) {
            String item = (String) menu.getSelectedItem();
            try {
                double price = getItemPrice(item);
                totalPrice += price;
                orderDetails.append(category).append(": ").append(item).append("\n");
                return true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error parsing price for " + category,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    private double getItemPrice(String item) throws NumberFormatException {
        String[] parts = item.split(" - ৳");
        if (parts.length != 2) throw new NumberFormatException();
        return Double.parseDouble(parts[1].replace(" BDT", "").trim());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FoodDeliveryApp().setVisible(true));
    }
}
