package com.db.LibraryApp.CheckOutBook;

import com.db.LibraryApp.UtilityClasses.LibraryAppException;
import com.db.LibraryApp.UtilityClasses.MySQLConnection;
import com.db.LibraryApp.UtilityClasses.ValidationMessage;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class CheckOutBook extends JPanel {

    public JLabel bookIdLabel;
    public JLabel branchIdLabel;
    public JLabel cardNumberLabel;
    public JTextField bookIdTextField;
    public JTextField branchIdTextField;
    public JTextField cardNumberTextField;
    public JLabel resultLabel;
    public JButton checkOutButton;

    String bookId = "";
    String branchId = "";
    String cardNo = "";

    public CheckOutBook() {

        bookIdLabel = new JLabel("Book ID :");
        bookIdLabel.setBounds(100, 20, 100, 20);
        bookIdTextField = new JTextField(20);
        bookIdTextField.setBounds(210, 20, 200, 20);

        branchIdLabel = new JLabel("Branch ID :");
        branchIdLabel.setBounds(100, 50, 100, 20);
        branchIdTextField = new JTextField(20);
        branchIdTextField.setBounds(210, 50, 200, 20);

        cardNumberLabel = new JLabel("Card Number :");
        cardNumberLabel.setBounds(100, 80, 100, 20);
        cardNumberTextField = new JTextField(20);
        cardNumberTextField.setBounds(210, 80, 200, 20);

        resultLabel = new JLabel("");
        resultLabel.setBounds(150, 190, 450, 30);
        resultLabel.setForeground(Color.red);
        resultLabel.setFont(new Font("Calibri", Font.BOLD, 15));

        checkOutButton = new JButton("Check Out");
        checkOutButton.setBounds(150, 140, 100, 20);

        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultLabel.setText("");
                bookId = bookIdTextField.getText();
                branchId = branchIdTextField.getText();
                cardNo = cardNumberTextField.getText();

                if (bookId.equals("") || branchId.equals("") || cardNo.equals("")) {
                    if (bookId.equals("")) {
                        ValidationMessage.alertMessage("Please enter the Book ID");
                        bookIdTextField.requestFocusInWindow();
                    } else if (branchId.equals("")) {
                        ValidationMessage.alertMessage("Please enter the Branch ID");
                        branchIdTextField.requestFocusInWindow();
                    } else if (cardNo.equals("")) {
                        ValidationMessage.alertMessage("Please enter the Card No");
                        cardNumberTextField.requestFocusInWindow();
                    }

                } else {

                    CheckOutFields checkOutFields = new CheckOutFields();
                    checkOutFields.setCardNumber(cardNo);
                    checkOutFields.setBranchId(branchId);
                    checkOutFields.setBookId(bookId);
                    InsertRecordForCheckOut insertRecordForCheckOut = new InsertRecordForCheckOut();
                    try {
                        insertRecordForCheckOut.checkOutBooks(checkOutFields);
                        resultLabel.setText("The Book has been issued successfully.");
                    } catch (LibraryAppException ex) {
                        JOptionPane.showMessageDialog(new JFrame(),
                                ex.getMessage());
                    } catch (Exception ep) {
                        ep.printStackTrace();
                    } finally {
                        MySQLConnection.closeConnection();
                    }
                }
            }
        });
        setLayout(null);

        add(bookIdLabel);
        add(bookIdTextField);
        add(branchIdLabel);
        add(branchIdTextField);
        add(cardNumberLabel);
        add(cardNumberTextField);
        add(checkOutButton);
        add(resultLabel);
        setVisible(true);
    }
}
