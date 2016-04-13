/*
 * Name: Anthony Dombrowski
 * Date: February 26, 2016
 * File: StudentGUI.java
 * Assignment: Project 4
 * Purpose: Creates GUI for Student database program
 */

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StudentGUI extends JFrame {
	// action to be performed selected through combo box
	private String action;
	
	// input fields for id, name, and major
	private JTextField idText = new JTextField();
	private JTextField nameText = new JTextField();
	private JTextField majorText = new JTextField();
	
	// database for the id, Student mappings
	private HashMap<Integer, Student> data = new HashMap<Integer, Student>();
	
	// default constructor
	public StudentGUI() {
		// set up a frame
		super("Student Database");
		setSize(400, 225);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// creates the face and actions of the GUI
		addMainScreenFeatures();
		
		// make frame visible
		setVisible(true);
	} // end default constructor
	
	// adds the features to a panel, then the panel to the gui frame
	private void addMainScreenFeatures() {
		// main panel with all features to add to frame
		JPanel panel = new JPanel(new GridBagLayout());
		
		// labels for input and combo box
		JLabel idLabel = new JLabel("ID:");
		JLabel nameLabel = new JLabel("Name:"); 
		JLabel majorLabel = new JLabel("Major:");
		JLabel selectionLabel = new JLabel("Choose Selection:");
		
		// constraints for adding components to the panel
		GridBagConstraints c = new GridBagConstraints();
		
		// add components to the panel
		c.insets = new Insets(10, 5, 10, 5);
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(idLabel, c);
		
		c.gridy = 1;
		panel.add(nameLabel, c);
		
		c.gridy = 2;
		panel.add(majorLabel, c);
		
		c.gridy = 3;
		panel.add(selectionLabel, c);
		
		c.gridy = 4;
		panel.add(addButton(), c);
		
		c.anchor = GridBagConstraints.LAST_LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.ipadx = 100;
		c.gridy = 0;
		panel.add(idText, c);
		
		c.gridy = 1;
		panel.add(nameText, c);
		
		c.gridy = 2;
		panel.add(majorText, c);
		
		c.gridy = 3;
		
		panel.add(addComboBox(), c);

		// add the panel to the frame
		add(panel, BorderLayout.CENTER);
	} // end addMainScreenFeatures method
	
	// creates process button
	private JButton addButton() {
		// process button
		final JButton process = new JButton("Process Request");
		
		// button calls the action selected from combo box
		process.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// perform the action selected, or default to insert action
				if(action != null) {
					buttonAction(action);
				} else {
					buttonAction("Insert");
				} // end else
			} // end actionPerformed
		}); // end addActionListener
		
		// returns the button
		return process;
	} // end addButton method
	
	// creates combo box with action options
	private JComboBox addComboBox() {
		// actions
		String[] options = { "Insert", "Delete", "Find", "Update" };
		
		// create combo box and add listener
		final JComboBox mainCombo = new JComboBox(options);
		mainCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// turns the selected option into a string: action
				action = (String) mainCombo.getSelectedItem();
			} // end itemStateChanged override
		}); // end addItemListener
		
		// returns the combo box
		return mainCombo;
	} // end addComboBox method
	
	// performs the corresponding action when button is pressed
	private void buttonAction(String action) {
		switch(action.toLowerCase()) {
			case "insert": 
				insert();
				break;
			case "delete":
				delete();
				break;
			case "find":
				find();
				break;
			case "update": 
				update();
				break;
			default: 
				break;
		} // end switch
	} // end buttonAction method
	
	// inserts an id, Student pair into the database
	private void insert() {
		// get Student information from input
		Integer id = getID();
		String name = getStudentName();
		String major = getMajor();
		
		// only perform if id input can be read as a positive integer
		if(id != -1) {
			// add mapping only if the id doesn't exist already as a key
			if(!data.containsKey(id)) {
				// add in student with corresponding id to database
				data.put(id, new Student(name, major));
				
				// show a success message
				JOptionPane.showMessageDialog(null, 
						"Following student was added: \nID#" + id + "\n" + 
						data.get(id).toString(), "ID#" + id + " Added", 
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				// open a message if id number (key) is already in the database
				JOptionPane.showMessageDialog(null, 
						"ID#" + id + " is already in the database.", 
						 "Couldn't Add ID#" + id, 
						JOptionPane.ERROR_MESSAGE);
			} // end else
		} // end first if
	} // end insert method
	
	// removes a Student from the database with corresponding id
	private void delete() {
		// gets id from input
		Integer id = getID();
		
		// only perform if id input can be made into a positive integer
		if(id != -1) {		
			// if the id is found, then remove the id, student mapping
			if(data.containsKey(id)) {
				data.remove(id);
				
				// successful deletion message
				JOptionPane.showMessageDialog(null, 
						"ID#" + id + " removed from the database.", 
						"ID#" + id + " Deleted", 
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				// error message: id couldn't be found
				JOptionPane.showMessageDialog(null,  
						"ID#" + id + " is not in the database.", 
						 "Couldn't Delete ID#" + id, 
						JOptionPane.ERROR_MESSAGE);
			} // end else
		} // end first if
	} // end delete method
	
	// shows a Student's information or error message
	private void find() {
		// get id information from input
		Integer id = getID();
		
		// only run if input can be made into a positive integer
		if(id != -1) {
			// show student's information if one is associated with the id
			if(data.get(id) != null) {
				// create student to show information with toString
				Student student = data.get(id);
				
				// shows student's information
				JOptionPane.showMessageDialog(null, 
						"ID = " + id + "\n" + student.toString(), 
						"ID#" + id + " Found", 
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				// error message if student isn't found
				JOptionPane.showMessageDialog(null, 
						"ID#" + id + " isn't in the database.", 
						"Couldn't Find ID#" + id, JOptionPane.ERROR_MESSAGE);
			} // end else
		} // end first if
	} // end find method
	
	// updates a student's grades
	private void update() {
		// get id from input
		Integer id = getID();
		
		// only act if input can be made into a positive integer
		if(id != -1) {
			// if a student is found with the id supplied update grades
			if(data.get(id) != null) {
				updateStudent(id);
			} else {
				// error message if student can't be found to update
				JOptionPane.showMessageDialog(null, 
						"ID #" + id + " not in database.", 
						"Couldn't Update ID#" + id, JOptionPane.ERROR_MESSAGE);
			} // end if else
		} // end outer if
	} // end update method
	
	// action to perform a successful update 
	private void updateStudent(Integer id) {
		// possible grades and credits for combo boxes
		final String[] grades = { "A", "B", "C", "D", "F" };
		final Integer[] credits = { 3, 6 };
		
		// get the Student from id supplied
		Student student = data.get(id);
		
		// open window to update grade, then save the grade as a string
		String updatedGrade = (String) JOptionPane.showInputDialog(
				null, "Choose grade:", "Update Grade", 
				JOptionPane.QUESTION_MESSAGE, null, grades, grades[0]);
		
		// open window to update credits, then save number of credits as int
		int updatedCredits = (Integer) JOptionPane.showInputDialog(
				null, "Choose Credits:", "Update Credits", 
				JOptionPane.QUESTION_MESSAGE, null, credits, credits[0]);
		
		// update the Student object with new grade and credits
		student.courseCompleted(updatedGrade, updatedCredits);
		
		// shows student's updated information
		JOptionPane.showMessageDialog(null, 
				"ID = " + id + "\n" + student.toString(), 
				"ID#" + id + " Updated", 
				JOptionPane.INFORMATION_MESSAGE);
	} // end updateStudent method
	
	// converts id input to a positive integer, 
	// or opens an error window and returns -1
	private Integer getID() {
		try {
			// tries to convert id input into an Integer
			return Math.abs(Integer.parseInt(idText.getText()));
		} catch(NumberFormatException e) {
			// error message
			JOptionPane.showMessageDialog(null, 
					"Please enter a positive integer for an ID.", 
					"Not A Positive Integer", JOptionPane.ERROR_MESSAGE);
			// return -1 to alert that an error message was displayed
			// trying to convert the id input into a positive integer
			return -1;
		} // end catch
	} // end method
	
	// gets name input
	private String getStudentName() {
		return nameText.getText();
	} // end getStudentName method
	
	// gets major input
	private String getMajor() {
		return majorText.getText();
	} // end getMajor method
	
	// tests the program
	public static void main(String[] args) {
		new StudentGUI();
	} // end main
} // end class
