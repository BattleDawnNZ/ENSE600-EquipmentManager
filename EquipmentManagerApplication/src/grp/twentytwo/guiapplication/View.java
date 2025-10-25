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
    public Speaker<ActionEvent> logout;
    public Speaker<ActionEvent> addItem;
    public Speaker<String> removeItem;
    public Speaker<String> bookItem;
    public Speaker<String> returnItem;
    public Speaker<String> searchForItem;
    public Speaker<String> viewItem;
    public Speaker<String> getEditItemDetails;
    public Speaker<String> editItem;
    public Speaker<String> viewBooking;
    public Speaker<String> viewBookingDetails;
    public Speaker<String> addNote;
    public Speaker<String> flagItem;
    public Speaker<String> calibrateItem;
    public Speaker<String> viewHistory;

    public Speaker<ActionEvent> addUser;
    public Speaker<String> getEditUserDetails;
    public Speaker<String> editUser;
    public Speaker<String> removeUser;
    public Speaker<String> searchForUser;
    public Speaker<String> viewUser;

    public Speaker<ActionEvent> addLocation;
    public Speaker<String> removeLocation;
    public Speaker<String> searchForLocation;
    public Speaker<String> viewLocation;
    public Speaker<ActionEvent> getLocations;
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
	login = new Speaker<>();
	button_login.addActionListener((ActionEvent e) -> {
	    login.notifyListeners(e);
	});
	logout = new Speaker<>();
	button_logout.addActionListener((ActionEvent e) -> {
	    logout.notifyListeners(e);
	});

	// Item Tab ------------------------------------------------------------
	getLocations = new Speaker<>();
	// Item Adding
	button_addItem.addActionListener((ActionEvent e) -> {
	    setupAdditemDialog();
	    dialog_addItem.setVisible(true);
	});
	button_addItemCancel.addActionListener((ActionEvent e) -> {
	    dialog_addItem.setVisible(false);
	});
	addItem = new Speaker<>();
	button_addItemConfirm.addActionListener((ActionEvent e) -> {
	    if (verifyAddItemDetails()) {
		addItem.notifyListeners(e);
		dialog_addItem.setVisible(false);
	    }
	    searchForItem.notifyListeners(field_searchItem.getText());
	});
	// Item Removal
	removeItem = new Speaker<>();
	button_removeItem.addActionListener((ActionEvent e) -> {
	    if (currentItemID() == null || currentItemID().isBlank()) {
		showInvalidEntry("No Item Selected", "No Item has been selected.\nPlease select an Item from the\nitem list on the left.");
	    } else {
		String itemID = currentItemID();
		if (getConfirmation("Remove Item", "Are you sure you want remove Item " + itemID + "?")) {
		    removeItem.notifyListeners(itemID);
		    searchForItem.notifyListeners(field_searchItem.getText());
		    setItemPreview(null);
		    setItemBookings(null);
		}
	    }

	});
	// Item editing
	getEditItemDetails = new Speaker<>();
	button_editItem.addActionListener((ActionEvent e) -> {
	    if (currentItemID() == null || currentItemID().isBlank()) {
		showInvalidEntry("No Item Selected", "No Item has been selected.\nPlease select an Item from the\nitem list on the left.");
	    } else {
		getEditItemDetails.notifyListeners(currentItemID());
	    }
	});
	ActionListener closeEditItem = (ActionEvent e) -> {
	    dialog_editItem.setVisible(false);
	};
	button_editItemCancel.addActionListener(closeEditItem);
	editItem = new Speaker<>();
	button_editItemConfirm.addActionListener((ActionEvent e) -> {
	    editItem.notifyListeners(currentItemID());
	    viewItem.notifyListeners(currentItemID());
	});
	button_editItemConfirm.addActionListener(closeEditItem);
	// Item Searching
	searchForItem = new Speaker<>();
	ActionListener searchItemListener = (ActionEvent e) -> {
	    searchForItem.notifyListeners(field_searchItem.getText());
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
	// Item Note
	button_addNote.addActionListener((ActionEvent e) -> {
	    if (currentItemID() == null || currentItemID().isBlank()) {
		showInvalidEntry("No Item Selected", "No Item has been selected.\nPlease select an Item from the\nitem list on the left.");
	    } else {
		clearAddNoteDialog();
		dialog_addNote.setVisible(true);
	    }
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
	    if (currentItemID() == null || currentItemID().isBlank()) {
		showInvalidEntry("No Item Selected", "No Item has been selected.\nPlease select an Item from the\nitem list on the left.");
	    } else {
		flagItem.notifyListeners(currentItemID());
		viewItem.notifyListeners(currentItemID());
	    }
	});

	calibrateItem = new Speaker<>();
	button_calibrateItem.addActionListener((ActionEvent e) -> {
	    if (currentItemID() == null || currentItemID().isBlank()) {
		showInvalidEntry("No Item Selected", "No Item has been selected.\nPlease select an Item from the\nitem list on the left.");
	    } else {
		calibrateItem.notifyListeners(currentItemID());
		viewItem.notifyListeners(currentItemID());
	    }
	});

	// Item History
	viewHistory = new Speaker<>();
	button_viewHistory.addActionListener((ActionEvent e) -> {
	    if (currentItemID() == null || currentItemID().isBlank()) {
		showInvalidEntry("No Item Selected", "No Item has been selected.\nPlease select an Item from the\nitem list on the left.");
	    } else {
		viewHistory.notifyListeners(currentItemID());
		dialog_viewHistory.setVisible(true);
	    }
	});
	button_viewHistoryClose.addActionListener((ActionEvent e) -> {
	    dialog_viewHistory.setVisible(false);
	});

	// Bookings ------------------------------------------------------------
	// Preview Booking Details
	viewBookingDetails = new Speaker<>();
	button_viewBookingDetails.addActionListener((ActionEvent e) -> {
	    if (currentBookingID() != null) {
		viewBookingDetails.notifyListeners(currentBookingID());
	    } else {
		showInvalidEntry("No Booking Selected", "No booking has been selected.\nPlease select a booking from the\nbookings list on the right to view its details.");
	    }
	});
	button_viewBookingDetailsClose.addActionListener((ActionEvent e) -> {
	    dialog_viewBookingDetails.setVisible(false);
	});
	// Item Preview Bookings
	viewBooking = new Speaker<>();
	list_itemBookings.addListSelectionListener((ListSelectionEvent e) -> {
	    if (!e.getValueIsAdjusting()) { // Avoid duplicate events
		String selectedValue = list_itemBookings.getSelectedValue();
		if (selectedValue != null) {
		    viewBooking.notifyListeners(selectedValue);
		}
	    }
	});
	// Item Booking
	button_bookItem.addActionListener((ActionEvent e) -> {
	    if (currentItemID() == null || currentItemID().isBlank()) {
		showInvalidEntry("No Item Selected", "No Item has been selected.\nPlease select an Item from the\nitem list on the left to book it.");
	    } else {
		setupBookItemDialog();
		dialog_bookItem.setVisible(true);
	    }
	});
	ActionListener closeBookItem = (ActionEvent e) -> {
	    dialog_bookItem.setVisible(false);
	};
	button_bookItemCancel.addActionListener(closeBookItem);
	bookItem = new Speaker<>();
	button_bookItemConfirm.addActionListener((ActionEvent e) -> {
	    bookItem.notifyListeners(currentItemID());
	    viewItem.notifyListeners(currentItemID());
	});
	button_bookItemConfirm.addActionListener(closeBookItem);
	// Item Returning
	returnItem = new Speaker<>();
	button_returnItem.addActionListener((ActionEvent e) -> {
	    if (currentBookingID() != null) {
		returnItem.notifyListeners(currentBookingID());
		viewItem.notifyListeners(currentItemID());
	    } else {
		showInvalidEntry("No Booking Selected", "No booking has been selected.\nPlease select a booking from the\nbookings list on the right to return it.");
	    }
	});

	// User Tab ------------------------------------------------------------
	// User Adding
	button_addUser.addActionListener((ActionEvent e) -> {
	    clearAddUserDialog();
	    dialog_addUser.setVisible(true);
	});
	button_addUserCancel.addActionListener((ActionEvent e) -> {
	    dialog_addUser.setVisible(false);
	});
	addUser = new Speaker<>();
	button_addUserConfirm.addActionListener((ActionEvent e) -> {
	    if (verifyAddUserDetails()) {
		addUser.notifyListeners(e);
		dialog_addUser.setVisible(false);
	    }
	});
	// User editing
	getEditUserDetails = new Speaker<>();
	button_editUser.addActionListener((ActionEvent e) -> {
	    getEditUserDetails.notifyListeners(currentUserID());
	});
	ActionListener closeEditUser = (ActionEvent e) -> {
	    dialog_editUser.setVisible(false);
	};
	button_editUserCancel.addActionListener(closeEditUser);
	editUser = new Speaker<>();
	button_editUserConfirm.addActionListener((ActionEvent e) -> {
	    editUser.notifyListeners(currentUserID());
	    viewUser.notifyListeners(currentUserID());
	});
	button_editUserConfirm.addActionListener(closeEditUser);
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
		    if (selectedValue != null) {
			viewUser.notifyListeners(selectedValue);
		    }
		}
	    }
	});
	// Location Tab --------------------------------------------------------
	// Location Adding
	button_addLocation.addActionListener((ActionEvent e) -> {
	    clearAddLocationDialog();
	    dialog_addLocation.setVisible(true);
	});
	button_addLocationCancel.addActionListener((ActionEvent e) -> {
	    dialog_addLocation.setVisible(false);
	});
	addLocation = new Speaker<>();
	button_addLocationConfirm.addActionListener((ActionEvent e) -> {
	    if (verifyAddLocationDetails()) {
		addLocation.notifyListeners(e);
		dialog_addLocation.setVisible(false);
		refreshLocationSearch();
	    }
	});
	// Location Removal
	removeLocation = new Speaker<>();
	button_removeLocation.addActionListener((ActionEvent e) -> {
	    if (currentLocationName() == null || currentLocationName().isBlank()) {
		showInvalidEntry("No Item Selected", "No Item has been selected.\nPlease select an Item from the\nitem list on the left.");
	    } else {
		String itemID = currentItemID();
		if (getConfirmation("Remove Location", "Are you sure you want remove Location " + currentLocationName() + "?")) {
		    removeLocation.notifyListeners(currentLocationID());
		    refreshLocationSearch();
		    setLocationPreview(null);
		    setLocationItemsPreview(null);
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
		    if (selectedValue != null) {
			viewLocation.notifyListeners(selectedValue);
		    }
		}
	    }
	});
    }

    public void initialSearch() {
	text_itemID.setText("");
	refreshItemSearch();
	text_userID.setText("");
	refreshUserSearch();
	text_locationName.setText("");
	refreshLocationSearch();
    }

    // <editor-fold defaultstate="collapsed" desc="Helper Dialogs">
    public void showError(Exception err) {
	JOptionPane.showMessageDialog(this, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInvalidEntry(String title, String message) {
	JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean getConfirmation(String title, String message) {
	return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION) == 0;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Login">
    public User getLoginDetails(User user) {
	user.setID(field_loginUserID.getText());
	String password = "";
	for (char c : field_loginPassword.getPassword()) {
	    password += c;
	}
	user.setPassword(password);
	return user;
    }

    public void loginUser(boolean successful) {
	if (successful) {
	    initialSearch();
	    ((java.awt.CardLayout) getContentPane().getLayout()).next(getContentPane());
	}
    }

    public void logoutUser() {
	((java.awt.CardLayout) getContentPane().getLayout()).next(getContentPane());
	field_loginUserID.setText("");
	field_loginPassword.setText("");
    }

    public void secureUI(User activeUser) {
	label_loggedInAs.setText("Logged in as " + activeUser.getName() + " [" + activeUser.getID() + "] (" + activeUser.getSecurityLevel().toString() + ")");
	boolean employee = activeUser.getSecurityLevel().compareTo(User.SecurityLevels.EMPLOYEE) >= 0;
	boolean manager = activeUser.getSecurityLevel().compareTo(User.SecurityLevels.MANAGER) >= 0;
	// Set Item Secrutities
	// Employee
	button_bookItem.setVisible(employee);
	button_returnItem.setVisible(employee);
	button_viewHistory.setVisible(employee);
	separator_booking.setVisible(employee);
	// Manager
	button_addItem.setVisible(manager);
	button_editItem.setVisible(manager);
	button_removeItem.setVisible(manager);
	button_addNote.setVisible(manager);
	button_flagForCalibration.setVisible(manager);
	button_calibrateItem.setVisible(manager);
	separator_item.setVisible(manager);

	// Set User Securities
	if (manager) {
	    tabs_application.addTab("Users", tab_users);
	} else {
	    int tabIndex = tabs_application.indexOfTab("Users");
	    if (tabIndex >= 0) {
		tabs_application.removeTabAt(tabIndex);
	    }
	}
	button_addUser.setVisible(manager);
	button_editUser.setVisible(manager);
	button_removeUser.setVisible(manager);

	// Set Location Securities
	button_addLocation.setVisible(manager);
	button_removeLocation.setVisible(manager);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Item Functions">
    public String currentItemID() {
	return text_itemID.getText();
    }

    public void refreshItemSearch() {
	searchForItem.notifyListeners(field_searchItem.getText());
    }

    public void setupAdditemDialog() {
	getLocations.notifyListeners(null);
	field_addItemName.setText("");
	field_addItemType.setText("");
    }

    public boolean verifyAddItemDetails() {
	// Check Name
	if (field_addItemName.getText().length() < 1) {
	    showInvalidEntry("Invalid Item Name", "The Item name entered is too short.\nItem names must be 1 characters or more.");
	    return false;
	}
	if (field_addItemName.getText().length() > 30) {
	    showInvalidEntry("Invalid Item Name", "The Item name entered is too long.\nItem names must be 30 characters or less.");
	    return false;
	}
	// Check Type
	if (field_addItemType.getText().length() < 1) {
	    showInvalidEntry("Invalid Item Type", "The Item type entered is too short.\nItem locations must be 1 characters or more.");
	    return false;
	}
	if (field_addItemType.getText().length() > 40) {
	    showInvalidEntry("Invalid Item Type", "The Item type entered is too long.\nItem locations must be 40 characters or less.");
	    return false;
	}
	// Check Location
	if (combo_addItemLocation.getSelectedItem() == null) {
	    showInvalidEntry("Invalid Item Location", "The Item must have a location.");
	    return false;
	}
	return true;
    }

    public void setNewItemDetails(Item item) {
	item.setName(field_addItemName.getText());
	item.setLocation((String) combo_addItemLocation.getSelectedItem());
	item.setType(field_addItemType.getText());
    }

    public void editItemDetails(Item item) {
	item.setName(field_editItemName.getText());
	item.setDescription(field_editItemDescription.getText());
	item.setLocation((String) combo_editItemLocation.getSelectedItem());
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
	    if (itemData == null) {
		text_itemID.setText("");
		text_itemName.setText("");
		text_itemDescription.setText("");
		text_itemLocation.setText("");
		text_itemStatus.setText("");
		text_itemType.setText("");
		text_lastCalibration.setText("");
		check_needsCalibration.setSelected(false);
	    } else {
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

    public void setItemEditingPreview(Item itemData) {
	try {
	    if (itemData != null) {
		getLocations.notifyListeners(null);
		field_editItemName.setText(itemData.getName());
		field_editItemDescription.setText(itemData.getDescription());
		combo_editItemLocation.setSelectedItem(itemData.getLocation());
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Booking Functions">
    public String currentBookingID() {
	return list_itemBookings.getSelectedValue();
    }

    public void setItemBookings(List<Booking> newList) {
	if (newList == null) {
	    list_itemBookings.setModel(new DefaultListModel<>());
	    return;
	}
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

    public boolean setNewBookingDetails(Booking booking) {
	try {
	    LocalDate bookedDate = dateTimePicker_bookItemBookedDate.datePicker.getDate();
	    if (bookedDate == null) {
		showInvalidEntry("Invalid Booking Date", "A booking requires a book date.");
		return false;
	    }
	    LocalTime bookedTime = dateTimePicker_bookItemBookedDate.timePicker.getTime();
	    if (bookedTime == null) {
		showInvalidEntry("Invalid Booking Time", "A booking requires a book time.");
		return false;
	    }
	    LocalDateTime bookedDateTime = bookedDate.atTime(bookedTime);
	    LocalDate returnDate = dateTimePicker_bookItemReturnDate.datePicker.getDate();
	    if (returnDate == null) {
		showInvalidEntry("Invalid Booking Date", "A booking requires a return date.");
		return false;
	    }
	    LocalTime returnTime = dateTimePicker_bookItemReturnDate.timePicker.getTime();
	    if (returnTime == null) {
		showInvalidEntry("Invalid Booking Time", "A booking requires a return time.");
		return false;
	    }
	    LocalDateTime returnDateTime = returnDate.atTime(returnTime);
	    if (bookedDateTime.isAfter(returnDateTime)) {
		showInvalidEntry("Invalid Booking Range", "A bookings return date must be after its book date.");
		return false;
	    }
	    booking.setBookingRange(bookedDateTime, returnDateTime);
	    return true;
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return false;
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

    public void setupBookItemDialog() {
	dateTimePicker_bookItemBookedDate.clear();
	dateTimePicker_bookItemReturnDate.clear();
    }

    public void previewBookingDetails(Booking booking) {
	try {
	    if (booking != null) {
		field_viewBookingDetailsBookingID.setText(booking.getID());
		field_viewBookingDetailsUserID.setText(booking.getUserID());
		field_viewBookingDetailsItemID.setText(booking.getItemID());
		dateTimePicker_viewBookingDetailsBookedDate.setDateTimeStrict(booking.getBookedDate());
		dateTimePicker_viewBookingDetailsReturnDate.setDateTimeStrict(booking.getReturnDate());
		dialog_viewBookingDetails.setVisible(true);
	    }
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="User Functions">
    public String currentUserID() {
	return text_userID.getText();
    }

    public void refreshUserSearch() {
	searchForUser.notifyListeners(field_searchUser.getText());
    }

    public void clearAddUserDialog() {
	field_addUserID.setText("");
	field_addUserName.setText("");
	field_addUserPassword.setText("");
	field_addUserConfirmPassword.setText("");
	// Setup the Security Levels Combo Box
	DefaultComboBoxModel<User.SecurityLevels> securityLevels = new DefaultComboBoxModel<>();
	for (User.SecurityLevels level : User.SecurityLevels.values()) {
	    securityLevels.addElement(level);
	}
	combo_addUserSecurityLevel.setModel(securityLevels);
    }

    public void setUserEditingPreview(User userData) {
	try {
	    if (userData != null) {
		field_editUserName.setText(userData.getName());
		// Setup the Security Levels Combo Box
		DefaultComboBoxModel<User.SecurityLevels> securityLevels = new DefaultComboBoxModel<>();
		for (User.SecurityLevels level : User.SecurityLevels.values()) {
		    securityLevels.addElement(level);
		}
		combo_editUserSecurityLevel.setModel(securityLevels);
		combo_editUserSecurityLevel.setSelectedItem(userData.getSecurityLevel());
		dialog_editUser.setVisible(true);
	    }
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void editUserDetails(User user) {
	user.setName(field_editUserName.getText());
	user.setSecurityLevel((User.SecurityLevels) combo_editUserSecurityLevel.getSelectedItem());
    }

    public boolean verifyAddUserDetails() {
	// Check ID
	if (field_addUserID.getText().length() < 3) {
	    showInvalidEntry("Invalid User ID", "The User ID entered is too short.\nUser IDs must be 3 characters or more.");
	    return false;
	}
	if (field_addUserID.getText().length() > 12) {
	    showInvalidEntry("Invalid User ID", "The User ID entered is too long.\nUser IDs must be 12 characters or less.");
	    return false;
	}
	// Check Name
	if (field_addUserName.getText().length() < 1) {
	    showInvalidEntry("Invalid User Name", "The User name entered is too short.\nUser names must be 1 characters or more.");
	    return false;
	}
	if (field_addUserName.getText().length() > 30) {
	    showInvalidEntry("Invalid User Name", "The User name entered is too long.\nUser names must be 30 characters or less.");
	    return false;
	}
	// Check Password
	String password = "";
	for (char c : field_addUserPassword.getPassword()) {
	    password += c;
	}
	String confirmPassword = "";
	for (char c : field_addUserConfirmPassword.getPassword()) {
	    confirmPassword += c;
	}
	if (password.length() < 3) {
	    showInvalidEntry("Invalid User Password", "The User password entered is too short.\nUser passwords must be 3 characters or more.");
	    return false;
	}
	if (password.length() > 15) {
	    showInvalidEntry("Invalid User Password", "The User password entered is too long.\nUser passwords must be 15 characters or less.");
	    return false;
	}
	if (!password.equals(confirmPassword)) {
	    showInvalidEntry("Invalid User Password", "The User passwords entered do not match.\nUser password must match the confirmation password.");
	    return false;
	}
	return true;
    }

    public void setNewUserDetails(User user) {
	user.setID(field_addUserID.getText());
	user.setName(field_addUserName.getText());
	user.setSecurityLevel((User.SecurityLevels) combo_addUserSecurityLevel.getSelectedItem());

	// Set Password
	String password = "";
	for (char c : field_addUserPassword.getPassword()) {
	    password += c;
	}
	user.setPassword(password);
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Location Functions">
    public String currentLocationID() {
	return text_locationID.getText();
    }

    public String currentLocationName() {
	return text_locationName.getText();
    }

    public void refreshLocationSearch() {
	searchForLocation.notifyListeners(field_searchLocation.getText());
    }

    public void clearAddLocationDialog() {
	field_addLocationName.setText("");
    }

    public boolean verifyAddLocationDetails() {// Check Name
	if (field_addLocationName.getText().length() < 1) {
	    showInvalidEntry("Invalid Location Name", "The Location name entered is too short.\nLocation names must be 1 characters or more.");
	    return false;
	}
	if (field_addLocationName.getText().length() > 30) {
	    showInvalidEntry("Invalid Location Name", "The Location name entered is too long.\nLocation names must be 30 characters or less.");
	    return false;
	}
	return true;
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
	    if (locationData == null) {
		text_locationID.setText("");
		text_locationName.setText("");
	    } else {
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
	    if (newList == null) {
		list_locationItemsPreview.setModel(new DefaultListModel<>());
	    } else {
		DefaultListModel<String> locations = new DefaultListModel<>();
		locations.addAll(newList);
		list_locationItemsPreview.setModel(locations);
	    }
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void setLocations(ArrayList<String> newList) {
	DefaultComboBoxModel<String> locations = new DefaultComboBoxModel<>();
	locations.addAll(newList);
	combo_addItemLocation.setModel(locations);
	combo_editItemLocation.setModel(locations);
    }
    // </editor-fold>

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
        field_addItemType = new javax.swing.JTextField();
        combo_addItemLocation = new javax.swing.JComboBox<>();
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
        field_editItemType = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        field_editItemDescription = new javax.swing.JTextArea();
        combo_editItemStatus = new javax.swing.JComboBox<>();
        combo_editItemLocation = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        button_editItemCancel = new javax.swing.JButton();
        button_editItemConfirm = new javax.swing.JButton();
        dialog_bookItem = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        dateTimePicker_bookItemBookedDate = new com.github.lgooddatepicker.components.DateTimePicker();
        dateTimePicker_bookItemReturnDate = new com.github.lgooddatepicker.components.DateTimePicker();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        button_bookItemCancel = new javax.swing.JButton();
        button_bookItemConfirm = new javax.swing.JButton();
        dialog_addNote = new javax.swing.JDialog();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        field_addNote = new javax.swing.JTextArea();
        jPanel16 = new javax.swing.JPanel();
        button_addNoteCancel = new javax.swing.JButton();
        button_addNoteConfirm = new javax.swing.JButton();
        dialog_viewHistory = new javax.swing.JDialog();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        list_viewHistory = new javax.swing.JList<>();
        jPanel14 = new javax.swing.JPanel();
        button_viewHistoryClose = new javax.swing.JButton();
        dialog_addUser = new javax.swing.JDialog();
        jPanel17 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        field_addUserName = new javax.swing.JTextField();
        combo_addUserSecurityLevel = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        field_addUserID = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        field_addUserPassword = new javax.swing.JPasswordField();
        jLabel30 = new javax.swing.JLabel();
        field_addUserConfirmPassword = new javax.swing.JPasswordField();
        jPanel18 = new javax.swing.JPanel();
        button_addUserCancel = new javax.swing.JButton();
        button_addUserConfirm = new javax.swing.JButton();
        dialog_addLocation = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        field_addLocationName = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        button_addLocationCancel = new javax.swing.JButton();
        button_addLocationConfirm = new javax.swing.JButton();
        dialog_editUser = new javax.swing.JDialog();
        jPanel19 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        field_editUserName = new javax.swing.JTextField();
        combo_editUserSecurityLevel = new javax.swing.JComboBox<>();
        jPanel20 = new javax.swing.JPanel();
        button_editUserCancel = new javax.swing.JButton();
        button_editUserConfirm = new javax.swing.JButton();
        dialog_viewBookingDetails = new javax.swing.JDialog();
        jPanel21 = new javax.swing.JPanel();
        dateTimePicker_viewBookingDetailsBookedDate = new com.github.lgooddatepicker.components.DateTimePicker();
        dateTimePicker_viewBookingDetailsReturnDate = new com.github.lgooddatepicker.components.DateTimePicker();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        field_viewBookingDetailsBookingID = new javax.swing.JTextField();
        field_viewBookingDetailsUserID = new javax.swing.JTextField();
        field_viewBookingDetailsItemID = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        button_viewBookingDetailsClose = new javax.swing.JButton();
        panel_login = new javax.swing.JPanel();
        field_loginPassword = new javax.swing.JPasswordField();
        button_login = new javax.swing.JButton();
        label_password = new javax.swing.JLabel();
        field_loginUserID = new javax.swing.JTextField();
        label_username = new javax.swing.JLabel();
        panel_main = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel2 = new javax.swing.JPanel();
        button_logout = new javax.swing.JButton();
        label_loggedInAs = new javax.swing.JLabel();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
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
        separator_item = new javax.swing.JToolBar.Separator();
        button_bookItem = new javax.swing.JButton();
        button_returnItem = new javax.swing.JButton();
        button_viewBookingDetails = new javax.swing.JButton();
        separator_booking = new javax.swing.JToolBar.Separator();
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

        dialog_addItem.setTitle("Add Item");
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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel5.add(field_addItemType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel5.add(combo_addItemLocation, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel6.add(combo_editItemLocation, gridBagConstraints);

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

        dialog_bookItem.setTitle("Book Item");
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

        jLabel25.setText("Book Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel11.add(jLabel25, gridBagConstraints);

        jLabel26.setText("Return Date");
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

        dialog_viewHistory.setTitle("Item History");
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

        dialog_addUser.setTitle("Add Item");
        dialog_addUser.setMinimumSize(new java.awt.Dimension(350, 250));
        dialog_addUser.setModal(true);
        dialog_addUser.setResizable(false);

        jPanel17.setLayout(new java.awt.GridBagLayout());

        jLabel24.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel17.add(jLabel24, gridBagConstraints);

        jLabel28.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel17.add(jLabel28, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel17.add(field_addUserName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel17.add(combo_addUserSecurityLevel, gridBagConstraints);

        jLabel27.setText("ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel17.add(jLabel27, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel17.add(field_addUserID, gridBagConstraints);

        jLabel29.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel17.add(jLabel29, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel17.add(field_addUserPassword, gridBagConstraints);

        jLabel30.setText("Confirm Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel17.add(jLabel30, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel17.add(field_addUserConfirmPassword, gridBagConstraints);

        dialog_addUser.getContentPane().add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel18.setLayout(new java.awt.GridBagLayout());

        button_addUserCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel18.add(button_addUserCancel, gridBagConstraints);

        button_addUserConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel18.add(button_addUserConfirm, gridBagConstraints);

        dialog_addUser.getContentPane().add(jPanel18, java.awt.BorderLayout.PAGE_END);

        dialog_addLocation.setTitle("Add Item");
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

        dialog_editUser.setTitle("Add Item");
        dialog_editUser.setMinimumSize(new java.awt.Dimension(350, 250));
        dialog_editUser.setModal(true);
        dialog_editUser.setResizable(false);

        jPanel19.setLayout(new java.awt.GridBagLayout());

        jLabel31.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(jLabel31, gridBagConstraints);

        jLabel32.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(jLabel32, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel19.add(field_editUserName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        jPanel19.add(combo_editUserSecurityLevel, gridBagConstraints);

        dialog_editUser.getContentPane().add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel20.setLayout(new java.awt.GridBagLayout());

        button_editUserCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel20.add(button_editUserCancel, gridBagConstraints);

        button_editUserConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel20.add(button_editUserConfirm, gridBagConstraints);

        dialog_editUser.getContentPane().add(jPanel20, java.awt.BorderLayout.PAGE_END);

        dialog_viewBookingDetails.setTitle("Booking Details");
        dialog_viewBookingDetails.setMinimumSize(new java.awt.Dimension(400, 250));
        dialog_viewBookingDetails.setResizable(false);

        jPanel21.setLayout(new java.awt.GridBagLayout());

        dateTimePicker_viewBookingDetailsBookedDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanel21.add(dateTimePicker_viewBookingDetailsBookedDate, gridBagConstraints);

        dateTimePicker_viewBookingDetailsReturnDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        jPanel21.add(dateTimePicker_viewBookingDetailsReturnDate, gridBagConstraints);

        jLabel33.setText("Booked Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel21.add(jLabel33, gridBagConstraints);

        jLabel34.setText("Return Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel21.add(jLabel34, gridBagConstraints);

        jLabel35.setText("Booking ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel21.add(jLabel35, gridBagConstraints);

        jLabel36.setText("User ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel21.add(jLabel36, gridBagConstraints);

        jLabel37.setText("Item ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel21.add(jLabel37, gridBagConstraints);

        field_viewBookingDetailsBookingID.setEditable(false);
        field_viewBookingDetailsBookingID.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel21.add(field_viewBookingDetailsBookingID, gridBagConstraints);

        field_viewBookingDetailsUserID.setEditable(false);
        field_viewBookingDetailsUserID.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel21.add(field_viewBookingDetailsUserID, gridBagConstraints);

        field_viewBookingDetailsItemID.setEditable(false);
        field_viewBookingDetailsItemID.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel21.add(field_viewBookingDetailsItemID, gridBagConstraints);

        dialog_viewBookingDetails.getContentPane().add(jPanel21, java.awt.BorderLayout.CENTER);

        jPanel22.setLayout(new java.awt.GridBagLayout());

        button_viewBookingDetailsClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel22.add(button_viewBookingDetailsClose, gridBagConstraints);

        dialog_viewBookingDetails.getContentPane().add(jPanel22, java.awt.BorderLayout.PAGE_END);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Equipment Manager");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(800, 600));
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

        jLayeredPane1.setLayout(new javax.swing.OverlayLayout(jLayeredPane1));

        jPanel2.setFocusable(false);
        jPanel2.setVerifyInputWhenFocusTarget(false);
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        button_logout.setText("Logout");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        jPanel2.add(button_logout, gridBagConstraints);

        label_loggedInAs.setText("Logged in as ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel2.add(label_loggedInAs, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        jPanel2.add(filler7, gridBagConstraints);

        jLayeredPane1.add(jPanel2);

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
        jToolBar1.add(separator_item);

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

        button_viewBookingDetails.setText("View Booking Details");
        button_viewBookingDetails.setFocusable(false);
        button_viewBookingDetails.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_viewBookingDetails.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(button_viewBookingDetails);
        jToolBar1.add(separator_booking);

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
        text_itemID.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemID, gridBagConstraints);

        text_itemName.setEditable(false);
        text_itemName.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemName, gridBagConstraints);

        text_itemLocation.setEditable(false);
        text_itemLocation.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemLocation, gridBagConstraints);

        text_itemStatus.setEditable(false);
        text_itemStatus.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(text_itemStatus, gridBagConstraints);

        text_itemType.setEditable(false);
        text_itemType.setCaretColor(new java.awt.Color(255, 255, 255));
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

        text_itemDescription.setColumns(20);
        text_itemDescription.setEditable(false);
        text_itemDescription.setRows(5);
        text_itemDescription.setCaretColor(new java.awt.Color(255, 255, 255));
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
        text_lastCalibration.setCaretColor(new java.awt.Color(255, 255, 255));
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
        text_locationID.setCaretColor(new java.awt.Color(255, 255, 255));
        text_locationID.setPreferredSize(new java.awt.Dimension(232, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel4.add(text_locationID, gridBagConstraints);

        text_locationName.setEditable(false);
        text_locationName.setCaretColor(new java.awt.Color(255, 255, 255));
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
        text_userID.setCaretColor(new java.awt.Color(255, 255, 255));
        text_userID.setPreferredSize(new java.awt.Dimension(232, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel3.add(text_userID, gridBagConstraints);

        text_userName.setEditable(false);
        text_userName.setCaretColor(new java.awt.Color(255, 255, 255));
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
        text_userSecurityLevel.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(text_userSecurityLevel, gridBagConstraints);

        panel_userDetails.setViewportView(jPanel3);

        panel_userView.add(panel_userDetails, java.awt.BorderLayout.CENTER);

        tab_users.setRightComponent(panel_userView);

        tabs_application.addTab("Users", tab_users);

        jLayeredPane1.add(tabs_application);

        panel_main.add(jLayeredPane1);

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
    private javax.swing.JButton button_addUserCancel;
    private javax.swing.JButton button_addUserConfirm;
    private javax.swing.JButton button_bookItem;
    private javax.swing.JButton button_bookItemCancel;
    private javax.swing.JButton button_bookItemConfirm;
    private javax.swing.JButton button_calibrateItem;
    private javax.swing.JButton button_editItem;
    private javax.swing.JButton button_editItemCancel;
    private javax.swing.JButton button_editItemConfirm;
    private javax.swing.JButton button_editUser;
    private javax.swing.JButton button_editUserCancel;
    private javax.swing.JButton button_editUserConfirm;
    private javax.swing.JButton button_flagForCalibration;
    private javax.swing.JButton button_login;
    private javax.swing.JButton button_logout;
    private javax.swing.JButton button_removeItem;
    private javax.swing.JButton button_removeLocation;
    private javax.swing.JButton button_removeUser;
    private javax.swing.JButton button_returnItem;
    private javax.swing.JButton button_searchItem;
    private javax.swing.JButton button_searchLocation;
    private javax.swing.JButton button_searchUser;
    private javax.swing.JButton button_viewBookingDetails;
    private javax.swing.JButton button_viewBookingDetailsClose;
    private javax.swing.JButton button_viewHistory;
    private javax.swing.JButton button_viewHistoryClose;
    private com.github.lgooddatepicker.components.CalendarPanel calendar_itemBookings;
    private javax.swing.JCheckBox check_needsCalibration;
    private javax.swing.JComboBox<String> combo_addItemLocation;
    private javax.swing.JComboBox<User.SecurityLevels> combo_addUserSecurityLevel;
    private javax.swing.JComboBox<String> combo_editItemLocation;
    private javax.swing.JComboBox<Item.Status> combo_editItemStatus;
    private javax.swing.JComboBox<User.SecurityLevels> combo_editUserSecurityLevel;
    private com.github.lgooddatepicker.components.DateTimePicker dateTimePicker_bookItemBookedDate;
    private com.github.lgooddatepicker.components.DateTimePicker dateTimePicker_bookItemReturnDate;
    private com.github.lgooddatepicker.components.DateTimePicker dateTimePicker_viewBookingDetailsBookedDate;
    private com.github.lgooddatepicker.components.DateTimePicker dateTimePicker_viewBookingDetailsReturnDate;
    private javax.swing.JDialog dialog_addItem;
    private javax.swing.JDialog dialog_addLocation;
    private javax.swing.JDialog dialog_addNote;
    private javax.swing.JDialog dialog_addUser;
    private javax.swing.JDialog dialog_bookItem;
    private javax.swing.JDialog dialog_editItem;
    private javax.swing.JDialog dialog_editUser;
    private javax.swing.JDialog dialog_viewBookingDetails;
    private javax.swing.JDialog dialog_viewHistory;
    private javax.swing.JTextField field_addItemName;
    private javax.swing.JTextField field_addItemType;
    private javax.swing.JTextField field_addLocationName;
    private javax.swing.JTextArea field_addNote;
    private javax.swing.JPasswordField field_addUserConfirmPassword;
    private javax.swing.JTextField field_addUserID;
    private javax.swing.JTextField field_addUserName;
    private javax.swing.JPasswordField field_addUserPassword;
    private javax.swing.JTextArea field_editItemDescription;
    private javax.swing.JTextField field_editItemName;
    private javax.swing.JTextField field_editItemType;
    private javax.swing.JTextField field_editUserName;
    private javax.swing.JPasswordField field_loginPassword;
    private javax.swing.JTextField field_loginUserID;
    private javax.swing.JTextField field_searchItem;
    private javax.swing.JTextField field_searchLocation;
    private javax.swing.JTextField field_searchUser;
    private javax.swing.JTextField field_viewBookingDetailsBookingID;
    private javax.swing.JTextField field_viewBookingDetailsItemID;
    private javax.swing.JTextField field_viewBookingDetailsUserID;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
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
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
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
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel label_loggedInAs;
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
    private javax.swing.JToolBar.Separator separator_booking;
    private javax.swing.JToolBar.Separator separator_item;
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
