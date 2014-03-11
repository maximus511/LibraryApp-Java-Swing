package com.db.LibraryApp.UpdateBookCount;

import com.db.LibraryApp.InsertBook.BookFields;
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
 * Date: 12/10/13
 * Time: 10:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class UpdateBookCountScreen extends JPanel {
    public JLabel bookIdLabel;
    public JLabel branchIdLabel;
    public JLabel noOfCopiesLabel;
    public JLabel resultLabel;
    public JTextField bookIdText;
    public JTextField branchIdText;
    public JTextField noOfCopiesText;
    public JButton updateCount;

    String bookId = "";
    String branchId = "";
    Integer numOfCopies = 0;

    public UpdateBookCountScreen() {

        bookIdLabel = new JLabel("Book ID :");
        bookIdLabel.setBounds(100, 20, 100, 20);
        bookIdText = new JTextField(20);
        bookIdText.setBounds(210, 20, 200, 20);

        branchIdLabel = new JLabel("Branch ID :");
        branchIdLabel.setBounds(100, 50, 100, 20);
        branchIdText = new JTextField(20);
        branchIdText.setBounds(210, 50, 200, 20);

        noOfCopiesLabel = new JLabel("No. of new copies added :");
        noOfCopiesLabel.setBounds(40, 80, 150, 20);
        noOfCopiesText = new JTextField(20);
        noOfCopiesText.setBounds(210, 80, 200, 20);

        resultLabel = new JLabel("");
        resultLabel.setBounds(150, 190, 550, 30);
        resultLabel.setForeground(Color.red);
        resultLabel.setFont(new Font("Cambria", Font.BOLD, 15));

        updateCount = new JButton("Update");
        updateCount.setBounds(150, 170, 100, 20);

        updateCount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                bookId = bookIdText.getText();
                branchId = branchIdText.getText();
                try {
                    numOfCopies = Integer.valueOf(noOfCopiesText.getText());
                } catch (NumberFormatException nf) {
                    ValidationMessage.alertMessage("Please enter a valid number for number of copies to be added");
                    noOfCopiesText.requestFocusInWindow();
                    return;
                }

                if (bookId.equals("") || branchId.equals("")) {
                    if (bookId.equals("")) {
                        ValidationMessage.alertMessage("Please enter Book ID");
                        bookIdText.requestFocusInWindow();
                    } else if (branchId.equals("")) {
                        ValidationMessage.alertMessage("Please enter the Branch ID");
                        branchIdText.requestFocusInWindow();
                    }

                } else {

                    BookFields bookFields = new BookFields();
                    bookFields.setBookId(bookId);
                    bookFields.setBranchId(branchId);
                    bookFields.setNumOfCopies(numOfCopies);
                    try {
                        new UpdateBookCountResults().updateBookCount(bookFields);
                        resultLabel.setText("The number of copies has been successfully updated in the system.");
                    } catch (LibraryAppException le) {
                        JOptionPane.showMessageDialog(new JFrame(),
                                le.getMessage());
                    } finally {
                        MySQLConnection.closeConnection();
                    }
                }
            }
        });
        setLayout(null);

        add(bookIdLabel);
        add(bookIdText);
        add(branchIdText);
        add(branchIdLabel);
        add(noOfCopiesLabel);
        add(noOfCopiesText);
        add(updateCount);

        add(resultLabel);

        setVisible(true);
    }
}
