package com.db.LibraryApp.SearchBook;

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
public class BookAvailabilityScreen extends JPanel {

    public JLabel resultLabel;
    public JLabel bookIdLabel;
    public JLabel titleLabel;
    public JLabel authorNameLabel;
    public JTextField bookIdTextField;
    public JTextField titleTextField;
    public JTextField authorNameTextField;
    public JButton searchButton;

    public BookAvailabilityScreen() {

        bookIdLabel = new JLabel("Book ID :");
        bookIdLabel.setBounds(100, 20, 100, 20);
        bookIdTextField = new JTextField(20);
        bookIdTextField.setBounds(210, 20, 200, 20);

        titleLabel = new JLabel("Title :");
        titleLabel.setBounds(100, 50, 100, 20);
        titleTextField = new JTextField(20);
        titleTextField.setBounds(210, 50, 200, 20);

        authorNameLabel = new JLabel("Author Name :");
        authorNameLabel.setBounds(100, 80, 100, 20);
        authorNameTextField = new JTextField(20);
        authorNameTextField.setBounds(210, 80, 200, 20);

        searchButton = new JButton("Search");
        searchButton.setBounds(150, 140, 100, 20);


        resultLabel = new JLabel("");
        resultLabel.setBounds(130, 190, 450, 30);
        resultLabel.setForeground(Color.red);
        resultLabel.setFont(new Font("Cambria", Font.BOLD, 22));

        final JTable table = new JTable();
        table.setVisible(true);
        table.setCellSelectionEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(80, 250, 600, 300);
        add(scrollPane);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("".equals(bookIdTextField.getText()) && "".equals(authorNameTextField.getText()) && "".equals(titleTextField.getText())) {
                    ValidationMessage.alertMessage("Please enter at least one field for searching");
                    bookIdTextField.requestFocusInWindow();
                } else {
                    resultLabel.setText("Search Results ");
                    table.clearSelection();
                    BookSearchFields bookSearchFields = new BookSearchFields();
                    bookSearchFields.setAuthorName(authorNameTextField.getText());
                    bookSearchFields.setTitle(titleTextField.getText());
                    bookSearchFields.setBookId(bookIdTextField.getText());
                    try {
                        BookAvailabilityResults bookAvailabilityResults = new BookAvailabilityResults(table, bookSearchFields);
                        bookAvailabilityResults.bindResultSetWithTable();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        ValidationMessage.alertMessage(ex.getMessage());
                    }
                }
            }
        });
        setLayout(null);

        add(bookIdLabel);
        add(bookIdTextField);
        add(titleLabel);
        add(titleTextField);
        add(authorNameLabel);
        add(authorNameTextField);
        add(searchButton);
        add(resultLabel);
        setVisible(true);
    }
}
