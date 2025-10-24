/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package grp.twentytwo.guiapplication;

import grp.twentytwo.equipmentmanager.Booking;
import grp.twentytwo.equipmentmanager.History;
import grp.twentytwo.equipmentmanager.Item;
import grp.twentytwo.equipmentmanager.Location;
import grp.twentytwo.equipmentmanager.ModelManager;
import grp.twentytwo.equipmentmanager.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author cwitt
 */
public class View extends javax.swing.JFrame {

    // <editor-fold desc="Speakers">
    public Speaker<ActionEvent> login;
    public Speaker<ActionEvent> addItem;
    public Speaker<String> removeItem;
    public Speaker<String> bookItem;
    public Speaker<String> searchForItem;
    public Speaker<String> viewItem;
    public Speaker<String> getEditDetails;
    public Speaker<String> editItem;
    public Speaker<String> viewBooking;
    public Speaker<String> addNote;
    public Speaker<String> flagItem;
    public Speaker<String> calibrateItem;
    public Speaker<String> viewHistory;

    public Speaker<String> removeUser;
    public Speaker<String> searchForUser;
    public Speaker<String> viewUser;

    public Speaker<ActionEvent> addLocation;
    public Speaker<String> removeLocation;
    public Speaker<String> searchForLocation;
    public Speaker<String> viewLocation;
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
	login = new Speaker<>(button_login);

	// Item Tab ------------------------------------------------------------
	// Item Adding
	button_addItem.addActionListener((ActionEvent e) -> {
	    clearAddItemDialog();
	    dialog_addItem.setVisible(true);
	});
	ActionListener closeAddItem = (ActionEvent e) -> {
	    dialog_addItem.setVisible(false);
	};
	button_addItemCancel.addActionListener(closeAddItem);
	addItem = new Speaker<>();
	button_addItemConfirm.addActionListener(addItem.passthrough());
	button_addItemConfirm.addActionListener(closeAddItem);
	// Item Removal
	removeItem = new Speaker<>();
	button_removeItem.addActionListener((ActionEvent e) -> {
	    String itemID = currentItemID();
	    if (!itemID.isBlank()) {
		if (getConfirmation("Remove Item", "Are you sure you want remove Item " + itemID + "?")) {
		    removeItem.notifyListeners(itemID);
		}
	    }
	});
	// Item editing
	getEditDetails = new Speaker<>();
	button_editItem.addActionListener((ActionEvent e) -> {
	    getEditDetails.notifyListeners(currentItemID());
	});
	ActionListener closeEditItem = (ActionEvent e) -> {
	    dialog_editItem.setVisible(false);
	};
	button_editItemCancel.addActionListener(closeEditItem);
	editItem = new Speaker<>();
	button_editItemConfirm.addActionListener((ActionEvent e) -> {
	    editItem.notifyListeners(currentItemID());
	});
	button_editItemConfirm.addActionListener(closeEditItem);
	// Item Searching
	searchForItem = new Speaker<>();
	ActionListener searchItemListener = (ActionEvent e) -> {
	    String searchString = field_searchItem.getText();
	    searchForItem.notifyListeners(searchString);
	};
	button_searchItem.addActionListener(searchItemListener);
	field_searchItem.addActionListener(searchItemListener);
	// Item Selection
	viewItem = new Speaker<>();
	list_itemSearchResults.addListSelectionListener((ListSelectionEvent e) -> {
	    if (!e.getValueIsAdjusting()) { // Avoid duplicate events
		String selectedValue = list_itemSearchResults.getSelectedValue();
		if (selectedValue != null) {
		    viewItem.notifyListeners(selectedValue);
		}
	    }
	});
	// Item Preview Bookings
	viewBooking = new Speaker<>();
	list_itemBookings.addListSelectionListener((ListSelectionEvent e) -> {
	    if (!e.getValueIsAdjusting()) { // Avoid duplicate events
		String selectedValue = list_itemBookings.getSelectedValue();
		list_itemBookings.clearSelection();
		if (selectedValue != null) {
		    viewBooking.notifyListeners(selectedValue);
		}
	    }
	});
	// Item Booking
	button_bookItem.addActionListener((ActionEvent e) -> {
	    setupBookItemDialog();
	    dialog_bookItem.setVisible(true);
	});
	ActionListener closeBookItem = (ActionEvent e) -> {
	    dialog_bookItem.setVisible(false);
	};
	button_bookItemCancel.addActionListener(closeBookItem);
	bookItem = new Speaker<>();
	button_bookItemConfirm.addActionListener((ActionEvent e) -> {
	    bookItem.notifyListeners(currentItemID());
	});
	button_bookItemConfirm.addActionListener(closeBookItem);
	// Item Note
	button_addNote.addActionListener((ActionEvent e) -> {
	    clearAddNoteDialog();
	    dialog_addNote.setVisible(true);
	});
	ActionListener closeAddNote = (ActionEvent e) -> {
	    dialog_addNote.setVisible(false);
	};
	button_addNoteCancel.addActionListener(closeAddNote);
	addNote = new Speaker<>();
	button_addNoteConfirm.addActionListener((ActionEvent e) -> {
	    addNote.notifyListeners(currentItemID());
	});
	button_addNoteConfirm.addActionListener(closeAddNote);
	// Item Maintenance

	flagItem = new Speaker<>();
	button_flagForCalibration.addActionListener((ActionEvent e) -> {
	    flagItem.notifyListeners(currentItemID());
	});

	calibrateItem = new Speaker<>();
	button_calibrateItem.addActionListener((ActionEvent e) -> {
	    calibrateItem.notifyListeners(currentItemID());
	});

	// Item History
	viewHistory = new Speaker<>();
	button_viewHistory.addActionListener((ActionEvent e) -> {
	    viewHistory.notifyListeners(currentItemID());
	    dialog_viewHistory.setVisible(true);
	});
	button_viewHistoryClose.addActionListener((ActionEvent e) -> {
	    dialog_viewHistory.setVisible(false);
	});
	// Test Code for vetoing DateTimes
//	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//	ZonedDateTime bookedDate = LocalDateTime.parse("13-10-2025 02:30:00", formatter).atZone(ZoneId.systemDefault());
//	ZonedDateTime returnDate = LocalDateTime.parse("13-11-2025 04:30:00", formatter).atZone(ZoneId.systemDefault());
//	Booking testBooking = new Booking("0", "0", "0", bookedDate, returnDate);
//	DatePickerSettings datePickerSettings = dateTimePicker1.datePicker.getSettings();
//	datePickerSettings.setVetoPolicy((LocalDate localDate) -> {
//	    ZonedDateTime zonedDate = localDate.atStartOfDay(ZoneId.systemDefault());
//	    return (returnDate.isBefore(zonedDate.plusDays(1)) || returnDate.isEqual(zonedDate) || bookedDate.isAfter(zonedDate));
//	});
//	TimePickerSettings timePickerSettings = dateTimePicker1.timePicker.getSettings();
//	timePickerSettings.setVetoPolicy((LocalTime localTime) -> {
//	    ZonedDateTime zonedDate = dateTimePicker1.datePicker.getDate().atTime(localTime).atZone(ZoneId.systemDefault());
//	    return (returnDate.isBefore(zonedDate) || returnDate.isEqual(zonedDate) || bookedDate.isAfter(zonedDate) || bookedDate.isEqual(zonedDate));
//	});
	// User Tab ------------------------------------------------------------
	// User Removal
	removeUser = new Speaker<>();
	button_removeUser.addActionListener((ActionEvent e) -> {
	    String userID = currentUserID();
	    if (!userID.isBlank()) {
		if (getConfirmation("Remove User", "Are you sure you want remove User " + userID + "?")) {
		    removeUser.notifyListeners(userID);
		}
	    }
	});
	// User Searching
	searchForUser = new Speaker<>();
	ActionListener searchUserListener = (ActionEvent e) -> {
	    searchForUser.notifyListeners(field_searchUser.getText());
	};
	button_searchUser.addActionListener(searchUserListener);
	field_searchUser.addActionListener(searchUserListener);
	// User Selection
	viewUser = new Speaker<>();
	list_userSearchResults.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) { // Avoid duplicate events
		    String selectedValue = list_userSearchResults.getSelectedValue();
		    viewUser.notifyListeners(selectedValue);
		}
	    }
	});
	// Location Tab --------------------------------------------------------
	// Location Adding
	button_addLocation.addActionListener((ActionEvent e) -> {
	    clearAddLocationDialog();
	    dialog_addLocation.setVisible(true);
	});
	ActionListener closeAddLocation = (ActionEvent e) -> {
	    dialog_addLocation.setVisible(false);
	};
	button_addLocationCancel.addActionListener(closeAddLocation);
	addLocation = new Speaker<>();
	button_addLocationConfirm.addActionListener(addLocation.passthrough());
	button_addLocationConfirm.addActionListener(closeAddLocation);
	// Location Removal
	removeLocation = new Speaker<>();
	button_removeLocation.addActionListener((ActionEvent e) -> {
	    if (!currentLocationID().isBlank()) {
		if (getConfirmation("Remove Location", "Are you sure you want remove Location " + currentLocationName() + "?")) {
		    removeLocation.notifyListeners(currentLocationID());
		}
	    }
	});
	// Location Searching
	searchForLocation = new Speaker<>();
	ActionListener searchLocationListener = new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		searchForLocation.notifyListeners(field_searchLocation.getText());
	    }
	};
	button_searchLocation.addActionListener(searchLocationListener);
	field_searchLocation.addActionListener(searchLocationListener);
	// Location Selection
	viewLocation = new Speaker<>();
	list_locationSearchResults.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) { // Avoid duplicate events
		    String selectedValue = list_locationSearchResults.getSelectedValue();
		    viewLocation.notifyListeners(selectedValue);
		}
	    }
	});
    }

    // Helper Dialogs ----------------------------------------------------------
    public void showError(Exception err) {
	JOptionPane.showMessageDialog(this, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean getConfirmation(String title, String message) {
	return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION) == 0;
    }

    // Login -------------------------------------------------------------------
    public User getLoginDetails(User user) {
	user.setID(field_loginUserID.getText());
	String password = "";
	for (char c : field_loginPassword.getPassword()) {
	    password += c;
	}
	user.setPassword(password);
	return user;
    }

    public void login(boolean successful) {
	if (successful) {
	    ((java.awt.CardLayout) getContentPane().getLayout()).next(getContentPane());
	}
    }

    // Item Functions ----------------------------------------------------------
    public String currentItemID() {
	return text_itemID.getText();
    }

    public void clearAddItemDialog() {
	field_addItemName.setText("");
	field_addItemLocation.setText("");
	field_addItemType.setText("");
    }

    public void setNewItemDetails(Item item) {
	item.setName(field_addItemName.getText());
	item.setLocation(field_addItemLocation.getText());
	item.setType(field_addItemType.getText());
    }

    public void editItemDetails(Item item) {
	item.setName(field_editItemName.getText());
	item.setDescription(field_editItemDescription.getText());
	item.setLocation(field_editItemLocation.getText());
	item.setStatus((Item.Status) combo_editItemStatus.getSelectedItem());
	item.setType(field_editItemType.getText());
    }

    public void setItemSearchResults(LinkedHashSet<String> newList) {
	DefaultListModel<String> items = new DefaultListModel<>();
	items.addAll(newList);
	list_itemSearchResults.setModel(items);
    }

    public void setItemPreview(Item itemData) {
	try {
	    if (itemData != null) {
		text_itemID.setText(itemData.getID());
		text_itemName.setText(itemData.getName());
		text_itemDescription.setText(itemData.getDescription());
		text_itemLocation.setText(itemData.getLocation());
		text_itemStatus.setText(itemData.getStatus().toString());
		text_itemType.setText(itemData.getType());
		text_lastCalibration.setText(itemData.getLastCalibrationFormatted());
		check_needsCalibration.setSelected(itemData.getNeedsCalibration());
	    }
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void setItemBookings(List<Booking> newList) {
	DefaultListModel<String> bookings = new DefaultListModel<>();
	for (Booking booking : newList) {
	    bookings.addElement(booking.getID());
	}
	list_itemBookings.setModel(bookings);
	calendar_itemBookings.getSettings().setVetoPolicy((LocalDate localDate) -> {
	    for (Booking booking : newList) {
		if (!booking.isOutsideBookedDate(localDate)) {
		    return false;
		}
	    }
	    return true;
	});
	dateTimePicker_bookItemBookedDate.datePicker.getSettings().setVetoPolicy((LocalDate localDate) -> {
	    for (Booking booking : newList) {
		if (!booking.isOutsideBookedDate(localDate)) {
		    return false;
		}
	    }
	    return true;
	});
	dateTimePicker_bookItemReturnDate.datePicker.getSettings().setVetoPolicy((LocalDate localDate) -> {
	    for (Booking booking : newList) {
		if (!booking.isOutsideBookedDate(localDate)) {
		    return false;
		}
	    }
	    return true;
	});
	dateTimePicker_bookItemBookedDate.timePicker.getSettings().setVetoPolicy((LocalTime localTime) -> {
	    LocalDateTime localDateTime = dateTimePicker_bookItemBookedDate.datePicker.getDate().atTime(localTime);
	    for (Booking booking : newList) {
		if (!booking.isOutsideBookedDateAndTime(localDateTime)) {
		    return false;
		}
	    }
	    return true;
	});
	dateTimePicker_bookItemReturnDate.timePicker.getSettings().setVetoPolicy((LocalTime localTime) -> {
	    LocalDateTime localDateTime = dateTimePicker_bookItemReturnDate.datePicker.getDate().atTime(localTime);
	    for (Booking booking : newList) {
		if (!booking.isOutsideBookedDateAndTime(localDateTime)) {
		    return false;
		}
	    }
	    return true;
	});
    }

    public void setBookingPreview(Booking booking) {
	try {
	    if (booking != null) {
		calendar_itemBookings.setSelectedDate(booking.getBookedDate().toLocalDate());
	    }
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void setItemEditingPreview(Item itemData) {
	try {
	    if (itemData != null) {
		field_editItemName.setText(itemData.getName());
		field_editItemDescription.setText(itemData.getDescription());
		field_editItemLocation.setText(itemData.getLocation());
		DefaultComboBoxModel<Item.Status> statuses = new DefaultComboBoxModel<>();
		for (Item.Status status : Item.Status.values()) {
		    statuses.addElement(status);
		}
		combo_editItemStatus.setModel(statuses);
		combo_editItemStatus.setSelectedIndex(itemData.getStatus().ordinal());
		field_editItemType.setText(itemData.getType());
		dialog_editItem.setVisible(true);
	    }
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public boolean setNewBookingDetails(Booking booking) {
	try {
	    LocalDateTime bookedDate = dateTimePicker_bookItemBookedDate.datePicker.getDate().atTime(dateTimePicker_bookItemBookedDate.timePicker.getTime());
	    LocalDateTime returnDate = dateTimePicker_bookItemReturnDate.datePicker.getDate().atTime(dateTimePicker_bookItemReturnDate.timePicker.getTime());
	    booking.setBookingRange(bookedDate, returnDate);
	    return true;
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return false;
    }

    public void setupBookItemDialog() {
	dateTimePicker_bookItemBookedDate.clear();
	dateTimePicker_bookItemReturnDate.clear();
    }

    public void clearAddNoteDialog() {
	field_addNote.setText("");
    }

    public String getNote() {
	return field_addNote.getText();
    }

    public void setupViewHistoryDialog(ArrayList<History> newList) {
	DefaultListModel<String> history = new DefaultListModel<>();
	for (History h : newList) {
	    history.addElement(h.toString());
	}
	list_viewHistory.setModel(history);
    }

    // User Functions ----------------------------------------------------------
    public String currentUserID() {
	return text_userID.getText();
    }

    public void setUserSearchResults(LinkedHashSet<String> newList) {
	DefaultListModel<String> userListModel = new DefaultListModel<>();
	userListModel.addAll(newList);
	list_userSearchResults.setModel(userListModel);
    }

    public void setUserPreview(User userData) {
	try {
	    if (userData != null) {
		text_userID.setText(userData.getID());
		text_userName.setText(userData.getName());
		text_userSecurityLevel.setText(userData.getSecurityLevel().toString());
	    }
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    // Location Functions ------------------------------------------------------
    public String currentLocationID() {
	return text_locationID.getText();
    }

    public String currentLocationName() {
	return text_locationName.getText();
    }

    public void clearAddLocationDialog() {
	field_addLocationName.setText("");
    }

    public String getNewLocationDetails() {
	return field_addLocationName.getText();
    }

    public void setLocationSearchResults(List<String> newList) {
	try {
	    DefaultListModel<String> locations = new DefaultListModel<>();
	    locations.addAll(newList);
	    list_locationSearchResults.setModel(locations);
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void setLocationPreview(Location locationData) {
	try {
	    if (locationData != null) {
		text_locationID.setText(locationData.getID());
		text_locationName.setText(locationData.getName());
	    }
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void setLocationItemsPreview(ArrayList<String> newList) {
	try {
	    DefaultListModel<String> locations = new DefaultListModel<>();
	    locations.addAll(newList);
	    list_locationItemsPreview.setModel(locations);
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
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

        dialog_addItem = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        field_addItemName = new javax.swing.JTextField();
        field_addItemLocation = new javax.swing.JTextField();
        field_addItemType = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        button_addItemCancel = new javax.swing.JButton();
        button_addItemConfirm = new javax.swing.JButton();
        dialog_editItem = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        field_editItemName = new javax.swing.JTextField();
        field_editItemLocation = new javax.swing.JTextField();
        field_editItemType = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        field_editItemDescription = new javax.swing.JTextArea();
        combo_editItemStatus = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        button_editItemCancel = new javax.swing.JButton();
        button_editItemConfirm = new javax.swing.JButton();
        dialog_addLocation = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        field_addLocationName = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        button_addLocationCancel = new javax.swing.JButton();
        button_addLocationConfirm = new javax.swing.JButton();
        dialog_bookItem = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        dateTimePicker_bookItemBookedDate = new com.github.lgooddatepicker.components.DateTimePicker();
        dateTimePicker_bookItemReturnDate = new com.github.lgooddatepicker.components.DateTimePicker();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        button_bookItemCancel = new javax.swing.JButton();
        button_bookItemConfirm = new javax.swing.JButton();
        dialog_viewHistory = new javax.swing.JDialog();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        list_viewHistory = new javax.swing.JList<>();
        jPanel14 = new javax.swing.JPanel();
        button_viewHistoryClose = new javax.swing.JButton();
        dialog_addNote = new javax.swing.JDialog();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        field_addNote = new javax.swing.JTextArea();
        jPanel16 = new javax.swing.JPanel();
        button_addNoteCancel = new javax.swing.JButton();
        button_addNoteConfirm = new javax.swing.JButton();
        panel_login = new javax.swing.JPanel();
        field_loginPassword = new javax.swing.JPasswordField();
        button_login = new javax.swing.JButton();
        label_password = new javax.swing.JLabel();
        field_loginUserID = new javax.swing.JTextField();
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
        button_editItem = new javax.swing.JButton();
        button_removeItem = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        button_bookItem = new javax.swing.JButton();
        button_returnItem = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        button_addNote = new javax.swing.JButton();
        button_flagForCalibration = new javax.swing.JButton();
        button_calibrateItem = new javax.swing.JButton();
        button_viewHistory = new javax.swing.JButton();
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
        panel_itemBookings = new javax.swing.JPanel();
        calendar_itemBookings = new com.github.lgooddatepicker.components.CalendarPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        list_itemBookings = new javax.swing.JList<>();
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
        button_editUser = new javax.swing.JButton();
        button_removeUser = new javax.swing.JButton();
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
        tab_locations = new javax.swing.JSplitPane();
        panel_locationSearch = new javax.swing.JPanel();
        field_searchLocation = new javax.swing.JTextField();
        button_addLocation = new javax.swing.JButton();
        button_searchLocation = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        list_locationSearchResults = new javax.swing.JList<>();
        panel_locationView = new javax.swing.JPanel();
        toolbar_locationTools = new javax.swing.JToolBar();
        button_removeLocation = new javax.swing.JButton();
        panel_locationDetails = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        text_locationID = new javax.swing.JTextField();
        text_locationName = new javax.swing.JTextField();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jScrollPane9 = new javax.swing.JScrollPane();
        list_locationItemsPreview = new javax.swing.JList<>();
        jLabel7 = new javax.swing.JLabel();

        dialog_addItem.setTitle("Add Item");
        dialog_addItem.setAlwaysOnTop(true);
        dialog_addItem.setMinimumSize(new java.awt.Dimension(300, 200));
        dialog_addItem.setModal(true);
        dialog_addItem.setResizable(false);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel15.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel15, gridBagConstraints);

        jLabel16.setText("Location");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel16, gridBagConstraints);

        jLabel17.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel17, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel5.add(field_addItemName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel5.add(field_addItemLocation, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel5.add(field_addItemType, gridBagConstraints);

        dialog_addItem.getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        button_addItemCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel7.add(button_addItemCancel, gridBagConstraints);

        button_addItemConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel7.add(button_addItemConfirm, gridBagConstraints);

        dialog_addItem.getContentPane().add(jPanel7, java.awt.BorderLayout.PAGE_END);

        dialog_editItem.setTitle("Edit Item");
        dialog_editItem.setMinimumSize(new java.awt.Dimension(400, 300));
        dialog_editItem.setModal(true);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel19.setText("Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel6.add(jLabel19, gridBagConstraints);

        jLabel20.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel6.add(jLabel20, gridBagConstraints);

        jLabel21.setText("Location:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel6.add(jLabel21, gridBagConstraints);

        jLabel22.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel6.add(jLabel22, gridBagConstraints);

        jLabel23.setText("Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel6.add(jLabel23, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel6.add(field_editItemName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel6.add(field_editItemLocation, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel6.add(field_editItemType, gridBagConstraints);

        field_editItemDescription.setColumns(20);
        field_editItemDescription.setRows(5);
        jScrollPane2.setViewportView(field_editItemDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel6.add(jScrollPane2, gridBagConstraints);

        combo_editItemStatus.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel6.add(combo_editItemStatus, gridBagConstraints);

        dialog_editItem.getContentPane().add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel9.setLayout(new java.awt.GridBagLayout());

        button_editItemCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel9.add(button_editItemCancel, gridBagConstraints);

        button_editItemConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel9.add(button_editItemConfirm, gridBagConstraints);

        dialog_editItem.getContentPane().add(jPanel9, java.awt.BorderLayout.PAGE_END);

        dialog_addLocation.setTitle("Add Item");
        dialog_addLocation.setAlwaysOnTop(true);
        dialog_addLocation.setMinimumSize(new java.awt.Dimension(300, 200));
        dialog_addLocation.setModal(true);
        dialog_addLocation.setResizable(false);

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLabel18.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(jLabel18, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel8.add(field_addLocationName, gridBagConstraints);

        dialog_addLocation.getContentPane().add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel10.setLayout(new java.awt.GridBagLayout());

        button_addLocationCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel10.add(button_addLocationCancel, gridBagConstraints);

        button_addLocationConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel10.add(button_addLocationConfirm, gridBagConstraints);

        dialog_addLocation.getContentPane().add(jPanel10, java.awt.BorderLayout.PAGE_END);

        dialog_bookItem.setTitle("Book Item");
        dialog_bookItem.setAlwaysOnTop(true);
        dialog_bookItem.setMinimumSize(new java.awt.Dimension(400, 200));
        dialog_bookItem.setModal(true);
        dialog_bookItem.setResizable(false);

        jPanel11.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel11.add(dateTimePicker_bookItemBookedDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel11.add(dateTimePicker_bookItemReturnDate, gridBagConstraints);

        jLabel25.setText("Book From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel11.add(jLabel25, gridBagConstraints);

        jLabel26.setText("Until");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel11.add(jLabel26, gridBagConstraints);

        dialog_bookItem.getContentPane().add(jPanel11, java.awt.BorderLayout.CENTER);

        jPanel12.setLayout(new java.awt.GridBagLayout());

        button_bookItemCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel12.add(button_bookItemCancel, gridBagConstraints);

        button_bookItemConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel12.add(button_bookItemConfirm, gridBagConstraints);

        dialog_bookItem.getContentPane().add(jPanel12, java.awt.BorderLayout.PAGE_END);

        dialog_viewHistory.setTitle("Item History");
        dialog_viewHistory.setAlwaysOnTop(true);
        dialog_viewHistory.setMinimumSize(new java.awt.Dimension(400, 200));
        dialog_viewHistory.setModal(true);

        jPanel13.setLayout(new java.awt.GridBagLayout());

        list_viewHistory.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane8.setViewportView(list_viewHistory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel13.add(jScrollPane8, gridBagConstraints);

        dialog_viewHistory.getContentPane().add(jPanel13, java.awt.BorderLayout.CENTER);

        jPanel14.setLayout(new java.awt.GridBagLayout());

        button_viewHistoryClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel14.add(button_viewHistoryClose, gridBagConstraints);

        dialog_viewHistory.getContentPane().add(jPanel14, java.awt.BorderLayout.PAGE_END);

        dialog_addNote.setTitle("Add Note");
        dialog_addNote.setMinimumSize(new java.awt.Dimension(400, 300));
        dialog_addNote.setModal(true);

        jPanel15.setLayout(new java.awt.GridBagLayout());

        field_addNote.setColumns(20);
        field_addNote.setRows(5);
        jScrollPane4.setViewportView(field_addNote);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel15.add(jScrollPane4, gridBagConstraints);

        dialog_addNote.getContentPane().add(jPanel15, java.awt.BorderLayout.CENTER);

        jPanel16.setLayout(new java.awt.GridBagLayout());

        button_addNoteCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel16.add(button_addNoteCancel, gridBagConstraints);

        button_addNoteConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel16.add(button_addNoteConfirm, gridBagConstraints);

        dialog_addNote.getContentPane().add(jPanel16, java.awt.BorderLayout.PAGE_END);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Equipment Manager");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(400, 300));
        setName("applicationFrame"); // NOI18N
        getContentPane().setLayout(new java.awt.CardLayout());

        panel_login.setLayout(new java.awt.GridBagLayout());

        field_loginPassword.setText("123123");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panel_login.add(field_loginPassword, gridBagConstraints);

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        panel_login.add(label_password, gridBagConstraints);

        field_loginUserID.setText("111");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panel_login.add(field_loginUserID, gridBagConstraints);

        label_username.setText("User ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
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

        list_itemSearchResults.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

        button_editItem.setText("Edit Item");
        button_editItem.setFocusable(false);
        button_editItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_editItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(button_editItem);

        button_removeItem.setText("Remove Item");
        button_removeItem.setFocusable(false);
        button_removeItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_removeItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(button_removeItem);
        jToolBar1.add(jSeparator1);

        button_bookItem.setText("Book Item");
        button_bookItem.setFocusable(false);
        button_bookItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_bookItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(button_bookItem);

        button_returnItem.setText("Return Item");
        button_returnItem.setFocusable(false);
        button_returnItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_returnItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(button_returnItem);
        jToolBar1.add(jSeparator2);

        button_addNote.setText("Add Note");
        button_addNote.setFocusable(false);
        button_addNote.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_addNote.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(button_addNote);

        button_flagForCalibration.setText("Flag for Calibration");
        button_flagForCalibration.setFocusable(false);
        button_flagForCalibration.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_flagForCalibration.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(button_flagForCalibration);

        button_calibrateItem.setText("Calibrate Item");
        button_calibrateItem.setFocusable(false);
        button_calibrateItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_calibrateItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(button_calibrateItem);

        button_viewHistory.setText("View History");
        button_viewHistory.setFocusable(false);
        button_viewHistory.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_viewHistory.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(button_viewHistory);

        panel_itemView.add(jToolBar1, java.awt.BorderLayout.PAGE_END);

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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemID, gridBagConstraints);

        text_itemName.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemName, gridBagConstraints);

        text_itemLocation.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemLocation, gridBagConstraints);

        text_itemStatus.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemStatus, gridBagConstraints);

        text_itemType.setEditable(false);
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

        panel_itemDetails.setViewportView(jPanel1);

        panel_itemView.add(panel_itemDetails, java.awt.BorderLayout.CENTER);

        panel_itemBookings.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panel_itemBookings.add(calendar_itemBookings, gridBagConstraints);

        jScrollPane5.setViewportView(list_itemBookings);

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

        panel_itemView.add(panel_itemBookings, java.awt.BorderLayout.LINE_END);

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

        list_userSearchResults.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

        button_editUser.setText("Edit User");
        button_editUser.setFocusable(false);
        button_editUser.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_editUser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_userTools.add(button_editUser);

        button_removeUser.setText("Remove User");
        button_removeUser.setFocusable(false);
        button_removeUser.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_removeUser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_userTools.add(button_removeUser);

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
        text_userID.setPreferredSize(new java.awt.Dimension(232, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel3.add(text_userID, gridBagConstraints);

        text_userName.setEditable(false);
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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(text_userSecurityLevel, gridBagConstraints);

        panel_userDetails.setViewportView(jPanel3);

        panel_userView.add(panel_userDetails, java.awt.BorderLayout.CENTER);

        tab_users.setRightComponent(panel_userView);

        tabs_application.addTab("Users", tab_users);

        panel_locationSearch.setLayout(new java.awt.GridBagLayout());

        field_searchLocation.setMinimumSize(new java.awt.Dimension(100, 22));
        field_searchLocation.setToolTipText("Input a string to search for a list of corresponding users.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panel_locationSearch.add(field_searchLocation, gridBagConstraints);

        button_addLocation.setText("Add Location");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panel_locationSearch.add(button_addLocation, gridBagConstraints);

        button_searchLocation.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panel_locationSearch.add(button_searchLocation, gridBagConstraints);

        list_locationSearchResults.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane7.setViewportView(list_locationSearchResults);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_locationSearch.add(jScrollPane7, gridBagConstraints);

        tab_locations.setLeftComponent(panel_locationSearch);

        panel_locationView.setLayout(new java.awt.BorderLayout());

        toolbar_locationTools.setFloatable(false);
        toolbar_locationTools.setRollover(true);

        button_removeLocation.setText("Remove Location");
        button_removeLocation.setFocusable(false);
        button_removeLocation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_removeLocation.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_locationTools.add(button_removeLocation);

        panel_locationView.add(toolbar_locationTools, java.awt.BorderLayout.PAGE_END);

        panel_locationDetails.setMinimumSize(new java.awt.Dimension(321, 16));

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel13.setText("ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanel4.add(jLabel13, gridBagConstraints);

        jLabel14.setText("Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanel4.add(jLabel14, gridBagConstraints);

        text_locationID.setEditable(false);
        text_locationID.setPreferredSize(new java.awt.Dimension(232, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel4.add(text_locationID, gridBagConstraints);

        text_locationName.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel4.add(text_locationName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0.1;
        jPanel4.add(filler5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(filler6, gridBagConstraints);

        list_locationItemsPreview.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane9.setViewportView(list_locationItemsPreview);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(jScrollPane9, gridBagConstraints);

        jLabel7.setText("Item IDs:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanel4.add(jLabel7, gridBagConstraints);

        panel_locationDetails.setViewportView(jPanel4);

        panel_locationView.add(panel_locationDetails, java.awt.BorderLayout.CENTER);

        tab_locations.setRightComponent(panel_locationView);

        tabs_application.addTab("Locations", tab_locations);

        panel_main.add(tabs_application);

        getContentPane().add(panel_main, "card4");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_addItem;
    private javax.swing.JButton button_addItemCancel;
    private javax.swing.JButton button_addItemConfirm;
    private javax.swing.JButton button_addLocation;
    private javax.swing.JButton button_addLocationCancel;
    private javax.swing.JButton button_addLocationConfirm;
    private javax.swing.JButton button_addNote;
    private javax.swing.JButton button_addNoteCancel;
    private javax.swing.JButton button_addNoteConfirm;
    private javax.swing.JButton button_addUser;
    private javax.swing.JButton button_bookItem;
    private javax.swing.JButton button_bookItemCancel;
    private javax.swing.JButton button_bookItemConfirm;
    private javax.swing.JButton button_calibrateItem;
    private javax.swing.JButton button_editItem;
    private javax.swing.JButton button_editItemCancel;
    private javax.swing.JButton button_editItemConfirm;
    private javax.swing.JButton button_editUser;
    private javax.swing.JButton button_flagForCalibration;
    private javax.swing.JButton button_login;
    private javax.swing.JButton button_removeItem;
    private javax.swing.JButton button_removeLocation;
    private javax.swing.JButton button_removeUser;
    private javax.swing.JButton button_returnItem;
    private javax.swing.JButton button_searchItem;
    private javax.swing.JButton button_searchLocation;
    private javax.swing.JButton button_searchUser;
    private javax.swing.JButton button_viewHistory;
    private javax.swing.JButton button_viewHistoryClose;
    private com.github.lgooddatepicker.components.CalendarPanel calendar_itemBookings;
    private javax.swing.JCheckBox check_needsCalibration;
    private javax.swing.JComboBox<Item.Status> combo_editItemStatus;
    private com.github.lgooddatepicker.components.DateTimePicker dateTimePicker_bookItemBookedDate;
    private com.github.lgooddatepicker.components.DateTimePicker dateTimePicker_bookItemReturnDate;
    private javax.swing.JDialog dialog_addItem;
    private javax.swing.JDialog dialog_addLocation;
    private javax.swing.JDialog dialog_addNote;
    private javax.swing.JDialog dialog_bookItem;
    private javax.swing.JDialog dialog_editItem;
    private javax.swing.JDialog dialog_viewHistory;
    private javax.swing.JTextField field_addItemLocation;
    private javax.swing.JTextField field_addItemName;
    private javax.swing.JTextField field_addItemType;
    private javax.swing.JTextField field_addLocationName;
    private javax.swing.JTextArea field_addNote;
    private javax.swing.JTextArea field_editItemDescription;
    private javax.swing.JTextField field_editItemLocation;
    private javax.swing.JTextField field_editItemName;
    private javax.swing.JTextField field_editItemType;
    private javax.swing.JPasswordField field_loginPassword;
    private javax.swing.JTextField field_loginUserID;
    private javax.swing.JTextField field_searchItem;
    private javax.swing.JTextField field_searchLocation;
    private javax.swing.JTextField field_searchUser;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel label_password;
    private javax.swing.JLabel label_username;
    private javax.swing.JList<String> list_itemBookings;
    private javax.swing.JList<String> list_itemSearchResults;
    private javax.swing.JList<String> list_locationItemsPreview;
    private javax.swing.JList<String> list_locationSearchResults;
    private javax.swing.JList<String> list_userSearchResults;
    private javax.swing.JList<String> list_viewHistory;
    private javax.swing.JPanel panel_itemBookings;
    private javax.swing.JScrollPane panel_itemDetails;
    private javax.swing.JPanel panel_itemSearch;
    private javax.swing.JPanel panel_itemView;
    private javax.swing.JScrollPane panel_locationDetails;
    private javax.swing.JPanel panel_locationSearch;
    private javax.swing.JPanel panel_locationView;
    private javax.swing.JPanel panel_login;
    private javax.swing.JPanel panel_main;
    private javax.swing.JScrollPane panel_userDetails;
    private javax.swing.JPanel panel_userSearch;
    private javax.swing.JPanel panel_userView;
    private javax.swing.JSplitPane tab_items;
    private javax.swing.JSplitPane tab_locations;
    private javax.swing.JSplitPane tab_users;
    private javax.swing.JTabbedPane tabs_application;
    private javax.swing.JTextArea text_itemDescription;
    private javax.swing.JTextField text_itemID;
    private javax.swing.JTextField text_itemLocation;
    private javax.swing.JTextField text_itemName;
    private javax.swing.JTextField text_itemStatus;
    private javax.swing.JTextField text_itemType;
    private javax.swing.JTextField text_lastCalibration;
    private javax.swing.JTextField text_locationID;
    private javax.swing.JTextField text_locationName;
    private javax.swing.JTextField text_userID;
    private javax.swing.JTextField text_userName;
    private javax.swing.JTextField text_userSecurityLevel;
    private javax.swing.JToolBar toolbar_locationTools;
    private javax.swing.JToolBar toolbar_userTools;
    // End of variables declaration//GEN-END:variables
}
