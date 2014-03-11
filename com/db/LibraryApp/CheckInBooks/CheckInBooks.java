package com.db.LibraryApp.CheckInBooks;

import com.db.LibraryApp.UtilityClasses.MySQLConnection;
import com.db.LibraryApp.UtilityClasses.ResultSetTableModel;
import com.db.LibraryApp.UtilityClasses.ValidationMessage;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class CheckInBooks extends JPanel {

    public JLabel resultLabel;
    public JLabel bookIdLabel;
    public JLabel cardNumberLabel;
    public JLabel borrowerNameLabel;
    public JTextField bookIdTextField;
    public JTextField cardNumberTextField;
    public JTextField borrowerNameTextField;
    public JButton searchButton;
    public JButton deleteButton;

    public CheckInBooks() {

        bookIdLabel = new JLabel("Book ID :");
        bookIdLabel.setBounds(100, 20, 100, 20);
        bookIdTextField = new JTextField(20);
        bookIdTextField.setBounds(210, 20, 200, 20);

        cardNumberLabel = new JLabel("Card Number :");
        cardNumberLabel.setBounds(100, 50, 100, 20);
        cardNumberTextField = new JTextField(20);
        cardNumberTextField.setBounds(210, 50, 200, 20);

        borrowerNameLabel = new JLabel("Borrower Name :");
        borrowerNameLabel.setBounds(100, 80, 100, 20);
        borrowerNameTextField = new JTextField(20);
        borrowerNameTextField.setBounds(210, 80, 200, 20);

        searchButton = new JButton("Search");
        searchButton.setBounds(150, 140, 100, 20);

        deleteButton = new JButton("Check-In");
        deleteButton.setBounds(690, 280, 100, 20);
        deleteButton.setVisible(false);

        resultLabel = new JLabel("");
        resultLabel.setBounds(80, 190, 450, 30);
        resultLabel.setForeground(Color.red);
        resultLabel.setFont(new Font("Cambria", Font.BOLD, 20));

        final JTable table = new JTable();
        table.setVisible(true);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(80, 250, 600, 300);
        add(scrollPane);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = table.getSelectedRows();
                if (selectedRows.length == 0) {
                    ValidationMessage.alertMessage("Please select a record from the table to Check-In");
                    table.requestFocusInWindow();
                    return;
                }
                HashMap<Integer, String[]> rowsToDelete = new HashMap<Integer, String[]>();
                for (int selectedRow : selectedRows) {
                    rowsToDelete.put(selectedRow, new String[3]);
                    for (int index = 0; index < 3; index++) {
                        rowsToDelete.get(selectedRow)[index] = (String) table.getValueAt(selectedRow, index);
                    }
                }
                try {
                    CheckInLookupResult.removeRecord(rowsToDelete);
                    ((ResultSetTableModel) table.getModel()).deleteRow(selectedRows);
                } catch (Exception ep) {
                    ep.printStackTrace();
                } finally {
                    MySQLConnection.closeConnection();
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultLabel.setText("Results - ");
                table.clearSelection();
                CheckInLookupFields checkInLookupFields = new CheckInLookupFields();
                checkInLookupFields.setBorrowerName(borrowerNameTextField.getText());
                checkInLookupFields.setCardNumber(cardNumberTextField.getText());
                checkInLookupFields.setBookId(bookIdTextField.getText());
                CheckInLookupResult checkInLookupResult = new CheckInLookupResult(table, checkInLookupFields);
                try {
                    checkInLookupResult.bindResultsetWithTable();
                    deleteButton.setVisible(true);
                } catch (Exception ep) {
                    ep.printStackTrace();
                } finally {
                    MySQLConnection.closeConnection();
                }
            }
        });
        setLayout(null);

        add(bookIdLabel);
        add(bookIdTextField);
        add(cardNumberLabel);
        add(cardNumberTextField);
        add(borrowerNameLabel);
        add(borrowerNameTextField);
        add(searchButton);
        add(resultLabel);
        add(deleteButton);
        setVisible(true);
    }
}

