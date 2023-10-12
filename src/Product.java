import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class Product extends JPanel {
    String name = "New product";
    double price = 0.0;
    JTabbedPane tabbedPane;
    JLabel tab;
    InvoiceItem invoiceItem;
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public Product(JTabbedPane tabbedPane, JLabel tab, InvoiceItem invoiceItem) {
        super();
        this.tabbedPane = tabbedPane;
        this.tab = tab;
        this.invoiceItem = invoiceItem;
        invoiceItem.setProduct(this);

        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)));

        tab.setText(name);
        tab.setFont(InvoiceFrame.smallPlainFont);
        tab.setOpaque(false);

        GridLayout gridLayout = new GridLayout(3, 3);
        gridLayout.setHgap(40);
        gridLayout.setVgap(40);
        setLayout(gridLayout);

        JTextField nameLbl = InvoiceFrame.createFauxHeaderLabel("Name"),
                priceLbl = InvoiceFrame.createFauxHeaderLabel("Price"),
                quantityLbl = InvoiceFrame.createFauxHeaderLabel("Quantity");

        Box nameLblBox = Box.createHorizontalBox(),
                priceLblBox = Box.createHorizontalBox(),
                quantityLblBox = Box.createHorizontalBox();
        nameLblBox.add(Box.createHorizontalGlue());
        nameLblBox.add(nameLbl);
        priceLblBox.add(Box.createHorizontalGlue());
        priceLblBox.add(priceLbl);
        quantityLblBox.add(Box.createHorizontalGlue());
        quantityLblBox.add(quantityLbl);

        JTextField nameField = InvoiceFrame.createTextField(name, null),
                quantityField = InvoiceFrame.createTextField("0", null),
                priceField = InvoiceFrame.createTextField("$0.00", null);
        nameField.addActionListener(e -> updateName(nameField.getText()));
        priceField.addActionListener(e -> updatePrice(priceField));
        quantityField.addActionListener(e -> invoiceItem.updateQuantity(quantityField));

        add(nameLblBox);
        add(nameField);
        add(new JLabel());
        add(priceLblBox);
        add(priceField);
        add(new JLabel());
        add(quantityLblBox);
        add(quantityField);
        add(new JLabel());
    }

    private void updateName(String newName) {
        name = newName;
        tab.setText(newName);
        tabbedPane.setTabComponentAt(tabbedPane.getSelectedIndex(), tab);
    }

    private void updatePrice(JTextField textField) {
        StringBuilder text = new StringBuilder(textField.getText());
        if (!text.isEmpty() && text.charAt(0) == '$') text.deleteCharAt(0);
        if (text.length() == 0) {
            textField.setText(numberFormat.format(price));
            return;
        }

        int decimalIndex = text.indexOf(".");

        for (int i = 0; i < text.length(); i++) {
            if (i == decimalIndex) continue;
            if (!Character.isDigit(text.charAt(i))) {
                textField.setText(numberFormat.format(price));
                return;
            }
        }

        if (decimalIndex == -1) {
            textField.setText("$" + text + ".00");
            price = Double.parseDouble(text.toString());

        } else {

            if (text.length() - 1 > decimalIndex + 2)
                text.delete(decimalIndex + 3, text.length());

            if (decimalIndex == 0) {
                text.insert(0, '0');
                decimalIndex++;
            }

            if (text.length() - 1 == decimalIndex) {
                text.append("00");
            } else if (text.length() - 2 == decimalIndex) {
                text.append('0');
            }

            textField.setText('$' + String.valueOf(text));
            price = Double.parseDouble(String.valueOf(text));
        }
    }

    public boolean isComplete() {
        return !name.equals("New product") && price != 0.0;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public JLabel getNameLabel() {
        return InvoiceFrame.createInvoiceLabel(name, InvoiceFrame.smallPlainFont);
    }

    public JLabel getPriceLabel() {
        return InvoiceFrame.createInvoiceLabel(String.format("$%-10.2f", price), InvoiceFrame.smallPlainFont);
    }
}
