package com.db.LibraryApp.AddBorrower;


import com.db.LibraryApp.UtilityClasses.MySQLConnection;
import com.db.LibraryApp.UtilityClasses.ValidationMessage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class AddBorrower extends JPanel {

    public JLabel borrowerFNameLabel;
    public JLabel borrowerLNameLabel;
    public JLabel addressLabel;
    public JLabel phoneNumberLabel;
    public JTextField borrowerFNameText;
    public JTextField borrowerLNameText;
    public JTextField addressText;
    public JTextField phoneNumberText;
    public JLabel cardNumberLabel;
    public JLabel cardNumber;
    String displayCardNumber = "";
    public JButton addBorrowerButton;

    String firstName = "";
    String lastName = "";
    String address = "";

    public AddBorrower() {

        borrowerFNameLabel = new JLabel("First Name :");
        borrowerFNameLabel.setBounds(100, 20, 100, 20);
        borrowerFNameText = new JTextField(20);
        borrowerFNameText.setBounds(210, 20, 200, 20);

        borrowerLNameLabel = new JLabel("Last Name :");
        borrowerLNameLabel.setBounds(100, 50, 100, 20);
        borrowerLNameText = new JTextField(20);
        borrowerLNameText.setBounds(210, 50, 200, 20);

        addressLabel = new JLabel("Address :");
        addressLabel.setBounds(100, 80, 100, 20);
        addressText = new JTextField(20);
        addressText.setBounds(210, 80, 200, 20);

        phoneNumberLabel = new JLabel("Phone Number :");
        phoneNumberLabel.setBounds(100, 110, 100, 20);
        phoneNumberText = new JTextField(20);
        phoneNumberText.setBounds(210, 110, 200, 20);

        cardNumberLabel = new JLabel("Card No. :");
        cardNumberLabel.setBounds(100, 140, 100, 20);
        cardNumber = new JLabel("");
        cardNumber.setBounds(210, 140, 400, 20);

        addBorrowerButton = new JButton("Add Borrower");
        addBorrowerButton.setBounds(150, 170, 150, 20);
        addBorrowerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                firstName = borrowerFNameText.getText();
                lastName = borrowerLNameText.getText();
                address = addressText.getText();

                if (firstName.equals("") || lastName.equals("") || address.equals("")) {
                    if (firstName.equals("")) {
                        ValidationMessage.alertMessage("Please enter your first name");
                        borrowerFNameText.requestFocusInWindow();
                    } else if (lastName.equals("")) {
                        ValidationMessage.alertMessage("Please enter your last name");
                        borrowerLNameText.requestFocusInWindow();
                    } else if (address.equals("")) {
                        ValidationMessage.alertMessage("Please enter your address");
                        addressText.requestFocusInWindow();
                    }

                } else {

                    BorrowerFields borrowerFields = new BorrowerFields();
                    borrowerFields.setFirstName(firstName);
                    borrowerFields.setLastName(lastName);
                    borrowerFields.setAddress(address);
                    borrowerFields.setPhoneNumber(phoneNumberText.getText());
                    try {
                        String res = new InsertNewBorrower().addBorrower(borrowerFields);
                        if (res.contains("Exists")) {
                            displayCardNumber = "The borrower ID already exists!!";
                            cardNumber.setText(displayCardNumber);
                        } else {
                            cardNumber.setText(res);
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        ValidationMessage.alertMessage(ex.getMessage());
                    } finally {
                        MySQLConnection.closeConnection();
                    }
                }
            }
        });
        setLayout(null);

        add(borrowerFNameLabel);
        add(borrowerFNameText);
        add(borrowerLNameLabel);
        add(borrowerLNameText);
        add(addressLabel);
        add(addressText);
        add(phoneNumberLabel);
        add(phoneNumberText);
        add(cardNumberLabel);
        add(cardNumber);
        add(addBorrowerButton);

        setVisible(true);
    }
}
