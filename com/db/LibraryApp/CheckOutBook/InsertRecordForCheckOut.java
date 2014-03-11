package com.db.LibraryApp.CheckOutBook;

import com.db.LibraryApp.UtilityClasses.LibraryAppException;
import com.db.LibraryApp.UtilityClasses.MySQLConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class InsertRecordForCheckOut {

    public String checkOutBooks(CheckOutFields checkOutFields) throws LibraryAppException {
        Connection con = null;
        PreparedStatement chkBorrowedBooks;
        PreparedStatement chkBookAvailability;
        PreparedStatement checkAlreadyIssued;
        String returnStr = "";
        try {
            con = MySQLConnection.getConnection();
            String queryForLoanCount = "Select count(*) as loan_count from book_loans where card_no = ?";
            chkBorrowedBooks = con.prepareStatement(queryForLoanCount);
            chkBorrowedBooks.setString(1, checkOutFields.getCardNumber());

            ResultSet loanedBookCount = chkBorrowedBooks.executeQuery();

            if (loanedBookCount.next() && loanedBookCount.getInt("loan_count") >= 3) {
                throw new LibraryAppException("This card has already reached the limit for books issued");
            }

            String queryForAlreadyIssued = "Select count(*) as count from book_loans where card_no = ? " +
                    "and book_id= ? and branch_id=?";
            checkAlreadyIssued = con.prepareStatement(queryForAlreadyIssued);
            checkAlreadyIssued.setString(1, checkOutFields.getCardNumber());
            checkAlreadyIssued.setString(2, checkOutFields.getBookId());
            checkAlreadyIssued.setString(3, checkOutFields.getBranchId());

            ResultSet alreadyIssuedCount = checkAlreadyIssued.executeQuery();

            if (alreadyIssuedCount.next() && alreadyIssuedCount.getInt("count") > 0) {
                throw new LibraryAppException("This branch has already issued this book to this card");
            }

            String queryForAvailability = "SELECT (no_of_copies - IFNULL(b.num_out, 0)) AS num_avail from " +
                    "(SELECT bc.book_id, bc.branch_id, no_of_copies FROM book_copies bc LEFT OUTER JOIN book_loans bl " +
                    "ON bl.book_id = bc.book_id AND bl.branch_id = bc.branch_id " +
                    "GROUP BY bc.book_id, bc.branch_id) a " +
                    "LEFT JOIN (SELECT bl.book_id, bl.branch_id, COUNT(*) AS num_out FROM book_loans bl JOIN book_copies bc " +
                    "ON bl.book_id = bc.book_id AND bl.branch_id = bc.branch_id " +
                    "GROUP BY bl.book_id, bl.branch_id) b " +
                    "ON a.book_id = b.book_id AND a.branch_id = b.branch_id " +
                    "WHERE a.book_id = ? " +
                    "and a.branch_id = ?";
            chkBookAvailability = con.prepareStatement(queryForAvailability);
            chkBookAvailability.setString(1, checkOutFields.getBookId());
            chkBookAvailability.setString(2, checkOutFields.getBranchId());

            ResultSet availableBookCount = chkBookAvailability.executeQuery();

            if (availableBookCount.next() && availableBookCount.getInt("num_avail") < 1) {
                throw new LibraryAppException("Book is not available in the given branch");
            }
            String insertBookLoanQuery = "INSERT INTO book_loans VALUES(?,?,?,?,?)";

            chkBookAvailability = con.prepareStatement(insertBookLoanQuery);
            chkBookAvailability.setString(1, checkOutFields.getBookId());
            chkBookAvailability.setString(2, checkOutFields.getBranchId());
            chkBookAvailability.setString(3, checkOutFields.getCardNumber());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            Calendar c = Calendar.getInstance();

            c.add(Calendar.DATE,14);
            java.util.Date tempDate = c.getTime();
            Date dueDate = new Date(tempDate.getTime());

            chkBookAvailability.setString(4, sdf.format(date));
            chkBookAvailability.setString(5, sdf.format(dueDate) );
            returnStr = Integer.toString(chkBookAvailability.executeUpdate());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new LibraryAppException(e.getMessage());
        }
        return returnStr;
    }
}
