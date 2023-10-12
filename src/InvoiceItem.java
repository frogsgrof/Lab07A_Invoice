import javax.swing.*;

public class InvoiceItem {

    Product product;
    int count = 0;

    public InvoiceItem() {
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void updateQuantity(JTextField textField) {
        StringBuilder text = new StringBuilder(textField.getText());
        if (text.isEmpty()) {
            count = 0;
            return;
        }

        while (!text.isEmpty() && text.charAt(0) == '0') {
            text.deleteCharAt(0);
        }

        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                text.delete(i, text.length());
                if (text.isEmpty()) {
                    textField.setText("0");
                    count = 0;
                } else {
                    textField.setText(text.toString());
                    count = Integer.parseInt(text.toString());
                }
                return;
            }
        }

        textField.setText(text.toString());
        count = Integer.parseInt(text.toString());
    }

    public boolean isComplete() {
        return product != null && product.isComplete() && count != 0;
    }

    public JLabel getProductNameLabel() {
        return product.getNameLabel();
    }

    public JLabel getProductPriceLabel() {
        return product.getPriceLabel();
    }

    public JLabel getQuantityLabel() {
        return InvoiceFrame.createInvoiceLabel(String.valueOf(count), InvoiceFrame.smallPlainFont);
    }

    public JLabel getTotalPriceLabel() {
        return InvoiceFrame.createInvoiceLabel(String.format("$%-10.2f", calculateTotal()), InvoiceFrame.smallPlainFont);
    }

    public double calculateTotal() {
        return product.getPrice() * count;
    }
}
