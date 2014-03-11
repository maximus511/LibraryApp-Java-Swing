package com.db.LibraryApp;

import com.db.LibraryApp.AddBorrower.AddBorrower;
import com.db.LibraryApp.CheckInBooks.CheckInBooks;
import com.db.LibraryApp.CheckOutBook.CheckOutBook;
import com.db.LibraryApp.InsertBook.InsertBook;
import com.db.LibraryApp.SearchBook.BookAvailabilityScreen;
import com.db.LibraryApp.UpdateBookCount.UpdateBookCountScreen;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class MainPanel extends JPanel {

    public MainPanel() {
        super(new BorderLayout());
        JPanel labelPanel = new JPanel();
        labelPanel.setSize(200, 50);

        JLabel text = new JLabel("LIBRARY APPLICATION");
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setForeground(Color.red);
        text.setFont(new Font("Cambria", Font.BOLD, 25));
        labelPanel.add(text);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);

        BookAvailabilityScreen bookAvailabilityScreen = new BookAvailabilityScreen();
        bookAvailabilityScreen.setPreferredSize(new Dimension(800, 600));
        tabbedPane.addTab("Search Books", bookAvailabilityScreen);

        CheckOutBook checkOutBook = new CheckOutBook();
        tabbedPane.addTab("Check-Out Book", checkOutBook);

        CheckInBooks checkInBooks = new CheckInBooks();
        tabbedPane.addTab("Check-In Books", checkInBooks);

        AddBorrower addBorrower = new AddBorrower();
        tabbedPane.addTab("Add Borrower", addBorrower);

        InsertBook insertBook = new InsertBook();
        tabbedPane.addTab("Insert Book", insertBook);

        UpdateBookCountScreen updateCount = new UpdateBookCountScreen();
        tabbedPane.addTab("Update Copy count", updateCount);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

        add(labelPanel,BorderLayout.NORTH);
        add(tabbedPane,BorderLayout.SOUTH);
    }

    private static void StartLibraryApplication() {
        JFrame frame = new JFrame("Library Application");
        frame.add(new MainPanel(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StartLibraryApplication();
            }
        });

    }
}