package com.db.LibraryApp.InsertBook;

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
public class InsertBook extends JPanel {
    public JLabel bookIdLabel;
    public JLabel titleLabel;
    public JLabel authorNameLabel;
    public JLabel branchIdLabel;
    public JLabel noOfCopiesLabel;
    public JLabel resultLabel;
    public JTextField bookIdText;
    public JTextField titleText;
    public JTextField authorNameText;
    public JTextField branchIdText;
    public JTextField noOfCopiesText;
    public JButton addBook;

    String bookId = "";
    String authorNames = "";
    String title = "";
    String branchId = "";
    Integer numOfCopies = 0;

    public InsertBook() {

        bookIdLabel = new JLabel("Book ID :");
        bookIdLabel.setBounds(100, 20, 100, 20);
        bookIdText = new JTextField(20);
        bookIdText.setBounds(210, 20, 200, 20);

        titleLabel = new JLabel("Title :");
        titleLabel.setBounds(100, 50, 100, 20);
        titleText = new JTextField(20);
        titleText.setBounds(210, 50, 200, 20);

        authorNameLabel = new JLabel("Author Name(s) :");
        authorNameLabel.setBounds(100, 80, 100, 20);
        authorNameText = new JTextField(20);
        authorNameText.setBounds(210, 80, 200, 20);
        authorNameText.setToolTipText("Add multiple Author names separated by \',\'");

        branchIdLabel = new JLabel("Branch ID :");
        branchIdLabel.setBounds(100, 110, 100, 20);
        branchIdText = new JTextField(20);
        branchIdText.setBounds(210, 110, 200, 20);

        noOfCopiesLabel = new JLabel("No. of Copies :");
        noOfCopiesLabel.setBounds(100, 140, 100, 20);
        noOfCopiesText = new JTextField(20);
        noOfCopiesText.setBounds(210, 140, 100, 20);
        noOfCopiesText.setToolTipText("Blank value will be treated as 0 copies");

        resultLabel = new JLabel("");
        resultLabel.setBounds(150, 190, 450, 30);
        resultLabel.setForeground(Color.red);
        resultLabel.setFont(new Font("Cambria", Font.BOLD, 15));

        addBook = new JButton("Add Book");
        addBook.setBounds(150, 170, 100, 20);

        addBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                bookId = bookIdText.getText();
                authorNames = authorNameText.getText();
                title = titleText.getText();
                branchId = branchIdText.getText();
                try {
                    numOfCopies = !"".equals(noOfCopiesText.getText()) ? Integer.valueOf(noOfCopiesText.getText()) : 0;
                } catch (NumberFormatException nf) {
                    ValidationMessage.alertMessage("Please enter a valid number for number of copies");
                    noOfCopiesText.requestFocusInWindow();
                    return;
                }

                if (bookId.equals("") || authorNames.equals("") || title.equals("") || branchId.equals("")) {
                    if (bookId.equals("")) {
                        ValidationMessage.alertMessage("Please enter Book ID");
                        bookIdText.requestFocusInWindow();
                    } else if (authorNames.equals("")) {
                        ValidationMessage.alertMessage("Please enter the author name(s)");
                        authorNameText.requestFocusInWindow();
                    } else if (title.equals("")) {
                        ValidationMessage.alertMessage("Please enter the title");
                        titleText.requestFocusInWindow();
                    } else if (branchId.equals("")) {
                        ValidationMessage.alertMessage("Please enter the Branch ID");
                        branchIdText.requestFocusInWindow();
                    }

                } else {

                    BookFields bookFields = new BookFields();
                    bookFields.setBookId(bookId);
                    bookFields.setBranchId(branchId);
                    bookFields.setTitle(title);
                    bookFields.setNumOfCopies(numOfCopies);
                    bookFields.setAuthorNames(authorNames.split(","));
                    try {
                        new InsertNewBook().insertNewBook(bookFields);
                        resultLabel.setText("The Book has been successfully added into the system.");
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
        add(titleLabel);
        add(titleText);
        add(authorNameLabel);
        add(authorNameText);
        add(branchIdText);
        add(branchIdLabel);
        add(noOfCopiesLabel);
        add(noOfCopiesText);
        add(addBook);

        add(resultLabel);

        setVisible(true);
    }
}
