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

    // <editor-fold  defaultstate="collapsed" desc="Speakers">
    // Login Speakers
    public Speaker<ActionEvent> login;
    public Speaker<ActionEvent> logout;

    // Item Speakers
    public Speaker<ActionEvent> addItem;
    public Speaker<String> removeItem;
    public Speaker<String> searchForItem;
    public Speaker<String> viewItem;
    public Speaker<String> getEditItemDetails;
    public Speaker<String> editItem;
    public Speaker<String> addNote;
    public Speaker<String> flagItem;
    public Speaker<String> calibrateItem;
    public Speaker<String> viewHistory;

    // Booking Speakers
    public Speaker<String> bookItem;
    public Speaker<String> returnItem;
    public Speaker<String> viewBooking;
    public Speaker<String> viewBookingDetails;

    // User Speakers
    public Speaker<ActionEvent> addUser;
    public Speaker<String> getEditUserDetails;
    public Speaker<String> editUser;
    public Speaker<String> removeUser;
    public Speaker<String> searchForUser;
    public Speaker<String> viewUser;
    // Location Speakers
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
		    break;
		}
	    }
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	initComponents();

	// Initalise GUI Functions
	initialiseLogin();
	initialiseItemsTab();
	initialiseBookingFunctions();
	intialiseUsersTab();
	initialiseLocationsTab();
    }

    private void initialiseLogin() {
	// Login
	login = new Speaker<>();
	button_login.addActionListener((ActionEvent e) -> {
	    login.notifyListeners(e);
	});
	// Logout
	logout = new Speaker<>();
	button_logout.addActionListener((ActionEvent e) -> {
	    logout.notifyListeners(e);
	});
    }

    private void initialiseItemsTab() {
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
		    refreshItemSearch();
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
	button_addNoteCancel.addActionListener((ActionEvent e) -> {
	    dialog_addNote.setVisible(false);
	});
	addNote = new Speaker<>();
	button_addNoteConfirm.addActionListener((ActionEvent e) -> {
	    if (verifyAddNote()) {
		addNote.notifyListeners(currentItemID());
		dialog_addNote.setVisible(false);
	    }
	});
	// Item Flagging
	flagItem = new Speaker<>();
	button_flagForCalibration.addActionListener((ActionEvent e) -> {
	    if (currentItemID() == null || currentItemID().isBlank()) {
		showInvalidEntry("No Item Selected", "No Item has been selected.\nPlease select an Item from the\nitem list on the left.");
	    } else {
		flagItem.notifyListeners(currentItemID());
		viewItem.notifyListeners(currentItemID());
	    }
	});
	// Item Calibration
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
    }

    private void initialiseBookingFunctions() {
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
	button_bookItemCancel.addActionListener((ActionEvent e) -> {
	    dialog_bookItem.setVisible(false);
	});
	bookItem = new Speaker<>();
	button_bookItemConfirm.addActionListener((ActionEvent e) -> {
	    if (verifyAddBooking()) {
		bookItem.notifyListeners(currentItemID());
		viewItem.notifyListeners(currentItemID());
		dialog_bookItem.setVisible(false);
	    }
	});
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
	// Set Calendar
	calendar_itemBookings.getSettings().setVetoPolicy((LocalDate localDate) -> {
	    if (localDate.isBefore(LocalDate.now())) {
		return false;
	    }
	    return true;
	});
    }

    private void initialiseLocationsTab() {
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

    private void intialiseUsersTab() {
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
		refreshUserSearch();
	    }
	});
	// User editing
	getEditUserDetails = new Speaker<>();
	button_editUser.addActionListener((ActionEvent e) -> {
	    if (currentUserID() == null || currentUserID().isBlank()) {
		showInvalidEntry("No User Selected", "No User has been selected.\nPlease select a User from the\nuser list on the left.");
	    } else {
		getEditUserDetails.notifyListeners(currentUserID());
	    }
	});
	button_editUserCancel.addActionListener((ActionEvent e) -> {
	    dialog_editUser.setVisible(false);
	});
	editUser = new Speaker<>();
	button_editUserConfirm.addActionListener((ActionEvent e) -> {
	    if (verifyEditUserDetails()) {
		editUser.notifyListeners(currentUserID());
		viewUser.notifyListeners(currentUserID());
		dialog_editUser.setVisible(false);
	    }
	});
	// User Removal
	removeUser = new Speaker<>();
	button_removeUser.addActionListener((ActionEvent e) -> {
	    if (currentUserID() == null || currentUserID().isBlank()) {
		showInvalidEntry("No User Selected", "No User has been selected.\nPlease select a User from the\nuser list on the left.");
	    } else {
		String userID = currentUserID();
		if (getConfirmation("Remove User", "Are you sure you want remove User " + userID + "?")) {
		    removeUser.notifyListeners(userID);
		    refreshUserSearch();
		    setUserPreview(null);
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
    }

    public void initialSearch() {
	field_searchItem.setText("");
	refreshItemSearch();
	field_searchUser.setText("");
	refreshUserSearch();
	field_searchLocation.setText("");
	refreshLocationSearch();
    }

    // <editor-fold defaultstate="collapsed" desc="Helper Dialogs">
    public void showError(Exception err) {
	JOptionPane.showMessageDialog(this, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInvalidEntry(String title, String message) {
	JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean getConfirmation(String title, String message) {
	return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION) == 0;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Login Functions">
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
	button_viewBookingDetails.setVisible(employee);
	panel_itemBookings.setVisible(employee);
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
	return field_itemDetailsID.getText();
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
	    showInvalidEntry("Invalid Item Type", "The Item type entered is too short.\nItem types must be 1 characters or more.");
	    return false;
	}
	if (field_addItemType.getText().length() > 40) {
	    showInvalidEntry("Invalid Item Type", "The Item type entered is too long.\nItem types must be 40 characters or less.");
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
		field_itemDetailsID.setText("");
		field_itemDetailsName.setText("");
		text_itemDescription.setText("");
		field_itemDetailsLocation.setText("");
		field_itemDetailsStatus.setText("");
		field_itemDetailsType.setText("");
		field_itemDetailsLastCalibration.setText("");
		check_itemDetailsNeedsCalibration.setSelected(false);
	    } else {
		field_itemDetailsID.setText(itemData.getID());
		field_itemDetailsName.setText(itemData.getName());
		text_itemDescription.setText(itemData.getDescription());
		field_itemDetailsLocation.setText(itemData.getLocation());
		field_itemDetailsStatus.setText(itemData.getStatus().toString());
		field_itemDetailsType.setText(itemData.getType());
		field_itemDetailsLastCalibration.setText(itemData.getLastCalibrationFormatted());
		check_itemDetailsNeedsCalibration.setSelected(itemData.getNeedsCalibration());
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

    public boolean verifyAddNote() {
	// Check Note
	if (field_addNote.getText().length() < 1) {
	    showInvalidEntry("Invalid Note", "The note entered is too short.\nNotes must be 1 characters or more.");
	    return false;
	}
	if (field_addNote.getText().length() > 65) {
	    showInvalidEntry("Invalid Note", "The note entered is too long.\nNotes must be 65 characters or less.");
	    return false;
	}
	return true;
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
	calendar_itemBookings.setSelectedDateWithoutShowing(null);
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
	    if (localDate.isBefore(LocalDate.now())) {
		return false;
	    }
	    for (Booking booking : newList) {
		if (!booking.isOutsideBookedDate(localDate)) {
		    return false;
		}
	    }
	    return true;
	});
	dateTimePicker_bookItemBookedDate.datePicker.getSettings().setVetoPolicy((LocalDate localDate) -> {
	    if (localDate.isBefore(LocalDate.now())) {
		return false;
	    }
	    for (Booking booking : newList) {
		if (!booking.isOutsideBookedDate(localDate)) {
		    return false;
		}
	    }
	    return true;
	});
	dateTimePicker_bookItemReturnDate.datePicker.getSettings().setVetoPolicy((LocalDate localDate) -> {
	    if (localDate.isBefore(LocalDate.now())) {
		return false;
	    }
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

    public boolean verifyAddBooking() {
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
	if (bookedDateTime.isAfter(returnDateTime) || bookedDateTime.isEqual(returnDateTime)) {
	    showInvalidEntry("Invalid Booking Range", "A bookings return date must be after its book date.");
	    return false;
	}
	return true;
    }

    public boolean setNewBookingDetails(Booking booking) {
	try {
	    if (verifyAddBooking()) {
		LocalDate bookedDate = dateTimePicker_bookItemBookedDate.datePicker.getDate();
		LocalTime bookedTime = dateTimePicker_bookItemBookedDate.timePicker.getTime();
		LocalDateTime bookedDateTime = bookedDate.atTime(bookedTime);
		LocalDate returnDate = dateTimePicker_bookItemReturnDate.datePicker.getDate();
		LocalTime returnTime = dateTimePicker_bookItemReturnDate.timePicker.getTime();
		LocalDateTime returnDateTime = returnDate.atTime(returnTime);
		booking.setBookingRange(bookedDateTime, returnDateTime);
		return true;
	    }
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
	return field_userDetailsID.getText();
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

    public boolean verifyEditUserDetails() {
	// Check Name
	if (field_editUserName.getText().length() < 1) {
	    showInvalidEntry("Invalid User Name", "The User name entered is too short.\nUser names must be 1 characters or more.");
	    return false;
	}
	if (field_editUserName.getText().length() > 30) {
	    showInvalidEntry("Invalid User Name", "The User name entered is too long.\nUser names must be 30 characters or less.");
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
	    if (userData == null) {
		field_userDetailsID.setText("");
		field_userDetailsName.setText("");
		field_userDetailsSecurityLevel.setText("");
	    } else {
		field_userDetailsID.setText(userData.getID());
		field_userDetailsName.setText(userData.getName());
		field_userDetailsSecurityLevel.setText(userData.getSecurityLevel().toString());
	    }
	} catch (Exception err) {
	    showError(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Location Functions">
    public String currentLocationID() {
	return field_locationDetailsID.getText();
    }

    public String currentLocationName() {
	return field_locationDetailsName.getText();
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
		field_locationDetailsID.setText("");
		field_locationDetailsName.setText("");
	    } else {
		field_locationDetailsID.setText(locationData.getID());
		field_locationDetailsName.setText(locationData.getName());
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

    // <editor-fold defaultstate="collapsed" desc="Auto-Generated Code">
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
        panel_addItemTop = new javax.swing.JPanel();
        label_addItemName = new javax.swing.JLabel();
        label_addItemLocation = new javax.swing.JLabel();
        label_addItemType = new javax.swing.JLabel();
        field_addItemName = new javax.swing.JTextField();
        field_addItemType = new javax.swing.JTextField();
        combo_addItemLocation = new javax.swing.JComboBox<>();
        label_addItemTypeHint1 = new javax.swing.JLabel();
        label_addItemTypeHint2 = new javax.swing.JLabel();
        panel_addItemBottom = new javax.swing.JPanel();
        button_addItemCancel = new javax.swing.JButton();
        button_addItemConfirm = new javax.swing.JButton();
        dialog_editItem = new javax.swing.JDialog();
        panel_editItemTop = new javax.swing.JPanel();
        label_editItemName = new javax.swing.JLabel();
        label_editItemDescription = new javax.swing.JLabel();
        label_editItemLocation = new javax.swing.JLabel();
        label_editItemStatus = new javax.swing.JLabel();
        label_editItemType = new javax.swing.JLabel();
        field_editItemName = new javax.swing.JTextField();
        field_editItemType = new javax.swing.JTextField();
        scrollPane_editItemDescription = new javax.swing.JScrollPane();
        field_editItemDescription = new javax.swing.JTextArea();
        combo_editItemStatus = new javax.swing.JComboBox<>();
        combo_editItemLocation = new javax.swing.JComboBox<>();
        panel_editItemBottom = new javax.swing.JPanel();
        button_editItemCancel = new javax.swing.JButton();
        button_editItemConfirm = new javax.swing.JButton();
        dialog_bookItem = new javax.swing.JDialog();
        panel_bookItemTop = new javax.swing.JPanel();
        dateTimePicker_bookItemBookedDate = new com.github.lgooddatepicker.components.DateTimePicker();
        dateTimePicker_bookItemReturnDate = new com.github.lgooddatepicker.components.DateTimePicker();
        label_bookItemBookDate = new javax.swing.JLabel();
        label_bookItemReturnDate = new javax.swing.JLabel();
        panel_bookItemBottom = new javax.swing.JPanel();
        button_bookItemCancel = new javax.swing.JButton();
        button_bookItemConfirm = new javax.swing.JButton();
        dialog_addNote = new javax.swing.JDialog();
        panel_addNoteTop = new javax.swing.JPanel();
        scrollPane_addNote = new javax.swing.JScrollPane();
        field_addNote = new javax.swing.JTextArea();
        panel_addNoteBottom = new javax.swing.JPanel();
        button_addNoteCancel = new javax.swing.JButton();
        button_addNoteConfirm = new javax.swing.JButton();
        dialog_viewHistory = new javax.swing.JDialog();
        panel_viewHistoryTop = new javax.swing.JPanel();
        scrollPane_viewHistory = new javax.swing.JScrollPane();
        list_viewHistory = new javax.swing.JList<>();
        panel_viewHistoryBottom = new javax.swing.JPanel();
        button_viewHistoryClose = new javax.swing.JButton();
        dialog_addUser = new javax.swing.JDialog();
        panel_addUserTop = new javax.swing.JPanel();
        label_addUserName = new javax.swing.JLabel();
        label_addUserSecurityLevel = new javax.swing.JLabel();
        field_addUserName = new javax.swing.JTextField();
        combo_addUserSecurityLevel = new javax.swing.JComboBox<>();
        label_addUserID = new javax.swing.JLabel();
        field_addUserID = new javax.swing.JTextField();
        label_addUserPassword = new javax.swing.JLabel();
        field_addUserPassword = new javax.swing.JPasswordField();
        label_addUserConfirmPassword = new javax.swing.JLabel();
        field_addUserConfirmPassword = new javax.swing.JPasswordField();
        panel_addUserBottom = new javax.swing.JPanel();
        button_addUserCancel = new javax.swing.JButton();
        button_addUserConfirm = new javax.swing.JButton();
        dialog_addLocation = new javax.swing.JDialog();
        panel_addLocationTop = new javax.swing.JPanel();
        label_addLocationName = new javax.swing.JLabel();
        field_addLocationName = new javax.swing.JTextField();
        panel_addLocationBottom = new javax.swing.JPanel();
        button_addLocationCancel = new javax.swing.JButton();
        button_addLocationConfirm = new javax.swing.JButton();
        dialog_editUser = new javax.swing.JDialog();
        panel_editUserTop = new javax.swing.JPanel();
        label_editUserName = new javax.swing.JLabel();
        label_editUserSecurityLevel = new javax.swing.JLabel();
        field_editUserName = new javax.swing.JTextField();
        combo_editUserSecurityLevel = new javax.swing.JComboBox<>();
        panel_editUserBottom = new javax.swing.JPanel();
        button_editUserCancel = new javax.swing.JButton();
        button_editUserConfirm = new javax.swing.JButton();
        dialog_viewBookingDetails = new javax.swing.JDialog();
        panel_viewBookingDetailsTop = new javax.swing.JPanel();
        label_viewBookingDetailsBookedDate = new javax.swing.JLabel();
        dateTimePicker_viewBookingDetailsBookedDate = new com.github.lgooddatepicker.components.DateTimePicker();
        label_viewBookingDetailsReturnDate = new javax.swing.JLabel();
        dateTimePicker_viewBookingDetailsReturnDate = new com.github.lgooddatepicker.components.DateTimePicker();
        label_viewBookingDetailsBookingID = new javax.swing.JLabel();
        field_viewBookingDetailsBookingID = new javax.swing.JTextField();
        label_viewBookingDetailsUserID = new javax.swing.JLabel();
        field_viewBookingDetailsUserID = new javax.swing.JTextField();
        label_viewBookingDetailsItemID = new javax.swing.JLabel();
        field_viewBookingDetailsItemID = new javax.swing.JTextField();
        panel_viewBookingDetailsBottom = new javax.swing.JPanel();
        button_viewBookingDetailsClose = new javax.swing.JButton();
        panel_login = new javax.swing.JPanel();
        field_loginPassword = new javax.swing.JPasswordField();
        button_login = new javax.swing.JButton();
        label_password = new javax.swing.JLabel();
        field_loginUserID = new javax.swing.JTextField();
        label_username = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(100, 100), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(100, 100), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        panel_main = new javax.swing.JPanel();
        layeredPane_main = new javax.swing.JLayeredPane();
        panel_logout = new javax.swing.JPanel();
        button_logout = new javax.swing.JButton();
        label_loggedInAs = new javax.swing.JLabel();
        filler_logout = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        tabs_application = new javax.swing.JTabbedPane();
        tab_items = new javax.swing.JSplitPane();
        panel_itemSearch = new javax.swing.JPanel();
        field_searchItem = new javax.swing.JTextField();
        button_addItem = new javax.swing.JButton();
        button_searchItem = new javax.swing.JButton();
        scrollPane_itemSearchResults = new javax.swing.JScrollPane();
        list_itemSearchResults = new javax.swing.JList<>();
        panel_itemView = new javax.swing.JPanel();
        toolbar_itemTools = new javax.swing.JToolBar();
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
        scrollPane_itemDetails = new javax.swing.JScrollPane();
        panel_itemDetails = new javax.swing.JPanel();
        label_itemDetailsID = new javax.swing.JLabel();
        label_itemDetailsName = new javax.swing.JLabel();
        label_itemDetailsDescription = new javax.swing.JLabel();
        label_itemDetailsLocation = new javax.swing.JLabel();
        label_itemDetailsStatus = new javax.swing.JLabel();
        label_itemDetailsType = new javax.swing.JLabel();
        field_itemDetailsID = new javax.swing.JTextField();
        field_itemDetailsName = new javax.swing.JTextField();
        field_itemDetailsLocation = new javax.swing.JTextField();
        field_itemDetailsStatus = new javax.swing.JTextField();
        field_itemDetailsType = new javax.swing.JTextField();
        scrollPane_itemDescription = new javax.swing.JScrollPane();
        text_itemDescription = new javax.swing.JTextArea();
        label_itemDetailsLastCalibration = new javax.swing.JLabel();
        field_itemDetailsLastCalibration = new javax.swing.JTextField();
        check_itemDetailsNeedsCalibration = new javax.swing.JCheckBox();
        filler_itemDetailsLabels = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler_itemDetailsFields = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler_itemDetailsExcess = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        panel_itemBookings = new javax.swing.JPanel();
        calendar_itemBookings = new com.github.lgooddatepicker.components.CalendarPanel();
        scrollPane_itemBookings = new javax.swing.JScrollPane();
        list_itemBookings = new javax.swing.JList<>();
        label_itemBookings = new javax.swing.JLabel();
        tab_locations = new javax.swing.JSplitPane();
        panel_locationSearch = new javax.swing.JPanel();
        field_searchLocation = new javax.swing.JTextField();
        button_addLocation = new javax.swing.JButton();
        button_searchLocation = new javax.swing.JButton();
        scrollPane_locationSearchResults = new javax.swing.JScrollPane();
        list_locationSearchResults = new javax.swing.JList<>();
        panel_locationView = new javax.swing.JPanel();
        toolbar_locationTools = new javax.swing.JToolBar();
        button_removeLocation = new javax.swing.JButton();
        scrollPane_locationDetails = new javax.swing.JScrollPane();
        panel_locationDetails = new javax.swing.JPanel();
        label_locationDetailsID = new javax.swing.JLabel();
        label_locationDetailsName = new javax.swing.JLabel();
        field_locationDetailsName = new javax.swing.JTextField();
        label_locationDetailsItemIDs = new javax.swing.JLabel();
        scrollPane_locationItemPreview = new javax.swing.JScrollPane();
        list_locationItemsPreview = new javax.swing.JList<>();
        filler_locationDetailsLabels = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler_locationDetailsFields = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler_locationDetailsExcess = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        field_locationDetailsID = new javax.swing.JTextField();
        tab_users = new javax.swing.JSplitPane();
        panel_userSearch = new javax.swing.JPanel();
        field_searchUser = new javax.swing.JTextField();
        button_addUser = new javax.swing.JButton();
        button_searchUser = new javax.swing.JButton();
        scrollPane_userSearchresults = new javax.swing.JScrollPane();
        list_userSearchResults = new javax.swing.JList<>();
        panel_userView = new javax.swing.JPanel();
        toolbar_userTools = new javax.swing.JToolBar();
        button_editUser = new javax.swing.JButton();
        button_removeUser = new javax.swing.JButton();
        scrollPane_userDetails = new javax.swing.JScrollPane();
        panel_userDetails = new javax.swing.JPanel();
        label_userDetailsID = new javax.swing.JLabel();
        label_userDetailsName = new javax.swing.JLabel();
        label_userDetailsSecurityLevel = new javax.swing.JLabel();
        field_userDetailsID = new javax.swing.JTextField();
        field_userDetailsName = new javax.swing.JTextField();
        field_userDetailsSecurityLevel = new javax.swing.JTextField();
        filler_userDetailsLabels = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        filler_userDetailsFields = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        filler_userDetailsExcess = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

        dialog_addItem.setTitle("Add Item");
        dialog_addItem.setLocation(new java.awt.Point(400, 400));
        dialog_addItem.setMinimumSize(new java.awt.Dimension(300, 200));
        dialog_addItem.setModal(true);
        dialog_addItem.setResizable(false);

        panel_addItemTop.setLayout(new java.awt.GridBagLayout());

        label_addItemName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_addItemTop.add(label_addItemName, gridBagConstraints);

        label_addItemLocation.setText("Location");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_addItemTop.add(label_addItemLocation, gridBagConstraints);

        label_addItemType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_addItemTop.add(label_addItemType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        panel_addItemTop.add(field_addItemName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        panel_addItemTop.add(field_addItemType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        panel_addItemTop.add(combo_addItemLocation, gridBagConstraints);

        label_addItemTypeHint1.setText("Types should be of the form \"example/type\"");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        panel_addItemTop.add(label_addItemTypeHint1, gridBagConstraints);

        label_addItemTypeHint2.setText("to get an item of the form \"ET00001\"");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        panel_addItemTop.add(label_addItemTypeHint2, gridBagConstraints);

        dialog_addItem.getContentPane().add(panel_addItemTop, java.awt.BorderLayout.CENTER);

        panel_addItemBottom.setLayout(new java.awt.GridBagLayout());

        button_addItemCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_addItemBottom.add(button_addItemCancel, gridBagConstraints);

        button_addItemConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_addItemBottom.add(button_addItemConfirm, gridBagConstraints);

        dialog_addItem.getContentPane().add(panel_addItemBottom, java.awt.BorderLayout.PAGE_END);

        dialog_editItem.setTitle("Edit Item");
        dialog_editItem.setMinimumSize(new java.awt.Dimension(400, 300));
        dialog_editItem.setModal(true);

        panel_editItemTop.setLayout(new java.awt.GridBagLayout());

        label_editItemName.setText("Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panel_editItemTop.add(label_editItemName, gridBagConstraints);

        label_editItemDescription.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panel_editItemTop.add(label_editItemDescription, gridBagConstraints);

        label_editItemLocation.setText("Location:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panel_editItemTop.add(label_editItemLocation, gridBagConstraints);

        label_editItemStatus.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panel_editItemTop.add(label_editItemStatus, gridBagConstraints);

        label_editItemType.setText("Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panel_editItemTop.add(label_editItemType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_editItemTop.add(field_editItemName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_editItemTop.add(field_editItemType, gridBagConstraints);

        field_editItemDescription.setColumns(20);
        field_editItemDescription.setRows(5);
        scrollPane_editItemDescription.setViewportView(field_editItemDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_editItemTop.add(scrollPane_editItemDescription, gridBagConstraints);

        combo_editItemStatus.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panel_editItemTop.add(combo_editItemStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        panel_editItemTop.add(combo_editItemLocation, gridBagConstraints);

        dialog_editItem.getContentPane().add(panel_editItemTop, java.awt.BorderLayout.CENTER);

        panel_editItemBottom.setLayout(new java.awt.GridBagLayout());

        button_editItemCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_editItemBottom.add(button_editItemCancel, gridBagConstraints);

        button_editItemConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_editItemBottom.add(button_editItemConfirm, gridBagConstraints);

        dialog_editItem.getContentPane().add(panel_editItemBottom, java.awt.BorderLayout.PAGE_END);

        dialog_bookItem.setTitle("Book Item");
        dialog_bookItem.setMinimumSize(new java.awt.Dimension(400, 200));
        dialog_bookItem.setModal(true);
        dialog_bookItem.setResizable(false);

        panel_bookItemTop.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panel_bookItemTop.add(dateTimePicker_bookItemBookedDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panel_bookItemTop.add(dateTimePicker_bookItemReturnDate, gridBagConstraints);

        label_bookItemBookDate.setText("Book Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_bookItemTop.add(label_bookItemBookDate, gridBagConstraints);

        label_bookItemReturnDate.setText("Return Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_bookItemTop.add(label_bookItemReturnDate, gridBagConstraints);

        dialog_bookItem.getContentPane().add(panel_bookItemTop, java.awt.BorderLayout.CENTER);

        panel_bookItemBottom.setLayout(new java.awt.GridBagLayout());

        button_bookItemCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_bookItemBottom.add(button_bookItemCancel, gridBagConstraints);

        button_bookItemConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_bookItemBottom.add(button_bookItemConfirm, gridBagConstraints);

        dialog_bookItem.getContentPane().add(panel_bookItemBottom, java.awt.BorderLayout.PAGE_END);

        dialog_addNote.setTitle("Add Note");
        dialog_addNote.setMinimumSize(new java.awt.Dimension(400, 300));
        dialog_addNote.setModal(true);

        panel_addNoteTop.setLayout(new java.awt.GridBagLayout());

        field_addNote.setColumns(20);
        field_addNote.setRows(5);
        scrollPane_addNote.setViewportView(field_addNote);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_addNoteTop.add(scrollPane_addNote, gridBagConstraints);

        dialog_addNote.getContentPane().add(panel_addNoteTop, java.awt.BorderLayout.CENTER);

        panel_addNoteBottom.setLayout(new java.awt.GridBagLayout());

        button_addNoteCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_addNoteBottom.add(button_addNoteCancel, gridBagConstraints);

        button_addNoteConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_addNoteBottom.add(button_addNoteConfirm, gridBagConstraints);

        dialog_addNote.getContentPane().add(panel_addNoteBottom, java.awt.BorderLayout.PAGE_END);

        dialog_viewHistory.setTitle("Item History");
        dialog_viewHistory.setMinimumSize(new java.awt.Dimension(400, 200));
        dialog_viewHistory.setModal(true);

        panel_viewHistoryTop.setLayout(new java.awt.GridBagLayout());

        list_viewHistory.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollPane_viewHistory.setViewportView(list_viewHistory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_viewHistoryTop.add(scrollPane_viewHistory, gridBagConstraints);

        dialog_viewHistory.getContentPane().add(panel_viewHistoryTop, java.awt.BorderLayout.CENTER);

        panel_viewHistoryBottom.setLayout(new java.awt.GridBagLayout());

        button_viewHistoryClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_viewHistoryBottom.add(button_viewHistoryClose, gridBagConstraints);

        dialog_viewHistory.getContentPane().add(panel_viewHistoryBottom, java.awt.BorderLayout.PAGE_END);

        dialog_addUser.setTitle("Add User");
        dialog_addUser.setMinimumSize(new java.awt.Dimension(350, 250));
        dialog_addUser.setModal(true);
        dialog_addUser.setResizable(false);

        panel_addUserTop.setLayout(new java.awt.GridBagLayout());

        label_addUserName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_addUserTop.add(label_addUserName, gridBagConstraints);

        label_addUserSecurityLevel.setText("Security Level");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_addUserTop.add(label_addUserSecurityLevel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        panel_addUserTop.add(field_addUserName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        panel_addUserTop.add(combo_addUserSecurityLevel, gridBagConstraints);

        label_addUserID.setText("ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_addUserTop.add(label_addUserID, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        panel_addUserTop.add(field_addUserID, gridBagConstraints);

        label_addUserPassword.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_addUserTop.add(label_addUserPassword, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        panel_addUserTop.add(field_addUserPassword, gridBagConstraints);

        label_addUserConfirmPassword.setText("Confirm Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_addUserTop.add(label_addUserConfirmPassword, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        panel_addUserTop.add(field_addUserConfirmPassword, gridBagConstraints);

        dialog_addUser.getContentPane().add(panel_addUserTop, java.awt.BorderLayout.CENTER);

        panel_addUserBottom.setLayout(new java.awt.GridBagLayout());

        button_addUserCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_addUserBottom.add(button_addUserCancel, gridBagConstraints);

        button_addUserConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_addUserBottom.add(button_addUserConfirm, gridBagConstraints);

        dialog_addUser.getContentPane().add(panel_addUserBottom, java.awt.BorderLayout.PAGE_END);

        dialog_addLocation.setTitle("Add Location");
        dialog_addLocation.setMinimumSize(new java.awt.Dimension(300, 200));
        dialog_addLocation.setModal(true);
        dialog_addLocation.setResizable(false);

        panel_addLocationTop.setLayout(new java.awt.GridBagLayout());

        label_addLocationName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_addLocationTop.add(label_addLocationName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        panel_addLocationTop.add(field_addLocationName, gridBagConstraints);

        dialog_addLocation.getContentPane().add(panel_addLocationTop, java.awt.BorderLayout.CENTER);

        panel_addLocationBottom.setLayout(new java.awt.GridBagLayout());

        button_addLocationCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_addLocationBottom.add(button_addLocationCancel, gridBagConstraints);

        button_addLocationConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_addLocationBottom.add(button_addLocationConfirm, gridBagConstraints);

        dialog_addLocation.getContentPane().add(panel_addLocationBottom, java.awt.BorderLayout.PAGE_END);

        dialog_editUser.setTitle("Add Item");
        dialog_editUser.setMinimumSize(new java.awt.Dimension(350, 250));
        dialog_editUser.setModal(true);
        dialog_editUser.setResizable(false);

        panel_editUserTop.setLayout(new java.awt.GridBagLayout());

        label_editUserName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_editUserTop.add(label_editUserName, gridBagConstraints);

        label_editUserSecurityLevel.setText("Security Level");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_editUserTop.add(label_editUserSecurityLevel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        panel_editUserTop.add(field_editUserName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        panel_editUserTop.add(combo_editUserSecurityLevel, gridBagConstraints);

        dialog_editUser.getContentPane().add(panel_editUserTop, java.awt.BorderLayout.CENTER);

        panel_editUserBottom.setLayout(new java.awt.GridBagLayout());

        button_editUserCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_editUserBottom.add(button_editUserCancel, gridBagConstraints);

        button_editUserConfirm.setText("Confirm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_editUserBottom.add(button_editUserConfirm, gridBagConstraints);

        dialog_editUser.getContentPane().add(panel_editUserBottom, java.awt.BorderLayout.PAGE_END);

        dialog_viewBookingDetails.setTitle("Booking Details");
        dialog_viewBookingDetails.setMinimumSize(new java.awt.Dimension(400, 250));
        dialog_viewBookingDetails.setResizable(false);

        panel_viewBookingDetailsTop.setLayout(new java.awt.GridBagLayout());

        label_viewBookingDetailsBookedDate.setText("Booked Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_viewBookingDetailsTop.add(label_viewBookingDetailsBookedDate, gridBagConstraints);

        dateTimePicker_viewBookingDetailsBookedDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panel_viewBookingDetailsTop.add(dateTimePicker_viewBookingDetailsBookedDate, gridBagConstraints);

        label_viewBookingDetailsReturnDate.setText("Return Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_viewBookingDetailsTop.add(label_viewBookingDetailsReturnDate, gridBagConstraints);

        dateTimePicker_viewBookingDetailsReturnDate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        panel_viewBookingDetailsTop.add(dateTimePicker_viewBookingDetailsReturnDate, gridBagConstraints);

        label_viewBookingDetailsBookingID.setText("Booking ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_viewBookingDetailsTop.add(label_viewBookingDetailsBookingID, gridBagConstraints);

        field_viewBookingDetailsBookingID.setEditable(false);
        field_viewBookingDetailsBookingID.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panel_viewBookingDetailsTop.add(field_viewBookingDetailsBookingID, gridBagConstraints);

        label_viewBookingDetailsUserID.setText("User ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_viewBookingDetailsTop.add(label_viewBookingDetailsUserID, gridBagConstraints);

        field_viewBookingDetailsUserID.setEditable(false);
        field_viewBookingDetailsUserID.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panel_viewBookingDetailsTop.add(field_viewBookingDetailsUserID, gridBagConstraints);

        label_viewBookingDetailsItemID.setText("Item ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panel_viewBookingDetailsTop.add(label_viewBookingDetailsItemID, gridBagConstraints);

        field_viewBookingDetailsItemID.setEditable(false);
        field_viewBookingDetailsItemID.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panel_viewBookingDetailsTop.add(field_viewBookingDetailsItemID, gridBagConstraints);

        dialog_viewBookingDetails.getContentPane().add(panel_viewBookingDetailsTop, java.awt.BorderLayout.CENTER);

        panel_viewBookingDetailsBottom.setLayout(new java.awt.GridBagLayout());

        button_viewBookingDetailsClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panel_viewBookingDetailsBottom.add(button_viewBookingDetailsClose, gridBagConstraints);

        dialog_viewBookingDetails.getContentPane().add(panel_viewBookingDetailsBottom, java.awt.BorderLayout.PAGE_END);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Equipment Manager");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(950, 600));
        setName("applicationFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1280, 750));
        getContentPane().setLayout(new java.awt.CardLayout());

        panel_login.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panel_login.add(field_loginPassword, gridBagConstraints);

        button_login.setText("Login");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(13, 0, 13, 0);
        panel_login.add(button_login, gridBagConstraints);

        label_password.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        panel_login.add(label_password, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panel_login.add(field_loginUserID, gridBagConstraints);

        label_username.setText("User ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        panel_login.add(label_username, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        panel_login.add(filler1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 0.1;
        panel_login.add(filler2, gridBagConstraints);

        getContentPane().add(panel_login, "card3");

        panel_main.setLayout(new javax.swing.BoxLayout(panel_main, javax.swing.BoxLayout.LINE_AXIS));

        layeredPane_main.setLayout(new javax.swing.OverlayLayout(layeredPane_main));

        panel_logout.setDoubleBuffered(false);
        panel_logout.setFocusable(false);
        panel_logout.setRequestFocusEnabled(false);
        panel_logout.setVerifyInputWhenFocusTarget(false);
        panel_logout.setOpaque(false);
        panel_logout.setLayout(new java.awt.GridBagLayout());

        button_logout.setText("Logout");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        panel_logout.add(button_logout, gridBagConstraints);

        label_loggedInAs.setText("Logged in as ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panel_logout.add(label_loggedInAs, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_logout.add(filler_logout, gridBagConstraints);

        layeredPane_main.add(panel_logout);

        tab_items.setDividerSize(0);
        tab_items.setEnabled(false);

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
        scrollPane_itemSearchResults.setViewportView(list_itemSearchResults);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_itemSearch.add(scrollPane_itemSearchResults, gridBagConstraints);

        tab_items.setLeftComponent(panel_itemSearch);

        panel_itemView.setLayout(new java.awt.BorderLayout());

        toolbar_itemTools.setFloatable(false);
        toolbar_itemTools.setRollover(true);

        button_editItem.setText("Edit Item");
        button_editItem.setFocusable(false);
        button_editItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_editItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_itemTools.add(button_editItem);

        button_removeItem.setText("Remove Item");
        button_removeItem.setFocusable(false);
        button_removeItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_removeItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_itemTools.add(button_removeItem);
        toolbar_itemTools.add(separator_item);

        button_bookItem.setText("Book Item");
        button_bookItem.setFocusable(false);
        button_bookItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_bookItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_itemTools.add(button_bookItem);

        button_returnItem.setText("Return Item");
        button_returnItem.setFocusable(false);
        button_returnItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_returnItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_itemTools.add(button_returnItem);

        button_viewBookingDetails.setText("View Booking Details");
        button_viewBookingDetails.setFocusable(false);
        button_viewBookingDetails.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_viewBookingDetails.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_itemTools.add(button_viewBookingDetails);
        toolbar_itemTools.add(separator_booking);

        button_addNote.setText("Add Note");
        button_addNote.setFocusable(false);
        button_addNote.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_addNote.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_itemTools.add(button_addNote);

        button_flagForCalibration.setText("Flag for Calibration");
        button_flagForCalibration.setFocusable(false);
        button_flagForCalibration.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_flagForCalibration.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_itemTools.add(button_flagForCalibration);

        button_calibrateItem.setText("Calibrate Item");
        button_calibrateItem.setFocusable(false);
        button_calibrateItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_calibrateItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_itemTools.add(button_calibrateItem);

        button_viewHistory.setText("View History");
        button_viewHistory.setFocusable(false);
        button_viewHistory.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_viewHistory.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolbar_itemTools.add(button_viewHistory);

        panel_itemView.add(toolbar_itemTools, java.awt.BorderLayout.PAGE_END);

        scrollPane_itemDetails.setMinimumSize(new java.awt.Dimension(321, 16));

        panel_itemDetails.setMinimumSize(new java.awt.Dimension(310, 100));
        panel_itemDetails.setLayout(new java.awt.GridBagLayout());

        label_itemDetailsID.setText("ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panel_itemDetails.add(label_itemDetailsID, gridBagConstraints);

        label_itemDetailsName.setText("Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panel_itemDetails.add(label_itemDetailsName, gridBagConstraints);

        label_itemDetailsDescription.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        panel_itemDetails.add(label_itemDetailsDescription, gridBagConstraints);

        label_itemDetailsLocation.setText("Location:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panel_itemDetails.add(label_itemDetailsLocation, gridBagConstraints);

        label_itemDetailsStatus.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panel_itemDetails.add(label_itemDetailsStatus, gridBagConstraints);

        label_itemDetailsType.setText("Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panel_itemDetails.add(label_itemDetailsType, gridBagConstraints);

        field_itemDetailsID.setEditable(false);
        field_itemDetailsID.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_itemDetails.add(field_itemDetailsID, gridBagConstraints);

        field_itemDetailsName.setEditable(false);
        field_itemDetailsName.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_itemDetails.add(field_itemDetailsName, gridBagConstraints);

        field_itemDetailsLocation.setEditable(false);
        field_itemDetailsLocation.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_itemDetails.add(field_itemDetailsLocation, gridBagConstraints);

        field_itemDetailsStatus.setEditable(false);
        field_itemDetailsStatus.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_itemDetails.add(field_itemDetailsStatus, gridBagConstraints);

        field_itemDetailsType.setEditable(false);
        field_itemDetailsType.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_itemDetails.add(field_itemDetailsType, gridBagConstraints);

        text_itemDescription.setColumns(20);
        text_itemDescription.setEditable(false);
        text_itemDescription.setRows(5);
        text_itemDescription.setCaretColor(new java.awt.Color(255, 255, 255));
        scrollPane_itemDescription.setViewportView(text_itemDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_itemDetails.add(scrollPane_itemDescription, gridBagConstraints);

        label_itemDetailsLastCalibration.setText("Last Calibration:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panel_itemDetails.add(label_itemDetailsLastCalibration, gridBagConstraints);

        field_itemDetailsLastCalibration.setEditable(false);
        field_itemDetailsLastCalibration.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_itemDetails.add(field_itemDetailsLastCalibration, gridBagConstraints);

        check_itemDetailsNeedsCalibration.setText("Needs Calibration");
        check_itemDetailsNeedsCalibration.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_itemDetails.add(check_itemDetailsNeedsCalibration, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        panel_itemDetails.add(filler_itemDetailsLabels, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 275;
        panel_itemDetails.add(filler_itemDetailsFields, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_itemDetails.add(filler_itemDetailsExcess, gridBagConstraints);

        scrollPane_itemDetails.setViewportView(panel_itemDetails);

        panel_itemView.add(scrollPane_itemDetails, java.awt.BorderLayout.CENTER);

        panel_itemBookings.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panel_itemBookings.add(calendar_itemBookings, gridBagConstraints);

        scrollPane_itemBookings.setViewportView(list_itemBookings);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        panel_itemBookings.add(scrollPane_itemBookings, gridBagConstraints);

        label_itemBookings.setText("Bookings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 7);
        panel_itemBookings.add(label_itemBookings, gridBagConstraints);

        panel_itemView.add(panel_itemBookings, java.awt.BorderLayout.LINE_END);

        tab_items.setRightComponent(panel_itemView);

        tabs_application.addTab("Items", tab_items);

        tab_locations.setDividerSize(0);
        tab_locations.setEnabled(false);

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
        scrollPane_locationSearchResults.setViewportView(list_locationSearchResults);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_locationSearch.add(scrollPane_locationSearchResults, gridBagConstraints);

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

        scrollPane_locationDetails.setMinimumSize(new java.awt.Dimension(321, 16));

        panel_locationDetails.setMinimumSize(new java.awt.Dimension(310, 78));
        panel_locationDetails.setLayout(new java.awt.GridBagLayout());

        label_locationDetailsID.setText("ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panel_locationDetails.add(label_locationDetailsID, gridBagConstraints);

        label_locationDetailsName.setText("Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panel_locationDetails.add(label_locationDetailsName, gridBagConstraints);

        field_locationDetailsName.setEditable(false);
        field_locationDetailsName.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_locationDetails.add(field_locationDetailsName, gridBagConstraints);

        label_locationDetailsItemIDs.setText("Item IDs:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        panel_locationDetails.add(label_locationDetailsItemIDs, gridBagConstraints);

        list_locationItemsPreview.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollPane_locationItemPreview.setViewportView(list_locationItemsPreview);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        panel_locationDetails.add(scrollPane_locationItemPreview, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        panel_locationDetails.add(filler_locationDetailsLabels, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 275;
        panel_locationDetails.add(filler_locationDetailsFields, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_locationDetails.add(filler_locationDetailsExcess, gridBagConstraints);

        field_locationDetailsID.setEditable(false);
        field_locationDetailsID.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panel_locationDetails.add(field_locationDetailsID, gridBagConstraints);

        scrollPane_locationDetails.setViewportView(panel_locationDetails);

        panel_locationView.add(scrollPane_locationDetails, java.awt.BorderLayout.CENTER);

        tab_locations.setRightComponent(panel_locationView);

        tabs_application.addTab("Locations", tab_locations);

        tab_users.setDividerSize(0);
        tab_users.setEnabled(false);

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
        scrollPane_userSearchresults.setViewportView(list_userSearchResults);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_userSearch.add(scrollPane_userSearchresults, gridBagConstraints);

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

        scrollPane_userDetails.setMinimumSize(new java.awt.Dimension(321, 16));

        panel_userDetails.setMinimumSize(new java.awt.Dimension(310, 66));
        panel_userDetails.setLayout(new java.awt.GridBagLayout());

        label_userDetailsID.setText("ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panel_userDetails.add(label_userDetailsID, gridBagConstraints);

        label_userDetailsName.setText("Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panel_userDetails.add(label_userDetailsName, gridBagConstraints);

        label_userDetailsSecurityLevel.setText("Security Level:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panel_userDetails.add(label_userDetailsSecurityLevel, gridBagConstraints);

        field_userDetailsID.setEditable(false);
        field_userDetailsID.setCaretColor(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_userDetails.add(field_userDetailsID, gridBagConstraints);

        field_userDetailsName.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panel_userDetails.add(field_userDetailsName, gridBagConstraints);

        field_userDetailsSecurityLevel.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panel_userDetails.add(field_userDetailsSecurityLevel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        panel_userDetails.add(filler_userDetailsLabels, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 275;
        panel_userDetails.add(filler_userDetailsFields, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        panel_userDetails.add(filler_userDetailsExcess, gridBagConstraints);

        scrollPane_userDetails.setViewportView(panel_userDetails);

        panel_userView.add(scrollPane_userDetails, java.awt.BorderLayout.CENTER);

        tab_users.setRightComponent(panel_userView);

        tabs_application.addTab("Users", tab_users);

        layeredPane_main.add(tabs_application);

        panel_main.add(layeredPane_main);

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
    private javax.swing.JCheckBox check_itemDetailsNeedsCalibration;
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
    private javax.swing.JTextField field_itemDetailsID;
    private javax.swing.JTextField field_itemDetailsLastCalibration;
    private javax.swing.JTextField field_itemDetailsLocation;
    private javax.swing.JTextField field_itemDetailsName;
    private javax.swing.JTextField field_itemDetailsStatus;
    private javax.swing.JTextField field_itemDetailsType;
    private javax.swing.JTextField field_locationDetailsID;
    private javax.swing.JTextField field_locationDetailsName;
    private javax.swing.JPasswordField field_loginPassword;
    private javax.swing.JTextField field_loginUserID;
    private javax.swing.JTextField field_searchItem;
    private javax.swing.JTextField field_searchLocation;
    private javax.swing.JTextField field_searchUser;
    private javax.swing.JTextField field_userDetailsID;
    private javax.swing.JTextField field_userDetailsName;
    private javax.swing.JTextField field_userDetailsSecurityLevel;
    private javax.swing.JTextField field_viewBookingDetailsBookingID;
    private javax.swing.JTextField field_viewBookingDetailsItemID;
    private javax.swing.JTextField field_viewBookingDetailsUserID;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler_itemDetailsExcess;
    private javax.swing.Box.Filler filler_itemDetailsFields;
    private javax.swing.Box.Filler filler_itemDetailsLabels;
    private javax.swing.Box.Filler filler_locationDetailsExcess;
    private javax.swing.Box.Filler filler_locationDetailsFields;
    private javax.swing.Box.Filler filler_locationDetailsLabels;
    private javax.swing.Box.Filler filler_logout;
    private javax.swing.Box.Filler filler_userDetailsExcess;
    private javax.swing.Box.Filler filler_userDetailsFields;
    private javax.swing.Box.Filler filler_userDetailsLabels;
    private javax.swing.JLabel label_addItemLocation;
    private javax.swing.JLabel label_addItemName;
    private javax.swing.JLabel label_addItemType;
    private javax.swing.JLabel label_addItemTypeHint1;
    private javax.swing.JLabel label_addItemTypeHint2;
    private javax.swing.JLabel label_addLocationName;
    private javax.swing.JLabel label_addUserConfirmPassword;
    private javax.swing.JLabel label_addUserID;
    private javax.swing.JLabel label_addUserName;
    private javax.swing.JLabel label_addUserPassword;
    private javax.swing.JLabel label_addUserSecurityLevel;
    private javax.swing.JLabel label_bookItemBookDate;
    private javax.swing.JLabel label_bookItemReturnDate;
    private javax.swing.JLabel label_editItemDescription;
    private javax.swing.JLabel label_editItemLocation;
    private javax.swing.JLabel label_editItemName;
    private javax.swing.JLabel label_editItemStatus;
    private javax.swing.JLabel label_editItemType;
    private javax.swing.JLabel label_editUserName;
    private javax.swing.JLabel label_editUserSecurityLevel;
    private javax.swing.JLabel label_itemBookings;
    private javax.swing.JLabel label_itemDetailsDescription;
    private javax.swing.JLabel label_itemDetailsID;
    private javax.swing.JLabel label_itemDetailsLastCalibration;
    private javax.swing.JLabel label_itemDetailsLocation;
    private javax.swing.JLabel label_itemDetailsName;
    private javax.swing.JLabel label_itemDetailsStatus;
    private javax.swing.JLabel label_itemDetailsType;
    private javax.swing.JLabel label_locationDetailsID;
    private javax.swing.JLabel label_locationDetailsItemIDs;
    private javax.swing.JLabel label_locationDetailsName;
    private javax.swing.JLabel label_loggedInAs;
    private javax.swing.JLabel label_password;
    private javax.swing.JLabel label_userDetailsID;
    private javax.swing.JLabel label_userDetailsName;
    private javax.swing.JLabel label_userDetailsSecurityLevel;
    private javax.swing.JLabel label_username;
    private javax.swing.JLabel label_viewBookingDetailsBookedDate;
    private javax.swing.JLabel label_viewBookingDetailsBookingID;
    private javax.swing.JLabel label_viewBookingDetailsItemID;
    private javax.swing.JLabel label_viewBookingDetailsReturnDate;
    private javax.swing.JLabel label_viewBookingDetailsUserID;
    private javax.swing.JLayeredPane layeredPane_main;
    private javax.swing.JList<String> list_itemBookings;
    private javax.swing.JList<String> list_itemSearchResults;
    private javax.swing.JList<String> list_locationItemsPreview;
    private javax.swing.JList<String> list_locationSearchResults;
    private javax.swing.JList<String> list_userSearchResults;
    private javax.swing.JList<String> list_viewHistory;
    private javax.swing.JPanel panel_addItemBottom;
    private javax.swing.JPanel panel_addItemTop;
    private javax.swing.JPanel panel_addLocationBottom;
    private javax.swing.JPanel panel_addLocationTop;
    private javax.swing.JPanel panel_addNoteBottom;
    private javax.swing.JPanel panel_addNoteTop;
    private javax.swing.JPanel panel_addUserBottom;
    private javax.swing.JPanel panel_addUserTop;
    private javax.swing.JPanel panel_bookItemBottom;
    private javax.swing.JPanel panel_bookItemTop;
    private javax.swing.JPanel panel_editItemBottom;
    private javax.swing.JPanel panel_editItemTop;
    private javax.swing.JPanel panel_editUserBottom;
    private javax.swing.JPanel panel_editUserTop;
    private javax.swing.JPanel panel_itemBookings;
    private javax.swing.JPanel panel_itemDetails;
    private javax.swing.JPanel panel_itemSearch;
    private javax.swing.JPanel panel_itemView;
    private javax.swing.JPanel panel_locationDetails;
    private javax.swing.JPanel panel_locationSearch;
    private javax.swing.JPanel panel_locationView;
    private javax.swing.JPanel panel_login;
    private javax.swing.JPanel panel_logout;
    private javax.swing.JPanel panel_main;
    private javax.swing.JPanel panel_userDetails;
    private javax.swing.JPanel panel_userSearch;
    private javax.swing.JPanel panel_userView;
    private javax.swing.JPanel panel_viewBookingDetailsBottom;
    private javax.swing.JPanel panel_viewBookingDetailsTop;
    private javax.swing.JPanel panel_viewHistoryBottom;
    private javax.swing.JPanel panel_viewHistoryTop;
    private javax.swing.JScrollPane scrollPane_addNote;
    private javax.swing.JScrollPane scrollPane_editItemDescription;
    private javax.swing.JScrollPane scrollPane_itemBookings;
    private javax.swing.JScrollPane scrollPane_itemDescription;
    private javax.swing.JScrollPane scrollPane_itemDetails;
    private javax.swing.JScrollPane scrollPane_itemSearchResults;
    private javax.swing.JScrollPane scrollPane_locationDetails;
    private javax.swing.JScrollPane scrollPane_locationItemPreview;
    private javax.swing.JScrollPane scrollPane_locationSearchResults;
    private javax.swing.JScrollPane scrollPane_userDetails;
    private javax.swing.JScrollPane scrollPane_userSearchresults;
    private javax.swing.JScrollPane scrollPane_viewHistory;
    private javax.swing.JToolBar.Separator separator_booking;
    private javax.swing.JToolBar.Separator separator_item;
    private javax.swing.JSplitPane tab_items;
    private javax.swing.JSplitPane tab_locations;
    private javax.swing.JSplitPane tab_users;
    private javax.swing.JTabbedPane tabs_application;
    private javax.swing.JTextArea text_itemDescription;
    private javax.swing.JToolBar toolbar_itemTools;
    private javax.swing.JToolBar toolbar_locationTools;
    private javax.swing.JToolBar toolbar_userTools;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}
