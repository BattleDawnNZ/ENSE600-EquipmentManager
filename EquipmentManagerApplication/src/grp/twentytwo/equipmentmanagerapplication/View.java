/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package grp.twentytwo.equipmentmanagerapplication;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings;
import grp.twentytwo.equipmentmanager.Booking;
import grp.twentytwo.equipmentmanager.Item;
import grp.twentytwo.equipmentmanager.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author cwitt
 */
public class View extends javax.swing.JFrame {

    // <editor-fold desc="Speakers">
    public Speaker<ActionEvent> login;
    public Speaker<String> searchForItem;
    public Speaker<String> viewItem;

    public Speaker<String> searchForUser;
    public Speaker<String> viewUser;
    // </editor-fold>

    /**
     * Creates new form EquipmentManagerGUI
     */
    public View() {
	/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
	 */
	try {
	    String look = "Nimbus";//javax.swing.UIManager.getSystemLookAndFeelClassName();
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		if (look.equals(info.getName())) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    //break;
		}
	    }
	} catch (ClassNotFoundException ex) {
	    java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	initComponents();

	// Login
	login = new Speaker(button_login);

	// Item Tab ------------------------------------------------------------
	// Item Searching
	searchForItem = new Speaker<>();
	ActionListener searchItemListener = new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		searchForItem.notifyListeners(field_searchItem.getText());
	    }
	};
	button_searchItem.addActionListener(searchItemListener);
	field_searchItem.addActionListener(searchItemListener);
	// Item Selection
	viewItem = new Speaker<>();
	list_itemSearchResults.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) { // Avoid duplicate events
		    String selectedValue = list_itemSearchResults.getSelectedValue();
		    viewItem.notifyListeners(selectedValue);
		    System.out.println("Selected: " + selectedValue);
		}
	    }
	});

	// Test Code for vetoing DatTimes
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	ZonedDateTime bookedDate = LocalDateTime.parse("13-10-2025 02:30:00", formatter).atZone(ZoneId.systemDefault());
	ZonedDateTime returnDate = LocalDateTime.parse("13-11-2025 04:30:00", formatter).atZone(ZoneId.systemDefault());
	Booking testBooking = new Booking("0", "0", "0", bookedDate, returnDate);
	DatePickerSettings datePickerSettings = dateTimePicker1.datePicker.getSettings();
	datePickerSettings.setVetoPolicy((LocalDate localDate) -> {
	    ZonedDateTime zonedDate = localDate.atStartOfDay(ZoneId.systemDefault());
	    return (returnDate.isBefore(zonedDate.plusDays(1)) || returnDate.isEqual(zonedDate) || bookedDate.isAfter(zonedDate));
	});
	TimePickerSettings timePickerSettings = dateTimePicker1.timePicker.getSettings();
	timePickerSettings.setVetoPolicy((LocalTime localTime) -> {
	    ZonedDateTime zonedDate = dateTimePicker1.datePicker.getDate().atTime(localTime).atZone(ZoneId.systemDefault());
	    return (returnDate.isBefore(zonedDate) || returnDate.isEqual(zonedDate) || bookedDate.isAfter(zonedDate) || bookedDate.isEqual(zonedDate));
	});

	// User Tab ------------------------------------------------------------
	// Item Searching
	searchForUser = new Speaker<>();
	ActionListener searchUserListener = new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		searchForUser.notifyListeners(field_searchUser.getText());
	    }
	};
	button_searchUser.addActionListener(searchUserListener);
	field_searchUser.addActionListener(searchUserListener);
	// Item Selection
	viewUser = new Speaker<>();
	list_userSearchResults.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) { // Avoid duplicate events
		    String selectedValue = list_userSearchResults.getSelectedValue();
		    viewUser.notifyListeners(selectedValue);
		    System.out.println("Selected: " + selectedValue);
		}
	    }
	});
    }

    public void loginComplete() {
	((java.awt.CardLayout) getContentPane().getLayout()).next(getContentPane());
    }

    // Item Functions ----------------------------------------------------------
    public void setItemSearchResults(List<String> newList) {
	DefaultListModel<String> model = new DefaultListModel<>();
	model.clear();
	model.addAll(newList);
	list_itemSearchResults.setModel(model);
    }

    public void setItemPreview(Item itemData) {
	text_itemID.setText(itemData.getId());
	text_itemName.setText(itemData.getName());
	text_itemDescription.setText(itemData.getDescription());
	text_itemLocation.setText(itemData.getLocation());
	text_itemStatus.setText(itemData.getStatus().toString());
	text_itemType.setText(itemData.getType());
	text_lastCalibration.setText(itemData.getLastCalibration() != null ? itemData.getLastCalibration().toString() : "Uncalibrated");
	check_needsCalibration.setSelected(itemData.getNeedsCalibration());
    }

    public void setItemBookings(List<Booking> newList) {
	// Todo Apply All Bookings To The Booking List.
    }

    // User Functions ----------------------------------------------------------
    public void setUserSearchResults(List<String> newList) {
	DefaultListModel<String> model = new DefaultListModel<>();
	model.clear();
	model.addAll(newList);
	list_userSearchResults.setModel(model);
    }

    public void setUserPreview(User userData) {
	text_userID.setText(userData.getUserID());
	text_userName.setText(userData.getName());
	text_userSecurityLevel.setText(userData.getSecurityLevel().toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dialog_viewBookings = new javax.swing.JDialog();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel7 = new javax.swing.JLabel();
        calendarPanel1 = new com.github.lgooddatepicker.components.CalendarPanel();
        panel_login = new javax.swing.JPanel();
        jPasswordField1 = new javax.swing.JPasswordField();
        button_login = new javax.swing.JButton();
        label_password = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        label_username = new javax.swing.JLabel();
        panel_main = new javax.swing.JPanel();
        tabs_application = new javax.swing.JTabbedPane();
        tab_items = new javax.swing.JSplitPane();
        panel_itemSearch = new javax.swing.JPanel();
        field_searchItem = new javax.swing.JTextField();
        button_addItem = new javax.swing.JButton();
        button_searchItem = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        list_itemSearchResults = new javax.swing.JList<>();
        panel_itemView = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jSplitPane3 = new javax.swing.JSplitPane();
        panel_itemDetails = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        text_itemID = new javax.swing.JTextField();
        text_itemName = new javax.swing.JTextField();
        text_itemLocation = new javax.swing.JTextField();
        text_itemStatus = new javax.swing.JTextField();
        text_itemType = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jScrollPane1 = new javax.swing.JScrollPane();
        text_itemDescription = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        text_lastCalibration = new javax.swing.JTextField();
        check_needsCalibration = new javax.swing.JCheckBox();
        dateTimePicker1 = new com.github.lgooddatepicker.components.DateTimePicker();
        panel_itemBookings = new javax.swing.JPanel();
        calendarPanel2 = new com.github.lgooddatepicker.components.CalendarPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jLabel8 = new javax.swing.JLabel();
        tab_users = new javax.swing.JSplitPane();
        panel_userSearch = new javax.swing.JPanel();
        field_searchUser = new javax.swing.JTextField();
        button_addUser = new javax.swing.JButton();
        button_searchUser = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        list_userSearchResults = new javax.swing.JList<>();
        panel_userView = new javax.swing.JPanel();
        toolbar_userTools = new javax.swing.JToolBar();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        panel_userDetails = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        text_userID = new javax.swing.JTextField();
        text_userName = new javax.swing.JTextField();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        text_userSecurityLevel = new javax.swing.JTextField();

        dialog_viewBookings.setTitle("Item Bookings");
        dialog_viewBookings.setMinimumSize(new java.awt.Dimension(400, 255));

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(jScrollPane4, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Bookings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel7, gridBagConstraints);

        jSplitPane1.setLeftComponent(jPanel2);
        jSplitPane1.setRightComponent(calendarPanel1);

        dialog_viewBookings.getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Equipment Manager");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(400, 300));
        setName("applicationFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1000, 600));
        getContentPane().setLayout(new java.awt.CardLayout());

        panel_login.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panel_login.add(jPasswordField1, gridBagConstraints);

        button_login.setText("Login");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(13, 13, 13, 13);
        panel_login.add(button_login, gridBagConstraints);

        label_password.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        panel_login.add(label_password, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panel_login.add(jTextField2, gridBagConstraints);

        label_username.setText("Username");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        panel_login.add(label_username, gridBagConstraints);

        getContentPane().add(panel_login, "card3");

        panel_main.setLayout(new javax.swing.BoxLayout(panel_main, javax.swing.BoxLayout.LINE_AXIS));

        panel_itemSearch.setLayout(new java.awt.GridBagLayout());

        field_searchItem.setToolTipText("Input a string to search for a list of corresponding users.");
        field_searchItem.setMinimumSize(new java.awt.Dimension(100, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panel_itemSearch.add(field_searchItem, gridBagConstraints);

        button_addItem.setText("Add Item");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panel_itemSearch.add(button_addItem, gridBagConstraints);

        button_searchItem.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panel_itemSearch.add(button_searchItem, gridBagConstraints);

        list_itemSearchResults.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(list_itemSearchResults);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_itemSearch.add(jScrollPane3, gridBagConstraints);

        tab_items.setLeftComponent(panel_itemSearch);

        panel_itemView.setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setText("jButton1");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        jButton2.setText("jButton2");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

        jButton3.setText("jButton3");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton3);

        jButton4.setText("jButton4");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton4);

        jButton5.setText("jButton5");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton5);

        jButton6.setText("jButton6");
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton6);

        jButton7.setText("jButton7");
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton7);

        panel_itemView.add(jToolBar1, java.awt.BorderLayout.PAGE_END);

        jSplitPane3.setResizeWeight(1.0);

        panel_itemDetails.setMinimumSize(new java.awt.Dimension(321, 16));

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Location:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel6, gridBagConstraints);

        text_itemID.setEditable(false);
        text_itemID.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemID, gridBagConstraints);

        text_itemName.setEditable(false);
        text_itemName.setText("jTextField3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemName, gridBagConstraints);

        text_itemLocation.setEditable(false);
        text_itemLocation.setText("jTextField5");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemLocation, gridBagConstraints);

        text_itemStatus.setEditable(false);
        text_itemStatus.setText("jTextField6");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemStatus, gridBagConstraints);

        text_itemType.setEditable(false);
        text_itemType.setText("jTextField7");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0.1;
        jPanel1.add(filler1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        jPanel1.add(filler2, gridBagConstraints);

        text_itemDescription.setEditable(false);
        text_itemDescription.setColumns(20);
        text_itemDescription.setRows(5);
        jScrollPane1.setViewportView(text_itemDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jLabel9.setText("Last Calibration:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel1.add(jLabel9, gridBagConstraints);

        text_lastCalibration.setEditable(false);
        text_lastCalibration.setText("jTextField3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_lastCalibration, gridBagConstraints);

        check_needsCalibration.setText("Needs Calibration");
        check_needsCalibration.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(check_needsCalibration, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        jPanel1.add(dateTimePicker1, gridBagConstraints);

        panel_itemDetails.setViewportView(jPanel1);

        jSplitPane3.setLeftComponent(panel_itemDetails);

        panel_itemBookings.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panel_itemBookings.add(calendarPanel2, gridBagConstraints);

        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(jList2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        panel_itemBookings.add(jScrollPane5, gridBagConstraints);

        jLabel8.setText("Bookings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 7);
        panel_itemBookings.add(jLabel8, gridBagConstraints);

        jSplitPane3.setRightComponent(panel_itemBookings);

        panel_itemView.add(jSplitPane3, java.awt.BorderLayout.CENTER);

        tab_items.setRightComponent(panel_itemView);

        tabs_application.addTab("Items", tab_items);

        panel_userSearch.setLayout(new java.awt.GridBagLayout());

        field_searchUser.setToolTipText("Input a string to search for a list of corresponding users.");
        field_searchUser.setMinimumSize(new java.awt.Dimension(100, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panel_userSearch.add(field_searchUser, gridBagConstraints);

        button_addUser.setText("Add User");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panel_userSearch.add(button_addUser, gridBagConstraints);

        button_searchUser.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panel_userSearch.add(button_searchUser, gridBagConstraints);

        list_userSearchResults.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "User 1", "User 2", "User 3", "User 4", "User 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane6.setViewportView(list_userSearchResults);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_userSearch.add(jScrollPane6, gridBagConstraints);

        tab_users.setLeftComponent(panel_userSearch);

        panel_userView.setLayout(new java.awt.BorderLayout());

        toolbar_userTools.setFloatable(false);
        toolbar_userTools.setRollover(true);

        jButton8.setText("jButton1");
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_userTools.add(jButton8);

        jButton9.setText("jButton2");
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_userTools.add(jButton9);

        jButton10.setText("jButton3");
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_userTools.add(jButton10);

        jButton11.setText("jButton4");
        jButton11.setFocusable(false);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_userTools.add(jButton11);

        jButton12.setText("jButton5");
        jButton12.setFocusable(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_userTools.add(jButton12);

        jButton13.setText("jButton6");
        jButton13.setFocusable(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_userTools.add(jButton13);

        jButton14.setText("jButton7");
        jButton14.setFocusable(false);
        jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_userTools.add(jButton14);

        panel_userView.add(toolbar_userTools, java.awt.BorderLayout.PAGE_END);

        panel_userDetails.setMinimumSize(new java.awt.Dimension(321, 16));

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel10.setText("ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(jLabel10, gridBagConstraints);

        jLabel11.setText("Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(jLabel11, gridBagConstraints);

        jLabel12.setText("Security Level:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel3.add(jLabel12, gridBagConstraints);

        text_userID.setEditable(false);
        text_userID.setText("jTextField1");
        text_userID.setPreferredSize(new java.awt.Dimension(232, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel3.add(text_userID, gridBagConstraints);

        text_userName.setEditable(false);
        text_userName.setText("jTextField3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel3.add(text_userName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0.1;
        jPanel3.add(filler3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        jPanel3.add(filler4, gridBagConstraints);

        text_userSecurityLevel.setEditable(false);
        text_userSecurityLevel.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(text_userSecurityLevel, gridBagConstraints);

        panel_userDetails.setViewportView(jPanel3);

        panel_userView.add(panel_userDetails, java.awt.BorderLayout.CENTER);

        tab_users.setRightComponent(panel_userView);

        tabs_application.addTab("Users", tab_users);

        panel_main.add(tabs_application);

        getContentPane().add(panel_main, "card4");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_addItem;
    private javax.swing.JButton button_addUser;
    private javax.swing.JButton button_login;
    private javax.swing.JButton button_searchItem;
    private javax.swing.JButton button_searchUser;
    private com.github.lgooddatepicker.components.CalendarPanel calendarPanel1;
    private com.github.lgooddatepicker.components.CalendarPanel calendarPanel2;
    private javax.swing.JCheckBox check_needsCalibration;
    private com.github.lgooddatepicker.components.DateTimePicker dateTimePicker1;
    private javax.swing.JDialog dialog_viewBookings;
    private javax.swing.JTextField field_searchItem;
    private javax.swing.JTextField field_searchUser;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel label_password;
    private javax.swing.JLabel label_username;
    private javax.swing.JList<String> list_itemSearchResults;
    private javax.swing.JList<String> list_userSearchResults;
    private javax.swing.JPanel panel_itemBookings;
    private javax.swing.JScrollPane panel_itemDetails;
    private javax.swing.JPanel panel_itemSearch;
    private javax.swing.JPanel panel_itemView;
    private javax.swing.JPanel panel_login;
    private javax.swing.JPanel panel_main;
    private javax.swing.JScrollPane panel_userDetails;
    private javax.swing.JPanel panel_userSearch;
    private javax.swing.JPanel panel_userView;
    private javax.swing.JSplitPane tab_items;
    private javax.swing.JSplitPane tab_users;
    private javax.swing.JTabbedPane tabs_application;
    private javax.swing.JTextArea text_itemDescription;
    private javax.swing.JTextField text_itemID;
    private javax.swing.JTextField text_itemLocation;
    private javax.swing.JTextField text_itemName;
    private javax.swing.JTextField text_itemStatus;
    private javax.swing.JTextField text_itemType;
    private javax.swing.JTextField text_lastCalibration;
    private javax.swing.JTextField text_userID;
    private javax.swing.JTextField text_userName;
    private javax.swing.JTextField text_userSecurityLevel;
    private javax.swing.JToolBar toolbar_userTools;
    // End of variables declaration//GEN-END:variables
}
