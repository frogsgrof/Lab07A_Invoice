import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InvoiceFrame extends JFrame {

    final int WIDTH, HEIGHT;
    public static Font smallPlainFont, medPlainFont, smallBoldFont, medBoldFont, bigBoldFont;
    static ImageIcon nextIcon, quitIcon;
    static final String ADDRESS_PANEL_KEY = "address panel key",
            PRODUCT_PANEL_KEY = "product panel",
            INVOICE_PANEL_KEY = "invoice panel key";
    static final Border BLACK_LINE_BORDER = BorderFactory.createLineBorder(Color.BLACK, 3, true);
    ArrayList<InvoiceItem> items = new ArrayList<>();
    final JButton newProdBtn = new JButton("Add product"),
            showProductsBtn = new JButton(),
            showInvoiceBtn = new JButton();
    final JPanel nextBtnPnl = new JPanel(new BorderLayout(10, 10)),
            cardsPnl = new JPanel(new CardLayout(10, 10)),
            quitPnl = new JPanel(new BorderLayout(10, 10)),
            invoiceTextPnl = new JPanel();
    final JTextArea invoiceAddressArea = new JTextArea();
    final JLabel grandTotalLbl = createInvoiceLabel("$0.00", medBoldFont);
    String clientBusinessName = "",
            clientAddressLine1 = "",
            clientAddressLine2 = "";

    public InvoiceFrame() {
        getFonts();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenW = toolkit.getScreenSize().width;
        int screenH = toolkit.getScreenSize().height;
        WIDTH = screenW * 3 / 4;
        HEIGHT = screenH * 7 / 8;
        setSize(new Dimension(WIDTH, HEIGHT));
        setLocation((screenW - WIDTH) / 2, (screenH - HEIGHT) / 4);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        try {
            setIconImage(ImageIO.read(new File(System.getProperty("user.dir") + "//images//invoice.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setTitle("Invoice");
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 50, 50, 50));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane productTabs = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
        addProduct(productTabs);

        JPanel productEntryPnl = createProductsPanel(productTabs);
        JPanel addressPnl = createAddressPanel(WIDTH, HEIGHT);
        JPanel invoicePnl = createInvoicePanel();

        showProductsBtn.setIcon(nextIcon);
        showProductsBtn.addActionListener(e -> showProductsPanel());
        showProductsBtn.setFocusPainted(false);
        showInvoiceBtn.setIcon(nextIcon);
        showInvoiceBtn.addActionListener(e -> showInvoice());
        showInvoiceBtn.setFocusPainted(false);
        nextBtnPnl.add(showProductsBtn, BorderLayout.EAST);

        JButton quitBtn = new JButton(quitIcon);
        quitBtn.addActionListener(e -> quit());
        quitBtn.setFocusPainted(false);
        quitPnl.add(quitBtn, BorderLayout.EAST);

        cardsPnl.setBorder(BorderFactory.createTitledBorder(BLACK_LINE_BORDER, "Enter client address",
                TitledBorder.CENTER, TitledBorder.TOP, bigBoldFont));

        cardsPnl.add(addressPnl, ADDRESS_PANEL_KEY);
        cardsPnl.add(productEntryPnl, PRODUCT_PANEL_KEY);
        cardsPnl.add(invoicePnl, INVOICE_PANEL_KEY);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(nextBtnPnl);
        add(Box.createVerticalStrut(10));
        add(cardsPnl);
        add(Box.createVerticalStrut(10));
        add(quitPnl);
    }

    private JPanel createAddressPanel(int frameWidth, int frameHeight) {

        Box businessBox = Box.createHorizontalBox(),
                addressBox1 = Box.createHorizontalBox(),
                addressBox2 = Box.createHorizontalBox();

        businessBox.add(Box.createHorizontalGlue());
        businessBox.add(createFauxHeaderLabel("Business name"));
        businessBox.add(Box.createHorizontalGlue());
        addressBox1.add(Box.createHorizontalGlue());
        addressBox1.add(createFauxHeaderLabel("Address line 1"));
        addressBox1.add(Box.createHorizontalGlue());
        addressBox2.add(Box.createHorizontalGlue());
        addressBox2.add(createFauxHeaderLabel("Address line 2"));
        addressBox2.add(Box.createHorizontalGlue());

        Dimension textFieldSize = new Dimension(frameWidth * 2 / 3, frameHeight / 9);
        JTextField businessField = createTextField(clientBusinessName = "Sam's Small Appliances", textFieldSize),
                addressField1 = createTextField(clientAddressLine1 = "100 Main Street", textFieldSize),
                addressField2 = createTextField(clientAddressLine2 = "Anytown, CA 98765", textFieldSize);
        businessField.addActionListener(e -> clientBusinessName = businessField.getText());
        addressField1.addActionListener(e -> clientAddressLine1 = addressField1.getText());
        addressField2.addActionListener(e -> clientAddressLine2 = addressField2.getText());

        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));

        pnl.add(Box.createVerticalStrut(50));
        pnl.add(Box.createVerticalGlue());
        pnl.add(businessBox);
        pnl.add(Box.createVerticalStrut(2));
        pnl.add(businessField);
        pnl.add(Box.createVerticalStrut(30));
        pnl.add(addressBox1);
        pnl.add(Box.createVerticalStrut(2));
        pnl.add(addressField1);
        pnl.add(Box.createVerticalStrut(30));
        pnl.add(addressBox2);
        pnl.add(Box.createVerticalStrut(2));
        pnl.add(addressField2);
        return pnl;
    }

    private JPanel createProductsPanel(JTabbedPane tabbedPane) {

        newProdBtn.addActionListener(e -> addProduct(tabbedPane));
        newProdBtn.setFont(medBoldFont);
        newProdBtn.setFocusPainted(false);

        getImages(newProdBtn.getPreferredSize().height);

        Box topBtnsBox = Box.createHorizontalBox();
        topBtnsBox.add(Box.createHorizontalGlue());
        topBtnsBox.add(newProdBtn);
        topBtnsBox.add(Box.createHorizontalGlue());

        JPanel pnl = new JPanel();
        pnl.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));

        pnl.add(Box.createVerticalStrut(30));
        pnl.add(topBtnsBox);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(tabbedPane);

        return pnl;
    }

    private JPanel createInvoicePanel() {

        invoiceAddressArea.setOpaque(false);
        invoiceAddressArea.setFont(smallPlainFont);
        invoiceAddressArea.setMargin(new Insets(10, 10, 10, 10));
        invoiceAddressArea.setEditable(false);
        invoiceAddressArea.setLineWrap(true);
        invoiceAddressArea.setWrapStyleWord(true);
        Box addressBox = Box.createHorizontalBox();
        addressBox.add(invoiceAddressArea);
        addressBox.add(Box.createHorizontalStrut(WIDTH / 3));

        invoiceTextPnl.setOpaque(false);
        invoiceTextPnl.setSize(new Dimension(WIDTH * 2 / 3, HEIGHT * 2 / 3));
        invoiceTextPnl.setLayout(new BoxLayout(invoiceTextPnl, BoxLayout.X_AXIS));
        invoiceTextPnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(invoiceTextPnl);
        scrollPane.setBorder(BorderFactory.createMatteBorder(5, 0, 5, 0, Color.BLACK));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JLabel lbl = createInvoiceLabel("AMOUNT DUE: ", medBoldFont);
        Box totalBox = Box.createHorizontalBox();
        totalBox.add(lbl);
        totalBox.add(grandTotalLbl);
        totalBox.add(Box.createHorizontalStrut(WIDTH / 3));

        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));

        pnl.add(Box.createVerticalStrut(10));
        pnl.add(addressBox);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(scrollPane);
        pnl.add(Box.createVerticalStrut(10));
        pnl.add(totalBox);
        pnl.add(Box.createVerticalGlue());

        return pnl;
    }

    private void showProductsPanel() {
        if (JOptionPane.showConfirmDialog(null, "Customer address cannot be changed later. Continue?",
                "Confirm address", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)
                == JOptionPane.OK_OPTION) {

            cardsPnl.setBorder(BorderFactory.createTitledBorder(BLACK_LINE_BORDER, "Edit products",
                    TitledBorder.CENTER, TitledBorder.TOP, bigBoldFont));
            nextBtnPnl.remove(showProductsBtn);
            nextBtnPnl.add(showInvoiceBtn, BorderLayout.EAST);
            ((CardLayout) cardsPnl.getLayout()).show(cardsPnl, PRODUCT_PANEL_KEY);
        }
    }

    private void showInvoice() {

        boolean incompleteItemsFound = false;

        StringBuilder warningMessage = new StringBuilder("The following items may be incomplete: ");
        for (InvoiceItem invoiceItem : items)
            if (!invoiceItem.isComplete()) {
                incompleteItemsFound = true;
                warningMessage.append(invoiceItem.product.getName());
            }

        if (!incompleteItemsFound || JOptionPane.showConfirmDialog(null, warningMessage.toString(),
                "Warning",JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) ==
                JOptionPane.OK_OPTION) {

            writeInvoice();

            showInvoiceBtn.setContentAreaFilled(false);
            showInvoiceBtn.setOpaque(false);
            showInvoiceBtn.setEnabled(false);
            ((CardLayout) cardsPnl.getLayout()).show(cardsPnl, INVOICE_PANEL_KEY);
        }
    }

    private void writeInvoice() {

        cardsPnl.setBorder(BorderFactory.createTitledBorder(BLACK_LINE_BORDER, "INVOICE",
                TitledBorder.CENTER, TitledBorder.TOP, bigBoldFont));

        invoiceAddressArea.setText(clientBusinessName + (clientBusinessName.endsWith("\n") ? "" : "\n") +
                clientAddressLine1 + (clientAddressLine1.endsWith("\n") ? "" : "\n") + clientAddressLine2);

        Box nameBox = Box.createVerticalBox(),
                quantityBox = Box.createVerticalBox(),
                priceBox = Box.createVerticalBox(),
                totalBox = Box.createVerticalBox();

        JLabel nameLbl = createInvoiceLabel("Name", smallBoldFont),
                quantityLbl = createInvoiceLabel("Quantity", smallBoldFont),
                priceLbl = createInvoiceLabel("Price", smallBoldFont),
                totalLbl = createInvoiceLabel("Total", smallBoldFont);
        nameBox.add(nameLbl);
        quantityBox.add(quantityLbl);
        priceBox.add(priceLbl);
        totalBox.add(totalLbl);

        double grandTotal = 0.0;

        for (InvoiceItem item : items) {
            grandTotal += item.calculateTotal();
            nameBox.add(Box.createVerticalStrut(5));
            nameBox.add(item.getProductNameLabel());
            quantityBox.add(Box.createVerticalStrut(5));
            quantityBox.add(item.getQuantityLabel());
            priceBox.add(Box.createVerticalStrut(5));
            priceBox.add(item.getProductPriceLabel());
            totalBox.add(Box.createVerticalStrut(5));
            totalBox.add(item.getTotalPriceLabel());
        }

        grandTotalLbl.setText(String.format("$%-10.2f", grandTotal));

        invoiceTextPnl.add(nameBox);
        invoiceTextPnl.add(Box.createVerticalStrut(10));
        invoiceTextPnl.add(quantityBox);
        invoiceTextPnl.add(Box.createVerticalStrut(10));
        invoiceTextPnl.add(priceBox);
        invoiceTextPnl.add(Box.createVerticalStrut(10));
        invoiceTextPnl.add(totalBox);
    }

    public static JLabel createInvoiceLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setOpaque(false);
        return lbl;
    }

    private void addProduct(JTabbedPane tabbedPane) {
        JLabel tabLbl = new JLabel();
        InvoiceItem invoiceItem = new InvoiceItem();
        tabbedPane.add(new Product(tabbedPane, tabLbl, invoiceItem), tabLbl);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabLbl);
        items.add(invoiceItem);
    }

    private void quit() {
        if (JOptionPane.showConfirmDialog(null, "Are you sure you'd like to quit?", "Quit",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    public static JTextField createTextField(String text, Dimension size) {
        JTextField textField = new JTextField(text);
        textField.setPreferredSize(size);
        textField.setFont(medPlainFont);
        textField.setMargin(new Insets(10, 10, 10, 10));
        textField.setOpaque(false);
        textField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        return textField;
    }

    public static JTextField createFauxHeaderLabel(String text) {
        JTextField textField = new JTextField(text);
        textField.setFont(medBoldFont);
        textField.setEditable(false);
        textField.setOpaque(false);
        textField.setBorder(null);
        return textField;
    }

    private void getFonts() {
        try {
            Font plain = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty
                    ("user.dir") + "//fonts//hack_regular.ttf"));
            smallPlainFont = plain.deriveFont(Font.PLAIN, 18f);
            medPlainFont = plain.deriveFont(Font.PLAIN, 20f);
            Font bold = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty
                    ("user.dir") +"//fonts//hack_bold.ttf"));
            smallBoldFont = bold.deriveFont(Font.PLAIN, 18f);
            medBoldFont = bold.deriveFont(Font.PLAIN, 20f);
            bigBoldFont = bold.deriveFont(Font.PLAIN, 26f);

        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getImages(int buttonHeight) {
        final int targetHeight = buttonHeight - 9;
        nextIcon = new ImageIcon(System.getProperty("user.dir") + "//images//right_arrow.png");
        nextIcon = new ImageIcon(nextIcon.getImage().getScaledInstance(nextIcon.getIconWidth() *
                targetHeight / nextIcon.getIconHeight(), targetHeight,
                Image.SCALE_SMOOTH));
        quitIcon = new ImageIcon(System.getProperty("user.dir") + "//images//x.png");
        quitIcon = new ImageIcon(quitIcon.getImage().getScaledInstance(quitIcon.getIconWidth() * targetHeight /
                quitIcon.getIconHeight(), targetHeight, Image.SCALE_SMOOTH));
    }
}
